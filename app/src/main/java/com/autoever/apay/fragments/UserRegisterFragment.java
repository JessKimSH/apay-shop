package com.autoever.apay.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.autoever.apay.R;
import com.autoever.apay.databinding.FragmentUserRegisterBinding;
import com.autoever.apay.utils.DatabaseUtil;
import com.autoever.apay.utils.FragmentCommunicator;
import com.autoever.apay.utils.FragmentResult;
import com.autoever.apay.utils.TextFieldPatternMatcher;
import com.google.android.gms.iid.InstanceID;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserRegisterFragment extends Fragment {
    private FragmentUserRegisterBinding binding;
    private FragmentCommunicator fragmentCommunicator;

    public UserRegisterFragment(FragmentCommunicator fragmentCommunicator) {
        // Required empty public constructor
        this.fragmentCommunicator = fragmentCommunicator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.nameInput.setOnEditorActionListener((v, actionId, event) -> false);

        binding.birthdayInput.setOnEditorActionListener((v, actionId, event) -> {

            TextFieldPatternMatcher textFieldPatternMatcher = new TextFieldPatternMatcher();
            if (!textFieldPatternMatcher.birthdayTypeMatches(v.getText().toString())) {
                binding.birthdayInputWarningMessage.setText("8자리의 숫자를 입력하세요");
                return true;
            }

            binding.birthdayInputWarningMessage.setText("");
            return false;
        });

        binding.birthdayInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO.08 숫자8자리인지 확인해야 한다. 숫자8자리가 아닌경우 true 를 반환하고 warning message 뿌려준다.
                TextFieldPatternMatcher textFieldPatternMatcher = new TextFieldPatternMatcher();
                if (!textFieldPatternMatcher.birthdayTypeMatches(s.toString())) {
                    binding.birthdayInputWarningMessage.setText("8자리의 숫자를 입력하세요");
                } else {
                    binding.birthdayInputWarningMessage.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

                binding.phoneNumberInput.setOnEditorActionListener((v, actionId, event) -> false);

        binding.phoneNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO.09 숫자11자리인지 확인해야 한다. 숫자11자리가 아닌경우 true 를 반환하고 warning message 뿌려준다.
                TextFieldPatternMatcher textFieldPatternMatcher = new TextFieldPatternMatcher();
                if (!textFieldPatternMatcher.phoneNumberTypeMatches(s.toString())) {
                    binding.phoneNumberInputWarningMessage.setText("정확한 휴대폰번호를 입력바랍니다.(\"-\" 제외)");
                } else {
                    binding.phoneNumberInputWarningMessage.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.authNumberInput.setOnEditorActionListener((v, actionId, event) -> false);

        binding.authNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO.10 숫자6자리인지 확인해야 한다. 숫자6자리가 아닌경우 true 를 반환하고 warning message 뿌려준다.
                TextFieldPatternMatcher textFieldPatternMatcher = new TextFieldPatternMatcher();
                if (!textFieldPatternMatcher.authNumberTypeMatches(s.toString())) {
                    binding.authNumberInputWarningMessage.setText("문자메세지로 수신한 6자리의 숫자를 입력바랍니다");
                } else {
                    binding.authNumberInputWarningMessage.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.emailInput.setOnEditorActionListener((v, actionId, event) -> false);

        binding.emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO.11 이메일 형식에 맞는지 확인해야 한다. 이메일 형식이 아닌경우 true 를 반환하고 warning message 뿌려준다.
                TextFieldPatternMatcher textFieldPatternMatcher = new TextFieldPatternMatcher();
                if (!textFieldPatternMatcher.emailTypeMatches(s.toString())) {
                    binding.emailInputWarningMessage.setText("이메일주소 형식에 맞지 않습니다");
                } else {
                    binding.emailInputWarningMessage.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.completeRegisterButton.setOnClickListener(v -> {
            //TODO.12 회원가입양식에 입력된 값이 모두 이상없는지 확인한 후 회원정보를 데이터베이스에 저장한다.
            if (binding.birthdayInputWarningMessage.getText().toString().isEmpty()
                    || binding.phoneNumberInputWarningMessage.getText().toString().isEmpty()
                    || binding.authNumberInputWarningMessage.getText().toString().isEmpty()
                    || binding.emailInputWarningMessage.getText().toString().isEmpty()) {
                DatabaseUtil databaseUtil = new DatabaseUtil(getActivity());
                SQLiteDatabase db = databaseUtil.getWritableDatabase();

                String sql = "insert into user (instance_id, is_register) values(?, ?)";

                String uniqueID = InstanceID.getInstance(getActivity()).getId();

                String[] arg1 = {uniqueID, "0"};

                db.execSQL(sql, arg1);

                db.close();

                Log.d("debug", "가입완료");

                //여기서 다이얼로그를 띄워준다.
                // custom dialog
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialog);

                Button okButton = dialog.findViewById(R.id.ok_button);

                okButton.setOnClickListener(v1 -> {
                    dialog.dismiss();

                    //회원가입이 완료되어 간편비밀번호 등록화면으로 이동.
                    fragmentCommunicator.onReceivedResultFromFragment(FragmentResult.USER_REGISTER_COMPLETED);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(UserRegisterFragment.this).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                });

                dialog.show();

            } else {
                //TODO.13 인증확인을 하지 않은 경우 메세지를 띄운다.
                Toast.makeText(getActivity(), "휴대폰 인증확인을 해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
