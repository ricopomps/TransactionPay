package com.example.transactionpay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties({"createdAt", "updatedAt", "__v"})
public class Account implements Serializable {
    private String _id;
    private String bank_branch;
    private String code;
    private String user;
    private double account_balance;
    private int status;

    public  Account(){
        
    }

    public Account(String _id, String bank_branch, String code,String user, double balance, int status) {
        this._id = _id;
        this.bank_branch = bank_branch;
        this.code = code;
        this.user = user;
        this.account_balance = balance;
        this.status = status;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String get_id(){
        return _id;
    }

    public String getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(String bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(double account_balance) {
        this.account_balance = account_balance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
