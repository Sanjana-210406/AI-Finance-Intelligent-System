package com.javeriya.aifinance.service;

import com.javeriya.aifinance.entity.*;
import com.javeriya.aifinance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

// 🔥 Intelligence Layer Imports
import com.finance.intelligence.duplicate.DuplicateDetector;
import com.finance.intelligence.anomaly.AnomalyDetector;
import com.finance.intelligence.calculator.HealthScoreCalculator;
import com.finance.intelligence.decision.DecisionEngine;
import com.finance.intelligence.projection.ProjectionEngine;
import com.finance.intelligence.projection.ProjectionResult;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // Existing classification service (DO NOT REMOVE)
    @Autowired
    private com.javeriya.aifinance.service.ClassificationService classificationService;

    // ================= ADD TRANSACTION =================
    public Transaction addTransaction(Long userId, Transaction transaction) {

        // 1. Get User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        transaction.setUser(user);

        // ================= DUPLICATE CHECK =================
        String fingerprint = generateFingerprint(
                userId,
                transaction.getDate(),
                transaction.getAmount(),
                transaction.getDescription()
        );

        transaction.setFingerprint(fingerprint);

        if (transactionRepository.findByUserIdAndFingerprint(userId, fingerprint).isPresent()) {
            throw new RuntimeException("Duplicate transaction detected!");
        }

        // ================= CLASSIFICATION =================
        if (transaction.getCategoryId() == null) {
            ExpenseCategory category = classificationService.classify(
                    transaction.getDescription(),
                    transaction.getAmount()
            );

            if (category != null) {
                transaction.setCategoryId(category.getId());
            }
        }

        // ================= INTELLIGENCE LAYER =================

        // 1. AI Classification
        String aiCategory = com.finance.intelligence.classification.ClassificationService
                .classify(transaction.getDescription(), transaction.getAmount().doubleValue());

        System.out.println("AI Category: " + aiCategory);

        // 2. AI Duplicate Check
        boolean aiDuplicate = DuplicateDetector.isDuplicate(
                String.valueOf(userId),
                transaction.getDate().toString(),
                transaction.getAmount().doubleValue(),
                transaction.getDescription()
        );

        if (aiDuplicate) {
            System.out.println("AI Duplicate detected!");
        }

        // 3. Fetch user history
        List<Transaction> userHistory = transactionRepository.findByUserId(userId);

        List<Double> amounts = userHistory.stream()
                .map(t -> t.getAmount().doubleValue())
                .collect(Collectors.toList());

        List<LocalDateTime> timestamps = userHistory.stream()
                .map(t -> t.getDate().atStartOfDay()) // simplified
                .collect(Collectors.toList());

        // 4. Anomaly Detection
        String anomaly = AnomalyDetector.detect(
                transaction.getAmount().doubleValue(),
                amounts,
                timestamps
        );

        System.out.println("Anomaly: " + anomaly);

        // ================= HEALTH SCORE (OPTION 1) =================

        double income = 20000; // ✅ student assumption

        double totalExpense = amounts.stream()
                .mapToDouble(Double::doubleValue)
                .sum() + transaction.getAmount().doubleValue();

        double savingsGoal = income * 0.1; // ✅ 10% student rule

        double currentSavings = income - totalExpense;

        double healthScore = HealthScoreCalculator.calculate(
                income,
                totalExpense,
                savingsGoal,
                currentSavings
        );

        System.out.println("Health Score: " + healthScore);

        // ================= DECISION =================

       boolean isImpulse = "IMPULSE".equals(anomaly);
       boolean isOverspending = totalExpense > (income * 0.9);

        String decision = DecisionEngine.getDecision(
                healthScore,
                isOverspending,
                isImpulse,
                anomaly
        );

        System.out.println("Decision: " + decision);
        transaction.setAiCategory(aiCategory);
        transaction.setAnomaly(anomaly);
        transaction.setDecision(decision);
        transaction.setHealthScore(healthScore);
// ================= PROJECTION =================

// Example budget (later fetch from DB)
double monthlyBudget = Math.max(income * 0.7, totalExpense);

ProjectionResult projection = ProjectionEngine.analyze(
        totalExpense,
        monthlyBudget
);

System.out.println("Safe Daily Spend: " + projection.getSafePerDay());
        // ================= SAVE =================
        return transactionRepository.save(transaction);
    }

    // ================= OTHER METHODS =================

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByMonth(Long userId, int month, int year) {
        return transactionRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }

    public List<Transaction> getTransactionsByDateRange(Long userId, LocalDate start, LocalDate end) {
        return transactionRepository.findByUserIdAndDateBetween(userId, start, end);
    }

    public boolean isDuplicate(Long userId, String fingerprint) {
        return transactionRepository.findByUserIdAndFingerprint(userId, fingerprint).isPresent();
    }

    public BigDecimal getCategorySpendLast30Days(Long userId, Long categoryId) {
        LocalDate fromDate = LocalDate.now().minusDays(30);

        List<Transaction> transactions = transactionRepository
                .findByUserIdAndCategoryIdAndDateAfter(userId, categoryId, fromDate);

        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ================= FINGERPRINT =================

    private String generateFingerprint(Long userId, LocalDate date,
                                       BigDecimal amount, String description) {
        try {
            String raw = userId + "|" + date + "|" + amount + "|" + description.toLowerCase().trim();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash).substring(0, 32);

        } catch (Exception e) {
            return String.valueOf(System.currentTimeMillis());
        }
    }
    public Transaction addTransactionFromOCR(Long userId, String text) {

    double amount = com.finance.intelligence.ocr.OCRParser.extractAmount(text);
    String merchant = com.finance.intelligence.ocr.OCRParser.extractMerchant(text);

    Transaction transaction = new Transaction();
    transaction.setAmount(BigDecimal.valueOf(amount));
    transaction.setDescription(merchant);
    transaction.setDate(LocalDate.now());

    return addTransaction(userId, transaction);
        }

}