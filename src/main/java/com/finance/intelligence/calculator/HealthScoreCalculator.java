package com.finance.intelligence.calculator;

public class HealthScoreCalculator {

    // Main function
    public static double calculate(double income, double totalExpense, double savingsGoal, double currentSavings) {

        double savingsRate = calculateSavingsRate(income, totalExpense);
        double budgetAdherence = calculateBudgetAdherence(income, totalExpense);
        double goalProgress = calculateGoalProgress(savingsGoal, currentSavings);

        double score =
                (savingsRate * 40) +
                (budgetAdherence * 35) +
                (goalProgress * 25);

        return Math.min(score, 100); // cap at 100
    }

    // 1. Savings Rate
    private static double calculateSavingsRate(double income, double expense) {
    if (income == 0) return 0;

    double savings = income - expense;
    double rate = savings / income;

    // student expectation: 10%
    if (rate >= 0.1) return 1.0;
    else if (rate >= 0.05) return 0.7;
    else return 0.3;
}

    // 2. Budget Adherence
    private static double calculateBudgetAdherence(double income, double expense) {
    if (income == 0) return 0;

    double ratio = expense / income;

    // Student thresholds
    if (ratio <= 0.6) return 1.0;       // very good
    else if (ratio <= 0.9) return 0.7;  // acceptable
    else return 0.3;                    // overspending
}

    // 3. Goal Progress
    private static double calculateGoalProgress(double goal, double current) {
        if (goal == 0) return 0;

        double progress = current / goal;

        return Math.min(progress, 1.0); // cap at 100%
    }
}