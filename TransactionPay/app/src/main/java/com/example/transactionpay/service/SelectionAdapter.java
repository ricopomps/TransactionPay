package com.example.transactionpay.service;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transactionpay.MainActivity;
import com.example.transactionpay.R;
import com.example.transactionpay.TransactionsActivity;
import com.example.transactionpay.model.Transaction;
import com.example.transactionpay.model.Transferencia;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.SelectionHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    public List<Integer> list;
    public static final String EXTRA = "EXTRA";

    public SelectionAdapter(Context context, List<Integer> list) {
        this.list = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SelectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout, parent, false);
        return new SelectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionHolder holder, int position) {
        int stringId = list.get(position);
        holder.selectionText.setText(context.getString(stringId));
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class SelectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView selectionText;
        private ConstraintLayout selectionLayout;

        public SelectionHolder(@NonNull View itemView) {
            super(itemView);
            selectionLayout = itemView.findViewById(R.id.layoutObject);
            selectionText = itemView.findViewById(R.id.layoutText);
            selectionLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent= new Intent(context,TransactionsActivity.class);
            Integer selection =list.get(getLayoutPosition());
            intent.putExtra(EXTRA,selection);
            context.startActivity(intent);
//


        }
    }
}
