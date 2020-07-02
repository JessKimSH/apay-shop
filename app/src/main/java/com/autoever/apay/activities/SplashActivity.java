package com.autoever.apay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 3000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO.01 splash screen 이 뜨는 동안 사용자가 앱을 강제종료할 경우 dialog box 를 띄워 사용자에게 알린다
//        int id= android.os.Process.myPid();
//        android.os.Process.killProcess(id);
    }
}
