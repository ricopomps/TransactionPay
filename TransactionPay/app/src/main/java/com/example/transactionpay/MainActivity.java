package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.Account;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list.add("deposit");
        list.add("transfer");
        Intent intent = getIntent();
        mUsername = findViewById(R.id.usernameShow);
        mBalance = findViewById(R.id.avaiableMoney);
        user = (User) intent.getSerializableExtra(LoginActivity.USER);
        mUsername.setText(user.getName());
        aSelectionAdapter = new SelectionAdapter(this, list);
        mSelectionRecyclerView = findViewById(R.id.recyclerView);
        mSelectionRecyclerView.setAdapter(aSelectionAdapter);
        mSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        aSelectionAdapter.list = list;
        mSelectionRecyclerView.getAdapter().notifyItemChanged(list.size());
        mSelectionRecyclerView.getAdapter().notifyDataSetChanged();
        getBalance();

    }


    public void goToTransactions(View view) {
        Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
        startActivityForResult(intent, REQUESTCODE);
    }



    public void getBalance() {
//
        Call<Account> call= new RetrofitConfig().getBankService().getAccount(user.getCpf(),user.getPws());
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                mBalance.setText(String.valueOf(account.getAccount_balance()));



            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });


    }
}