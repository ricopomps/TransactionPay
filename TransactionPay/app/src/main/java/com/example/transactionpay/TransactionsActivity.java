package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.Account;
import com.example.transactionpay.model.AccountCreator;
import com.example.transactionpay.model.Amount;
import com.example.transactionpay.model.Boleto;
import com.example.transactionpay.model.Status;
import com.example.transactionpay.model.Transaction;
import com.example.transactionpay.model.Transferencia;
import com.example.transactionpay.model.Type;
import com.example.transactionpay.model.User;
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
    User user;
    private String selection;
    private EditText editText2;
    private EditText confirmPassword;
    private CheckBox checkBox;
    private Intent intent;
    private Intent backIntent;

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
        checkBox = findViewById(R.id.checkBox);
        intent = getIntent();
        backIntent = new Intent(TransactionsActivity.this, MainActivity.class);
        selection = intent.getStringExtra(SelectionAdapter.EXTRA);
        checkBox.setVisibility(View.INVISIBLE);
        if (MainActivity.sharedPreferences.getInt("userAccountStatus", 0) == 1) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        switch (selection) {
            case ("deposit"):
                title.setText("Deposito");
                text1.setText("Amount");
                editText2.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.INVISIBLE);

                break;
            case ("transferencia"):
                title.setText("Transferencia");
                text1.setText("Type target account");

                break;
            case ("boleto"):
                title.setText("Pagar boleto");
                break;
            case ("config"):
                title.setText("Configuration");
                checkBox.setVisibility(View.VISIBLE);
                text1.setText("Name");
                editText1.setText(MainActivity.sharedPreferences.getString("userName", "default name"));
                text2.setText("Phone");
                editText2.setText(String.valueOf(MainActivity.sharedPreferences.getInt("userPhone", 0)));
                break;
            case ("history"):

                TransactionsActivity.this.startActivity(new Intent(TransactionsActivity.this, HistoryActivity.class));
//                finish();
                break;
            default:
                title.setText("DEFAULT");
                break;
        }


    }

    public void onCheckBoxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            //activate account
            Call<Account> call = new RetrofitConfig().getBankService().changeAccountStatus(MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Status(1));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    MainActivity.editor.putInt("userAccountStatus", 1);
                    MainActivity.editor.apply();
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    MainActivity.editor.putInt("userAccountStatus", 1);
                    MainActivity.editor.apply();
                }
            });
        } else {
            //deactivate account
            Call<Account> call = new RetrofitConfig().getBankService().changeAccountStatus(MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Status(3));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    MainActivity.editor.putInt("userAccountStatus", 3);
                    MainActivity.editor.apply();

                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    MainActivity.editor.putInt("userAccountStatus", 3);
                    MainActivity.editor.apply();
                }
            });

        }
    }

    public void start(View view) {
        switch (selection) {
            case ("deposit"):
                call = new RetrofitConfig().getBankService().deposit(MainActivity.sharedPreferences.getString("userAccount", ""), MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Amount(Double.parseDouble(editText1.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        finish();
                    }
                });

                break;
            case ("transferencia"):
                text1.setText("Type target account");


                call = new RetrofitConfig().getBankService().transfer(MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Transferencia(MainActivity.sharedPreferences.getString("userAccount", ""), editText1.getText().toString(), Double.parseDouble(editText2.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        finish();
                    }
                });

                break;
            case ("boleto"):
                call = new RetrofitConfig().getBankService().payBoleto(MainActivity.sharedPreferences.getString("userAccount", ""), MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Boleto(editText1.getText().toString(), Double.parseDouble(editText2.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        finish();
                    }
                });
                break;
            case ("config"):
                SharedPreferences sP = MainActivity.sharedPreferences;
                user = new User(sP.getString("userId", ""), sP.getString("userCpf", ""), editText1.getText().toString(), sP.getString("userAvatar", ""), Integer.parseInt(editText2.getText().toString()), confirmPassword.getText().toString());
                Call<User> call = new RetrofitConfig().getBankService().updateUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        Toast.makeText(TransactionsActivity.this, "SUCESS", Toast.LENGTH_LONG);
                        MainActivity.editor.putString("userName", user.getName());
                        MainActivity.editor.putInt("userPhone", user.getTelefone());
                        MainActivity.editor.apply();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(TransactionsActivity.this, "FAILURE", Toast.LENGTH_LONG);
                    }
                });

                break;
            default:

                break;
        }


    }

}