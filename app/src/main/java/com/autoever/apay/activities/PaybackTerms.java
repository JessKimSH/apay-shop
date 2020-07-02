package com.autoever.apay.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.utils.DatabaseUtil;

import java.util.ArrayList;

public class PaybackTerms extends AppCompatActivity {
    private final static int PAYBACK_TERMS_AGREE = 2;
    private final static int PAYBACK_COMPLETE = 3;
    private final static int PAYBACK = 8;

    private CheckBox agreeAll;
    private CheckBox agreeTerm01;
    private CheckBox agreeTerm02;
    private CheckBox agreeTerm03;
    private CheckBox agreeTerm04;
    private TextView nextButton;
    private ImageView backButton, closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug",":onCreate(PaybackTerms)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payback_terms);

        agreeAll = findViewById(R.id.agree_all_checkbox);
        agreeTerm01 = findViewById(R.id.agree_term01_checkbox);
        agreeTerm02 = findViewById(R.id.agree_term02_checkbox);
        agreeTerm03 = findViewById(R.id.agree_term03_checkbox);
        agreeTerm04 = findViewById(R.id.agree_term04_checkbox);
        backButton = findViewById(R.id.back_imageview);
        closeButton = findViewById(R.id.close_imageview);

        final ArrayList<CheckBox> agreeTermCheckBoxList = new ArrayList<CheckBox>();
        agreeTermCheckBoxList.add(agreeTerm01);
        agreeTermCheckBoxList.add(agreeTerm02);
        agreeTermCheckBoxList.add(agreeTerm03);
        agreeTermCheckBoxList.add(agreeTerm04);

        nextButton = findViewById(R.id.join_terms_next_button);

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
                } else {
                    //"모두 동의합니다" 버튼을 해제했을 때는 모든 약관동의 버튼에 체크해제한다.
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    for (CheckBox checkBox : agreeTermCheckBoxList) {
                        checkBox.setButtonDrawable(R.drawable.ic_icon_check_dis);
                        checkBox.setChecked(false);
                    }
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
                    } else if (agreeTerm02.isChecked() && agreeTerm03.isChecked() && agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm01.setButtonDrawable(R.drawable.ic_icon_check_dis);
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
                    } else if (agreeTerm01.isChecked() && agreeTerm03.isChecked() && agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm02.setButtonDrawable(R.drawable.ic_icon_check_dis);
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
                    } else if (agreeTerm01.isChecked() && agreeTerm02.isChecked() && agreeTerm04.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm03.setButtonDrawable(R.drawable.ic_icon_check_dis);
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
                    } else if (agreeTerm01.isChecked() && agreeTerm02.isChecked() && agreeTerm03.isChecked()) {
                        agreeAll.setButtonDrawable(R.drawable.ic_icon_check);
                    }
                } else {
                    agreeAll.setButtonDrawable(R.drawable.ic_icon_check_dis);
                    agreeTerm04.setButtonDrawable(R.drawable.ic_icon_check_dis);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!agreeTerm01.isChecked() || !agreeTerm02.isChecked() || !agreeTerm03.isChecked()) {
                    //필수항목 중 하나라도 동의가 되어 있지 않은 경우
                    Toast.makeText(getApplicationContext(), "필수항목에 모두 동의하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //TODO.07 서비스 이용약관 동의에 대한 정의가 되면 수정해야 함.
//                    HashMap<String, Boolean> agreedTerms = new HashMap<>();
//                    agreedTerms.put("term01", agreeTerm01.isChecked());
//                    agreedTerms.put("term02", agreeTerm02.isChecked());
//                    agreedTerms.put("term03", agreeTerm03.isChecked());
//                    agreedTerms.put("term04", agreeTerm04.isChecked());

//                    DatabaseUtil databaseUtil = new DatabaseUtil(PaybackTerms.this);
//                    SQLiteDatabase db = databaseUtil.getWritableDatabase();
//
//                    String sql = "insert into terms (term01, term02, term03, term04) values(?, ?, ?, ?)";
//
//                    String[] arg1 = {
//                            agreeTerm01.isChecked() ? "0" : "1",
//                            agreeTerm02.isChecked() ? "0" : "1",
//                            agreeTerm03.isChecked() ? "0" : "1",
//                            agreeTerm04.isChecked() ? "0" : "1"
//                    };
//
//                    db.execSQL(sql, arg1);
//
//                    db.close();
                    Intent intent = new Intent(PaybackTerms.this, PaybackActivity.class);
                    startActivityForResult(intent, PAYBACK_COMPLETE);

                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "환전을 취소합니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PAYBACK_COMPLETE:
                setResult(PAYBACK);

                //서비스 이용약관 동의가 완료되어 회원가입 화면으로 이동.
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("debug:onDestroy(PaybackTerms)");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
        System.out.println("debug:onStop(PaybackTerms)");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("debug:onRestart(PaybackTerms)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("debug:onResume(PaybackTerms)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("debug:onStart(PaybackTerms)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("debug:onPause(PaybackTerms)");
    }
}
