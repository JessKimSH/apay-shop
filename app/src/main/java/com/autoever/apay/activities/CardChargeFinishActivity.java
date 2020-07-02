package com.autoever.apay.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;

public class CardChargeFinishActivity extends AppCompatActivity {
    private TextView chargeBalanceTextView, previousBalanceTextView, postChargeBalanceTextView, finishTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(CardChargeFinishActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_charge_finish);

        chargeBalanceTextView = findViewById(R.id.charge_balance);
        previousBalanceTextView = findViewById(R.id.previous_balance);
        postChargeBalanceTextView = findViewById(R.id.post_charge_balance);
        finishTextView = findViewById(R.id.finish_textview);

        String chargeBalance = getIntent().getStringExtra("charge_balance");
        String previousBalance = getIntent().getStringExtra("previous_balance");
        String postChargeBalance = getIntent().getStringExtra("post_charge_balance");

        Log.d("debug", chargeBalance + " " + previousBalance + " " + postChargeBalance);

        chargeBalanceTextView.setText(chargeBalance);
        previousBalanceTextView.setText(previousBalance);
        postChargeBalanceTextView.setText(postChargeBalance);

        finishTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
