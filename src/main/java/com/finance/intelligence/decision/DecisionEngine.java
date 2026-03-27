package com.finance.intelligence.decision;

public class DecisionEngine {

    public static String getDecision(
            double healthScore,
            boolean isOverspending,
            boolean isImpulse,
            String anomalyType
    ) {

        // 1. CELEBRATE
        if (healthScore >= 85) {
            return "CELEBRATE: Outstanding! Your financial health is excellent!";
        }

        // 2. OVESPENDING WARNING
        if (isOverspending) {
            return "WARNING: You're exceeding your budget. Let's slow down!";
        }

        // 3. IMPULSE
        if (isImpulse) {
            return "CONCERN: Multiple quick purchases detected. Think before spending!";
        }

        // 4. ANOMALY
        if ("SPIKE".equals(anomalyType)) {
            return "CONCERN: This expense is unusually high. Please verify.";
        }

        // 5. SAFE
        return "NEUTRAL: You're on track. Safe to spend wisely.";
    }
}