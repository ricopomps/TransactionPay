package com.example.transactionpay.service;

import com.example.transactionpay.model.Account;
import com.example.transactionpay.model.AccountCreator;
import com.example.transactionpay.model.Amount;
import com.example.transactionpay.model.Boleto;
import com.example.transactionpay.model.Code;
import com.example.transactionpay.model.Receipt;
import com.example.transactionpay.model.Status;
import com.example.transactionpay.model.Transaction;
import com.example.transactionpay.model.Transferencia;
import com.example.transactionpay.model.Type;
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
    Call<List<User>> getAllUsers(@Header("cpf") String adiminCpf);

    @PUT("user/update")
    Call<User> updateUser(@Body User user);

    @POST("accounts")
    Call<Account> createAccount(@Header("cpf") String cpf, @Header("pws") String pws, @Body AccountCreator accountCreator);

    @GET("accounts")
    Call<Account> getAccount(@Header("cpf") String cpf, @Header("pws") String pws);

    @PUT("accounts")
    Call<Account> changeAccountStatus(@Header("cpf") String cpf, @Header("pws") String pws, @Body Status status);

    @GET("getAllAccounts")
    Call<List<Account>> getAllAccounts(@Header("cpf") String cpf, @Header("pws") String pws);

    @POST("transaction/pagamento")
    Call<Transaction> payBoleto(@Header("account") String account, @Header("cpf") String cpf, @Header("pws") String pws, @Body Boleto boleto);

    @POST("transaction/transferencia")
    Call<Transaction> transfer(@Header("cpf") String cpf, @Header("pws") String pws, @Body Transferencia transferencia);

    @POST("transaction/deposito")
    Call<Code> deposit(@Header("account") String account, @Header("cpf") String cpf, @Header("pws")String pws, @Body Amount amount);

    @GET("transaction/getByUser")
    Call<List<Receipt>> getHistory(@Header("cpf") String cpf, @Header("pws") String pws);


}
