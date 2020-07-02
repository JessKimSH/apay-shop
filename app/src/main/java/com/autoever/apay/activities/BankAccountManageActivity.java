package com.autoever.apay.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.autoever.apay.R;
import com.autoever.apay.models.BankAccountInfo;
import com.autoever.apay.utils.DatabaseUtil;
import com.google.android.gms.iid.InstanceID;

import java.util.regex.Pattern;

public class BankAccountManageActivity extends AppCompatActivity {
    private final static int ADD_ACCOUNT_COMPLETE = 20;

    private TextView addBankAccountText, bankAccountNumber, bankAccountAddedDate, bankName;
    private ImageView dellBankAccountButton, backButton, closeButton, bankLogo;
    private BankAccountInfo bankAccountInfo;
    private LinearLayout accountInfoLayout;
    private CheckBox selectAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(BankAccountManageActivity)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_list);

        addBankAccountText = findViewById(R.id.add_bank_account_text);
        dellBankAccountButton = findViewById(R.id.del_bank_account_button);
        accountInfoLayout = findViewById(R.id.account_info_layout);
        backButton = findViewById(R.id.back_imageview);
        closeButton = findViewById(R.id.close_imageview);
        bankLogo = findViewById(R.id.bank_logo);
        bankAccountNumber = findViewById(R.id.bank_account_number);
        bankAccountAddedDate = findViewById(R.id.bank_account_added_date);
        bankName = findViewById(R.id.bank_name_text);
        selectAccount = findViewById(R.id.select_account);

        addBankAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BankAccountManageActivity.this, BankAccountAddStep01.class);
                startActivityForResult(intent, ADD_ACCOUNT_COMPLETE);
            }
        });

        backButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        closeButton.setOnClickListener(v -> {
            setResult(RESULT_FIRST_USER);
            finish();
        });

        dellBankAccountButton.setOnClickListener(v -> {
            if(selectAccount.isChecked()) {
                //여기서 다이얼로그를 띄워준다.
                // custom dialog
                final Dialog dialog = new Dialog(BankAccountManageActivity.this);
                dialog.setContentView(R.layout.custom_dialog_v2);

                Button okButton = dialog.findViewById(R.id.ok_button);
                Button cancelButton = dialog.findViewById(R.id.cancel_button);

                okButton.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    removeAccount();
                    bankAccountInfo = getUserBankAccountInfo();
                    layoutInitiate(bankAccountInfo);
                });

                cancelButton.setOnClickListener(v2 -> {
                    dialog.cancel();
                });

                dialog.show();

            } else {
                Toast.makeText(this, "삭제할 계좌를 선택하세요", Toast.LENGTH_LONG).show();
            }
        });

        selectAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectAccount.setButtonDrawable(R.drawable.ic_icon_check);
                } else {
                    selectAccount.setButtonDrawable(R.drawable.ic_icon_check_dis);
                }
            }
        });

        //사용자의 출금계좌 표시.
        bankAccountInfo = getUserBankAccountInfo();
        layoutInitiate(bankAccountInfo);
    }

    private void removeAccount() {
        Log.d("debug", "account remove");
        DatabaseUtil databaseUtil = new DatabaseUtil(BankAccountManageActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "update user set bank_account = ? and bank_account_number = ? and bank_account_add_date = ? where instance_id = ?";

        String uniqueID = InstanceID.getInstance(BankAccountManageActivity.this).getId();

        String[] arg1 = {null, null, null, uniqueID};

        db.execSQL(sql, arg1);

        db.close();
    }

    private void layoutInitiate(BankAccountInfo bankAccountInfo) {
        //사용자는 결제계좌를 하나만 등록할 수 있다. 그래서 계좌가 이미 등록이 되어 있으면 계좌추가하기 버튼이 비활성화 된다.
        if (bankAccountInfo.getBankId().isEmpty()) {
            //사용자가 결제계좌를 아직 등록하지 않은 경우.
            accountInfoLayout.setVisibility(View.INVISIBLE);
            dellBankAccountButton.setVisibility(View.INVISIBLE);
            addBankAccountText.setVisibility(View.VISIBLE);
        } else {
            //사용자가 결제계좌를 등록한 경우.
            dellBankAccountButton.setVisibility(View.VISIBLE);
            accountInfoLayout.setVisibility(View.VISIBLE);
            addBankAccountText.setVisibility(View.INVISIBLE);
            bankLogo.setImageResource(bankAccountInfo.getBankLogo());
            bankAccountNumber.setText(bankAccountInfo.getBankAccountNumber());
            bankAccountAddedDate.setText(bankAccountInfo.getBankAccountAddDate());
            bankName.setText(bankAccountInfo.getBankName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("debug", "step01 resultCode: " + resultCode);

        BankAccountInfo bankAccountInfo = getUserBankAccountInfo();
        layoutInitiate(bankAccountInfo);

        switch (resultCode) {
            case RESULT_FIRST_USER:
                finish();
                break;
        }
    }

    private BankAccountInfo getUserBankAccountInfo() {
        DatabaseUtil databaseUtil = new DatabaseUtil(BankAccountManageActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "select bank_account, bank_name, bank_account_number, bank_account_add_date " +
                "from user left outer join bank_master on user.bank_account = bank_master.bank_id " +
                "where user.instance_id = ?";

        String uniqueID = InstanceID.getInstance(BankAccountManageActivity.this).getId();

        String[] arg1 = {uniqueID};

        Cursor cursor = db.rawQuery(sql, arg1);

        cursor.moveToFirst();

        String bankAccount = cursor.getString(cursor.getColumnIndex("bank_account")) == null ? "" : cursor.getString(cursor.getColumnIndex("bank_account"));
        String bankName = cursor.getString(cursor.getColumnIndex("bank_name")) == null ? "" : cursor.getString(cursor.getColumnIndex("bank_name"));
        String bankAccountNumber = cursor.getString(cursor.getColumnIndex("bank_account_number")) == null ? "" : cursor.getString(cursor.getColumnIndex("bank_account_number"));
        String bankAccountAddDate = cursor.getString(cursor.getColumnIndex("bank_account_add_date")) == null ? "" : cursor.getString(cursor.getColumnIndex("bank_account_add_date"));

        db.close();
        cursor.close();

        return new BankAccountInfo(bankAccount, bankName, bankAccountNumber, bankAccountAddDate);
    }
}
