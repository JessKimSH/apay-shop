package com.autoever.apay.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autoever.apay.R;
import com.autoever.apay.databinding.ActivityMainBinding;
import com.autoever.apay.databinding.ActivityMainV2Binding;
import com.autoever.apay.databinding.ItemDrawerMainBinding;
import com.autoever.apay.fragments.AccessPermissionFragment;
import com.autoever.apay.fragments.LoadingDialogFragment;
import com.autoever.apay.models.Store;
import com.autoever.apay.models.StorePaymentHistory;
import com.autoever.apay.models.User;
import com.autoever.apay.models.VirtualAccount;
import com.autoever.apay.utils.DatabaseUtil;
import com.autoever.apay.utils.EasyPasswordFor;
import com.google.android.gms.iid.InstanceID;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.autoever.apay.activities.CommonApplication.getStore;
import static com.autoever.apay.activities.CommonApplication.getVirtualAccount;


public class MainActivity extends AppCompatActivity {

    private VirtualAccount virtualAccount = getVirtualAccount();
    private Store store = getStore();

    private ArrayList<StorePaymentHistory> storePaymentHistories;
    private RecyclerView.Adapter customAdpater;
    private RecyclerView.LayoutManager layoutManager;

    private static Context context;

    private ActivityMainBinding binding;
    private ItemDrawerMainBinding drawerMainBinding;
    private LoadingDialogFragment loadingDialogFragment;
    private CheckRegisterTask checkRegisterTask;

    private final static int EASY_PASSWORD_AUTH_COMPLETE = 2;
    private final static int SERVICE_TERMS_AGREED = 3;
    private final static int CARD_CHARGED = 4;
    private final static int REGISTER_COMPLETE = 5;
    private final static int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    private final static int PAYMENT_AMOUNT_INPUT_COMPLETE = 6;
    private final static int CARD_INFO = 7;
    private final static int PAYBACK = 8;
    private final static int BANK_ACCOUNT_MANAGEMENT = 9;
    private final static int CARD_USAGE_HISTORY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(main)");
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MainActivity.context = this;

        drawerMainBinding = binding.includeActMainDrawer;

        //1. DB 초기화
        initializeSQLite();

        //2. 회원가입 여부 확인.
        checkRegisterTask = new CheckRegisterTask();
        checkRegisterTask.execute(this);

        //사이드 네비게이션 열기버튼 설정
        binding.mainTopDrawerButton.setOnClickListener(v -> binding.drawerLayoutActMain.openDrawer(Gravity.LEFT));

