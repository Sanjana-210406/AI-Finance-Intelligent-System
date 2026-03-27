package com.finance.intelligence.projection;

import java.time.LocalDate;

public class ProjectionEngine {

    public static ProjectionResult analyze(double totalSpent, double monthlyBudget) {

        int currentDay = LocalDate.now().getDayOfMonth();
        int totalDays = LocalDate.now().lengthOfMonth();

        double dailyAvg = totalSpent / currentDay;

        double projectedSpend = dailyAvg * totalDays;

        double remainingBudget = Math.max(monthlyBudget - totalSpent, 0);
        double remainingDays = totalDays - currentDay;

        double safePerDay = (remainingDays > 0 && remainingBudget > 0)
                ? remainingBudget / remainingDays
                : 0;

        return new ProjectionResult(dailyAvg, projectedSpend, safePerDay);
    }
}