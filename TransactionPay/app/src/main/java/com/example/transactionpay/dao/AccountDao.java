package com.example.transactionpay.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.transactionpay.model.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account WHERE _id is :id")
    Account getAccountCodeById(String id);

    @Insert
    void insertAccounts(List<Account> accounts);
}
