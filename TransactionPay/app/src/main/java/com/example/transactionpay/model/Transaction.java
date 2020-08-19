package com.example.transactionpay.model;

import java.util.Date;

public class Transaction {
    private int OriginalAccountNumber;
    private Date date;
    private double value;
    private int targetAccountNumber;

    public Transaction(int originalAccountNumber, Date date, double value, int targetAccountNumber) {
        OriginalAccountNumber = originalAccountNumber;
        this.date = date;
        this.value = value;
        this.targetAccountNumber = targetAccountNumber;
    }

    public int getOriginalAccountNumber() {
        return OriginalAccountNumber;
    }

    public void setOriginalAccountNumber(int originalAccountNumber) {
        OriginalAccountNumber = originalAccountNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(int targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }
}
