package com.example.transactionpay.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transactionpay.R;
import com.example.transactionpay.model.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private final LayoutInflater layoutInflater;
    private Context context;
    public List<Receipt> list;
    public HistoryAdapter(Context context, List<Receipt> list) {
        this.context = context;
        this.list = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.history_layout, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Receipt receipt = list.get(position);
        holder.transactionType.setText("unknow");
        holder.sourceAccount.setText("Source account: " + receipt.getBank_account().get(0));
        holder.targetAccount.setText("Target account: " + receipt.getBank_account().get(1));
        holder.sourceAccount.setVisibility(View.INVISIBLE);
        holder.targetAccount.setVisibility(View.INVISIBLE);
        if (receipt.getSource_transaction() == 0) {
            holder.transactionType.setText(context.getString(R.string.transfer));
            holder.sourceAccount.setVisibility(View.VISIBLE);
            holder.targetAccount.setVisibility(View.VISIBLE);

        }
        if (receipt.getSource_transaction() == 1) {
            holder.transactionType.setText(context.getString(R.string.deposit));

        }

        if (receipt.getSource_transaction() == 2) {
            holder.transactionType.setText(context.getString(R.string.boleto));
        }

        holder.amount.setText("Amount: R$" + String.valueOf(receipt.getAmount()));
        holder.date.setText("Date: " + df.format(receipt.getCreatedAt()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView transactionType;
        private TextView sourceAccount;
        private TextView targetAccount;
        private TextView amount;
        private TextView date;
        private ConstraintLayout layout;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.history);
            transactionType = itemView.findViewById(R.id.transactionType);
            sourceAccount = itemView.findViewById(R.id.sourceAccount);
            targetAccount = itemView.findViewById(R.id.targetAccount);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
