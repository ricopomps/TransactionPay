package com.example.transactionpay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


@JsonIgnoreProperties({"codigo_de_barras","account","boleto","mensagem","comprovante", "bank_account", "createdAt", "updatedAt", "__v"})
public class Transaction {
    private String _id;
    private int source_transaction;
    private Date date;
    private double value;
    private int targetAccountNumber;

    public Transaction() {

    }

    public Transaction(String _id, int originalAccountNumber, Date date, double value, int targetAccountNumber) {
        this._id = _id;
        source_transaction = originalAccountNumber;
        this.date = date;
        this.value = value;
        this.targetAccountNumber = targetAccountNumber;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getSource_transaction() {
        return source_transaction;
    }

    public void setSource_transaction(int source_transaction) {
        this.source_transaction = source_transaction;
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
