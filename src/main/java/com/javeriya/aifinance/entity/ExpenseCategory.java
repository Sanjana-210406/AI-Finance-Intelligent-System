package com.javeriya.aifinance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "expense_categories")
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type;

    @Column(name = "icon_code")
    private String iconCode;

    public enum CategoryType {
        NEED,
        WANT,
        INVESTMENT
    }

    public ExpenseCategory() {}

    public ExpenseCategory(String name, CategoryType type, String iconCode) {
        this.name = name;
        this.type = type;
        this.iconCode = iconCode;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CategoryType getType() { return type; }
    public void setType(CategoryType type) { this.type = type; }

    public String getIconCode() { return iconCode; }
    public void setIconCode(String iconCode) { this.iconCode = iconCode; }
}
