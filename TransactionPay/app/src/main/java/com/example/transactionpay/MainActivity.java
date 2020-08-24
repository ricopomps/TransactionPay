package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.Account;
import com.example.transactionpay.model.AccountCreator;
import com.example.transactionpay.model.Type;
import com.example.transactionpay.model.User;
import com.example.transactionpay.repository.AppDatabase;
import com.example.transactionpay.service.RetrofitConfig;
import com.example.transactionpay.service.SelectionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mBalance;
    private User user;
    private List<Integer> list = new ArrayList<>();
    private RecyclerView mSelectionRecyclerView;
    private SelectionAdapter aSelectionAdapter;
    public static final int REQUESTCODE = 55;
    public static SharedPreferences sharedPreferences;
    List<Account> accountList;
    public static AppDatabase db;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Shared", Context.MODE_PRIVATE);
        list.add((R.string.deposit));
        list.add((R.string.transfer));
        list.add((R.string.boleto));
        list.add((R.string.config));
        list.add((R.string.history));
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bank_database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        Intent intent = getIntent();
        mUsername = findViewById(R.id.usernameShow);
        mBalance = findViewById(R.id.avaiableMoney);
        user = (User) intent.getSerializableExtra(LoginActivity.USER);
        try {
            db.userDao().insertUsers(user);
        } catch (RuntimeException e) {

        }
        editor = sharedPreferences.edit();
        editor.putString("userName", user.getName());
        editor.putString("userCpf", user.getCpf());
        editor.putString("userId", user.get_id());
        editor.putString("userAvatar", user.getAvatar());
        editor.putInt("userPhone", user.getTelefone());
        editor.apply();
        mUsername.setText(db.userDao().getUserByCpf(user.getCpf()).getName());
        aSelectionAdapter = new SelectionAdapter(this, list);
        mSelectionRecyclerView = findViewById(R.id.mainActivityRecyclerView);
        mSelectionRecyclerView.setAdapter(aSelectionAdapter);
        mSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        aSelectionAdapter.list = list;
        mSelectionRecyclerView.getAdapter().notifyItemChanged(list.size());
        mSelectionRecyclerView.getAdapter().notifyDataSetChanged();
        getBalance();
        Call<List<Account>> call = new RetrofitConfig().getBankService().getAllAccounts("adminUser","123456");
        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                accountList = response.body();
                db.accountDao().deleteAccounts();
               for(int i = 0;i<response.body().size();i++){
                   try {
                       db.accountDao().insertAccounts(accountList.get(i));
                    } catch (Throwable e){

                   }
               }

            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
        mUsername.setText(sharedPreferences.getString("userName", "no name"));
    }

    public void trigger(View view) {
        getBalance();
    }

    public void getBalance() {
        mUsername.setText(sharedPreferences.getString("userName", "no name"));
        Call<Account> call = new RetrofitConfig().getBankService().getAccount(user.getCpf(), user.getPws());
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                mBalance.setText("R$ " + String.valueOf(account.getAccount_balance()));
                editor.putFloat("userAccountBalance", (float) account.getAccount_balance());
                editor.putString("userAccount", account.getCode());
                editor.putInt("userAccountStatus", account.getStatus());
                editor.apply();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
            }
        });


    }
}