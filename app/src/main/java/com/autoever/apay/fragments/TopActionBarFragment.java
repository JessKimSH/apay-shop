package com.autoever.apay.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autoever.apay.databinding.FragmentTopActionBarBinding;
import com.autoever.apay.utils.EasyPasswordFor;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopActionBarFragment extends Fragment {
    private FragmentTopActionBarBinding binding;

    public TopActionBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTopActionBarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        switch (getArguments().getInt("PURPOSE")) {
            case EasyPasswordFor.APP_LOGIN:
                binding.backImageview.setVisibility(View.INVISIBLE);
                binding.closeImageview.setVisibility(View.INVISIBLE);
                binding.topActionBarTitle.setVisibility(View.INVISIBLE);
                break;
            case EasyPasswordFor.ENROLLMENT:
                binding.backImageview.setVisibility(View.INVISIBLE);
                binding.closeImageview.setVisibility(View.INVISIBLE);
                binding.topActionBarTitle.setText("간편비밀번호 등록");
                break;
            case EasyPasswordFor.VALID_CHECK:
                binding.backImageview.setVisibility(View.VISIBLE);
                binding.closeImageview.setVisibility(View.INVISIBLE);
                binding.topActionBarTitle.setText("간편비밀번호 확인");
                break;
            case EasyPasswordFor.PURCHASE:
                binding.backImageview.setVisibility(View.VISIBLE);
                binding.closeImageview.setVisibility(View.INVISIBLE);
                binding.topActionBarTitle.setText("결제 진행 중");
                break;
            default:
                break;
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
