package com.example.transactionpay.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transactionpay.R;
import com.example.transactionpay.model.Receipt;

import java.util.Date;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeHolder> {
    private final LayoutInflater layoutInflater;
    public List<Date> list;
    public List<Receipt> receipts;
    public HistoryAdapter aHistoryAdapter;
    public RecyclerView mHistoryRecyclerView;
    private Context context;

    public TimeAdapter(Context context, List<Date> list, List<Receipt> receipts) {
        this.context = context;
        this.list = list;
        this.receipts = receipts;
        this.layoutInflater = LayoutInflater.from(context);


    }

    @NonNull
    @Override
    public TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.timelayout, parent, false);
        return new TimeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TimeHolder extends RecyclerView.ViewHolder {

        public TimeHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
