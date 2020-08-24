package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.User;
import com.example.transactionpay.repository.UserRepository;
import com.example.transactionpay.service.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Intent intent;
    private User user;
    public static final String USER = "USER";
    public static final int START = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(LoginActivity.this, MainActivity.class);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);


    }

    public void login(View view) {
        Call<User> call = new RetrofitConfig().getBankService().getUser(username.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.code() != 400) {
                    user = response.body();
                    intent.putExtra(USER, user);
                    startActivityForResult(intent, START);
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciais inválidas", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Get user", "Error in get user");
            }
        });

    }

    public void signIn(View view) {
        startActivityForResult(new Intent(LoginActivity.this, SignInActivity.class), START);
    }

}