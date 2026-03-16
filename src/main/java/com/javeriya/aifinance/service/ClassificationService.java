package com.javeriya.aifinance.service;

import com.javeriya.aifinance.entity.ExpenseCategory;
import com.javeriya.aifinance.repository.ExpenseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ClassificationService {

    @Autowired
    private ExpenseCategoryRepository categoryRepository;

    // Layer 1 — Keyword Matching
    public Optional<ExpenseCategory> classifyByKeyword(String description) {
        String desc = description.toLowerCase();

        // Food & Dining
        if (containsAny(desc, "zomato", "swiggy", "restaurant", "cafe", "lunch",
                "dinner", "hotel", "food", "pizza", "burger", "biryani")) {
            return categoryRepository.findByNameIgnoreCase("Food & Dining");
        }

        // Housing
        if (containsAny(desc, "rent", "pg", "hostel", "paying guest")) {
            return categoryRepository.findByNameIgnoreCase("Housing");
        }

        // Utilities
        if (containsAny(desc, "electricity", "wifi", "water bill", "gas bill", "broadband")) {
            return categoryRepository.findByNameIgnoreCase("Utilities");
        }

        // Transport
        if (containsAny(desc, "metro", "bus", "uber", "ola", "rapido", "petrol", "fuel")) {
            return categoryRepository.findByNameIgnoreCase("Transport");
        }

        // Medical
        if (containsAny(desc, "medicine", "hospital", "pharmacy", "doctor", "clinic", "apollo")) {
            return categoryRepository.findByNameIgnoreCase("Medical");
        }

        // Entertainment / Subscriptions
        if (containsAny(desc, "netflix", "spotify", "hotstar", "prime", "movie",
                "game", "steam", "disney", "youtube", "discord", "notion", "canva", "adobe")) {
            return categoryRepository.findByNameIgnoreCase("Entertainment");
        }

        // Shopping
        if (containsAny(desc, "amazon", "flipkart", "myntra", "meesho", "clothing", "shoes")) {
            return categoryRepository.findByNameIgnoreCase("Shopping");
        }

        // Investment
        if (containsAny(desc, "sip", "mutual fund", "ppf", "rd", "fd", "gold", "nps")) {
            return categoryRepository.findByNameIgnoreCase("Investment");
        }

        // Education
        if (containsAny(desc, "book", "course", "udemy", "coursera", "fees", "college")) {
            return categoryRepository.findByNameIgnoreCase("Education");
        }

        // Loan
        if (containsAny(desc, "emi", "loan", "credit card", "credit")) {
            return categoryRepository.findByNameIgnoreCase("Loan Repayment");
        }

        // Grocery
        if (containsAny(desc, "grocery", "supermarket", "dmart", "bigbasket", "blinkit")) {
            return categoryRepository.findByNameIgnoreCase("Grocery");
        }

        return Optional.empty();
    }

    // Layer 2 — Amount Heuristic
    public Optional<ExpenseCategory> classifyByAmount(BigDecimal amount) {
        double amt = amount.doubleValue();

        if (amt < 200) {
            return categoryRepository.findByNameIgnoreCase("Food & Dining");
        } else if (amt < 500) {
            return categoryRepository.findByNameIgnoreCase("Food & Dining");
        } else if (amt < 2000) {
            return categoryRepository.findByNameIgnoreCase("Shopping");
        } else {
            return categoryRepository.findByNameIgnoreCase("Shopping");
        }
    }

    // Main classification method — tries Layer 1 first, then Layer 2
    public ExpenseCategory classify(String description, BigDecimal amount) {
        // Layer 1
        Optional<ExpenseCategory> result = classifyByKeyword(description);
        if (result.isPresent()) {
            return result.get();
        }

        // Layer 2
        result = classifyByAmount(amount);
        if (result.isPresent()) {
            return result.get();
        }

        // Layer 3 — return null, frontend will ask user
        return null;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) return true;
        }
        return false;
    }
}