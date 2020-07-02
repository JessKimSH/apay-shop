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

public class CardChargeActivity extends AppCompatActivity {
    private final static int EASY_PASSWORD_AUTH_COMPLETE = 2;
    private final static int CHARGE_CARD_USER_CONFIRMED = 3;

    private ImageView closeButton;
    private EditText chargeBalanceEditText;
    private Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, delButton, button00,
            plus1000button, plus5000button, plus10000button, plus50000button;
    private TextView previousBalance, finishButton;
    private String userBalance = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardChargeActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_charge);

        closeButton = findViewById(R.id.close_imageview);
        previousBalance = findViewById(R.id.previous_balance);
        chargeBalanceEditText = findViewById(R.id.card_charge_edittext);
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

        chargeBalanceEditText.requestFocus();

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = chargeBalanceEditText.getText().length();
                if (length > 0) chargeBalanceEditText.getText().delete(length - 1, length);
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("0");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("1");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("2");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("3");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("4");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("5");
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("6");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("7");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("8");
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("9");
            }
        });
        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeBalanceEditText.append("00");
            }
        });

        plus1000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (chargeBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 1000L;
                chargeBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        plus5000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (chargeBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 5000L;
                chargeBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        plus10000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (chargeBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 10000L;
                chargeBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        plus50000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (chargeBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 50000L;
                chargeBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        chargeBalanceEditText.addTextChangedListener(new TextWatcher() {
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
                chargeBalanceEditText.removeTextChangedListener(this);
                String cleanString = s.toString().replaceAll("[%s,.]", "");
                chargeBalanceEditText.setText(formatToKRW(cleanString));
                chargeBalanceEditText.addTextChangedListener(this);
            }
        });

        //터치이벤트를 소모해서 키보드를 뜨지 않게 함.
        chargeBalanceEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.valueOf(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", "")) < 10000L) {
                    Toast.makeText(getApplicationContext(), "10,000 P 이상의 금액부터 충전이 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Long.valueOf(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", "")) > 2000000L) {
                    Toast.makeText(getApplicationContext(), "2,000,000 P 이하의 금액부터 충전이 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Long.valueOf(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", ""))%10000L > 0) {
                    Toast.makeText(getApplicationContext(), "10,000 P 단위로 충전이 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(CardChargeActivity.this, EasyPasswordTotal.class);
                intent.putExtra("SCREEN_TITLE", "카드 충전");
                startActivityForResult(intent, EASY_PASSWORD_AUTH_COMPLETE); //사용자가 확인버튼을 누르면 onActivityResult에게 1을 반환한다.
            }
        });

        userBalance = getUserBalance();
        previousBalance.setText(formatToKRW(userBalance.replaceAll("[%s,.]", "")) + " P");
    }

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        return nf.format(Long.valueOf(number));
    }

    private String getUserBalance() {
        //TODO.16 백엔드 API를 호출하여 현재 사용자의 잔액을 확인 한다.
        DatabaseUtil databaseUtil = new DatabaseUtil(CardChargeActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "select balance from user where instance_id = ?";

        String uniqueID = InstanceID.getInstance(CardChargeActivity.this).getId();

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
                    Log.d("debug",chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                    Log.d("debug",getUserBalance());
                    Intent intent = new Intent(CardChargeActivity.this, CardChargeFinishActivity.class);
                    intent.putExtra("previous_balance", formatToKRW(userBalance.replaceAll("[%s,.]", "")));
                    intent.putExtra("charge_balance", formatToKRW(chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", "")));
                    intent.putExtra("post_charge_balance", formatToKRW(getUserBalance()));
                    startActivityForResult(intent, CHARGE_CARD_USER_CONFIRMED);
                    break;
            }

        } else if(requestCode == CHARGE_CARD_USER_CONFIRMED) {
            finish();
        }
    }

    private void completeCharge() {
        //TODO.16 충전백엔드 API를 호출하여 충전한다.
        DatabaseUtil databaseUtil = new DatabaseUtil(CardChargeActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "update user set balance = balance + ? where instance_id = ?";

        String uniqueID = InstanceID.getInstance(CardChargeActivity.this).getId();

        Log.d("debug", "instance id:" + uniqueID);

        String[] arg1 = {chargeBalanceEditText.getText().toString().replaceAll("[%s,.]", ""), uniqueID};

        db.execSQL(sql, arg1);

        db.close();

        Log.d("debug", "충전완료");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "onDestroy(CardChargeActivity)");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "onStop(CardChargeActivity)");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "onRestart(CardChargeActivity)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "onResume(CardChargeActivity)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "onStart(CardChargeActivity)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "onPause(CardChargeActivity)");
    }
}
