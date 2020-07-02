package com.autoever.apay.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.autoever.apay.R;
import com.autoever.apay.databinding.FragmentServiceUseTermsAgreeBinding;
import com.autoever.apay.utils.DatabaseUtil;
import com.autoever.apay.utils.FragmentCommunicator;
import com.autoever.apay.utils.FragmentResult;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceUseTermsAgreeFragment extends Fragment {
    private FragmentServiceUseTermsAgreeBinding binding;
    private FragmentCommunicator fragmentCommunicator;

    public ServiceUseTermsAgreeFragment(FragmentCommunicator fragmentCommunicator) {
        // Required empty public constructor
        this.fragmentCommunicator = fragmentCommunicator;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceUseTermsAgreeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //약관 동의 체크박스에 리스너 설정.
        CheckBoxListener checkBoxListener = new CheckBoxListener();
        binding.agreeAllCheckbox.setOnCheckedChangeListener(checkBoxListener);
        binding.agreeTerm01Checkbox.setOnCheckedChangeListener(checkBoxListener);
        binding.agreeTerm02Checkbox.setOnCheckedChangeListener(checkBoxListener);
        binding.agreeTerm03Checkbox.setOnCheckedChangeListener(checkBoxListener);
        binding.agreeTerm04Checkbox.setOnCheckedChangeListener(checkBoxListener);

        binding.joinTermsNextButton.setOnClickListener(v -> {
            CheckBox agreeAll = binding.agreeAllCheckbox,
                    agreeTerm01 = binding.agreeTerm01Checkbox,
                    agreeTerm02 = binding.agreeTerm02Checkbox,
                    agreeTerm03 = binding.agreeTerm03Checkbox,
                    agreeTerm04 = binding.agreeTerm04Checkbox;

            if (!agreeTerm01.isChecked() || !agreeTerm02.isChecked() || !agreeTerm03.isChecked()) {
                //필수항목 중 하나라도 동의가 되어 있지 않은 경우
                Toast.makeText(getActivity(), "필수항목에 모두 동의하셔야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //TODO.07 서비스 이용약관 동의에 대한 정의가 되면 수정해야 함.
//                    HashMap<String, Boolean> agreedTerms = new HashMap<>();
//                    agreedTerms.put("term01", agreeTerm01.isChecked());
//                    agreedTerms.put("term02", agreeTerm02.isChecked());
//                    agreedTerms.put("term03", agreeTerm03.isChecked());
//                    agreedTerms.put("term04", agreeTerm04.isChecked());

                DatabaseUtil databaseUtil = new DatabaseUtil(getActivity());
                SQLiteDatabase db = databaseUtil.getWritableDatabase();

                String sql = "insert into terms (term01, term02, term03, term04) values(?, ?, ?, ?)";

                String[] arg1 = {
                        agreeTerm01.isChecked() ? "0" : "1",
                        agreeTerm02.isChecked() ? "0" : "1",
                        agreeTerm03.isChecked() ? "0" : "1",
                        agreeTerm04.isChecked() ? "0" : "1"
                };

                db.execSQL(sql, arg1);

                db.close();

                //서비스 이용약관 동의가 완료되어 회원가입 화면으로 이동.
                fragmentCommunicator.onReceivedResultFromFragment(FragmentResult.SERVICE_USE_TERMS_AGREED);
                getActivity().getSupportFragmentManager().beginTransaction().remove(ServiceUseTermsAgreeFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    class CheckBoxListener implements CheckBox.OnCheckedChangeListener {
        CheckBox agreeAll = binding.agreeAllCheckbox,
                agreeTerm01 = binding.agreeTerm01Checkbox,
                agreeTerm02 = binding.agreeTerm02Checkbox,
                agreeTerm03 = binding.agreeTerm03Checkbox,
                agreeTerm04 = binding.agreeTerm04Checkbox;

        final ArrayList<CheckBox> agreeTermCheckBoxList = new ArrayList<>(Arrays.asList(
                agreeAll, agreeTerm01, agreeTerm02, agreeTerm03, agreeTerm04
        ));

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == agreeAll) {
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
            } else if (buttonView == agreeTerm01) {
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
            } else if (buttonView == agreeTerm02) {
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
            } else if (buttonView == agreeTerm03) {
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
            } else if (buttonView == agreeTerm04) {
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
        }
    }
}
