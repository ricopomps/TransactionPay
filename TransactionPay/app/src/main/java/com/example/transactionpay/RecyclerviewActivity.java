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
import com.example.transactionpay.service.TimeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerviewActivity extends AppCompatActivity {

    private TimeAdapter aTimeAdapter;
    private RecyclerView mTimeRecyclerView;
    private List<Receipt> receipts = new ArrayList<>();
    private List<Date> list = new ArrayList<>();
    private Button confirm;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse("22/08/2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list.add(d);
        confirm = (Button) findViewById(R.id.ok);
        editText = findViewById(R.id.historyPws);
        aTimeAdapter = new TimeAdapter(this, list, receipts);
        mTimeRecyclerView = findViewById(R.id.timeRecyclerView);
        mTimeRecyclerView.setAdapter(aTimeAdapter);
        mTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        aTimeAdapter.aHistoryAdapter = new HistoryAdapter(RecyclerviewActivity.this, receipts);
        aTimeAdapter.mHistoryRecyclerView = findViewById(R.id.historyRecyclerView);
        aTimeAdapter.mHistoryRecyclerView.setAdapter(aTimeAdapter.aHistoryAdapter);
        aTimeAdapter.mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(RecyclerviewActivity.this, LinearLayoutManager.VERTICAL, false));


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

                    receipts = response.body();
                    Collections.reverse(receipts);
                    aTimeAdapter.receipts = receipts;
                    aTimeAdapter.list = list;
                    mTimeRecyclerView.getAdapter().notifyItemChanged(response.body().size());
                    mTimeRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(RecyclerviewActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Receipt>> call, Throwable t) {
                if (editText.getText().toString().equals(MainActivity.db.userDao().getUserByCpf(MainActivity.sharedPreferences.getString("userCpf", "")).getPws())) {
                    editText.setVisibility(View.INVISIBLE);
                    confirm.setVisibility(View.INVISIBLE);
                    receipts = MainActivity.db.receiptDao().getAllReceipts();
                    Collections.reverse(receipts);
                    aTimeAdapter.receipts = receipts;
                    mTimeRecyclerView.getAdapter().notifyItemChanged(MainActivity.db.receiptDao().getAllReceipts().size());
                    mTimeRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(RecyclerviewActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}