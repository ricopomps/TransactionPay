package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.transactionpay.model.Account;
import com.example.transactionpay.model.AccountCreator;
import com.example.transactionpay.model.User;
import com.example.transactionpay.service.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private EditText mCpf;
    private EditText mName;
    private EditText mPhone;
    private EditText mPws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mCpf = findViewById(R.id.cpf);
        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mPws = findViewById(R.id.pws);
    }

    public void signIn(View view){
        User user = new User("", mCpf.getText().toString(),mName.getText().toString(),"",Integer.parseInt(mPhone.getText().toString()),mPws.getText().toString());
        Call<User> call = new RetrofitConfig().getBankService().createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Call<Account> accountCall = new RetrofitConfig().getBankService().createAccount(mCpf.getText().toString(),mPws.getText().toString(),new AccountCreator(mCpf.getText().toString(),0,0));
                accountCall.enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {

                        AccountCreator accountCreator = new AccountCreator(mCpf.getText().toString(), 100, 1);
                        String cpf =mCpf.getText().toString();
                        String pws = mPws.getText().toString();
                        Call<Account> accountCall = new RetrofitConfig().getBankService().createAccount(cpf, pws, accountCreator);
                        accountCall.enqueue(new Callback<Account>() {
                            @Override
                            public void onResponse(Call<Account> call, Response<Account> response) {
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Account> call, Throwable t) {
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}