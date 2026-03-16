package com.javeriya.aifinance;

import com.javeriya.aifinance.entity.Budget;
import com.javeriya.aifinance.entity.Transaction;
import com.javeriya.aifinance.service.BudgetService;
import com.javeriya.aifinance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private TransactionService transactionService;

    // Main Agent API — collect data and return mood + message
    @GetMapping("/analyse/{userId}")
    public ResponseEntity<?> analyseUser(@PathVariable Long userId) {
        try {
            LocalDate now = LocalDate.now();

            // Get current budget
            Optional<Budget> budgetOpt = budgetService.getCurrentBudget(userId);
            if (budgetOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("No budget found for current month");
            }

            Budget budget = budgetOpt.get();

            // Get current month transactions
            List<Transaction> transactions = transactionService
                    .getTransactionsByMonth(userId, now.getMonthValue(), now.getYear());

            // Calculate total spent
            BigDecimal totalSpent = transactions.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Calculate spending percentage
            BigDecimal spendableAmount = budget.getNeedsAllocation()
                    .add(budget.getWantsAllocation());

            BigDecimal spendingPercentage = BigDecimal.ZERO;
            if (spendableAmount.compareTo(BigDecimal.ZERO) > 0) {
                spendingPercentage = totalSpent
                        .divide(spendableAmount, 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
            }

            // Determine mood and message
            String mood;
            String message;

            if (spendingPercentage.compareTo(new BigDecimal("90")) >= 0) {
                mood = "CRITICAL";
                message = "You have spent " + spendingPercentage.intValue() +
                        "% of your budget! Immediate action needed.";
            } else if (spendingPercentage.compareTo(new BigDecimal("75")) >= 0) {
                mood = "WARNING";
                message = "You have spent " + spendingPercentage.intValue() +
                        "% of your budget. Be careful with spending.";
            } else if (spendingPercentage.compareTo(new BigDecimal("50")) >= 0) {
                mood = "MODERATE";
                message = "You have spent " + spendingPercentage.intValue() +
                        "% of your budget. You are on track.";
            } else {
                mood = "GOOD";
                message = "You have spent only " + spendingPercentage.intValue() +
                        "% of your budget. Keep it up!";
            }

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("month", now.getMonthValue());
            response.put("year", now.getYear());
            response.put("totalIncome", budget.getTotalIncome());
            response.put("savingsTarget", budget.getSavingsTarget());
            response.put("spendableAmount", spendableAmount);
            response.put("totalSpent", totalSpent);
            response.put("spendingPercentage", spendingPercentage);
            response.put("transactionCount", transactions.size());
            response.put("mood", mood);
            response.put("message", message);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get full financial summary for decision engine
    @GetMapping("/summary/{userId}")
    public ResponseEntity<?> getFinancialSummary(@PathVariable Long userId) {
        try {
            LocalDate now = LocalDate.now();

            Optional<Budget> budgetOpt = budgetService.getCurrentBudget(userId);
            if (budgetOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("No budget found");
            }

            Budget budget = budgetOpt.get();
            List<Transaction> transactions = transactionService
                    .getTransactionsByMonth(userId, now.getMonthValue(), now.getYear());

            BigDecimal totalSpent = transactions.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal remaining = budget.getNeedsAllocation()
                    .add(budget.getWantsAllocation())
                    .subtract(totalSpent);

            Map<String, Object> summary = new HashMap<>();
            summary.put("budget", budget);
            summary.put("totalSpent", totalSpent);
            summary.put("remaining", remaining);
            summary.put("transactionCount", transactions.size());
            summary.put("transactions", transactions);

            return ResponseEntity.ok(summary);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}