package com.autoever.apay.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.utils.DatabaseUtil;
import com.autoever.apay.utils.TextFieldPatternMatcher;
import com.google.android.gms.iid.InstanceID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BankAccountAddStep03 extends AppCompatActivity {
    private LinearLayout passwordInput;
    private ImageView closeButton, backButton;
    private TextView step01, step02, step01Text, step02Text, finishButton;
    private EditText password;
    private ImageView passwordChar01, passwordChar02, passwordChar03, passwordChar04, passwordChar05, passwordChar06;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, delButton, arrangeButton;
    private String selectedBankAccount = "";
    private String selectedBankName = "";
    private String bankAccountNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(BankAccountAddStep03)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_add_03);

        Intent intent = getIntent();
        //스텝01에서 넘겨준 사용자가 선택한 은행아이디에서 은행 이름을 받아온다.
        selectedBankAccount = intent.getStringExtra("bank_account");
        selectedBankName = intent.getStringExtra("bank_name");
        bankAccountNumber = intent.getStringExtra("bank_account_number");

        closeButton = findViewById(R.id.close_imageview);
        backButton = findViewById(R.id.back_imageview);
        step01 = findViewById(R.id.step_01);
        step02 = findViewById(R.id.step_02);
        step01Text = findViewById(R.id.step_01_text);
        step02Text = findViewById(R.id.step_02_text);
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
        finishButton = findViewById(R.id.finish_textview);



        final List<ImageView> passwordCharList = Arrays.asList(
                passwordChar01,
                passwordChar02,
                passwordChar03,
                passwordChar04,
                passwordChar05,
                passwordChar06
        );


        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = password.getText().length();
                if (length > 0) password.getText().delete(length - 1, length);
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

                if (passwordLength < 1) {
                    finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                } else {
                    finishButton.setBackgroundColor(Color.parseColor("#304db9"));
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


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutInitiate();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseUtil databaseUtil = new DatabaseUtil(BankAccountAddStep03.this);
                SQLiteDatabase db = databaseUtil.getWritableDatabase();

                String sql = "select * from user where instance_id = ? and easy_password = ?";

                String uniqueID = InstanceID.getInstance(BankAccountAddStep03.this).getId();

                String arg1[] = {
                        uniqueID,
                        password.getText().toString()
                };

                Cursor cursor = db.rawQuery(sql, arg1);

                if (cursor.getCount() > 0) {
                    Log.d("debug", "간편비번인증성공");
                    String updateSql = "update user set bank_account = ?," +
                            "bank_account_number = ?," +
                            "bank_account_add_date = ?" +
                            "where instance_id = ?";
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String today = df.format(new Date());
                    String updateArg1[] = {
                            selectedBankAccount,
                            bankAccountNumber,
                            today,
                            uniqueID
                    };
                    db.execSQL(updateSql, updateArg1);
                    db.close();

                    Intent intent = new Intent(BankAccountAddStep03.this, BankAccountAddFinish.class);
                    intent.putExtra("bank_account", selectedBankAccount);
                    intent.putExtra("bank_name", selectedBankName);
                    intent.putExtra("bank_account_add_date", today);
                    intent.putExtra("bank_account_number", bankAccountNumber);
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    db.close();
                    Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //TODO. 은행정보 등록하는 부분
    private void addBankInfoToUser(String selectedBankAccount, String bankAccountNumber) {
        DatabaseUtil databaseUtil = new DatabaseUtil(BankAccountAddStep03.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();

        String sql = "update user set bank_account = ?," +
                "bank_account_number = ?," +
                "bank_account_add_date = ?" +
                "where instance_id = ?";

        String uniqueID = InstanceID.getInstance(BankAccountAddStep03.this).getId();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = df.format(new Date());

        String arg1[] = {
                selectedBankAccount,
                bankAccountNumber,
                today,
                uniqueID
        };

        db.execSQL(sql, arg1);
        db.close();
    }

    private void layoutInitiate() {
        step01.setBackgroundResource(R.drawable.step_complete);
        step02.setBackgroundResource(R.drawable.step_complete);
        step01Text.setTextColor(Color.parseColor("#a5a8b9"));
        step02Text.setTextColor(Color.parseColor("#a5a8b9"));
        step01.setText("");
        step02.setText("");
        finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
    }


}
