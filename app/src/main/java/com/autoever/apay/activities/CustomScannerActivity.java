package com.autoever.apay.activities;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.w3c.dom.Text;


public class CustomScannerActivity extends CaptureActivity {
    private TextView balanceLeft;

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_custom_scanner);

        balanceLeft = findViewById(R.id.balance_left);

        String userBalance = getIntent().getStringExtra("balance");

        balanceLeft.setText(userBalance + " P");

        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);


    }
}