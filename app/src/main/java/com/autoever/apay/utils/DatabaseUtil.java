package com.autoever.apay.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseUtil extends SQLiteOpenHelper {

    public DatabaseUtil(Context context) {
        super(context, "apay.db", null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "create table user(" +
                "id integer primary key autoincrement," +
                "instance_id text not null," +
                "is_register int not null," +
                "easy_password text," +
                "balance long default 0," +
                "bank_account text," +
                "bank_account_number text," +
                "bank_account_add_date text" +
                ")";

        String createTermsTable = "create table terms(" +
                "id integer primary key autoincrement," +
                "term01 int," +
                "term02 int," +
                "term03 int," +
                "term04 int" +
                ")";

        String paymentHistoryTable = "create table payment_history(" +
                "id integer primary key autoincrement," +
                "shop_name text," +
                "purchase_amount long," +
                "purchase_date text," +
                "payment_type int" +
                ")";

        String bankMasterTable = "create table bank_master (" +
                "id integer primary key autoincrement," +
                "bank_id text not null," +
                "bank_name text not null" +
                ")";

        String historyData = "insert into payment_history (" +
                "shop_name," +
                "purchase_amount," +
                "purchase_date," +
                "payment_type)" +
                "values (" +
                "?, ?, ?, ?)";

        String bankData = "insert into bank_master (" +
                "bank_id, bank_name)" +
                "values (" +
                "?, ?)";

        //1 = 결제, 2 = 충전, 3 = 결제취소
        String[] arg1 = {"CU 삼성역점", "3,000P", "2020.05.05 15:24:34", "1"};
        String[] arg2 = {"스타벅스 삼성역점", "13,200P", "2020.05.05 15:24:34", "1"};
        String[] arg3 = {"온누리약국", "1,000P", "2020.05.05 15:24:34", "1"};
        String[] arg4 = {"현대백화점 무역센터점", "133,000P", "2020.05.05 15:24:34", "1"};
        String[] arg5 = {"신한은행 충전", "60,000P", "2020.05.05 15:24:34", "2"};
        String[] arg6 = {"스타벅스 삼성역점", "13,000P", "2020.05.05 15:24:34", "1"};
        String[] arg7 = {"CU 삼성역점", "13,000P", "2020.05.05 15:24:34", "3"};
        String[] arg8 = {"CU 삼성역점", "13,000P", "2020.05.05 15:24:34", "1"};
        String[] arg9 = {"CU 삼성역점", "13,000P", "2020.05.05 15:24:34", "1"};
        String[] arg10 = {"CU 삼성역점", "13,000P", "2020.05.05 15:24:34", "1"};

        String[] bankArg1 = {"01", "NH농협은행"};
        String[] bankArg2 = {"02", "우리은행"};
        String[] bankArg3 = {"03", "신한은행"};
        String[] bankArg4 = {"04", "국민은행"};
        String[] bankArg5 = {"05", "하나은행"};
        String[] bankArg6 = {"06", "씨티은행"};
        String[] bankArg7 = {"07", "IBK기업은행"};
        String[] bankArg8 = {"08", "케이뱅크"};
        String[] bankArg9 = {"09", "카카오뱅크"};

        db.execSQL(createUserTable);
        db.execSQL(createTermsTable);
        db.execSQL(paymentHistoryTable);
        db.execSQL(historyData, arg1);
        db.execSQL(historyData, arg2);
        db.execSQL(historyData, arg3);
        db.execSQL(historyData, arg4);
        db.execSQL(historyData, arg5);
        db.execSQL(historyData, arg6);
        db.execSQL(historyData, arg7);
        db.execSQL(historyData, arg8);
        db.execSQL(historyData, arg9);
        db.execSQL(historyData, arg10);
        db.execSQL(bankMasterTable);
        db.execSQL(bankData, bankArg1);
        db.execSQL(bankData, bankArg2);
        db.execSQL(bankData, bankArg3);
        db.execSQL(bankData, bankArg4);
        db.execSQL(bankData, bankArg5);
        db.execSQL(bankData, bankArg6);
        db.execSQL(bankData, bankArg7);
        db.execSQL(bankData, bankArg8);
        db.execSQL(bankData, bankArg9);

        Log.d("debug", "database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropUserTable = "drop table if exists user";
        String dropTermsTable = "drop table if exists terms";
        String dropPaymentHistoryTable = "drop table if exists payment_history";
        String dropBankMasterTable = "drop table if exists bank_master";
        db.execSQL(dropUserTable);
        db.execSQL(dropTermsTable);
        db.execSQL(dropPaymentHistoryTable);
        db.execSQL(dropBankMasterTable);
        onCreate(db);
    }
}
