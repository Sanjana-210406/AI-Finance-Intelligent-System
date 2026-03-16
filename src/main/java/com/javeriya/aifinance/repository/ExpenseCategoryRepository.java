package com.javeriya.aifinance.repository;

import com.javeriya.aifinance.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> findByNameIgnoreCase(String name);

    List<ExpenseCategory> findByType(ExpenseCategory.CategoryType type);
}
