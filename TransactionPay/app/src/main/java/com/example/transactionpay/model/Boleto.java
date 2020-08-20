package com.example.transactionpay.model;

public class Boleto {
    private String boleto;
    private double amount;

    public Boleto() {
    }

    public Boleto(String boleto, double amount) {
        this.boleto = boleto;
        this.amount = amount;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
