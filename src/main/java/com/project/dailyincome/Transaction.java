package com.project.dailyincome;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private double amount;

    private String category;

    private String type;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    // USER RELATIONSHIP
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Default Constructor
    public Transaction() {
    }

    // Parameterized Constructor
    public Transaction(Long id, String description,
                       double amount,
                       String category,
                       String type,
                       LocalDate transactionDate) {

        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    // USER GETTER & SETTER

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}