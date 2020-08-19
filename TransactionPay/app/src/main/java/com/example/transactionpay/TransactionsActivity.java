package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class TransactionsActivity extends AppCompatActivity {
    private LinearLayout mVendingsLayout;
    private LinearLayout mTransactionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        mVendingsLayout = findViewById(R.id.vendings);
        mTransactionLayout = findViewById(R.id.transactions);
        mVendingsLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        mTransactionLayout.setBackgroundColor(Color.parseColor("#ff00ddff"));
    }

    public void goToVendings(View view){
        Intent intent = new Intent(TransactionsActivity.this,MainActivity.class);
        startActivityForResult(intent,MainActivity.REQUESTCODE);
    }
}