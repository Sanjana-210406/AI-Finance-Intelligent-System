package com.javeriya.aifinance.service;

import com.javeriya.aifinance.entity.ExpenseCategory;
import com.javeriya.aifinance.entity.Transaction;
import com.javeriya.aifinance.entity.User;
import com.javeriya.aifinance.repository.TransactionRepository;
import com.javeriya.aifinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.HexFormat;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassificationService classificationService;

    // Add a transaction with duplicate check and auto-classification
    public Transaction addTransaction(Long userId, Transaction transaction) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        transaction.setUser(user);

        // Generate fingerprint for duplicate detection
        String fingerprint = generateFingerprint(userId,
                transaction.getDate(), transaction.getAmount(), transaction.getDescription());
        transaction.setFingerprint(fingerprint);

        // Check for duplicate
        if (transactionRepository.findByUserIdAndFingerprint(userId, fingerprint).isPresent()) {
            throw new RuntimeException("Duplicate transaction detected!");
        }

        // Auto-classify if category not set
        if (transaction.getCategoryId() == null) {
            ExpenseCategory category = classificationService.classify(
                    transaction.getDescription(), transaction.getAmount());
            if (category != null) {
                transaction.setCategoryId(category.getId());
            }
        }

        return transactionRepository.save(transaction);
    }

    // Get all transactions for a user
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    // Get transactions filtered by month and year
    public List<Transaction> getTransactionsByMonth(Long userId, int month, int year) {
        return transactionRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }

    // Get transactions between two dates
    public List<Transaction> getTransactionsByDateRange(Long userId, LocalDate start, LocalDate end) {
        return transactionRepository.findByUserIdAndDateBetween(userId, start, end);
    }

    // Check for duplicate
    public boolean isDuplicate(Long userId, String fingerprint) {
        return transactionRepository.findByUserIdAndFingerprint(userId, fingerprint).isPresent();
    }

    // Get total spent in a category for last 30 days
    public BigDecimal getCategorySpendLast30Days(Long userId, Long categoryId) {
        LocalDate fromDate = LocalDate.now().minusDays(30);
        List<Transaction> transactions = transactionRepository
                .findByUserIdAndCategoryIdAndDateAfter(userId, categoryId, fromDate);

        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Generate SHA-256 fingerprint
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
}