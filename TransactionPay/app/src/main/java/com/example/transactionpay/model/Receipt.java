package com.example.transactionpay.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;


@JsonIgnoreProperties({"updatedAt", "__v"})
@Entity(tableName = "receipt")
public class Receipt {
    @PrimaryKey @NonNull
    private String _id;
    @ColumnInfo(name = "bank_account")
    private List<String> bank_account;
    @ColumnInfo(name = "source_transaction")
    private int source_transaction;
    @ColumnInfo(name = "amount")
    private double amount;
    @ColumnInfo(name = "createdAt")
    private Date createdAt;

    public Receipt() {
    }

    public Receipt(List<String> bank_account, String _id, int source_transaction, double amount, Date createdAt) {
        this.bank_account = bank_account;
        this._id = _id;
        this.source_transaction = source_transaction;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public List<String> getBank_account() {
        return bank_account;
    }

    public void setBank_account(List<String> bank_account) {
        this.bank_account = bank_account;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
