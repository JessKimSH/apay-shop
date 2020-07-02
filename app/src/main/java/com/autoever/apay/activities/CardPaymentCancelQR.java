package com.autoever.apay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;

public class CardPaymentCancelQR extends AppCompatActivity {
    private final static int QR_IMAGE_SCAN_COMPLETE = 10;

    private ImageView closeButton, backButton, qrImage;
    private String shopName, purchaseAmount, purchaseDate, paymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardPaymentCancelQR)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment_cancel_qr);

        shopName = getIntent().getStringExtra("shop_name");
        purchaseAmount = getIntent().getStringExtra("purchase_amount");
        purchaseDate = getIntent().getStringExtra("purchase_date");
        paymentType = getIntent().getStringExtra("payment_type");


        closeButton = findViewById(R.id.close_imageview);
        backButton = findViewById(R.id.back_imageview);
        qrImage = findViewById(R.id.qr_image);

        closeButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        backButton.setOnClickListener(v -> finish());

        qrImage.setOnClickListener(v -> {
            Intent intent = new Intent(CardPaymentCancelQR.this, CardPaymentCancelFinish.class);
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
