package com.example.transactionpay.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.transactionpay.dao.AccountDao;
import com.example.transactionpay.dao.UserDao;
import com.example.transactionpay.model.Account;
import com.example.transactionpay.model.User;



@Database(entities = {User.class, Account.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract UserDao userDao();
    public abstract AccountDao accountDao();

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "bank_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
