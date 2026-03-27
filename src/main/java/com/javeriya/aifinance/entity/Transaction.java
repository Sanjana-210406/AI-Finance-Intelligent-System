package com.javeriya.aifinance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Transaction {
        private String aiCategory;
        private String anomaly;
        private String decision;
        private Double healthScore;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "budget_id")
    private Long budgetId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, unique = true)
    private String fingerprint;

    @Column(name = "is_anomaly")
    private boolean isAnomaly = false;

    @Column(name = "is_subscription")
    private boolean isSubscription = false;

    @Column(name = "is_emotional_spend")
    private boolean isEmotionalSpend = false;

    @Column(name = "user_corrected_category")
    private boolean userCorrectedCategory = false;

    public Transaction() {}

    // Getters & Setters

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getBudgetId() { return budgetId; }
    public void setBudgetId(Long budgetId) { this.budgetId = budgetId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public java.math.BigDecimal getAmount() { return amount; }
    public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getFingerprint() { return fingerprint; }
    public void setFingerprint(String fingerprint) { this.fingerprint = fingerprint; }

    public boolean isAnomaly() { return isAnomaly; }
    public void setAnomaly(boolean anomaly) { isAnomaly = anomaly; }

    public boolean isSubscription() { return isSubscription; }
    public void setSubscription(boolean subscription) { isSubscription = subscription; }

    public boolean isEmotionalSpend() { return isEmotionalSpend; }
    public void setEmotionalSpend(boolean emotionalSpend) { isEmotionalSpend = emotionalSpend; }

    public boolean isUserCorrectedCategory() { return userCorrectedCategory; }
    public void setUserCorrectedCategory(boolean userCorrectedCategory) { this.userCorrectedCategory = userCorrectedCategory; }
    public String getAiCategory() { return aiCategory; }
    public void setAiCategory(String aiCategory) { this.aiCategory = aiCategory; }

    public String getAnomaly() { return anomaly; }
    public void setAnomaly(String anomaly) { this.anomaly = anomaly; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public Double getHealthScore() { return healthScore; }
    public void setHealthScore(Double healthScore) { this.healthScore = healthScore; }
}
