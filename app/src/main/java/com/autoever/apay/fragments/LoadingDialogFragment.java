package com.autoever.apay.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autoever.apay.R;
import com.autoever.apay.databinding.FragmentLoadingDialogBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingDialogFragment extends DialogFragment {
    private FragmentLoadingDialogBinding binding;

    public LoadingDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0);
        String loadingMessage = getArguments().getString("loadingMessage");
        binding = FragmentLoadingDialogBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            int count = 0;

            @Override
            public void run() {
                count++;

                if (count == 1)
                {
                    binding.textView.setText("LOADING.");
                }
                else if (count == 2)
                {
                    binding.textView.setText("LOADING..");
                }
                else if (count == 3)
                {
                    binding.textView.setText("LOADING...");
                }

                if (count == 3)
                    count = 0;

                handler.postDelayed(this, 800);
            }
        };
        handler.postDelayed(runnable, 0);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_loading_dialog, container, false);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
//        textAndAnimationView.stopAnimation();
    }
}
