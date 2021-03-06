package com.autoever.apay.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.autoever.apay.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

public class StorePaymentActivity extends AppCompatActivity {
    private final static int PURCHASE_COMPLETE = 1;
    private final static int PURCHASE_COMPLETED = 15;


    private ImageView closeButton, backButton;
    private EditText paymentBalanceEditText;
    private Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, delButton, button00,
            plus1000button, plus5000button, plus10000button, plus50000button;
    private TextView shopName, userBalance, finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(main)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_payment);

        paymentBalanceEditText = findViewById(R.id.card_payment_edittext);
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

        closeButton = findViewById(R.id.back_imageview);
        backButton = findViewById(R.id.close_imageview);

        paymentBalanceEditText.requestFocus();

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = paymentBalanceEditText.getText().length();
                if (length > 0) paymentBalanceEditText.getText().delete(length - 1, length);
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("0");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("1");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("2");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("3");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("4");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("5");
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("6");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("7");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("8");
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("9");
            }
        });
        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBalanceEditText.append("00");
            }
        });

        plus1000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paymentBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paymentBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 1000L;
                paymentBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        plus5000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paymentBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paymentBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 5000L;
                paymentBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        plus10000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paymentBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paymentBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 10000L;
                paymentBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        plus50000button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long currentInputBalance;
                if (paymentBalanceEditText.getText().toString().equals("")) currentInputBalance = 0L;
                else
                    currentInputBalance = Long.valueOf(paymentBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));
                currentInputBalance = currentInputBalance + 50000L;
                paymentBalanceEditText.setText(String.valueOf(currentInputBalance));
            }
        });

        paymentBalanceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) return;
                //원화화폐단위로 포맷하여 보여줌
                paymentBalanceEditText.removeTextChangedListener(this);
                String cleanString = s.toString().replaceAll("[%s,.]", "");
                paymentBalanceEditText.setText(formatToKRW(cleanString));
                paymentBalanceEditText.addTextChangedListener(this);
            }
        });

        //터치이벤트를 소모해서 키보드를 뜨지 않게 함.
        paymentBalanceEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StorePaymentActivity.this, StorePaymentPriceActivity.class);

                int purchaseAmount = Integer.parseInt(paymentBalanceEditText.getText().toString().replaceAll("[%s,.]", ""));

                SyncData syncData = new SyncData();
                String username = null;
                try {
                    username = syncData.execute().get();
                    Log.i("USERNAME", username);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                intent.putExtra("amount", purchaseAmount);
                intent.putExtra("userId", getIntent().getStringExtra("userId"));
                intent.putExtra("username", username);

                startActivityForResult(intent, PURCHASE_COMPLETE); //사용자가 확인버튼을 누르면 onActivityResult에게 1을 반환한다.
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
                finish();
            }
        });
    }

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        return nf.format(Long.valueOf(number));
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String str, receiveMsg;
        StringBuffer stringBuffer = new StringBuffer();

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            Log.i("Common", " Object __ Store Start");
            try {

                // Store Part
                url = new URL("http://api.apay.ga:8080/user/" + getIntent().getStringExtra("userId"));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    while (((str = bufferedReader.readLine()) != null)) {
                        stringBuffer.append(str);
                    }
                    receiveMsg = stringBuffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    bufferedReader.close();
                } else {
                    Log.i("Connection", "Error");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return getUsername(stringBuffer.toString());
        }

        public String getUsername(String jsonString) {
            String username = null;

            try {
                JSONObject json = new JSONObject(jsonString).getJSONObject("data");

                username = String.valueOf(json.optString("userName"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return username;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_CANCELED: //사용자가 close 버튼을 누른경우에는 toast를 띄워주고 메인으로 돌아간다.
                Toast.makeText(getApplicationContext(), "결제가 취소되어 메인화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case RESULT_OK: //결제가 완료된 경우에는 activity를 종료한다.
                setResult(PURCHASE_COMPLETED);
                finish();
                break;
            case RESULT_FIRST_USER: //사용자가 back 버튼을 누른경우에는 아무것도 하지 않는다.
                break;
        }
    }
}

