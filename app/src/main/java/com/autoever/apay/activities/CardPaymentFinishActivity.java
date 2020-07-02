package com.autoever.apay.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.models.User;
import com.autoever.apay.utils.DatabaseUtil;
import com.google.android.gms.iid.InstanceID;

public class CardPaymentFinishActivity extends AppCompatActivity {
    private TextView shopNameTextView, purchaseAmountTextView, balanceTextView, finishTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardPaymentFinishActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_finish);

        shopNameTextView = findViewById(R.id.shop_name);
        purchaseAmountTextView = findViewById(R.id.purchase_amount);
        balanceTextView = findViewById(R.id.user_balance);
        finishTextView = findViewById(R.id.finish_textview);

        String amount = getIntent().getStringExtra("amount");
        String shopName = getIntent().getStringExtra("shop_name");

        shopNameTextView.setText(shopName);
        purchaseAmountTextView.setText(amount);
        balanceTextView.setText(String.valueOf(getUser().getBalanceLeft()));

        finishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private User getUser() {

        DatabaseUtil databaseUtil = new DatabaseUtil(CardPaymentFinishActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "select balance from user where instance_id = ?";

        String uniqueID = InstanceID.getInstance(CardPaymentFinishActivity.this).getId();

        String[] arg1 = {uniqueID};

        Cursor cursor = db.rawQuery(sql, arg1);

        cursor.moveToFirst();

        Long balanceLeft = cursor.getLong(cursor.getColumnIndex("balance"));

        db.close();
        cursor.close();

        //TODO.15 접속한 사용자 정보를 가져와야 한다. API 제공 필요.
        return new User("https://optimal.inven.co.kr/upload/2019/09/10/bbs/i14186125778.jpg",
                "James",
                "2020.05.05 12:00",
                balanceLeft);
    }
}
