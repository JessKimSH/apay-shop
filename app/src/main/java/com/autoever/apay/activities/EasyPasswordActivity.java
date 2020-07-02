package com.autoever.apay.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.autoever.apay.R;
import com.autoever.apay.databinding.ActivityEasyPasswordBinding;
import com.autoever.apay.fragments.ContentTitleFragment;
import com.autoever.apay.fragments.LinkToLostPasswordFragment;
import com.autoever.apay.fragments.TopActionBarFragment;
import com.autoever.apay.utils.ActivityResult;
import com.autoever.apay.utils.DatabaseUtil;
import com.autoever.apay.utils.EasyPasswordFor;
import com.google.android.gms.iid.InstanceID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EasyPasswordActivity extends AppCompatActivity {

    private TopActionBarFragment topActionBarFragment;
    private LinkToLostPasswordFragment linkToLostPasswordFragment;
    private ContentTitleFragment contentTitleFragment;
    private ActivityEasyPasswordBinding binding;
    private int PURPOSE;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEasyPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //main activity 에서 전달받은 사용용도에 맞게 fragment 를 rendering 한다.
        PURPOSE = getIntent().getIntExtra("PURPOSE", 0);
        initializeFragments(PURPOSE);

        //numeric keypad 리스너 설정
        NumericKeypadListener listener = new NumericKeypadListener();
        final ArrayList<Button> numericButtons = new ArrayList<Button>(Arrays.asList(
                binding.button0,
                binding.button1,
                binding.button2,
                binding.button3,
                binding.button4,
                binding.button5,
                binding.button6,
                binding.button7,
                binding.button8,
                binding.button9
        ));

        for (Button button : numericButtons) {
            button.setOnClickListener(listener);
        }

        final ArrayList<Button> functionButtons = new ArrayList<>(Arrays.asList(
                binding.buttonDelete,
                binding.buttonArrange,
                binding.confirmButton
        ));

        for (Button button : functionButtons) {
            button.setOnClickListener(listener);
        }

        //button별로 랜덤 넘버 할당.
        shuffleNumbers(numericButtons);

        //사용자가 비번을 입력할 때 이벤트 처리
        final ArrayList<ImageView> passwordCharList = new ArrayList<ImageView>(Arrays.asList(
                binding.passwordChar01,
                binding.passwordChar02,
                binding.passwordChar03,
                binding.passwordChar04,
                binding.passwordChar05,
                binding.passwordChar06
        ));

        binding.passwordEdit.addTextChangedListener(new TextWatcher() {
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
    }

    private void shuffleNumbers(ArrayList<Button> numericButtons) {
        Integer[] randomNumbers = new Integer[10];
        for (int i = 0; i < randomNumbers.length; i++) {
            randomNumbers[i] = i;
        }
        Collections.shuffle(Arrays.asList(randomNumbers));

        for (int i = 0; i < numericButtons.size(); i++) {
            numericButtons.get(i).setText(randomNumbers[i].toString());
        }
    }

    private void initializeFragments(int purpose) {
        topActionBarFragment = new TopActionBarFragment();
        linkToLostPasswordFragment = new LinkToLostPasswordFragment();
        contentTitleFragment = new ContentTitleFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("PURPOSE", purpose);

        switch (purpose) {
            case EasyPasswordFor.APP_LOGIN:
                //앱에 로그인을 시도하는 경우에는 액션바에 back, close 버튼을 삭제한다.
                topActionBarFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_action_bar_container, topActionBarFragment)
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.lower_contents_container, linkToLostPasswordFragment)
                        .commit();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.upper_contents_container, contentTitleFragment)
                        .commit();
                break;
            case EasyPasswordFor.ENROLLMENT:
                //간편비밀번호 등록을 시도하는 경우에는 액션바에 back, close 버튼을 삭제한다.
            case EasyPasswordFor.VALID_CHECK:
                //간편비밀번호 확인을 시도하는 경우에는 액션바에 back 버튼을 배치한다.
                binding.passwordEdit.setText("");
                topActionBarFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_action_bar_container, topActionBarFragment)
                        .commit();
                break;
             case EasyPasswordFor.PURCHASE:
                 topActionBarFragment.setArguments(bundle);
                 getSupportFragmentManager().beginTransaction()
                         .replace(R.id.top_action_bar_container, topActionBarFragment)
                         .commit();
                 getSupportFragmentManager().beginTransaction()
                         .replace(R.id.upper_contents_container, contentTitleFragment)
                         .commit();
                 getSupportFragmentManager().beginTransaction()
                         .replace(R.id.lower_contents_container, linkToLostPasswordFragment)
                         .commit();
                 break;
            default:
                break;
        }
    }

    //숫자키패드용 리스너
    class NumericKeypadListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == binding.buttonDelete) { //지우기 버튼을 눌렀을 때.
                int length = binding.passwordEdit.getText().length();
                if (length > 0) binding.passwordEdit.getText().delete(length - 1, length);
                return;
            } else if (v == binding.buttonArrange) { //재배열 버튼을 눌렀을 때.
                Log.d("debug", "arrange button click");
                final ArrayList<Button> numericButtons = new ArrayList<Button>(Arrays.asList(
                        binding.button0,
                        binding.button1,
                        binding.button2,
                        binding.button3,
                        binding.button4,
                        binding.button5,
                        binding.button6,
                        binding.button7,
                        binding.button8,
                        binding.button9
                ));
                shuffleNumbers(numericButtons);
            } else if (v == binding.button0 || v == binding.button1 || v == binding.button2
                    || v == binding.button3 || v == binding.button4 || v == binding.button5
                    || v == binding.button6 || v == binding.button7 || v == binding.button8
                    || v == binding.button9) { //숫자버튼들을 눌렀을 때.
                Button button = (Button) v;
                binding.passwordEdit.append(button.getText().toString());
            } else if (v == binding.confirmButton) { //확인버튼을 눌렀을 때.
                //사용자가 6자리 이하의 숫자를 입력하고 확인버튼을 눌렀을 때 Toast로 알려준다.
                if (binding.passwordEdit.getText().toString().length() < 6) {
                    Toast.makeText(
                            getApplicationContext(),
                            "숫자 6자리를 입력해야 합니다.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                Log.d("debug", "PURPOSE:" + PURPOSE);
                Log.d("debug", "password:" + password);
                Log.d("debug","passwordedit:" + binding.passwordEdit.getText().toString());

                switch (PURPOSE) {
                    case EasyPasswordFor.ENROLLMENT:
                        password = binding.passwordEdit.getText().toString();
                        PURPOSE = EasyPasswordFor.VALID_CHECK;
                        initializeFragments(PURPOSE);
                        return;
                    case EasyPasswordFor.VALID_CHECK:
                        if (password.equals(binding.passwordEdit.getText().toString())) {
                            Log.d("debug", "입력한 패스워드가 일치, 패스워드를 저장하고 EasyPasswordActivity 종료");
                            DatabaseUtil databaseUtil = new DatabaseUtil(EasyPasswordActivity.this);
                            SQLiteDatabase db = databaseUtil.getWritableDatabase();

                            String sql = "update user set easy_password = ? where instance_id = ?";

                            String[] arg1 = {
                                    password,
                                    InstanceID.getInstance(EasyPasswordActivity.this).getId()
                            };

                            db.execSQL(sql, arg1);

                            db.close();

                            setResult(ActivityResult.EASY_PASSWORD_ENROLLED);
                            finish();
                            return;
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "패스워드가 일치하지 않습니다. \n이전화면으로 돌아가시려면 화면상단의 돌아가기 버튼을 눌러주세요.",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }
                    case EasyPasswordFor.APP_LOGIN:
                    case EasyPasswordFor.PURCHASE:
                        DatabaseUtil databaseUtil = new DatabaseUtil(getApplicationContext());
                        SQLiteDatabase db = databaseUtil.getWritableDatabase();

                        String sql = "select * " +
                                "from user" +
                                " where instance_id = ? " +
                                "and easy_password = ?";

                        String uniqueID = InstanceID.getInstance(getApplicationContext()).getId();

                        String arg1[] = {
                                uniqueID,
                                binding.passwordEdit.getText().toString()
                        };

                        Cursor cursor = db.rawQuery(sql, arg1);

                        if (cursor.getCount() > 0) {
                            Log.d("debug", "간편비번 인증 성공");
                            db.close();
                            setResult(RESULT_OK);
                            finish();
                            return;
                        } else {
                            Log.d("debug", "간편비번 인증 실패");
                            db.close();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "패스워드가 일치하지 않습니다",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                }
            }
        }
    }
}
