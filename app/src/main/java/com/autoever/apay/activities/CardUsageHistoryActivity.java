package com.autoever.apay.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autoever.apay.R;
import com.autoever.apay.models.PaymentHistory;
import com.autoever.apay.utils.DatabaseUtil;

import java.util.ArrayList;

public class CardUsageHistoryActivity extends AppCompatActivity {
    private final static int CARD_PAYMENT_DETAIL = 30;

    private RecyclerView paymentHistoryListView;
    private CustomAdapter customAdapter;
    private ImageView closeButton;
    private Spinner paymentTypeFilter;
    private String[] payTypeList = {"전체","충전","결제", "결제취소"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardUsageHistoryActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_usage_history);

        closeButton = findViewById(R.id.close_imageview);
        paymentHistoryListView = findViewById(R.id.payment_history_list);
        paymentTypeFilter = findViewById(R.id.payment_type_spinner);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        paymentHistoryListView.setLayoutManager(mLinearLayoutManager);

        customAdapter = new CustomAdapter(getPaymentHistory());
        paymentHistoryListView.setAdapter(customAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                paymentHistoryListView.getContext(),
                mLinearLayoutManager.getOrientation()
        );
        paymentHistoryListView.addItemDecoration(dividerItemDecoration);

        closeButton.setOnClickListener(v -> finish());

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_dropdown_item, R.id.text1, payTypeList);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        paymentTypeFilter.setAdapter(aa);

        paymentTypeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<PaymentHistory> getPaymentHistory() {
        ArrayList<PaymentHistory> result = new ArrayList<>();

        DatabaseUtil databaseUtil = new DatabaseUtil(CardUsageHistoryActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "select * from payment_history";

        // create Cursor in order to parse our sqlite results
        Cursor cursor = db.rawQuery(sql, null);
        // if Cursor is contains results
        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    // Get version from Cursor
                    String shopName = cursor.getString(cursor.getColumnIndex("shop_name"));
                    String purchaseAmount = cursor.getString(cursor.getColumnIndex("purchase_amount"));
                    String purchaseDate = cursor.getString(cursor.getColumnIndex("purchase_date"));
                    int type = cursor.getInt(cursor.getColumnIndex("payment_type"));
//                    Log.d("debug", shopName + " " + purchaseAmount + " " + purchaseDate + " " + type);
                    result.add(new PaymentHistory(shopName, purchaseAmount, purchaseDate, type));

                    // move to next row
                } while (cursor.moveToNext());
            }
        }
        return result;
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        private ArrayList<PaymentHistory> mList;

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView shopNameTextView, purchaseAmountTextView, purchaseDateTextView, paymentTypeTextView;


            public CustomViewHolder(View view) {
                super(view);

                this.shopNameTextView = view.findViewById(R.id.shop_name);
                this.purchaseAmountTextView = view.findViewById(R.id.purchase_amount);
                this.purchaseDateTextView = view.findViewById(R.id.purchase_date);
                this.paymentTypeTextView = view.findViewById(R.id.payment_type);

                view.setOnClickListener(v -> {
                    Intent intent = new Intent(CardUsageHistoryActivity.this, CardPaymentDetailActivity.class);
                    intent.putExtra("shop_name", shopNameTextView.getText().toString());
                    intent.putExtra("purchase_amount", purchaseAmountTextView.getText().toString());
                    intent.putExtra("purchase_date", purchaseDateTextView.getText().toString());
                    intent.putExtra("payment_type", paymentTypeTextView.getText().toString());
                    startActivityForResult(intent, CARD_PAYMENT_DETAIL);
                });
            }
        }




        public CustomAdapter(ArrayList<PaymentHistory> list) {
            this.mList = list;
        }


        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
            if (getPaymentHistory().get(position).getType() == 3 || getPaymentHistory().get(position).getType() == 2) {
                Log.d("debug", "결제취소 또는 충전");
                viewholder.purchaseAmountTextView.setTextColor(Color.parseColor("#cb3333"));
                viewholder.paymentTypeTextView.setText(getPaymentHistory().get(position).getType() == 3 ? "결제취소" : "충전");
            } else {
                viewholder.purchaseAmountTextView.setTextColor(Color.parseColor("#304db9"));
                viewholder.paymentTypeTextView.setText("결제");
            }

            viewholder.shopNameTextView.setText(getPaymentHistory().get(position).getShopName());
            viewholder.purchaseAmountTextView.setText(getPaymentHistory().get(position).getPurchaseAmount());
            viewholder.purchaseDateTextView.setText(getPaymentHistory().get(position).getPurchaseDate());

        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }
    }
}
