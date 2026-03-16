package com.javeriya.aifinance.service;

import com.javeriya.aifinance.entity.Budget;
import com.javeriya.aifinance.entity.IncomeSource;
import com.javeriya.aifinance.entity.User;
import com.javeriya.aifinance.repository.BudgetRepository;
import com.javeriya.aifinance.repository.IncomeSourceRepository;
import com.javeriya.aifinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private IncomeSourceRepository incomeSourceRepository;

    @Autowired
    private UserRepository userRepository;

    // Add income source for a user
    public IncomeSource addIncome(Long userId, IncomeSource incomeSource) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        incomeSource.setUser(user);
        return incomeSourceRepository.save(incomeSource);
    }

    // Get all income sources for a user
    public List<IncomeSource> getIncomes(Long userId) {
        return incomeSourceRepository.findByUserId(userId);
    }

    // Auto-generate monthly budget based on profile
    public Budget generateBudget(Long userId, int month, int year) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if budget already exists
        Optional<Budget> existing = budgetRepository
                .findByUserIdAndMonthAndYear(userId, month, year);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Calculate total monthly income
        List<IncomeSource> incomes = incomeSourceRepository.findByUserId(userId);
        BigDecimal totalIncome = incomes.stream()
                .map(IncomeSource::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Apply savings-first allocation based on profile
        BigDecimal savingsPercent;
        BigDecimal needsPercent;
        BigDecimal wantsPercent;

        switch (user.getProfileType()) {
            case STUDENT:
                savingsPercent = new BigDecimal("0.10");
                needsPercent = new BigDecimal("0.50");
                wantsPercent = new BigDecimal("0.30");
                break;
            case RETIRED:
                savingsPercent = new BigDecimal("0.15");
                needsPercent = new BigDecimal("0.60");
                wantsPercent = new BigDecimal("0.15");
                break;
            default: // PROFESSIONAL
                savingsPercent = new BigDecimal("0.20");
                needsPercent = new BigDecimal("0.40");
                wantsPercent = new BigDecimal("0.30");
                break;
        }

        BigDecimal emergencyPercent = new BigDecimal("0.10");

        BigDecimal savingsTarget = totalIncome.multiply(savingsPercent).setScale(2, RoundingMode.HALF_UP);
        BigDecimal needsAllocation = totalIncome.multiply(needsPercent).setScale(2, RoundingMode.HALF_UP);
        BigDecimal wantsAllocation = totalIncome.multiply(wantsPercent).setScale(2, RoundingMode.HALF_UP);
        BigDecimal emergencyBuffer = totalIncome.multiply(emergencyPercent).setScale(2, RoundingMode.HALF_UP);

        Budget budget = new Budget(user, month, year, totalIncome,
                savingsTarget, needsAllocation, wantsAllocation, emergencyBuffer);

        return budgetRepository.save(budget);
    }

    // Get current month budget
    public Optional<Budget> getCurrentBudget(Long userId) {
        LocalDate now = LocalDate.now();
        return budgetRepository.findByUserIdAndMonthAndYear(userId, now.getMonthValue(), now.getYear());
    }
}