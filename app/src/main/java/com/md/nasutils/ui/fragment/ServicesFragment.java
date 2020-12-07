/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.md.nasutils.R;
import com.md.nasutils.ui.activity.ServicesActivity;

import androidx.fragment.app.Fragment;

/**
 * @author michaeldoyle
 */
public class ServicesFragment extends Fragment {
    
    @SuppressWarnings("unused")
    private static final String TAG = ServicesFragment.class.getSimpleName();
    
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
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ServicesActivity activity = (ServicesActivity) getActivity();
        activity.onServicesFragmentResume();
    }

    public interface OnServicesFragmentResume {
        void onServicesFragmentResume();
    }
}
