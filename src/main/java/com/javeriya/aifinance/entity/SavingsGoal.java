package com.javeriya.aifinance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savings_goals")
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "goal_name", nullable = false)
    private String goalName;

    @Column(name = "target_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "saved_so_far", precision = 12, scale = 2)
    private BigDecimal savedSoFar = BigDecimal.ZERO;

    @Column(name = "monthly_required", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyRequired;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "is_emergency_fund")
    private boolean isEmergencyFund = false;

    @Column(name = "is_yearly_target")
    private boolean isYearlyTarget = false;

    @Column(name = "is_completed")
    private boolean isCompleted = false;

    public SavingsGoal() {}

    public SavingsGoal(User user, String goalName, BigDecimal targetAmount,
                       BigDecimal monthlyRequired, LocalDate deadline) {
        this.user = user;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.monthlyRequired = monthlyRequired;
        this.deadline = deadline;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }

    public BigDecimal getTargetAmount() { return targetAmount; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }

    public BigDecimal getSavedSoFar() { return savedSoFar; }
    public void setSavedSoFar(BigDecimal savedSoFar) { this.savedSoFar = savedSoFar; }

    public BigDecimal getMonthlyRequired() { return monthlyRequired; }
    public void setMonthlyRequired(BigDecimal monthlyRequired) { this.monthlyRequired = monthlyRequired; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public boolean isEmergencyFund() { return isEmergencyFund; }
    public void setEmergencyFund(boolean emergencyFund) { isEmergencyFund = emergencyFund; }

    public boolean isYearlyTarget() { return isYearlyTarget; }
    public void setYearlyTarget(boolean yearlyTarget) { isYearlyTarget = yearlyTarget; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
