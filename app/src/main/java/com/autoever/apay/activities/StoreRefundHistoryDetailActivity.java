package com.autoever.apay.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.autoever.apay.R;
import com.autoever.apay.models.Store;
import com.autoever.apay.models.StorePaymentHistory;
import com.autoever.apay.models.VirtualAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.autoever.apay.activities.CommonApplication.getStore;
import static com.autoever.apay.activities.CommonApplication.getVirtualAccount;

public class StoreRefundHistoryDetailActivity extends AppCompatActivity {

    private ImageView backImageview;
    private ImageView closeImageview;

    private TextView purchaseAmount;
    private TextView username;
    private TextView createdDate;
    private TextView paymentId;
    private TextView paymentIdentifier;
    private TextView balance;

    private TextView nextTextview;

    private final static int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;

    private Store store = getStore();
    private VirtualAccount virtualAccount = getVirtualAccount();
    private StorePaymentHistory storePaymentHistory = new StorePaymentHistory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_refund_history_detail);

        backImageview = (ImageView) findViewById(R.id.back_imageview);
        closeImageview = (ImageView) findViewById(R.id.close_imageview);
        purchaseAmount = (TextView) findViewById(R.id.purchase_amount);
        username = (TextView) findViewById(R.id.username);
        createdDate = (TextView) findViewById(R.id.created_date);
        paymentId = (TextView) findViewById(R.id.payment_id);
        paymentIdentifier = (TextView) findViewById(R.id.payment_identifier);
        balance = (TextView) findViewById(R.id.balance);

        nextTextview = (TextView) findViewById(R.id.next_textview);

        storePaymentHistory = (StorePaymentHistory) getIntent().getSerializableExtra("StorePaymentHistory");

        backImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        closeImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPost();

                Intent intent = new Intent(StoreRefundHistoryDetailActivity.this, StoreRefundFinishActivity.class);
                intent.putExtra("StorePaymentHistory", storePaymentHistory);
                startActivity(intent);
            }
        });

        String createdDateString = storePaymentHistory.getCreatedDate();

        // StorePaymentCancelHisotryDetail 에서 값 가져오기
        purchaseAmount.setText(formatToKRW(""+storePaymentHistory.getAmount())+ " P");
        createdDate.setText(formatToDate(createdDateString));
        paymentId.setText(storePaymentHistory.getPaymentId());
        username.setText(storePaymentHistory.getUserName());
        paymentIdentifier.setText(storePaymentHistory.getIdentifier());

        // Virtual account Table에서 balance 가져오기
        balance.setText(formatToKRW(""+virtualAccount.getValue()) + " P");

    }

    // Refund / Do 보내기
    public void sendPost() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://api.apay.ga:8080/payment/refund/do");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    // post 보낼 때 json param 셋팅
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("userId", storePaymentHistory.getUserId());
                    jsonParam.put("storeId", store.getId());
                    jsonParam.put("tokenSystemId", store.getTokenSystemId());
                    jsonParam.put("amount", storePaymentHistory.getAmount());
                    jsonParam.put("paymentId", storePaymentHistory.getPaymentId());
                    jsonParam.put("identifier", storePaymentHistory.getIdentifier());

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    String responseCode = String.valueOf(conn.getResponseCode());
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));

                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);

        return nf.format(Long.valueOf(number));
    }

    private String formatToDate(String createdDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        SimpleDateFormat createdDateFormat = new SimpleDateFormat("yyyy-MM-dd(E) hh:mm:ss", Locale.KOREA);

        Date paidDate = null;
        try {
            paidDate = simpleDateFormat.parse(createdDate.replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return createdDateFormat.format(paidDate);
    }
}
