package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private TextView username;
private TextView password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);
        Intent intent = getIntent();
        username.setText((String)intent.getStringExtra(LoginActivity.USERNAME));
        password.setText((String)intent.getStringExtra(LoginActivity.PASSWORD));
}
}