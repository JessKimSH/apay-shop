package com.autoever.apay.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autoever.apay.R;
import com.autoever.apay.models.StorePaymentHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StoreRefundHistoryActivity extends AppCompatActivity {
    private ImageView goBack;
    private ImageView goForward;
    private ImageView closeImageview;

    private RecyclerView paymentCancelListRecyclerView;
    private RecyclerView.Adapter customAdpater;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<StorePaymentHistory> storePaymentHistories;

    String str, receiveMsg;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_refund_history);

        paymentCancelListRecyclerView = (RecyclerView) findViewById(R.id.payment_cancel_list_recyclerview);
        storePaymentHistories = new ArrayList<StorePaymentHistory>();

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

        // 조회 연월 이동
        goBack = (ImageView) findViewById(R.id.goBack);
        goForward = (ImageView) findViewById(R.id.goForward);
        closeImageview = (ImageView) findViewById(R.id.close_imageview);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        goForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // X 버튼 클릭리스너
        closeImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StoreRefundHistoryActivity.context = this;
    }

    public static Context getAppContext() {
        Log.i("layoutManage getAppContext()", StoreRefundHistoryActivity.context.toString());
        return StoreRefundHistoryActivity.context;
    }

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);

        return nf.format(Long.valueOf(number));
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings)
        {
            URL url = null;

            try {
                // TODO pagination 처리 필요!!
                url = new URL("http://api.apay.ga:8080/payment/store/2/refundReady?pageNo=0&pageSize=10");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    while (((str = bufferedReader.readLine()) != null)) {
                        stringBuffer.append(str);
                    }
                    receiveMsg = stringBuffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    storePaymentHistories = getStorePaymentHistory(stringBuffer.toString());

                    bufferedReader.close();
                } else {
                    Log.i("Connection", "Error");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String msg) {
            // RecyclerView 환경 설정
            paymentCancelListRecyclerView = (RecyclerView) findViewById(R.id.payment_cancel_list_recyclerview);
            paymentCancelListRecyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getAppContext());
            paymentCancelListRecyclerView.setLayoutManager(layoutManager);

            customAdpater = new StoreRefundHistoryActivity.CustomAdapter(storePaymentHistories);
            paymentCancelListRecyclerView.setAdapter(customAdpater);

        }
    }


    // PaymentCancelList 와 Data 연결
    class CustomAdapter extends RecyclerView.Adapter<StoreRefundHistoryActivity.CustomAdapter.CustomViewHolder> {

        private ArrayList<StorePaymentHistory> mList;

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView paymentNumberTextView, purchaseAmountTextView, purchaseDateTextView, paymentTypeTextView;
            protected LinearLayout recyclerId;

            public CustomViewHolder(View view) {
                super(view);

                this.recyclerId = view.findViewById(R.id.recycler_id);

                this.paymentNumberTextView = view.findViewById(R.id.payment_id);
                this.purchaseAmountTextView = view.findViewById(R.id.purchase_amount);
                this.purchaseDateTextView = view.findViewById(R.id.purchase_date);
                this.paymentTypeTextView = view.findViewById(R.id.payment_type);
            }
        }


        // CustomAdapter에 data 삽입
        public CustomAdapter(ArrayList<StorePaymentHistory> list) {
            this.mList = list;
        }

        // 데이터 한 개 당 View 생성
        @Override
        public StoreRefundHistoryActivity.CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_store, viewGroup, false);

            StoreRefundHistoryActivity.CustomAdapter.CustomViewHolder viewHolder = new StoreRefundHistoryActivity.CustomAdapter.CustomViewHolder(view);

            return viewHolder;
        }


        // 환불
        @Override
        public void onBindViewHolder(@NonNull StoreRefundHistoryActivity.CustomAdapter.CustomViewHolder viewholder, int position) {

            viewholder.purchaseAmountTextView.setTextColor(Color.parseColor("#cb3333"));
            viewholder.paymentTypeTextView.setText("환불");

            viewholder.paymentNumberTextView.setText(storePaymentHistories.get(position).getPaymentId());
            viewholder.purchaseAmountTextView.setText(formatToKRW(""+storePaymentHistories.get(position).getAmount()) + " P");
            viewholder.purchaseDateTextView.setText(formatToDate(storePaymentHistories.get(position).getCreatedDate()));

            viewholder.recyclerId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorePaymentHistory paramObject = storePaymentHistories.get(position);

                    Intent intent = new Intent(StoreRefundHistoryActivity.this, StoreRefundHistoryDetailActivity.class);
                    intent.putExtra("StorePaymentHistory", paramObject);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }
    }

    public ArrayList<StorePaymentHistory> getStorePaymentHistory(String jsonString) {
        ArrayList<StorePaymentHistory> returnArray = new ArrayList<StorePaymentHistory>();

        try {
            JSONObject json = new JSONObject(jsonString).getJSONObject("data");
            JSONArray jsonArray = json.getJSONArray("content");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                returnArray.add(new StorePaymentHistory(
                        jsonObject.optString("paymentId"),
                        jsonObject.optString("userId"),
                        jsonObject.optString("storeId"),
                        jsonObject.optString("tokenSystemId"),
                        jsonObject.optInt("amount"),
                        jsonObject.optString("paymentStatus"),
                        jsonObject.optString("identifier"),
                        jsonObject.optString("createdDate"),
                        jsonObject.optString("userName")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnArray;
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

