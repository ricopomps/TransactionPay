package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.example.transactionpay.model.Code;
import com.example.transactionpay.model.Status;
import com.example.transactionpay.model.Transaction;
import com.example.transactionpay.model.Transferencia;
import com.example.transactionpay.model.Type;
import com.example.transactionpay.model.User;
import com.example.transactionpay.service.RetrofitConfig;
import com.example.transactionpay.service.SelectionAdapter;

import org.w3c.dom.Text;

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
    private User user;
    private int stringId;
    private EditText editText2;
    private EditText confirmPassword;
    private CheckBox checkBox;
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
        checkBox = findViewById(R.id.checkBox);
        intent = getIntent();
        stringId = intent.getIntExtra(SelectionAdapter.EXTRA, 0);
        checkBox.setVisibility(View.INVISIBLE);
        if (MainActivity.sharedPreferences.getInt("userAccountStatus", 0) == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }


        switch (stringId) {
            case (R.string.deposit):
                title.setText(R.string.deposit);
                text1.setText("Amount");
                editText2.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.INVISIBLE);

                break;
            case (R.string.transfer):
                title.setText(R.string.transfer);
                text1.setText("Type target account");

                break;
            case (R.string.boleto):
                title.setText(R.string.boleto);
                break;
            case (R.string.config):
                title.setText(R.string.config);
                editText1.setInputType(InputType.TYPE_CLASS_TEXT);
                checkBox.setVisibility(View.VISIBLE);
                text1.setText("Name");
                editText1.setText(MainActivity.sharedPreferences.getString("userName", "default name"));
                text2.setText("Phone");
                editText2.setText(String.valueOf(MainActivity.sharedPreferences.getInt("userPhone", 0)));
                break;
            case (R.string.history):

                TransactionsActivity.this.startActivity(new Intent(TransactionsActivity.this, HistoryActivity.class));
                finish();
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
            Call<Account> call = new RetrofitConfig().getBankService().changeAccountStatus(MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Status(getResources().getInteger(R.integer.accountActiveStatus)));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if(response.code()!=400) {
                        MainActivity.editor.putInt("userAccountStatus", getResources().getInteger(R.integer.accountActiveStatus));
                        MainActivity.editor.apply();
                    } else {

                        Toast.makeText(TransactionsActivity.this,"Invalid password",Toast.LENGTH_LONG).show();

                    }
                }


                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    MainActivity.editor.putInt("userAccountStatus", getResources().getInteger(R.integer.accountActiveStatus));
                    MainActivity.editor.apply();


                }
            });

        } else {
            //deactivate account
            Call<Account> call = new RetrofitConfig().getBankService().changeAccountStatus(MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Status(getResources().getInteger(R.integer.accountCancelledStatus)));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if(response.code()!=400) {
                        MainActivity.editor.putInt("userAccountStatus", getResources().getInteger(R.integer.accountCancelledStatus));
                        MainActivity.editor.apply();
                    } else {

                        Toast.makeText(TransactionsActivity.this,"Invalid password",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    MainActivity.editor.putInt("userAccountStatus", getResources().getInteger(R.integer.accountCancelledStatus));
                    MainActivity.editor.apply();

                }
            });

        }
    }

    public void start(View view) {

        switch (stringId) {
            case (R.string.deposit):
                if (!isDouble(editText1.getText().toString())) {
                    Toast.makeText(TransactionsActivity.this, "Must be a valid amount", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Double.parseDouble(editText1.getText().toString()) <= 0) {
                    Toast.makeText(TransactionsActivity.this, "Must be a positive value", Toast.LENGTH_LONG).show();
                    return;
                }

                Call<Code> callDeposit = new RetrofitConfig().getBankService().deposit(MainActivity.sharedPreferences.getString("userAccount", ""), MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Amount(Double.parseDouble(editText1.getText().toString())));
                callDeposit.enqueue(new Callback<Code>() {
                    @Override
                    public void onResponse(Call<Code> call, Response<Code> response) {
                        if (response.code() != 400) {
                            Code code = response.body();
                            Intent intent = new Intent(TransactionsActivity.this, ReceiptActivity.class);
                            intent.putExtra("code", code);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(TransactionsActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Code> call, Throwable t) {
                        Toast.makeText(TransactionsActivity.this, "Unable to proceed without internet acess" , Toast.LENGTH_LONG).show();
                        finish();
                    }

                });

                break;
            case (R.string.transfer):
                if (!isDouble(editText2.getText().toString())) {
                    Toast.makeText(TransactionsActivity.this, "Must be a valid amount", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Double.parseDouble(editText2.getText().toString()) <= 0) {
                    Toast.makeText(TransactionsActivity.this, "Must be a positive value", Toast.LENGTH_LONG).show();
                    return;
                }
                if (editText1.getText().toString().equals(MainActivity.sharedPreferences.getString("userAccount", ""))) {
                    Toast.makeText(TransactionsActivity.this, "Source account is the same as target account", Toast.LENGTH_LONG).show();
                    break;
                }
                if (MainActivity.sharedPreferences.getFloat("userAccountBalance", 0) < Double.parseDouble(editText2.getText().toString())) {
                    Toast.makeText(TransactionsActivity.this, "Not enough money", Toast.LENGTH_LONG).show();
                    break;
                }
                try {
                    Account account = MainActivity.db.accountDao().getAccountByCode(editText1.getText().toString());
                    account.getCode();
                } catch (NullPointerException e){
                    Toast.makeText(TransactionsActivity.this, "invalid target account", Toast.LENGTH_LONG).show();
                    break;
                }
                call = new RetrofitConfig().getBankService().transfer(MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Transferencia(MainActivity.sharedPreferences.getString("userAccount", ""), editText1.getText().toString(), Double.parseDouble(editText2.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {

                        if (response.code() != 400) {
                            startActivity(new Intent(TransactionsActivity.this,ReceiptActivity.class));
                            finish();
                        } else {
                            Toast.makeText(TransactionsActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {

                        Toast.makeText(TransactionsActivity.this, "Unable to proceed without internet access", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });


                break;
            case (R.string.boleto):
                if (!isDouble(editText2.getText().toString())) {
                    Toast.makeText(TransactionsActivity.this, "Must be a valid amount", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Double.parseDouble(editText2.getText().toString()) <= 0) {
                    Toast.makeText(TransactionsActivity.this, "Must be a positive value", Toast.LENGTH_LONG).show();
                    return;
                }

                call = new RetrofitConfig().getBankService().payBoleto(MainActivity.sharedPreferences.getString("userAccount", ""), MainActivity.sharedPreferences.getString("userCpf", ""), confirmPassword.getText().toString(), new Boleto(editText1.getText().toString(), Double.parseDouble(editText2.getText().toString())));
                call.enqueue(new Callback<Transaction>() {
                    @Override
                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                        if (response.code() != 400) {
                            startActivity(new Intent(TransactionsActivity.this, ReceiptActivity.class));
                            finish();
                        } else {
                            Toast.makeText(TransactionsActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Transaction> call, Throwable t) {
                        Toast.makeText(TransactionsActivity.this, "Unable to proceed without internet access", Toast.LENGTH_LONG).show();

                        finish();
                    }
                });

                break;
            case (R.string.config):

                SharedPreferences sP = MainActivity.sharedPreferences;
                if (!isInt(editText2.getText().toString())) {
                    Toast.makeText(TransactionsActivity.this, "Not a valid phone number", Toast.LENGTH_LONG).show();
                    break;
                }
                user = new User(sP.getString("userId", ""), sP.getString("userCpf", ""), editText1.getText().toString(), sP.getString("userAvatar", ""), Integer.parseInt(editText2.getText().toString()), MainActivity.db.userDao().getUserByCpf(sP.getString("userCpf", "")).getPws());
                if (confirmPassword.getText().toString().equals(MainActivity.db.userDao().getUserByCpf(sP.getString("userCpf", "")).getPws())) {


                    Call<User> call = new RetrofitConfig().getBankService().updateUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() != 400) {
                                Toast.makeText(TransactionsActivity.this, "SUCCESS", Toast.LENGTH_LONG);
                                MainActivity.editor.putString("userName", user.getName());
                                MainActivity.editor.putInt("userPhone", user.getTelefone());
                                MainActivity.editor.apply();
                                finish();
                            } else {
                                Toast.makeText(TransactionsActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            if (confirmPassword.getText().toString().equals(MainActivity.db.userDao().getUserByCpf(MainActivity.sharedPreferences.getString("userCpf", "")).getPws())) {
                                Toast.makeText(TransactionsActivity.this, "Unable to proceed without internet access", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(TransactionsActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(TransactionsActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                }
                break;
            default:

                break;
        }


    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}