package com.autoever.apay.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.utils.DatabaseUtil;
import com.google.android.gms.iid.InstanceID;

import java.text.NumberFormat;

public class PaybackActivity extends AppCompatActivity {
    private final static int EASY_PASSWORD_AUTH_COMPLETE = 2;
    private final static int PAYBACK_CONFIRMED = 3;
    private final static int PAYBACK = 8;

    private ImageView backButton, closeButton;
    private EditText paybackEdittext;
    private Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, delButton, button00,
            plus1000button, plus5000button, plus10000button, plus50000button;
    private TextView currentBalance, finishButton;
    private String userBalance = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardChargeActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_exchange);

        backButton = findViewById(R.id.back_imageview);
        closeButton = findViewById(R.id.close_imageview);
        currentBalance = findViewById(R.id.current_balance);
        paybackEdittext = findViewById(R.id.payback_edittext);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        delButton = findViewById(R.id.button_delete);
        button00 = findViewById(R.id.button00);
        plus1000button = findViewById(R.id.price_1000);
        plus5000button = findViewById(R.id.price_5000);
        plus10000button = findViewById(R.id.price_10000);
        plus50000button = findViewById(R.id.price_50000);
        finishButton = findViewById(R.id.finish_textview);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        paybackEdittext.requestFocus();

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = paybackEdittext.getText().length();
                if (length > 0) paybackEdittext.getText().delete(length - 1, length);
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("0");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("1");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("2");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("3");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("4");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("5");
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("6");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("7");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("8");
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("9");
            }
        });
        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paybackEdittext.append("00");
            }
        });

        plus1000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paybackEdittext.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paybackEdittext.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 1000L;
                paybackEdittext.setText(String.valueOf(currentInputBalance));
            }
        });

        plus5000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paybackEdittext.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paybackEdittext.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 5000L;
                paybackEdittext.setText(String.valueOf(currentInputBalance));
            }
        });

        plus10000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paybackEdittext.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paybackEdittext.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 10000L;
                paybackEdittext.setText(String.valueOf(currentInputBalance));
            }
        });

        plus50000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paybackEdittext.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paybackEdittext.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 50000L;
                paybackEdittext.setText(String.valueOf(currentInputBalance));
            }
        });

        paybackEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()) return;
                //원화화폐단위로 포맷하여 보여줌
                paybackEdittext.removeTextChangedListener(this);
                String cleanString = s.toString().replaceAll("[%s,.]", "");
                paybackEdittext.setText(formatToKRW(cleanString));
                paybackEdittext.addTextChangedListener(this);
            }
        });

        //터치이벤트를 소모해서 키보드를 뜨지 않게 함.
        paybackEdittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.valueOf(paybackEdittext.getText().toString().replaceAll("[%s,.]", "")) < Long.valueOf(getUserBalance())) {
                    Toast.makeText(getApplicationContext(), "환전은 모든 금액만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Long.valueOf(paybackEdittext.getText().toString().replaceAll("[%s,.]", "")) > Long.valueOf(getUserBalance())) {
                    Toast.makeText(getApplicationContext(), "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(PaybackActivity.this, EasyPasswordTotal.class);
                intent.putExtra("SCREEN_TITLE", "환전");
                startActivityForResult(intent, EASY_PASSWORD_AUTH_COMPLETE); //사용자가 확인버튼을 누르면 onActivityResult에게 1을 반환한다.
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "환전을 취소합니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        userBalance = getUserBalance();
        currentBalance.setText(formatToKRW(userBalance.replaceAll("[%s,.]", "")) + " P");
    }

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        return nf.format(Long.valueOf(number));
    }

    private String getUserBalance() {
        //TODO.16 백엔드 API를 호출하여 현재 사용자의 잔액을 확인 한다.
        DatabaseUtil databaseUtil = new DatabaseUtil(PaybackActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "select balance from user where instance_id = ?";

        String uniqueID = InstanceID.getInstance(PaybackActivity.this).getId();

        String[] arg1 = {uniqueID};

        Cursor cursor = db.rawQuery(sql, arg1);

        cursor.moveToFirst();

        String result = String.valueOf(cursor.getLong(cursor.getColumnIndex("balance")));;

        db.close();
        cursor.close();

        return result;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EASY_PASSWORD_AUTH_COMPLETE) {
            Log.d("debug", "EASY_PASSWORD_AUTH_COMPLETE");
            switch (resultCode) {
                case RESULT_OK:
                    Log.d("debug", "EASY_PASSWORD_AUTH_COMPLETE");
                    completeCharge();
                    Log.d("debug",paybackEdittext.getText().toString().replaceAll("[%s,.]", ""));
                    Log.d("debug",getUserBalance());
                    Intent intent = new Intent(PaybackActivity.this, PaybackFinishActivity.class);
                    intent.putExtra("previous_balance", formatToKRW(userBalance.replaceAll("[%s,.]", "")));
                    intent.putExtra("charge_balance", formatToKRW(paybackEdittext.getText().toString().replaceAll("[%s,.]", "")));
                    intent.putExtra("post_charge_balance", formatToKRW(getUserBalance()));
                    startActivityForResult(intent, PAYBACK_CONFIRMED);
                    break;
            }

        } else if(requestCode == PAYBACK_CONFIRMED) {
            setResult(PAYBACK);
            finish();
        }
    }

    private void completeCharge() {
        //TODO.16 환전백엔드 API를 호출하여 환전한다.
        DatabaseUtil databaseUtil = new DatabaseUtil(PaybackActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "update user set balance = balance - ? where instance_id = ?";

        String uniqueID = InstanceID.getInstance(PaybackActivity.this).getId();

        Log.d("debug", "instance id:" + uniqueID);

        String[] arg1 = {paybackEdittext.getText().toString().replaceAll("[%s,.]", ""), uniqueID};

        db.execSQL(sql, arg1);

        db.close();

        Log.d("debug", "환전완료");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "onDestroy(PaybackActivity)");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "onStop(PaybackActivity)");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "onRestart(PaybackActivity)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "onResume(PaybackActivity)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "onStart(PaybackActivity)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "onPause(PaybackActivity)");
    }
}
