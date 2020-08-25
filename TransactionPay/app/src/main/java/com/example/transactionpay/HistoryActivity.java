package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.transactionpay.R;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.Receipt;
import com.example.transactionpay.service.HistoryAdapter;
import com.example.transactionpay.service.RetrofitConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter aHistoryAdapter;
    private RecyclerView mHistoryRecyclerView;
    private List<Receipt> list = new ArrayList<>();
    private Button confirm;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        confirm = (Button) findViewById(R.id.ok);
        editText = findViewById(R.id.historyPws);
        aHistoryAdapter = new HistoryAdapter(this, list);
        mHistoryRecyclerView = findViewById(R.id.historyRecyclerView);
        mHistoryRecyclerView.setAdapter(aHistoryAdapter);
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }

    public void onClick(View view) {


        Call<List<Receipt>> call = new RetrofitConfig().getBankService().getHistory(MainActivity.sharedPreferences.getString("userCpf", ""), editText.getText().toString());
        call.enqueue(new Callback<List<Receipt>>() {
            @Override
            public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {

                if (response.code() != 400) {
                    editText.setVisibility(View.INVISIBLE);
                    confirm.setVisibility(View.INVISIBLE);
                    MainActivity.db.receiptDao().deleteReceipts();
                    for (int i = 0; i < response.body().size(); i++) {
                        MainActivity.db.receiptDao().insertReceipt(response.body().get(i));
                    }

                    list = response.body();
                    Collections.reverse(list);
                    aHistoryAdapter.list = list;
                    mHistoryRecyclerView.getAdapter().notifyItemChanged(response.body().size());
                    mHistoryRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(HistoryActivity.this,"Invalid password",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Receipt>> call, Throwable t) {
                if (editText.getText().toString().equals(MainActivity.db.userDao().getUserByCpf(MainActivity.sharedPreferences.getString("userCpf", "")).getPws())) {
                    editText.setVisibility(View.INVISIBLE);
                    confirm.setVisibility(View.INVISIBLE);
                    list = MainActivity.db.receiptDao().getAllReceipts();
                    Collections.reverse(list);
                    aHistoryAdapter.list = list;
                    mHistoryRecyclerView.getAdapter().notifyItemChanged(MainActivity.db.receiptDao().getAllReceipts().size());
                    mHistoryRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(HistoryActivity.this,"Invalid password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}