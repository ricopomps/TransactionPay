package com.example.transactionpay.service;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transactionpay.R;
import com.example.transactionpay.TransactionsActivity;

import java.util.List;

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
        switch (stringId) {
            case (R.string.history):
                holder.selectionImage.setImageResource(R.drawable.historynoback);
                break;
            case  (R.string.deposit):
                holder.selectionImage.setImageResource(R.drawable.deposit);
                break;
            case (R.string.transfer):
                holder.selectionImage.setImageResource(R.drawable.transfer);
                break;
            case (R.string.config):
                holder.selectionImage.setImageResource(R.drawable.confignoback);
                break;
            case (R.string.boleto):
                holder.selectionImage.setImageResource(R.drawable.boleto);
                break;
            case (R.string.logoff):
                holder.selectionImage.setImageResource(R.drawable.logoff);
                break;
        }

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class SelectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView selectionText;
        private ImageView selectionImage;
        private ConstraintLayout selectionLayout;

        public SelectionHolder(@NonNull View itemView) {
            super(itemView);
            selectionLayout = itemView.findViewById(R.id.layoutObject);
            selectionImage = itemView.findViewById(R.id.layoutImage);
            selectionText = itemView.findViewById(R.id.layoutText);
            selectionLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, TransactionsActivity.class);
            Integer selection = list.get(getLayoutPosition());
            intent.putExtra(EXTRA, selection);
            context.startActivity(intent);
//


        }
    }
}
