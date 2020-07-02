package com.autoever.apay.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.utils.DatabaseUtil;
import com.autoever.apay.utils.TextFieldPatternMatcher;
import com.google.android.gms.iid.InstanceID;

import java.util.ArrayList;

public class BankAccountAddStep02 extends AppCompatActivity {
    private final static int STEP_03_COMPLETE = 11;


    private ImageView closeButton, backButton;
    private TextView step01, step03, step01Text, step03Text, finishButton, bankNameText;
    private CheckBox agreeAll, agreeTerm01, agreeTerm02, agreeTerm03, agreeTerm04;
    private EditText accountNumberEditText;
    private String selectedBankName = "";
    private String selectedBankAccount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(BankAccountAddStep02)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_add_02);

        closeButton = findViewById(R.id.close_imageview);
        backButton = findViewById(R.id.back_imageview);
        step01 = findViewById(R.id.step_01);
        step03 = findViewById(R.id.step_03);
        step01Text = findViewById(R.id.step_01_text);
        step03Text = findViewById(R.id.step_03_text);
        finishButton = findViewById(R.id.finish_textview);
        agreeAll = findViewById(R.id.agree_all_checkbox);
        agreeTerm01 = findViewById(R.id.agree_term01_checkbox);
        agreeTerm02 = findViewById(R.id.agree_term02_checkbox);
        agreeTerm03 = findViewById(R.id.agree_term03_checkbox);
        agreeTerm04 = findViewById(R.id.agree_term04_checkbox);
        accountNumberEditText = findViewById(R.id.account_number_edittext);
        bankNameText = findViewById(R.id.bank_name_text);

        Intent intent = getIntent();
        //스텝01에서 넘겨준 사용자가 선택한 은행아이디에서 은행 이름을 받아온다.
        selectedBankAccount = intent.getStringExtra("bank_account");
        selectedBankName = getBankName(selectedBankAccount);
        //은행이름 설정하기.
        bankNameText.setText(selectedBankName);

        accountNumberEditText.requestFocus();

        accountNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                    return;

                }
                //TODO 숫자만 포함되어 있는 지 확인해야 함.
                TextFieldPatternMatcher textFieldPatternMatcher = new TextFieldPatternMatcher();
                if (!textFieldPatternMatcher.accountNumberTypeMatches(s.toString())) {
                    Toast.makeText(getApplicationContext(), "계좌번호는 숫자만 입력가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (isTermAgreed())
                        finishButton.setBackgroundColor(Color.parseColor("#304db9"));
                }

            }
        });

        final ArrayList<CheckBox> agreeTermCheckBoxList = new ArrayList<CheckBox>();
        agreeTermCheckBoxList.add(agreeTerm01);
        agreeTermCheckBoxList.add(agreeTerm02);
        agreeTermCheckBoxList.add(agreeTerm03);
        agreeTermCheckBoxList.add(agreeTerm04);

        //"모두 동의합니다" 버튼의 리스너.
        agreeAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //"모두 동의합니다" 버튼을 눌렀을 때는 모든 약관동의 버튼에 체크한다.
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                    for (CheckBox checkBox : agreeTermCheckBoxList) {
                        checkBox.setButtonDrawable(R.drawable.ic_icon_check);
                        checkBox.setChecked(true);
                    }
                    if (!accountNumberEditText.getText().toString().isEmpty()) {
                        finishButton.setBackgroundColor(Color.parseColor("#304db9"));
                    }
                } else {
                    //"모두 동의합니다" 버튼을 해제했을 때는 모든 약관동의 버튼에 체크해제한다.
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    for (CheckBox checkBox : agreeTermCheckBoxList) {
                        checkBox.setButtonDrawable(R.drawable.ic_icon_check_dis);
                        checkBox.setChecked(false);
                    }
                    finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                }
            }
        });

        agreeTerm01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    agreeTerm01.setButtonDrawable(R.drawable.ic_icon_check);
                    if (!agreeTerm02.isChecked() || !agreeTerm03.isChecked() || !agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                        finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                    } else if (agreeTerm02.isChecked() && agreeTerm03.isChecked() && agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                        if (!accountNumberEditText.getText().toString().isEmpty()) {
                            finishButton.setBackgroundColor(Color.parseColor("#304db9"));
                        }
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm01.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                }
            }
        });

        agreeTerm02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    agreeTerm02.setButtonDrawable(R.drawable.ic_icon_check);
                    if (!agreeTerm01.isChecked() || !agreeTerm03.isChecked() || !agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                        finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                    } else if (agreeTerm01.isChecked() && agreeTerm03.isChecked() && agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                        if (!accountNumberEditText.getText().toString().isEmpty()) {
                            finishButton.setBackgroundColor(Color.parseColor("#304db9"));
                        }
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm02.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                }
            }
        });

        agreeTerm03.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    agreeTerm03.setButtonDrawable(R.drawable.ic_icon_check);
                    if (!agreeTerm01.isChecked() || !agreeTerm02.isChecked() || !agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                        finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                    } else if (agreeTerm01.isChecked() && agreeTerm02.isChecked() && agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                        if (!accountNumberEditText.getText().toString().isEmpty()) {
                            finishButton.setBackgroundColor(Color.parseColor("#304db9"));
                        }
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm03.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                }
            }
        });

        agreeTerm04.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    agreeTerm04.setButtonDrawable(R.drawable.ic_icon_check);
                    if (!agreeTerm01.isChecked() || !agreeTerm02.isChecked() || !agreeTerm03.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                        finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                    } else if (agreeTerm01.isChecked() && agreeTerm02.isChecked() && agreeTerm03.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                        if (!accountNumberEditText.getText().toString().isEmpty()) {
                            finishButton.setBackgroundColor(Color.parseColor("#304db9"));
                        }
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm04.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
                }
            }
        });

        closeButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        backButton.setOnClickListener(v ->  {
            setResult(RESULT_FIRST_USER);
            finish();
        });

        layoutInitiate();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextFieldPatternMatcher textFieldPatternMatcher = new TextFieldPatternMatcher();

                if (!agreeTerm01.isChecked() || !agreeTerm02.isChecked() || !agreeTerm03.isChecked()) {
                    //필수항목 중 하나라도 동의가 되어 있지 않은 경우
                    Toast.makeText(getApplicationContext(), "필수항목에 모두 동의하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (accountNumberEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "계좌번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!textFieldPatternMatcher.accountNumberTypeMatches(accountNumberEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "계좌번호는 숫자만 입력가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //TODO.07 서비스 이용약관 동의에 대한 정의가 되면 수정해야 함.
                    //서비스 이용약관 동의가 완료되어 회원가입 화면으로 이동.
                    Intent intent = new Intent(BankAccountAddStep02.this, BankAccountAddStep03.class);
                    intent.putExtra("bank_account", selectedBankAccount);
                    intent.putExtra("bank_name", selectedBankName);
                    intent.putExtra("bank_account_number", accountNumberEditText.getText().toString());
                    startActivityForResult(intent,STEP_03_COMPLETE);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("debug", "step3 request code :" + requestCode);
        Log.d("debug", "step3 resultcode:" + resultCode);

        switch(resultCode) {
            case RESULT_CANCELED:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case RESULT_OK:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    private String getBankName(String bank_account) {
        DatabaseUtil databaseUtil = new DatabaseUtil(BankAccountAddStep02.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "select bank_name from bank_master where bank_id = ?";

        String[] arg1 = {selectedBankAccount};

        Cursor cursor = db.rawQuery(sql, arg1);

        cursor.moveToFirst();

        selectedBankName = cursor.getString(cursor.getColumnIndex("bank_name"));
        db.close();

        cursor.close();

        return selectedBankName;

    }

    private boolean isTermAgreed() {
        //TODO. 사용자가 약관에 모두 동의했는지 체크한다.
        return true;
    }

    private void layoutInitiate() {
        step01.setBackgroundResource(R.drawable.step_complete);
        step01.setText("");
        step03.setBackgroundResource(R.drawable.step_03_dim);
        step03.setText("");
        step01Text.setTextColor(Color.parseColor("#a5a8b9"));
        step03Text.setTextColor(Color.parseColor("#a5a8b9"));
        finishButton.setBackgroundColor(Color.parseColor("#adb1bf"));
    }


}
