package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.Account;
import com.example.transactionpay.model.AccountCreator;
import com.example.transactionpay.model.Receipt;
import com.example.transactionpay.model.Type;
import com.example.transactionpay.model.User;
import com.example.transactionpay.repository.AppDatabase;
import com.example.transactionpay.service.RetrofitConfig;
import com.example.transactionpay.service.SelectionAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mBalance;
    private TextView mAccountCode;
    private TextView mLastTransaction;
    private TextView mSecondLastTransaction;
    private User user;
    private List<Integer> list = new ArrayList<>();
    private RecyclerView mSelectionRecyclerView;
    private SelectionAdapter aSelectionAdapter;
    public static final int REQUESTCODE = 55;
    public static SharedPreferences sharedPreferences;
    List<Account> accountList;
    public static AppDatabase db;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Shared", Context.MODE_PRIVATE);
        list.add((R.string.deposit));
        list.add((R.string.transfer));
        list.add((R.string.boleto));
        list.add((R.string.config));
        list.add((R.string.history));
        list.add((R.string.logoff));
//        list.add((R.string.recycle));
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bank_database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        Intent intent = getIntent();
        mUsername = findViewById(R.id.usernameShow);
        mBalance = findViewById(R.id.avaiableMoney);
        mAccountCode = findViewById(R.id.accountCode);
        mLastTransaction = findViewById(R.id.lastTransaction);
        mSecondLastTransaction = findViewById(R.id.secondLastTransaction);
        user = (User) intent.getSerializableExtra(LoginActivity.USER);
        try {
            db.userDao().insertUsers(user);
        } catch (RuntimeException e) {

        }
        editor = sharedPreferences.edit();
        editor.putString("userName", user.getName());
        editor.putString("userCpf", user.getCpf());
        editor.putString("userId", user.get_id());
        editor.putString("userAvatar", user.getAvatar());
        editor.putInt("userPhone", user.getTelefone());
        editor.apply();
        mUsername.setText(db.userDao().getUserByCpf(user.getCpf()).getName());
        aSelectionAdapter = new SelectionAdapter(this, list);
        mSelectionRecyclerView = findViewById(R.id.mainActivityRecyclerView);
        mSelectionRecyclerView.setAdapter(aSelectionAdapter);
        mSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        aSelectionAdapter.list = list;
        mSelectionRecyclerView.getAdapter().notifyItemChanged(list.size());
        mSelectionRecyclerView.getAdapter().notifyDataSetChanged();
        getBalance();
        refreshDatabase();
        getLastTransaction();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
        getLastTransaction();
        mUsername.setText(sharedPreferences.getString("userName", "no name"));
    }

    public void trigger(View view) {
        getBalance();
    }

    public void getBalance() {
        mUsername.setText(sharedPreferences.getString("userName", "no name"));
        mAccountCode.setText("Account code: " + sharedPreferences.getString("userAccountCode", ""));
        Call<Account> call = new RetrofitConfig().getBankService().getAccount(user.getCpf(), user.getPws());
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                mBalance.setText("R$ " + new DecimalFormat("#0.00").format((account.getAccount_balance())));
                editor.putFloat("userAccountBalance", (float) account.getAccount_balance());
                editor.putString("userAccount", account.getCode());
                editor.putInt("userAccountStatus", account.getStatus());
                editor.putString("userAccountCode", account.getCode());
                mAccountCode.setText("Account code: " + sharedPreferences.getString("userAccountCode", ""));
                if (account.getStatus() == 1) {
                    mAccountCode.setTextColor(getColor(R.color.colorWhiteWhite));
                } else {
                    mAccountCode.setTextColor(getColor(R.color.colorRed));
                }
                editor.apply();
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Account account = db.accountDao().getAccountByUser(user.get_id());
                mBalance.setText("R$ " + new DecimalFormat("#0.00").format((account.getAccount_balance())));;
                editor.putFloat("userAccountBalance", (float) account.getAccount_balance());
                editor.putString("userAccount", account.getCode());
                editor.putInt("userAccountStatus", account.getStatus());
                editor.putString("userAccountCode", account.getCode());
                editor.apply();
            }
        });


    }

    public void refreshDatabase() {
        Call<List<Account>> call = new RetrofitConfig().getBankService().getAllAccounts("adminUser", "123456");
        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                accountList = response.body();
                db.accountDao().deleteAccounts();
                for (int i = 0; i < response.body().size(); i++) {
                    try {
                        db.accountDao().insertAccounts(accountList.get(i));
                    } catch (Throwable e) {

                    }
                }

            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });

        Call<List<User>> userCall = new RetrofitConfig().getBankService().getAllUsers("userAdmin");
        userCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                db.userDao().deleteUsers();
                for (int i = 0; i < response.body().size(); i++) {
                    db.userDao().insertUsers(response.body().get(i));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });


    }

    public void getLastTransaction() {
        Call<List<Receipt>> call = new RetrofitConfig().getBankService().getHistory(MainActivity.sharedPreferences.getString("userCpf", ""), MainActivity.db.userDao().getUserByCpf(MainActivity.sharedPreferences.getString("userCpf", "")).getPws());
        call.enqueue(new Callback<List<Receipt>>() {
            @Override
            public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
                List<Receipt> receipts = response.body();
                Collections.reverse(receipts);
                try {
                    if (receipts.get(0).getSource_transaction() == 0) {
                        mLastTransaction.setText("Transfer: " + receipts.get(0).getAmount());
                        if (MainActivity.db.accountDao().getAccountCodeById(receipts.get(0).getBank_account().get(0)).getCode().equals(MainActivity.sharedPreferences.getString("userAccountCode", ""))) {
                            mLastTransaction.setTextColor(getResources().getColor(R.color.colorRed));
                        } else {
                            mLastTransaction.setTextColor(getResources().getColor(R.color.colorForest));
                        }

                    }
                    if (receipts.get(0).getSource_transaction() == 1) {
                        mLastTransaction.setText("Deposit: " + receipts.get(0).getAmount());
                        mLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorForest));
                    }
                    if (receipts.get(0).getSource_transaction() == 2) {
                        mLastTransaction.setText("Boleto: " + receipts.get(0).getAmount());
                        mLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorRed));
                    }
                    if (receipts.get(1).getSource_transaction() == 0) {
                        mSecondLastTransaction.setText("Transfer: " + receipts.get(1).getAmount());
                        if (MainActivity.db.accountDao().getAccountCodeById(receipts.get(1).getBank_account().get(0)).getCode().equals(MainActivity.sharedPreferences.getString("userAccountCode", ""))) {
                            mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorRed));
                        } else {
                            mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorForest));
                        }
                    }
                    if (receipts.get(1).getSource_transaction() == 1) {
                        mSecondLastTransaction.setText("Deposit: " + receipts.get(1).getAmount());
                        mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorForest));
                    }
                    if (receipts.get(1).getSource_transaction() == 2) {
                        mSecondLastTransaction.setText("Boleto: " + receipts.get(1).getAmount());
                        mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorRed));
                    }

                } catch (NullPointerException e) {

                } catch (IndexOutOfBoundsException e) {

                }
            }

            @Override
            public void onFailure(Call<List<Receipt>> call, Throwable t) {

                List <Receipt> receipts = MainActivity.db.receiptDao().getAllReceipts();
                Collections.reverse(receipts);
                try {
                    if (receipts.get(0).getSource_transaction() == 0) {
                        mLastTransaction.setText("Transfer: " + receipts.get(0).getAmount());
                        if (MainActivity.db.accountDao().getAccountCodeById(receipts.get(0).getBank_account().get(0)).getCode().equals(MainActivity.sharedPreferences.getString("userAccountCode", ""))) {
                            mLastTransaction.setTextColor(getResources().getColor(R.color.colorRed));
                        } else {
                            mLastTransaction.setTextColor(getResources().getColor(R.color.colorForest));
                        }

                    }
                    if (receipts.get(0).getSource_transaction() == 1) {
                        mLastTransaction.setText("Deposit: " + receipts.get(0).getAmount());
                        mLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorForest));
                    }
                    if (receipts.get(0).getSource_transaction() == 2) {
                        mLastTransaction.setText("Boleto: " + receipts.get(0).getAmount());
                        mLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorRed));
                    }
                    if (receipts.get(1).getSource_transaction() == 0) {
                        mSecondLastTransaction.setText("Transfer: " + receipts.get(1).getAmount());
                        if (MainActivity.db.accountDao().getAccountCodeById(receipts.get(1).getBank_account().get(0)).getCode().equals(MainActivity.sharedPreferences.getString("userAccountCode", ""))) {
                            mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorRed));
                        } else {
                            mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorForest));
                        }
                    }
                    if (receipts.get(1).getSource_transaction() == 1) {
                        mSecondLastTransaction.setText("Deposit: " + receipts.get(1).getAmount());
                        mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorForest));
                    }
                    if (receipts.get(1).getSource_transaction() == 2) {
                        mSecondLastTransaction.setText("Boleto: " + receipts.get(1).getAmount());
                        mSecondLastTransaction.setTextColor(MainActivity.this.getColor(R.color.colorRed));
                    }

                } catch (NullPointerException e) {

                } catch (IndexOutOfBoundsException e) {

                }
            }
        });


    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
