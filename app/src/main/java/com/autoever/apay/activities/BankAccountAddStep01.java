package com.autoever.apay.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;

import java.util.Arrays;
import java.util.List;

public class BankAccountAddStep01 extends AppCompatActivity {
    private final static int STEP_02_COMPLETE = 10;
    private final static int ADD_ACCOUNT_COMPLETE = 20;

    private ImageView closeButton, backButton;
    private TextView step02, step03, step02Text, step03Text, finishButton;
    private LinearLayout bank01, bank02, bank03, bank04, bank05, bank06, bank07, bank08, bank09;
    private List<LinearLayout> banks;
    private String selectedBankId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(BankAccountAddStep01)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_add_01);

        closeButton = findViewById(R.id.close_imageview);
        backButton = findViewById(R.id.back_imageview);
        step02 = findViewById(R.id.step_02);
        step03 = findViewById(R.id.step_03);
        step02Text = findViewById(R.id.step_02_text);
        step03Text = findViewById(R.id.step_03_text);
        finishButton = findViewById(R.id.finish_textview);
        bank01 = findViewById(R.id.nonghyub);
        bank02 = findViewById(R.id.woori);
        bank03 = findViewById(R.id.shinhan);
        bank04 = findViewById(R.id.kookmin);
        bank05 = findViewById(R.id.hana);
        bank06 = findViewById(R.id.city);
        bank07 = findViewById(R.id.ibk);
        bank08 = findViewById(R.id.kbank);
        bank09 = findViewById(R.id.kakao);
        banks = Arrays.asList(bank01, bank02, bank03, bank04, bank05, bank06, bank07, bank08, bank09);

        BankButtonListener listener = new BankButtonListener();

        for (LinearLayout bank : banks) {
            bank.setOnClickListener(listener);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_FIRST_USER);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBankId.isEmpty()) {
                    Toast.makeText(BankAccountAddStep01.this, "등록하실 은행을 선택하세요", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(BankAccountAddStep01.this, BankAccountAddStep02.class);
                    //다음 스텝에 사용자가 선택한 은행 아이디를 넘겨준다.
                    intent.putExtra("bank_account", selectedBankId);
                    startActivityForResult(intent, STEP_02_COMPLETE);
                }
            }
        });

        layoutInitiate();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("debug", "step02 result:" + resultCode);

        switch (resultCode) {
            case RESULT_CANCELED:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case RESULT_OK:
                setResult(ADD_ACCOUNT_COMPLETE);
                finish();
        }
    }

    private void layoutInitiate() {
        step02.setBackgroundResource(R.drawable.step_02_dim);
        step02.setText("");
        step03.setBackgroundResource(R.drawable.step_03_dim);
        step03.setText("");
        step02Text.setTextColor(Color.parseColor("#a5a8b9"));
        step03Text.setTextColor(Color.parseColor("#a5a8b9"));
        finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
    }

    class BankButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();

            for (LinearLayout bank : banks) {
                bank.setBackgroundResource(R.drawable.bg_bank_radius);
            }

            switch (id) {
                case R.id.nonghyub:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "01";
                    break;
                case R.id.woori:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "02";
                    break;
                case R.id.shinhan:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "03";
                    break;
                case R.id.kookmin:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "04";
                    break;
                case R.id.hana:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "05";
                    break;
                case R.id.city:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "06";
                    break;
                case R.id.ibk:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "07";
                    break;
                case R.id.kbank:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "08";
                    break;
                case R.id.kakao:
                    v.setBackgroundResource(R.drawable.bg_bank_radius_with_border);
                    selectedBankId = "09";
                    break;
            }

            finishButton.setBackgroundColor(Color.parseColor("#304db9"));
        }
    }
}
