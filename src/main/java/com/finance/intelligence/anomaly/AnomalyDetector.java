package com.finance.intelligence.anomaly;

import java.time.LocalDateTime;
import java.util.List;

public class AnomalyDetector {

    public static String detect(double amount, List<Double> history, List<LocalDateTime> timestamps) {

        // 1. SPIKE DETECTION
        if (history != null && !history.isEmpty()) {
            double avg = history.stream().mapToDouble(a -> a).average().orElse(0);

            if (amount > 2.5 * avg) {
                return "SPIKE";
            }
        }

        // 2. IMPULSE DETECTION (last 3 transactions within 90 min)
        if (timestamps != null && timestamps.size() >= 3) {

            int n = timestamps.size();

            LocalDateTime t1 = timestamps.get(n - 1);
            LocalDateTime t2 = timestamps.get(n - 2);
            LocalDateTime t3 = timestamps.get(n - 3);

            long minutes = java.time.Duration.between(t3, t1).toMinutes();

            if (minutes <= 90) {
                return "IMPULSE";
            }
        }

        // 3. LATE NIGHT DETECTION
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() >= 23) {
            return "LATE_NIGHT";
        }

        return "NORMAL";
    }
}