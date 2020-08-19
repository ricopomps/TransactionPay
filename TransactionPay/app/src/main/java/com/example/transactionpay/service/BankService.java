package com.example.transactionpay.service;

import com.example.transactionpay.model.Account;
import com.example.transactionpay.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface BankService {

    @POST("users")
    Call<User> createUser(@Body User user);

    @GET("users")
    Call<User> getUser(@Header("cpf") String cpf, @Header("pws") String pws);

    @GET("getAllUsers")
    Call<List<User>> getAllUsers(String adiminCpf);

    @PUT("user/update")
    Call<User> updateUser(User user);

    @POST("accounts")
    Call<Account> createAccount(@Header("cpf")String cpf,@Header("pws")String pws);
//create needs body
    @GET("accounts")
    Call<Account> getAccount(@Header("cpf") String cpf,@Header("pws") String pws);

    @PUT("transaction")
    Call<Account> changeAccountStatus(@Header("account") int accountNumber,@Header("cpf") String cpf,@Header("pws") String pws);
//change needs body
    @GET("getAllAccounnts")
    Call<List<Account>> getAllAccounts(@Header("cpf") String cpf,@Header("pws") String pws);


}
