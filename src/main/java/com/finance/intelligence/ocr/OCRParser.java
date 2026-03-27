package com.finance.intelligence.ocr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCRParser {

    // Extract amount
    public static double extractAmount(String text) {

        Pattern pattern = Pattern.compile("₹?\\d+(\\.\\d{1,2})?");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String amountStr = matcher.group().replace("₹", "");
            return Double.parseDouble(amountStr);
        }

        return 0;
    }

    // Extract merchant
    public static String extractMerchant(String text) {

        text = text.toLowerCase();

        if (text.contains("swiggy")) return "swiggy";
        if (text.contains("zomato")) return "zomato";
        if (text.contains("amazon")) return "amazon";
        if (text.contains("uber")) return "uber";
        if (text.contains("ola")) return "ola";

        return "unknown";
    }
}