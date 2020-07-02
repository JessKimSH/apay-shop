package com.autoever.apay.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;

public class CardPaymentCancelFinish extends AppCompatActivity {
    private TextView puchaseAmountTextView, cancelDateTextView, shopNameTextView, finishButton;
    private String shopName, purchaseAmount, purchaseDate, paymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardPaymentCancelQR)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_cancel_finish);

        puchaseAmountTextView = findViewById(R.id.purchase_amount);
        cancelDateTextView = findViewById(R.id.cancel_date);
        shopNameTextView = findViewById(R.id.shop_name);
        finishButton = findViewById(R.id.finish_textview);

        shopName = getIntent().getStringExtra("shop_name");
        purchaseAmount = getIntent().getStringExtra("purchase_amount");
        purchaseDate = getIntent().getStringExtra("purchase_date");
        paymentType = getIntent().getStringExtra("payment_type");

        cancelDateTextView.setText(purchaseDate);
        puchaseAmountTextView.setText(purchaseAmount);
        shopNameTextView.setText(shopName);

        finishButton.setOnClickListener(v -> {
            finish();
        });

    }
}
