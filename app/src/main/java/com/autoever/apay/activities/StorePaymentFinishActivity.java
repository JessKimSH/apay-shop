package com.autoever.apay.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.autoever.apay.R;
import com.autoever.apay.models.Store;
import com.autoever.apay.models.StorePaymentHistory;
import com.autoever.apay.models.VirtualAccount;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.autoever.apay.activities.CommonApplication.getStore;
import static com.autoever.apay.activities.CommonApplication.getVirtualAccount;
import static com.autoever.apay.activities.CommonApplication.reload;

public class StorePaymentFinishActivity extends AppCompatActivity {

    private TextView finishTextview;

    private TextView purchaseAmount;
    private TextView username;
    private TextView createdDate;
    private TextView paymentId;
    private TextView accountInfo;
    private TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(StorePaymentFinishActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_payment_finish);

        finishTextview = (TextView) findViewById(R.id.finish_textview);

        purchaseAmount = (TextView) findViewById(R.id.purchase_amount);
        username = (TextView) findViewById(R.id.username);
        createdDate = (TextView) findViewById(R.id.created_date);
        paymentId = (TextView) findViewById(R.id.payment_id);
        balance = (TextView) findViewById(R.id.balance);

        finishTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        // balance 값을 위해 CommonApplication 업데이트
        try {
            reload();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        VirtualAccount virtualAccount = getVirtualAccount();
        Store store = getStore();
        StorePaymentHistory storePaymentHistory = (StorePaymentHistory) getIntent().getSerializableExtra("StorePaymentHistory");

        // param 셋팅하기
        username.setText(getIntent().getStringExtra("username"));
        purchaseAmount.setText(formatToKRW(getIntent().getIntExtra("purchaseAmount", 0)+"")+ " P");
        createdDate.setText(formatToDate(getIntent().getStringExtra("createdDate")));
        paymentId.setText(getIntent().getStringExtra("paymentId"));

        // Virtual account Table에서 balance 가져오기
        balance.setText(formatToKRW(""+virtualAccount.getValue()) + " P");

    }

    // Data Format 변경
    private String formatToDate(String createdDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.KOREA);
        SimpleDateFormat createdDateFormat = new SimpleDateFormat("yyyy-MM-dd(E) hh:mm:ss", Locale.KOREA);

        Date paidDate = null;
        try {
            paidDate = simpleDateFormat.parse(createdDate.replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return createdDateFormat.format(paidDate);
    }

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);

        return nf.format(Long.valueOf(number));
    }
}
