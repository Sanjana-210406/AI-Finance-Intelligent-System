package com.javeriya.aifinance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "income_sources")
public class IncomeSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "source_name", nullable = false)
    private String sourceName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "income_type", nullable = false)
    private IncomeType incomeType;

    public enum Frequency {
        MONTHLY, QUARTERLY, YEARLY
    }

    public enum IncomeType {
        SALARY, PENSION, ANNUITY, PF_WITHDRAWAL,
        FD_INTEREST, POCKET_MONEY, OTHER
    }

    public IncomeSource() {}

    public IncomeSource(User user, String sourceName, BigDecimal amount,
                        Frequency frequency, IncomeType incomeType) {
        this.user = user;
        this.sourceName = sourceName;
        this.amount = amount;
        this.frequency = frequency;
        this.incomeType = incomeType;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Frequency getFrequency() { return frequency; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }

    public IncomeType getIncomeType() { return incomeType; }
    public void setIncomeType(IncomeType incomeType) { this.incomeType = incomeType; }
}
