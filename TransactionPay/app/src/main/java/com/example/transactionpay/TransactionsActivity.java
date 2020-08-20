package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.Amount;
import com.example.transactionpay.model.Boleto;
import com.example.transactionpay.model.Transaction;
import com.example.transactionpay.model.Transferencia;
import com.example.transactionpay.service.RetrofitConfig;
import com.example.transactionpay.service.SelectionAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;

public class TransactionsActivity extends AppCompatActivity {
    private TextView text1;
    private TextView text2;
    private TextView title;
    private EditText editText1;
    private Call<Transaction> call;
    private String selection;
    private EditText editText2;
    private EditText confirmPassword;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        text1 = findViewById(R.id.firstTextView);
        text2 = findViewById(R.id.secondTextView);
        title = findViewById(R.id.title);
        editText1 = findViewById(R.id.firstEditText);
        editText2 = findViewById(R.id.secondEditText);
        confirmPassword = findViewById(R.id.confirmPassword);
        Intent intent = getIntent();
        selection = intent.getStringExtra(SelectionAdapter.EXTRA);
        switch (selection) {
            case ("deposit"):
                title.setText("Deposito");
                text1.setText("Ammount");
                editText2.setWidth(1);
                editText2.setHeight(1);
                text2.setText(" ");
                break;
            case ("transferencia"):
                title.setText("Transferencia");
                text1.setText("Type target account");

                break;
            case ("boleto"):
                title.setText("Pagar boleto");
                break;
            default:

                break;
        }


    }


    public void start(View view) {
        switch (selection) {
            case ("deposit"):
                call = new RetrofitConfig().getBankService().deposit(MainActivity.sharedPreferences.getString("userAccount", ""),MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Amount(Double.parseDouble(editText1.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {

                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {

                    }
                });

                break;
            case ("transferencia"):
                text1.setText("Type target account");


                call = new RetrofitConfig().getBankService().transfer(MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Transferencia(MainActivity.sharedPreferences.getString("userAccount", ""), editText1.getText().toString(), Double.parseDouble(editText2.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        Toast.makeText(TransactionsActivity.this, "SUcess", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        Toast.makeText(TransactionsActivity.this, "nope", Toast.LENGTH_LONG);
                    }
                });
                break;
            case ("boleto"):
                call = new RetrofitConfig().getBankService().payBoleto(MainActivity.sharedPreferences.getString("userAccount", ""), MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Boleto(editText1.getText().toString(), Double.parseDouble(editText2.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {

                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {

                    }
                });
                break;
            default:

                break;
        }



    }

}