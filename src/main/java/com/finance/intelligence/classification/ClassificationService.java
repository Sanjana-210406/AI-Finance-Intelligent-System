package com.finance.intelligence.classification;

import java.util.HashMap;
import java.util.Map;

public class ClassificationService {

    private static final Map<String, String> merchantMap = new HashMap<>();

    static {
        merchantMap.put("swiggy", "FOOD");
        merchantMap.put("zomato", "FOOD");
        merchantMap.put("amazon", "SHOPPING");
        merchantMap.put("uber", "TRANSPORT");
        merchantMap.put("ola", "TRANSPORT");
        merchantMap.put("apollo", "HEALTH");
    }

    public static String classify(String description, double amount) {

        if (description == null) return "UNCATEGORIZED";

        String clean = description.toLowerCase();

        // Rule 1: Keyword match
        for (String key : merchantMap.keySet()) {
            if (clean.contains(key)) {
                return merchantMap.get(key);
            }
        }

        // Rule 2: Amount heuristic
        if (amount > 5000) return "LUXURY";
        if (amount < 100) return "SMALL_EXPENSE";

        // Rule 3: fallback
        return "UNCATEGORIZED";
    }
}
