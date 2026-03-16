package com.javeriya.aifinance;

import com.javeriya.aifinance.entity.Budget;
import com.javeriya.aifinance.entity.IncomeSource;
import com.javeriya.aifinance.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // Add income source
    @PostMapping("/income/{userId}")
    public ResponseEntity<?> addIncome(
            @PathVariable Long userId,
            @RequestBody IncomeSource incomeSource) {
        try {
            IncomeSource saved = budgetService.addIncome(userId, incomeSource);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get all income sources
    @GetMapping("/income/{userId}")
    public ResponseEntity<?> getIncomes(@PathVariable Long userId) {
        List<IncomeSource> incomes = budgetService.getIncomes(userId);
        return ResponseEntity.ok(incomes);
    }

    // Generate monthly budget
    @PostMapping("/budget/generate/{userId}")
    public ResponseEntity<?> generateBudget(
            @PathVariable Long userId,
            @RequestParam int month,
            @RequestParam int year) {
        try {
            Budget budget = budgetService.generateBudget(userId, month, year);
            return ResponseEntity.ok(budget);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get current month budget
    @GetMapping("/budget/current/{userId}")
    public ResponseEntity<?> getCurrentBudget(@PathVariable Long userId) {
        Optional<Budget> budget = budgetService.getCurrentBudget(userId);
        if (budget.isPresent()) {
            return ResponseEntity.ok(budget.get());
        } else {
            return ResponseEntity.badRequest().body("No budget found for current month");
        }
    }
}