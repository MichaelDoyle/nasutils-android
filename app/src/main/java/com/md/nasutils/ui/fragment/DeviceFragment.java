/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.md.nasutils.R;
import com.md.nasutils.ui.activity.StatusActivity;

/**
 * Displays basic NAS details and health such as
 * hardware/firmware details and system/cpu temperatures
 * 
 * @author michaeldoyle
 *
 */
public class DeviceFragment extends NasUtilsTrackedFragment {
    
    @SuppressWarnings("unused")
    private static final String TAG = DeviceFragment.class.getSimpleName();
    
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
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusActivity statusActivity = (StatusActivity) getActivity();
        statusActivity.onDeviceFragmentResume();
    }

    public interface OnDeviceFragmentResume {
        void onDeviceFragmentResume();
    }
}
