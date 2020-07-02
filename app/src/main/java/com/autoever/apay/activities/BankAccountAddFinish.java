package com.autoever.apay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.models.BankAccountInfo;

public class BankAccountAddFinish extends AppCompatActivity {
    private TextView finishTextView, finishMessage, finishDate, bankName, accountNumber;
    private ImageView bankLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardChargeFinishActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_add_finish);

        finishMessage = findViewById(R.id.add_account_finish_message);
        finishDate = findViewById(R.id.add_account_finish_date);
        bankName = findViewById(R.id.add_account_bank_name);
        accountNumber = findViewById(R.id.add_account_number);
        bankLogo = findViewById(R.id.add_account_bank_logo);
        finishTextView = findViewById(R.id.finish_textview);
        bankLogo = findViewById(R.id.add_account_bank_logo);


        Intent intent = getIntent();
        String selectedBankAccount = intent.getStringExtra("bank_account");
        String selectedBankName = intent.getStringExtra("bank_name");
        String bankAccountNumber = intent.getStringExtra("bank_account_number");
        String bankAccontAddDate = intent.getStringExtra("bank_account_add_date");

        BankAccountInfo bankAccountInfo = new BankAccountInfo(selectedBankAccount, selectedBankName, bankAccountNumber, bankAccontAddDate);

        finishMessage.setText(selectedBankName + " 계좌등록이 완료되었습니다");
        finishDate.setText(bankAccontAddDate);
        bankName.setText(selectedBankName);
        accountNumber.setText(bankAccountNumber);
        bankLogo.setImageResource(bankAccountInfo.getBankLogo());



        finishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
