package com.finance.intelligence.projection;

public class ProjectionResult {

    private double dailyAverage;
    private double projectedSpend;
    private double safePerDay;

    public ProjectionResult(double dailyAverage, double projectedSpend, double safePerDay) {
        this.dailyAverage = dailyAverage;
        this.projectedSpend = projectedSpend;
        this.safePerDay = safePerDay;
    }

    public double getDailyAverage() { return dailyAverage; }
    public double getProjectedSpend() { return projectedSpend; }
    public double getSafePerDay() { return safePerDay; }
}