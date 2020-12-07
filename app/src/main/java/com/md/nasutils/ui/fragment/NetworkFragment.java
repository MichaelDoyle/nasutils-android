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
 * Displays NAS network interface status and configuration
 * 
 * @author michaeldoyle
 *
 */
public class NetworkFragment extends NasUtilsTrackedFragment {

    @SuppressWarnings("unused")
    private static final String TAG = NetworkFragment.class.getSimpleName();
    
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
        statusActivity.onNetworkFragmentResume();
    }
    
    public interface OnNetworkFragmentResume {
        void onNetworkFragmentResume();
    }
}
