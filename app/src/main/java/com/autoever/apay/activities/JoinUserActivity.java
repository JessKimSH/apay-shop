package com.autoever.apay.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.autoever.apay.R;
import com.autoever.apay.databinding.ActivityJoinUserBinding;
import com.autoever.apay.fragments.AccessPermissionFragment;
import com.autoever.apay.fragments.ServiceUseTermsAgreeFragment;
import com.autoever.apay.fragments.UserRegisterFragment;
import com.autoever.apay.utils.ActivityResult;
import com.autoever.apay.utils.DatabaseUtil;
import com.autoever.apay.utils.EasyPasswordFor;
import com.autoever.apay.utils.FragmentCommunicator;
import com.autoever.apay.utils.FragmentResult;
import com.autoever.apay.utils.TextFieldPatternMatcher;
import com.google.android.gms.iid.InstanceID;

public class JoinUserActivity extends AppCompatActivity implements FragmentCommunicator {
    private ActivityJoinUserBinding binding;
    private AccessPermissionFragment accessPermissionFragment;
    private ServiceUseTermsAgreeFragment serviceUseTermsAgreeFragment;
    private UserRegisterFragment userRegisterFragment;

    private boolean appAccessPermissionGranted = false;
    private boolean serviceUseTermsAgreed = false;
    private boolean userRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate(JoinUser)");
        super.onCreate(savedInstanceState);
        binding = ActivityJoinUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.appAccessPermissionGranted = isAppAccessPermissionGranted();
        this.serviceUseTermsAgreed = isServiceUseTermsAgreed();
        checkPrerequisites();
    }

    private void checkPrerequisites() {
        //1. 앱 접근권한 허용 확인.
        if (!this.appAccessPermissionGranted) {
            openAccessPermissionFragment();
            return;
        }
        //2. 서비스약관 동의 확인.
        if (!this.serviceUseTermsAgreed) {
            openServiceUseTermsAgreementFragment();
            return;
        }

        //3. 회원가입.
        if (!this.userRegistered) {
            openUserRegisterFragment();
            return;
        }

        //4. 간편비밀번호 등록.
        openEasyPasswordEnrollActivity();
    }

    private void openEasyPasswordEnrollActivity() {
        Intent intent = new Intent(getApplicationContext(), EasyPasswordActivity.class);
        intent.putExtra("PURPOSE", EasyPasswordFor.ENROLLMENT);
        startActivityForResult(intent, ActivityResult.EASY_PASSWORD_ENROLLED);
    }

    private void openUserRegisterFragment() {
        Log.d("debug", "회원가입 fragment open");
        userRegisterFragment = new UserRegisterFragment(JoinUserActivity.this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.join_user_layout, userRegisterFragment)
                .commit();
    }

    private void openServiceUseTermsAgreementFragment() {
        Log.d("debug", "서비스약관동의 fragment open");
        serviceUseTermsAgreeFragment = new ServiceUseTermsAgreeFragment(JoinUserActivity.this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.join_user_layout, serviceUseTermsAgreeFragment)
                .commit();
    }

    private void openAccessPermissionFragment() {
        Log.d("debug", "앱 접근권한 확인 fragment open");
        accessPermissionFragment = new AccessPermissionFragment(JoinUserActivity.this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.join_user_layout, accessPermissionFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", "bbbbbb");

        if (resultCode == ActivityResult.EASY_PASSWORD_ENROLLED) {
            Log.d("debug", "easy password enrolled");
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "onDestroy(JoinUser)");
        Log.d("debug", "keyboard down(JoinUser)");
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "onStop(JoinUser)");
//        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "onRestart(JoinUser)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "onResume(JoinUser)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "onStart(JoinUser)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "onPause(JoinUser)");
    }

    private boolean isAppAccessPermissionGranted() {
        // 현재 안드로이드 버전이 6.0 미만이면 메서드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return this.appAccessPermissionGranted = true;
        }

        //필수 권한(CALL_PHONE, READ_SMS)을 획득하였는지 확인한다.
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED)) {
            // 권한이 부여되지 않은 경우 AppPermissionsActivity 로 사용자에게 필요한 권한을 알리고 권한을 획득한다.
            Log.d("debug", "permission not granted");
            return this.appAccessPermissionGranted = false;
        }

        Log.d("debug", "permission granted");
        return this.appAccessPermissionGranted = true;
    }

    private boolean isServiceUseTermsAgreed() {
        //TODO.14 운영단계에서는 사용자가 서비스 이용약관에 동의했는지 체크하는 api를 호출하여 결과를 반환한다.
        DatabaseUtil databaseUtil = new DatabaseUtil(this);
        SQLiteDatabase db = databaseUtil.getWritableDatabase();

        String sql = "select * from terms where term01 = 0 and term02 = 0 and term03 = 0";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            Log.d("debug", "이미 약관에 동의한 사용자입니다.");
            db.close();
            return this.serviceUseTermsAgreed = true;
        } else {
            Log.d("debug", "약관에 동의하지 않은 사용자입니다.");
            db.close();
            return this.serviceUseTermsAgreed = false;
        }
    }

    private boolean isUserRegistered() {
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
            return this.userRegistered = true;
        } else {
            Log.d("debug", "가입되지 않은 사용자입니다.");
            db.close();
            return this.userRegistered = false;
        }
    }

    @Override
    public void onReceivedResultFromFragment(FragmentResult result) {
        Log.d("debug", "result:" + result);
        switch (result) {
            case APP_ACCESS_PERMISSION_GRANTED: //권한 부여가 완료된 경우
                Log.d("debug", "permissions granted");
                this.appAccessPermissionGranted = true;
                checkPrerequisites();
                break;
            case SERVICE_USE_TERMS_AGREED:
                Log.d("debug", "서비스약관 동의 완료");
                this.serviceUseTermsAgreed = true;
                checkPrerequisites();
                break;
            case USER_REGISTER_COMPLETED:
                Log.d("debug", "회원가입 완료");
                this.userRegistered = true;
                checkPrerequisites();
            default:
                break;
        }
    }
}