        // Main 화면 결제버튼 리스너 설정
        binding.purchaseButton.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setOrientationLocked(false);
            integrator.setCaptureActivity(CustomScannerActivity.class);
            integrator.addExtra("balance", formatToKRW("" + virtualAccount.getValue()));
            integrator.initiateScan();
        });

        // Drawer 결제하기 버튼 리스너 설정
        drawerMainBinding.drawerPurchase.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setOrientationLocked(false);
            integrator.setCaptureActivity(CustomScannerActivity.class);
            integrator.addExtra("balance", formatToKRW("" + virtualAccount.getValue()));
            integrator.initiateScan();
        });

        // Drawer 환불하기 버튼 리스너 설정
        drawerMainBinding.drawerRefund.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StoreRefundHistoryActivity.class);
            startActivityForResult(intent, REGISTER_COMPLETE);
        });

        // Drawer Store 데이터 셋팅
        drawerMainBinding.textViewItemDrawerMainStoreName.setText(store.getStoreName());
        drawerMainBinding.textViewItemDrawerMainLastConnectedDate.setText("마지막 접속\n2020.06.29 08:46");

        // Main 화면의 잔액 셋팅
        binding.balance.setText(formatToKRW(virtualAccount.getValue()+"") + " P");

        // Store 거래 내역 데이터 불러 오기
        SyncData syncData = new SyncData();
        try {
            syncData.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initializeSQLite() {
        DatabaseUtil databaseUtil = new DatabaseUtil(getApplicationContext());
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
    }

    /* Store 앱에서 사용하지 않음
    private void setUserBalance(User user) {
        if (String.valueOf(user.getBalanceLeft()).length() > 5) {
            //잔액단위가 백만인 경우 글자크기를 조절한다.
            binding.cardBalanceNumber.setTextSize(25);
        } else {
            binding.cardBalanceNumber.setTextSize(30);
        }
        binding.cardBalanceNumber.setText(formatToKRW(String.valueOf(user.getBalanceLeft())));
    }

    private User getUser() {

        DatabaseUtil databaseUtil = new DatabaseUtil(MainActivity.this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();
        String sql = "select balance from user where instance_id = ?";

        String uniqueID = InstanceID.getInstance(MainActivity.this).getId();

        String[] arg1 = {uniqueID};

        Cursor cursor = db.rawQuery(sql, arg1);

        cursor.moveToFirst();

        Long balanceLeft = cursor.getLong(cursor.getColumnIndex("balance"));

        db.close();
        cursor.close();

        //TODO.15 접속한 사용자 정보를 가져와야 한다. API 제공 필요.
        return new User("https://optimal.inven.co.kr/upload/2019/09/10/bbs/i14186125778.jpg",
                "James",
                "2020.05.05 12:00",
                balanceLeft);
    }

    private void sideBarInitialize(User user) {

        //이미지를 동그랗게 세팅함.
        drawerMainBinding.imageViewItemDrawerMainUserProfile.setBackground(new ShapeDrawable(new OvalShape()));
        drawerMainBinding.imageViewItemDrawerMainUserProfile.setClipToOutline(true);

        try {
            Picasso.get()
                    //TODO.15 서버에서 사용자의 이미지를 가져와야 한다.
                    .load(user.getProfileImage())
                    .into(drawerMainBinding.imageViewItemDrawerMainUserProfile);

            drawerMainBinding.textViewItemDrawerMainUserName.setText(user.getUserName() + " 님");
            drawerMainBinding.textViewItemDrawerMainLastConnectedDate.setText("마지막접속\n" + user.getLastConnectedDate());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */

    private String formatToKRW(String number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        return nf.format(Long.valueOf(number));
    }

    private void moveToMainScreen() {
        if (checkRegister()) {
            moveToEasyPasswordCheckScreen();
            return;
        }

        moveToRegisterScreen();
    }

    private void moveToRegisterScreen() {
        Intent intent = new Intent(getApplicationContext(), JoinUserActivity.class);
        startActivityForResult(intent, REGISTER_COMPLETE);
        return;
    }

    private void moveToEasyPasswordCheckScreen() {
        Intent intent = new Intent(MainActivity.this, EasyPasswordActivity.class);
        intent.putExtra("PURPOSE", EasyPasswordFor.APP_LOGIN);
        startActivityForResult(intent, EASY_PASSWORD_AUTH_COMPLETE); //사용자가 확인버튼을 누르면 onActivityResult에게 1을 반환한다.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //여기는 회원가입, 간편비번 인증 관련 부분.
        switch (requestCode) {
            case EASY_PASSWORD_AUTH_COMPLETE: //권한부여가 완료되고 회원가입도 다 된 상태에서 간편비밀번호로 로그인 한 경우
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "로그인을 할 수 없어 앱을 종료합니다", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 초기화면에 바로 QR코드 인식기가 나오게 함
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.setOrientationLocked(false);
                    integrator.setCaptureActivity(CustomScannerActivity.class);
                    integrator.addExtra("balance", formatToKRW(""+virtualAccount.getValue()));
                    integrator.initiateScan();
//                    sideBarInitialize(getUser());
//                    setUserBalance(getUser());
                    binding.drawerLayoutActMain.setVisibility(View.VISIBLE);
                }
                break;
            case SERVICE_TERMS_AGREED:
                Log.d("debug", "권한부여 완료, 서비스 이용약관 동의 완료");
                moveToMainScreen();
                break;
//            case CARD_CHARGED:
//                setUserBalance(getUser());
//                break;
//            case CARD_INFO:
//                if (resultCode == CARD_CHARGED || resultCode == PAYBACK)
//                    setUserBalance(getUser());
//                break;
            case REGISTER_COMPLETE:
//                sideBarInitialize(getUser());
//                setUserBalance(getUser());
                binding.drawerLayoutActMain.setVisibility(View.VISIBLE);
                break;
            case PAYMENT_AMOUNT_INPUT_COMPLETE:
//                sideBarInitialize(getUser());
//                setUserBalance(getUser());
                break;
//            case BANK_ACCOUNT_MANAGEMENT:
//                switch (resultCode) {
//                    case RESULT_FIRST_USER:
//                        onBackPressed();
//                        break;
//                }
            default:
                break;
        }


        //여기서부터는 QR리더기를 위한 부분.
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Log.d("debug", "CUSTOMIZED_REQUEST_CODE");
                Toast.makeText(this, "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if (result.getContents() == null) {
            Intent originalIntent = result.getOriginalIntent();
            if (originalIntent == null) {
                Log.d("debug", "Cancelled scan");
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d("debug", "Cancelled scan due to missing camera permission");
                Toast.makeText(this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
            }
        } else {
            //사용자가 가맹점의 결제QR을 스캔성공함.
            Log.d("debug", "Scanned");

            JSONObject json = null;

            try {
                json = new JSONObject(result.getContents());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //금액입력화면으로 이동.
            Intent intent = new Intent(MainActivity.this, StorePaymentActivity.class);
            intent.putExtra("userId", json.optString("userId"));
            startActivityForResult(intent, PAYMENT_AMOUNT_INPUT_COMPLETE); //사용자가 확인버튼을 누르면 onActivityResult에게 1을 반환한다.
        }
    }

    private boolean checkRegister() {
        //TODO.06 회원가입여부를 확인하여 반환한다.
        //회원가입 방식을 협의해야 함.
        DatabaseUtil databaseUtil = new DatabaseUtil(this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();

        String sql = "select * from user where instance_id = ?";

        String uniqueID = InstanceID.getInstance(this).getId();

        String arg1[] = {uniqueID};

        Cursor cursor = db.rawQuery(sql, arg1);

        if (cursor.getCount() > 0) {
            Log.d("debug", "이미 가입된 사용자입니다.");
            db.close();
            return true;
        } else {
            Log.d("debug", "가입되지 않은 사용자입니다.");
            db.close();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayoutActMain.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayoutActMain.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    class CheckRegisterTask extends AsyncTask<Context, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            binding.drawerLayoutActMain.setVisibility(View.INVISIBLE);
            //Loading Dialog 생성.
            loadingDialogFragment = new LoadingDialogFragment();
            Bundle args = new Bundle();
            args.putString("loadingMessage", "회원정보를 확인 중 입니다..");
            loadingDialogFragment.setArguments(args);
            loadingDialogFragment.show(getSupportFragmentManager(), "loading dialog");
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Context... contexts) {
            SystemClock.sleep(2000);
            //TODO.06 회원가입여부를 확인하여 반환한다.
            //회원가입 방식을 협의해야 함.
            DatabaseUtil databaseUtil = new DatabaseUtil(contexts[0]);
            SQLiteDatabase db = databaseUtil.getWritableDatabase();
            String sql = "select * from user where instance_id = ?";

            String uniqueID = InstanceID.getInstance(contexts[0]).getId();

            String arg1[] = {uniqueID};

            Cursor cursor = db.rawQuery(sql, arg1);

            if (cursor.getCount() > 0) {
                Log.d("debug", "이미 가입된 사용자입니다.");
                db.close();
                return true;
            } else {
                Log.d("debug", "가입되지 않은 사용자입니다.");
                db.close();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) { //간편비밀번호 이동
                loadingDialogFragment.dismiss();
                moveToEasyPasswordCheckScreen();
                return;
            }

            //회원가입 Activity 를 실행.
            moveToRegisterScreen();
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }
    }

    /*
    Store의 전체 거래 내역 데이터 불러 오기
     */
    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String str, receiveMsg = null;

        @Override
        protected String doInBackground(String... strings)
        {
            URL url = null;

            try {
                url = new URL("http://api.apay.ga:8080/paymentHistories?tokenSystemId=1&storeId=2&pageSize=10");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    while (((str = bufferedReader.readLine()) != null)) {
                        stringBuffer.append(str);
                    }
                    receiveMsg = stringBuffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    storePaymentHistories = getStorePaymentHistory(stringBuffer.toString());

                    bufferedReader.close();
                } else {
                    Log.i("Connection", "Error");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String msg) {
            // RecyclerView 환경 설정
            binding.txRecyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(getAppContext());
            binding.txRecyclerView.setLayoutManager(layoutManager);

            customAdpater = new MainActivity.CustomAdapter(storePaymentHistories);
            binding.txRecyclerView.setAdapter(customAdpater);

        }
    }

    // PaymentCancelList 와 Data 연결
    class CustomAdapter extends RecyclerView.Adapter<MainActivity.CustomAdapter.CustomViewHolder> {

        private ArrayList<StorePaymentHistory> mList;

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView paymentNumberTextView, purchaseAmountTextView, purchaseDateTextView, paymentTypeTextView;
            protected LinearLayout recyclerId;

            public CustomViewHolder(View view) {
                super(view);

                this.recyclerId = view.findViewById(R.id.recycler_id);

                this.paymentNumberTextView = view.findViewById(R.id.payment_id);
                this.purchaseAmountTextView = view.findViewById(R.id.purchase_amount);
                this.purchaseDateTextView = view.findViewById(R.id.purchase_date);
                this.paymentTypeTextView = view.findViewById(R.id.payment_type);
            }
        }


        // CustomAdapter에 data 삽입
        public CustomAdapter(ArrayList<StorePaymentHistory> list) {
            this.mList = list;
        }

        // 데이터 한 개 당 View 생성
        @Override
        public MainActivity.CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_store, viewGroup, false);

            MainActivity.CustomAdapter.CustomViewHolder viewHolder = new MainActivity.CustomAdapter.CustomViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.CustomAdapter.CustomViewHolder viewholder, int position) {

            if (storePaymentHistories.get(position).getPaymentStatus().equals("PAY_COMPLETE")) {
                viewholder.purchaseAmountTextView.setTextColor(Color.parseColor("#304db9"));
                viewholder.paymentTypeTextView.setText("결제");
            } else if (storePaymentHistories.get(position).getPaymentStatus().equals("REFUND_COMPLETE")) {
                viewholder.purchaseAmountTextView.setTextColor(Color.parseColor("#cb3333"));
                viewholder.paymentTypeTextView.setText("환불");
            } else if (storePaymentHistories.get(position).getPaymentStatus().equals("PAY_FAIL")) {
                viewholder.paymentTypeTextView.setText("결제오류");
            }

            viewholder.paymentNumberTextView.setText(storePaymentHistories.get(position).getPaymentId());
            viewholder.purchaseAmountTextView.setText(formatToKRW(""+storePaymentHistories.get(position).getAmount()) + " P");
            viewholder.purchaseDateTextView.setText(formatToDate(storePaymentHistories.get(position).getCreatedDate().replace("T", " ")));
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }
    }

    public ArrayList<StorePaymentHistory> getStorePaymentHistory(String jsonString) {
        ArrayList<StorePaymentHistory> returnArray = new ArrayList<StorePaymentHistory>();

        try {
            JSONObject json = new JSONObject(jsonString).getJSONObject("data");
            JSONArray jsonArray = json.getJSONArray("content");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                returnArray.add(new StorePaymentHistory(
                        jsonObject.optString("paymentId"),
                        jsonObject.optString("userId"),
                        jsonObject.optString("storeId"),
                        jsonObject.optString("tokenSystemId"),
                        jsonObject.optInt("amount"),
                        jsonObject.optString("paymentStatus"),
                        jsonObject.optString("identifier"),
                        jsonObject.optString("createdDate")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnArray;
    }

    public static Context getAppContext() {
        Log.i("layoutManage getAppContext()", MainActivity.context.toString());
        return MainActivity.context;
    }

    private String formatToDate(String createdDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
        SimpleDateFormat createdDateFormat = new SimpleDateFormat("yyyy-MM-dd(E) hh:mm:ss", Locale.KOREA);

        Date paidDate = null;
        try {
            Log.i("CREATED", createdDate);
            paidDate = simpleDateFormat.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return createdDateFormat.format(paidDate);
    }
}
