package com.example.transactionpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transactionpay.model.Code;
import com.example.transactionpay.model.Receipt;
import com.example.transactionpay.model.Transaction;
import com.example.transactionpay.service.RetrofitConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptActivity extends AppCompatActivity {
    private TextView mTransaction;
    private TextView mSourceAccount;
    private TextView mTargetAccount;
    private TextView mAmount;
    private TextView mDate;
    private TextView mCode;
    private LinearLayout mLayout;
    private Button mReceiptConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        mTransaction = findViewById(R.id.receiptTransactionType);
        mSourceAccount = findViewById(R.id.receiptSourceAccount);
        mTargetAccount = findViewById(R.id.receiptTargetAccount);
        mAmount = findViewById(R.id.receiptAmount);
        mDate = findViewById(R.id.receiptDate);
        mCode = findViewById(R.id.codigoBoletoText);
        mLayout = findViewById(R.id.codigoBoleto);
        mReceiptConfirm = findViewById(R.id.receiptConfirm);
        final ProgressDialog dialog = new ProgressDialog(ReceiptActivity.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        Call<List<Receipt>> call = new RetrofitConfig().getBankService().getHistory(MainActivity.sharedPreferences.getString("userCpf", ""), MainActivity.db.userDao().getUserByCpf(MainActivity.sharedPreferences.getString("userCpf", "")).getPws());
        call.enqueue(new Callback<List<Receipt>>() {
            @Override
            public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
                try {
                    Intent intent = getIntent();
                    Code code = (Code) intent.getSerializableExtra("code");
                    mCode.setText(code.getCodigo_de_barras());
                    mLayout.setVisibility(View.VISIBLE);

                } catch (NullPointerException e) {

                }
                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                List<Receipt> receipts = response.body();
                Collections.reverse(receipts);
                Receipt receipt = receipts.get(0);
                if (receipt.getSource_transaction() == 0) {
                    mTransaction.setText(getResources().getString(R.string.transfer));
                    mSourceAccount.setText("Source account: " + MainActivity.db.accountDao().getAccountCodeById(receipt.getBank_account().get(0)).getCode());
                    mTargetAccount.setText("Target account: " + MainActivity.db.accountDao().getAccountCodeById(receipt.getBank_account().get(1)).getCode());
                }
                if (receipt.getSource_transaction() == 1) {
                    mTransaction.setText(getResources().getString(R.string.deposit));
                    mSourceAccount.setVisibility(View.INVISIBLE);
                    mTargetAccount.setVisibility(View.INVISIBLE);
                }
                if (receipt.getSource_transaction() == 2) {
                    mTransaction.setText(getResources().getString(R.string.boleto));
                    mSourceAccount.setVisibility(View.INVISIBLE);
                    mTargetAccount.setVisibility(View.INVISIBLE);
                }
                mAmount.setText("Amount: R$" + String.valueOf(receipt.getAmount()));
                mDate.setText("Date: " + df.format(receipt.getCreatedAt()));
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Receipt>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void endReceipt(View view) {
        finish();
    }

    public void copyToClipboard(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(ReceiptActivity.this.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", mCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(ReceiptActivity.this, "Copied code to clipboard", Toast.LENGTH_LONG).show();
    }
}