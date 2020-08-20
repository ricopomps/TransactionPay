package com.example.transactionpay.model;

public class Transferencia {
    private String origem;
    private String destino;
    private double amount;

    public Transferencia() {
    }

    public Transferencia(String origem, String destino, double amount) {
        this.origem = origem;
        this.destino = destino;
        this.amount = amount;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
