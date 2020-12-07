/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.md.nasutils.R;
import com.md.nasutils.ui.activity.TelemetryActivity;

import androidx.fragment.app.Fragment;

/**
 * @author michaeldoyle
 */
public class TelemetryFragment extends Fragment {
    
    @SuppressWarnings("unused")
    private static final String TAG = TelemetryFragment.class.getSimpleName();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_telemetry, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TelemetryActivity activity = (TelemetryActivity) getActivity();
        activity.onTelemetryFragmentResume();
    }

    public interface OnTelemetryFragmentResume {
        void onTelemetryFragmentResume();
    }
}
