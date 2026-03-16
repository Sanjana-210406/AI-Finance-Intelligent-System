package com.javeriya.aifinance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "is_read")
    private boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum AlertType {
        WARNING,
        DANGER,
        SAVINGS_RISK,
        GOAL_DEADLINE,
        ANOMALY,
        PREDICTIVE,
        SUBSCRIPTION_LEAK,
        EMOTIONAL_SPEND,
        BEHAVIORAL_BOT
    }

    public Alert() {}

    public Alert(User user, String message, AlertType alertType) {
        this.user = user;
        this.message = message;
        this.alertType = alertType;
        this.createdAt = LocalDateTime.now();
    }

    public Alert(User user, String message, AlertType alertType, Long categoryId) {
        this.user = user;
        this.message = message;
        this.alertType = alertType;
        this.categoryId = categoryId;
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public AlertType getAlertType() { return alertType; }
    public void setAlertType(AlertType alertType) { this.alertType = alertType; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}