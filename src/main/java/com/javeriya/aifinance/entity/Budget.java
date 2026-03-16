package com.javeriya.aifinance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    @Column(name = "total_income", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalIncome;

    @Column(name = "savings_target", nullable = false, precision = 12, scale = 2)
    private BigDecimal savingsTarget;

    @Column(name = "needs_allocation", nullable = false, precision = 12, scale = 2)
    private BigDecimal needsAllocation;

    @Column(name = "wants_allocation", nullable = false, precision = 12, scale = 2)
    private BigDecimal wantsAllocation;

    @Column(name = "emergency_buffer", nullable = false, precision = 12, scale = 2)
    private BigDecimal emergencyBuffer;

    @Column(name = "dynamic_monthly_target", precision = 12, scale = 2)
    private BigDecimal dynamicMonthlyTarget;

    public Budget() {}

    public Budget(User user, int month, int year, BigDecimal totalIncome,
                  BigDecimal savingsTarget, BigDecimal needsAllocation,
                  BigDecimal wantsAllocation, BigDecimal emergencyBuffer) {
        this.user = user;
        this.month = month;
        this.year = year;
        this.totalIncome = totalIncome;
        this.savingsTarget = savingsTarget;
        this.needsAllocation = needsAllocation;
        this.wantsAllocation = wantsAllocation;
        this.emergencyBuffer = emergencyBuffer;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    public BigDecimal getSavingsTarget() { return savingsTarget; }
    public void setSavingsTarget(BigDecimal savingsTarget) { this.savingsTarget = savingsTarget; }

    public BigDecimal getNeedsAllocation() { return needsAllocation; }
    public void setNeedsAllocation(BigDecimal needsAllocation) { this.needsAllocation = needsAllocation; }

    public BigDecimal getWantsAllocation() { return wantsAllocation; }
    public void setWantsAllocation(BigDecimal wantsAllocation) { this.wantsAllocation = wantsAllocation; }

    public BigDecimal getEmergencyBuffer() { return emergencyBuffer; }
    public void setEmergencyBuffer(BigDecimal emergencyBuffer) { this.emergencyBuffer = emergencyBuffer; }

    public BigDecimal getDynamicMonthlyTarget() { return dynamicMonthlyTarget; }
    public void setDynamicMonthlyTarget(BigDecimal dynamicMonthlyTarget) { this.dynamicMonthlyTarget = dynamicMonthlyTarget; }
}
