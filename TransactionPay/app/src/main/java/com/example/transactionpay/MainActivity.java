package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<String> list = new ArrayList<>();
    private RecyclerView mSelectionRecyclerView;
    private SelectionAdapter aSelectionAdapter;
    public static final int REQUESTCODE = 55;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //MAIN PROBLEMS
        //cancelar conta, getByUser, historico de transações, maybe give the boleto codigo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("Shared", Context.MODE_PRIVATE);
        list.add("deposit");
        list.add("transferencia");
        list.add("boleto");
        list.add("config");
        list.add("history");

        Intent intent = getIntent();
        mUsername = findViewById(R.id.usernameShow);
        mBalance = findViewById(R.id.avaiableMoney);
        user = (User) intent.getSerializableExtra(LoginActivity.USER);
        editor = sharedPreferences.edit();
        editor.putString("userName", user.getName());
        editor.putString("userCpf", user.getCpf());
        editor.putString("userId", user.get_id());
        editor.putString("userAvatar", user.getAvatar());
        editor.putInt("userPhone", user.getTelefone());
        editor.apply();
        mUsername.setText(sharedPreferences.getString("userName", "no name"));
        aSelectionAdapter = new SelectionAdapter(this, list);
        mSelectionRecyclerView = findViewById(R.id.mainActivityRecyclerView);
        mSelectionRecyclerView.setAdapter(aSelectionAdapter);
        mSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        aSelectionAdapter.list = list;
        mSelectionRecyclerView.getAdapter().notifyItemChanged(list.size());
        mSelectionRecyclerView.getAdapter().notifyDataSetChanged();
        getBalance();
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

    public void goToActivity(Intent intent) {
        startActivityForResult(intent, 20);
    }


    public void getBalance() {
        mUsername.setText(sharedPreferences.getString("userName","no name"));
        Call<Account> call = new RetrofitConfig().getBankService().getAccount(user.getCpf(), user.getPws());
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                mBalance.setText("R$ " + String.valueOf(account.getAccount_balance()));
                editor.putFloat("userAccountBalance",(float) account.getAccount_balance());
                editor.putString("userAccount", account.getCode());
                editor.putInt("userAccountStatus",account.getStatus());
                editor.apply();


            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });


    }
}