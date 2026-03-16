package com.javeriya.aifinance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "merchant_learned_mappings")
public class MerchantLearning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "merchant_name", nullable = false)
    private String merchantName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ExpenseCategory category;

    @Column(name = "times_used")
    private int timesUsed = 1;

    public MerchantLearning() {}

    public MerchantLearning(User user, String merchantName, ExpenseCategory category) {
        this.user = user;
        this.merchantName = merchantName;
        this.category = category;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }

    public int getTimesUsed() { return timesUsed; }
    public void setTimesUsed(int timesUsed) { this.timesUsed = timesUsed; }
}