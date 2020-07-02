package com.autoever.apay.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.models.User;
import com.autoever.apay.utils.DatabaseUtil;
import com.google.android.gms.iid.InstanceID;

public class CardPaymentPriceActivity extends AppCompatActivity {
    private final static int EASY_PASSWORD_AUTH_COMPLETE = 2;
    private final static int PAYMENT_CARD_USER_CONFIRMED = 3;

    private ImageView backImageView, closeImageView;
    private TextView purchaseAmountTextView, userBalanceTextView, shopNameTextView, finishTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardPaymentPriceActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_price);

        purchaseAmountTextView = findViewById(R.id.purchase_amount);
        userBalanceTextView = findViewById(R.id.user_balance);
        shopNameTextView = findViewById(R.id.shop_name);
        finishTextView = findViewById(R.id.finish_textview);
        backImageView = findViewById(R.id.back_imageview);
        closeImageView = findViewById(R.id.close_imageview);


        String purchaseAmount = getIntent().getStringExtra("amount");
        String userBalance = getIntent().getStringExtra("balance");
        String shopName = getIntent().getStringExtra("shop_name");

        purchaseAmountTextView.setText(purchaseAmount+ " P");
        userBalanceTextView.setText(userBalance);
        shopNameTextView.setText(shopName);
        finishTextView.setText(purchaseAmount + "P 결제");

        finishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardPaymentPriceActivity.this, EasyPasswordTotal.class);
                intent.putExtra("SCREEN_TITLE", "결제");
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

        if (requestCode == EASY_PASSWORD_AUTH_COMPLETE) {
            Log.d("debug", "EASY_PASSWORD_AUTH_COMPLETE");
            switch (resultCode) {
                case RESULT_OK:
                    Log.d("debug", "EASY_PASSWORD_AUTH_COMPLETE");
                    completePurchase();
                    Intent intent = new Intent(CardPaymentPriceActivity.this, CardPaymentFinishActivity.class);
                    intent.putExtra("amount",getIntent().getStringExtra("amount"));
                    intent.putExtra("shop_name", getIntent().getStringExtra("shop_name"));
                    startActivityForResult(intent, PAYMENT_CARD_USER_CONFIRMED);
                    break;
            }

        } else if(requestCode == PAYMENT_CARD_USER_CONFIRMED) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void completePurchase() {
        //TODO.16 충전백엔드 API를 호출하여 결제한다.
        DatabaseUtil databaseUtil = new DatabaseUtil(CardPaymentPriceActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "update user set balance = balance - ? where instance_id = ?";

        String uniqueID = InstanceID.getInstance(CardPaymentPriceActivity.this).getId();

        Log.d("debug", "instance id:" + uniqueID);

        String[] arg1 = {purchaseAmountTextView.getText().toString().replaceAll("[%s,.]", ""), uniqueID};

        db.execSQL(sql, arg1);

        db.close();

        Log.d("debug", "결제완료");
    }


}
