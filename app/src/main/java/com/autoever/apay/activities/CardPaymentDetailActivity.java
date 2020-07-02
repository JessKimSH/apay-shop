package com.autoever.apay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;

public class CardPaymentDetailActivity extends AppCompatActivity {
    private final static int QR_IMAGE_SCAN_COMPLETE = 10;

    private String shopName, purchaseAmount, purchaseDate, paymentType;
    private TextView puchaseAmountTextView, purchaseDateTextView, shopNameTextView, paymentCancelButton;
    private ImageView closeButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardPaymentDetailActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_detail);

        puchaseAmountTextView = findViewById(R.id.purchase_amount);
        purchaseDateTextView = findViewById(R.id.purchase_date);
        shopNameTextView = findViewById(R.id.shop_name);
        paymentCancelButton = findViewById(R.id.finish_textview);
        closeButton = findViewById(R.id.close_imageview);
        backButton = findViewById(R.id.back_imageview);


        shopName = getIntent().getStringExtra("shop_name");
        purchaseAmount = getIntent().getStringExtra("purchase_amount");
        purchaseDate = getIntent().getStringExtra("purchase_date");
        paymentType = getIntent().getStringExtra("payment_type");

        purchaseDateTextView.setText(purchaseDate);
        puchaseAmountTextView.setText(purchaseAmount);
        shopNameTextView.setText(shopName);

        closeButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        backButton.setOnClickListener(v -> finish());

        paymentCancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(CardPaymentDetailActivity.this, CardPaymentCancelFinish.class);
            intent.putExtra("shop_name", shopName);
            intent.putExtra("purchase_amount", purchaseAmount);
            intent.putExtra("purchase_date", purchaseDate);
            intent.putExtra("payment_type", paymentType);
            startActivityForResult(intent, QR_IMAGE_SCAN_COMPLETE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
    }
}
