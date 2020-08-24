package com.example.transactionpay.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.transactionpay.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE cpf is :cpf")
    User getUserByCpf(String cpf);

    @Insert
    void insertUsers(User... user);

    @Query("DELETE FROM user")
    void deleteUsers();
}
