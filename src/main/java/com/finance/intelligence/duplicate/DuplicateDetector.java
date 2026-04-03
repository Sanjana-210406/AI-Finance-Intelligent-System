package com.finance.intelligence.duplicate;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

public class DuplicateDetector {

    private static final Set<String> fingerprints = new HashSet<>();

    public static boolean isDuplicate(String userId, String date, double amount, String description) {

        String raw = userId + date + amount + description;
        String hash = generateHash(raw);

        if (fingerprints.contains(hash)) {
            return true;
        }

        fingerprints.add(hash);
        return false;
    }

    private static String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Hashing failed");
        }
    }
}