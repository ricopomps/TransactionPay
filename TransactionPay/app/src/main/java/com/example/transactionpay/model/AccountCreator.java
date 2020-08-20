package com.example.transactionpay.model;

public class AccountCreator {
    private String cpf;
    private double account_balance;
    private double status;

    public AccountCreator() {
    }

    public AccountCreator(String cpf, double account_balance, double status) {
        this.cpf = cpf;
        this.account_balance = account_balance;
        this.status = status;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(double account_balance) {
        this.account_balance = account_balance;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }
}
