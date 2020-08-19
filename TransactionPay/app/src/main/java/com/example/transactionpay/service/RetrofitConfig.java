package com.example.transactionpay.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    private Retrofit retrofit;

    public RetrofitConfig() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://newbank-backend.herokuapp.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
                .build();


    }


    public BankService getBankService() {
        return retrofit.create(BankService.class);
    }
}
