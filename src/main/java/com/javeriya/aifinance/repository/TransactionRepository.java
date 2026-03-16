package com.javeriya.aifinance.repository;

import com.javeriya.aifinance.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Get all transactions for a user
    List<Transaction> findByUserId(Long userId);

    // Get transactions for a user within a date range (used for monthly filtering)
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // Get transactions by category (used for budget tracking per category)
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);

    // Duplicate detection — check if fingerprint already exists for this user
    Optional<Transaction> findByUserIdAndFingerprint(Long userId, String fingerprint);

    // Get all emotional spend transactions (for behavioral analysis)
    List<Transaction> findByUserIdAndIsEmotionalSpendTrue(Long userId);

    // Get all anomaly-flagged transactions
    List<Transaction> findByUserIdAndIsAnomalyTrue(Long userId);

    // Get all subscription transactions
    List<Transaction> findByUserIdAndIsSubscriptionTrue(Long userId);

    // Get transactions for a specific month and year (used by predictive engine)
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findByUserIdAndMonthAndYear(
        @Param("userId") Long userId,
        @Param("month") int month,
        @Param("year") int year
    );

    // Get transactions for a category in a specific month (used for 30-day average calculation)
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND t.categoryId = :categoryId AND t.date >= :fromDate")
    List<Transaction> findByUserIdAndCategoryIdAndDateAfter(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("fromDate") LocalDate fromDate
    );
}
