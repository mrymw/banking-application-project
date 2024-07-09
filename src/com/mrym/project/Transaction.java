package com.mrym.project;

import java.time.LocalDateTime;

public class Transaction {
    private LocalDateTime date;
    private String transactionType;
    private double amount;
    private double balanceAfter;
    private String accountType;

    public Transaction(LocalDateTime date, String transactionType, double amount, double balanceAfter, String accountType) {
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.accountType = accountType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getAccountType() {
        return accountType;
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
