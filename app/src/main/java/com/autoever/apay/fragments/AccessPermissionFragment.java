package com.autoever.apay.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autoever.apay.R;
import com.autoever.apay.databinding.FragmentAccessPermissionBinding;
import com.autoever.apay.utils.FragmentCommunicator;
import com.autoever.apay.utils.FragmentResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessPermissionFragment extends Fragment {
    private FragmentAccessPermissionBinding binding;
    private FragmentCommunicator fragmentCommunicator;

    String[] permissionList = {
            Manifest.permission.CALL_PHONE,     //전화 권한 확인
            Manifest.permission.READ_SMS        //SMS 권한 확인
    };

    public AccessPermissionFragment(FragmentCommunicator fragmentCommunicator) {
        // Required empty public constructor
        this.fragmentCommunicator = fragmentCommunicator;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccessPermissionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.confirmButton.setOnClickListener(v -> {
            requestPermissions(permissionList, 1);
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("debug:onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 사용자가 권한 허용 여부를 확인한다.
        for(int i = 0 ; i < grantResults.length ; i++){
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
            } else {
                //TODO.04 사용자가 권한요청을 거부했을 때, Toast메세지로 추후 권한이 필요하다는 것을 알려준다.
                return;
            }
        }

        fragmentCommunicator.onReceivedResultFromFragment(FragmentResult.APP_ACCESS_PERMISSION_GRANTED);
        getActivity().getSupportFragmentManager().beginTransaction().remove(AccessPermissionFragment.this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
