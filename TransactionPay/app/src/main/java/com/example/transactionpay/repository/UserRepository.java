package com.example.transactionpay.repository;

import android.util.Log;

import com.example.transactionpay.model.User;
import com.example.transactionpay.service.BankService;
import com.example.transactionpay.service.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

   private BankService bankService;
   List<User> listUsers;
   User user;

   public List<User> getAll(String adminCPF){
       Call<List<User>> call = new RetrofitConfig().getBankService().getAllUsers(adminCPF);
       call.enqueue(new Callback<List<User>>() {
           @Override
           public void onResponse(Call<List<User>> call, Response<List<User>> response) {
               listUsers = response.body();
           }

           @Override
           public void onFailure(Call<List<User>> call, Throwable t) {
               Log.e("List All","N funfou o get all");
           }
       });

       return listUsers;
   }

   public User getUser(String CPF, String pws){
       Call<User> call = new RetrofitConfig().getBankService().getUser(CPF,pws);
       call.enqueue(new Callback<User>() {
           @Override
           public void onResponse(Call<User> call, Response<User> response) {
               user = response.body();
           }

           @Override
           public void onFailure(Call<User> call, Throwable t) {
               Log.e("Get user","N funfou o get user");
           }
       });
       return user;
   }

}
