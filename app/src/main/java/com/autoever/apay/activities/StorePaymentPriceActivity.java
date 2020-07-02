package com.autoever.apay.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.autoever.apay.R;
import com.autoever.apay.models.Store;
import com.autoever.apay.utils.EasyPasswordFor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.autoever.apay.activities.CommonApplication.getStore;

public class StorePaymentPriceActivity extends AppCompatActivity {
    private final static int EASY_PASSWORD_AUTH_COMPLETE = 2;
    private final static int PAYMENT_CARD_USER_CONFIRMED = 3;

    private ImageView backImageView, closeImageView;
    private TextView purchaseAmountTextView, userBalanceTextView, shopNameTextView, finishTextView,
            usernameTextView, createdDateTextView;

    private String username, userId;
    private int purchaseAmount;
    private Store store = getStore();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(StorePaymentPriceActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_payment_price);

        purchaseAmountTextView = findViewById(R.id.purchase_amount);
        finishTextView = findViewById(R.id.finish_textview);
        backImageView = findViewById(R.id.back_imageview);
        closeImageView = findViewById(R.id.close_imageview);
        usernameTextView = findViewById(R.id.username);
        createdDateTextView = findViewById(R.id.created_date);

        username = getIntent().getStringExtra("username");
        userId = getIntent().getStringExtra("userId");
        purchaseAmount = getIntent().getIntExtra("amount", 0);
        Log.d("debug", Calendar.getInstance().getTime().toString());

        createdDateTextView.setText(formatToDate(Calendar.getInstance().getTime()));
        usernameTextView.setText(username);
        purchaseAmountTextView.setText(formatToKRW(purchaseAmount+"")+ " P");
        finishTextView.setText(formatToKRW(purchaseAmount+"") + "P 결제");

        // 결제 진행
        finishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StorePaymentPriceActivity.this, EasyPasswordActivity.class);
                intent.putExtra("PURPOSE", EasyPasswordFor.PURCHASE);
                startActivityForResult(intent, EASY_PASSWORD_AUTH_COMPLETE); //사용자가 확인버튼을 누르면 onActivityResult에게 1을 반환한다.
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_FIRST_USER);
                finish();
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 결제 프로세스 ready, do
        if (requestCode == EASY_PASSWORD_AUTH_COMPLETE) {
            Log.d("debug", "EASY_PASSWORD_AUTH_COMPLETE");
            switch (resultCode) {
                case RESULT_OK:
                    Log.d("debug", "EASY_PASSWORD_AUTH_COMPLETE");

                    String finalResponseData = null;
                    JSONObject json = null;
                    Intent intent = null;

                    SyncData syncData = new SyncData();
                    try {
                        finalResponseData = syncData.execute().get();
                        Log.i("TEST1", finalResponseData);
                        json = new JSONObject(finalResponseData).getJSONObject("data");

                        intent = new Intent(StorePaymentPriceActivity.this, StorePaymentFinishActivity.class);
                        intent.putExtra("purchaseAmount", json.getInt("amount"));
                        intent.putExtra("createdDate", json.getString("createdDate"));
                        intent.putExtra("paymentId", json.getString("paymentId"));
                        intent.putExtra("username", getIntent().getStringExtra("username"));

                        startActivityForResult(intent, PAYMENT_CARD_USER_CONFIRMED);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        } else if(requestCode == PAYMENT_CARD_USER_CONFIRMED) {
            setResult(RESULT_OK);
            finish();
        }
    }

    // storeId로 hash로 만들기
    public StringBuffer String2SHachCode(String id) {
        StringBuffer hashedId = new StringBuffer();

        Log.i("String2SHachCode", "emerged");

        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(id.getBytes());

            byte byteData[] = sh.digest();

            for(int i = 0; i < byteData.length; i++)
                hashedId.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashedId;
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        return nf.format(Long.valueOf(number));
    }

    private String formatToDate(Date createdDate) {
        SimpleDateFormat createdDateFormat = new SimpleDateFormat("yyyy-MM-dd(E)", Locale.KOREA);

        return createdDateFormat.format(createdDate);
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {

        String identifier = "APAY_" + String2SHachCode(Calendar.getInstance().getTime().toString()).toString().substring(0, 26);
        String paymentId;
        String finalResponseData;

        @Override
        protected String doInBackground(String... strings)
        {
            // Payment/Ready 요청 보내기
            try {
                URL url = new URL("http://api.apay.ga:8080/payment/ready");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // post 보낼 때 json param 셋팅
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("userId", userId);
                jsonParam.put("hashedStoreId", String2SHachCode(store.getId() +""));
                jsonParam.put("tokenSystemId", store.getTokenSystemId());
                jsonParam.put("amount", purchaseAmount);
                jsonParam.put("identifier", identifier);

//                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                String responseCode = String.valueOf(conn.getResponseCode());
                Log.i("STATUS", responseCode);

                // post response 추출
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());

                String responseData = convertStreamToString(inputStream);
                Log.i("Response", responseData);

                conn.disconnect();

                JSONObject json = new JSONObject(responseData).getJSONObject("data");
                paymentId = json.optString("paymentId");

                Log.i("paymentID TEST", paymentId);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Payment/Do 요청 보내기
            try {
                URL url = new URL("http://api.apay.ga:8080/payment/do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                // post 보낼 때 json param 셋팅
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("userId", userId);
                jsonParam.put("storeId", store.getId());
                jsonParam.put("tokenSystemId", store.getTokenSystemId());
                jsonParam.put("amount", purchaseAmount);
                jsonParam.put("paymentId", paymentId);
                jsonParam.put("identifier", identifier);

                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));

                // post response 추출
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());

                finalResponseData = convertStreamToString(inputStream);
                Log.i("Response", finalResponseData);

                conn.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return finalResponseData;
        }
    }
}

