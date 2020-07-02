package com.autoever.apay.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.utils.DatabaseUtil;
import com.google.android.gms.iid.InstanceID;

import java.util.ArrayList;

public class EasyPasswordTotal extends AppCompatActivity {

    private LinearLayout passwordInput;
    private TextView title, finishButton;
    private EditText password;
    private ImageView passwordChar01, passwordChar02, passwordChar03, passwordChar04, passwordChar05, passwordChar06;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, delButton, arrangeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_easy_password_total);

        title = findViewById(R.id.card_easy_password_title_textview);
        password = findViewById(R.id.password_edit);
        passwordChar01 = findViewById(R.id.password_char_01);
        passwordChar02 = findViewById(R.id.password_char_02);
        passwordChar03 = findViewById(R.id.password_char_03);
        passwordChar04 = findViewById(R.id.password_char_04);
        passwordChar05 = findViewById(R.id.password_char_05);
        passwordChar06 = findViewById(R.id.password_char_06);
        finishButton = findViewById(R.id.finish_textview);
        passwordInput = findViewById(R.id.password_input);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        delButton = findViewById(R.id.button_delete);
        arrangeButton = findViewById(R.id.button_arrange);

        final ArrayList<ImageView> passwordCharList = new ArrayList<>();
        passwordCharList.add(passwordChar01);
        passwordCharList.add(passwordChar02);
        passwordCharList.add(passwordChar03);
        passwordCharList.add(passwordChar04);
        passwordCharList.add(passwordChar05);
        passwordCharList.add(passwordChar06);

        String screenTitle = getIntent().getStringExtra("SCREEN_TITLE");
        System.out.println("debug:" + screenTitle);
        title.setText(screenTitle);
        password.requestFocus();

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = password.getText().length();
                if(length > 0) password.getText().delete(length - 1, length);
            }
        });

        arrangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });


        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("0");
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("2");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("3");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("4");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("5");
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("6");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("7");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("8");
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.append("9");
            }
        });



        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //사용자가 글자를 쓸 때 쓴 부분을 파란색칸으로 채운다.
                int passwordLength = s.toString().length();
                for (int i = 0; i < passwordLength; i++) {
                    passwordCharList.get(i).setImageResource(R.drawable.ic_bluecircle);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //사용자가 글자를 지울 때 지운 부분을 빈칸으로 채운다.
                int passwordLength = s.length();
                for (int i = passwordLength; i < passwordCharList.size(); i++) {
                    passwordCharList.get(i).setImageResource(R.drawable.ic_greycircle);
                }
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), "숫자 6자리를 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        //사용자가 비번입력 부분을 누르면 키보드가 올라온다.
//        passwordInput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                password.requestFocus();
//                Log.d("debug", "keyboard up(EasyPasswordTotal)");
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//            }
//        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseUtil databaseUtil = new DatabaseUtil(EasyPasswordTotal.this);
                SQLiteDatabase db = databaseUtil.getWritableDatabase();

                String sql = "select * from user where instance_id = ? and easy_password = ?";

                String uniqueID = InstanceID.getInstance(EasyPasswordTotal.this).getId();

                String arg1[] = {
                        uniqueID,
                        password.getText().toString()
                };

                Cursor cursor = db.rawQuery(sql, arg1);

                if (cursor.getCount() > 0) {
                    Log.d("debug", "간편비번 인증 성공");
                    db.close();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    db.close();
                    Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "onDestroy(passwordtotal)");
//        Log.d("debug","keyboard down(EasyPasswordTotal)");
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "onStop(passwordtotal)");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "onRestart(passwordtotal)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "onResume(passwordtotal)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "onStart(passwordtotal)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "onPause(passwordtotal)");
        setResult(RESULT_CANCELED);
    }
}
