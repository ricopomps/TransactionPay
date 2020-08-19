package com.example.transactionpay.model;

public class Account {
    private String agency;
    private int accountNumber;
    private String ownerCPF;
    private double balance;
    private int status;

    public Account(String agency, int accountNumber, String ownerCPF, double balance, int status) {
        this.agency = agency;
        this.accountNumber = accountNumber;
        this.ownerCPF = ownerCPF;
        this.balance = balance;
        this.status = status;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerCPF() {
        return ownerCPF;
    }

    public void setOwnerCPF(String ownerCPF) {
        this.ownerCPF = ownerCPF;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
