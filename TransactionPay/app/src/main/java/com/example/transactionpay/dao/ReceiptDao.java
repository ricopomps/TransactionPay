package com.example.transactionpay.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.transactionpay.model.Receipt;

import java.util.Date;
import java.util.List;

@Dao
public interface ReceiptDao {

    @Insert
    void insertReceipt(Receipt... receipts);

    @Query("SELECT * FROM receipt")
    List<Receipt> getAllReceipts();

    @Query("DELETE FROM receipt")
    void deleteReceipts();

    @Query("SELECT * FROM receipt WHERE createdAt IS :date")
    List<Receipt> getReceiptsByDate(Date date);
}
