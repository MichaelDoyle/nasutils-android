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
 * Displays NAS Storage related status including disk usage 
 * and volume information
 * 
 * @author michaeldoyle
 *
 */
public class StorageFragment extends NasUtilsTrackedFragment {

    @SuppressWarnings("unused")
    private static final String TAG = StorageFragment.class.getSimpleName();
    
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
        statusActivity.onStorageFragmentResume();
    }
    
    public interface OnStorageFragmentResume {
        void onStorageFragmentResume();
    }
}
