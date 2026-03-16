package com.javeriya.aifinance;

import com.javeriya.aifinance.entity.ExpenseCategory;
import com.javeriya.aifinance.repository.ExpenseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ExpenseCategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        createIfNotExists("Food & Dining", ExpenseCategory.CategoryType.WANT, "restaurant_menu");
        createIfNotExists("Housing", ExpenseCategory.CategoryType.NEED, "home");
        createIfNotExists("Utilities", ExpenseCategory.CategoryType.NEED, "bolt");
        createIfNotExists("Transport", ExpenseCategory.CategoryType.NEED, "directions_car");
        createIfNotExists("Medical", ExpenseCategory.CategoryType.NEED, "local_hospital");
        createIfNotExists("Entertainment", ExpenseCategory.CategoryType.WANT, "movie");
        createIfNotExists("Shopping", ExpenseCategory.CategoryType.WANT, "shopping_cart");
        createIfNotExists("Investment", ExpenseCategory.CategoryType.INVESTMENT, "trending_up");
        createIfNotExists("Education", ExpenseCategory.CategoryType.NEED, "school");
        createIfNotExists("Loan Repayment", ExpenseCategory.CategoryType.NEED, "account_balance");
        createIfNotExists("Grocery", ExpenseCategory.CategoryType.NEED, "local_grocery_store");
        createIfNotExists("Other", ExpenseCategory.CategoryType.WANT, "category");
    }

    private void createIfNotExists(String name, ExpenseCategory.CategoryType type, String iconCode) {
        if (categoryRepository.findByNameIgnoreCase(name).isEmpty()) {
            categoryRepository.save(new ExpenseCategory(name, type, iconCode));
        }
    }
}
