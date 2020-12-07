/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.md.nasutils.R;
import com.md.nasutils.ui.activity.AddOnsActivity;

import androidx.fragment.app.Fragment;

/**
 * @author michaeldoyle
 */
public class AddOnsFragment extends Fragment {
    
    @SuppressWarnings("unused")
    private static final String TAG = AddOnsFragment.class.getSimpleName();
    
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
        return inflater.inflate(R.layout.fragment_addons, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AddOnsActivity activity = (AddOnsActivity) getActivity();
        activity.onAddOnsFragmentResume();
    }

    public interface OnAddOnsFragmentResume {
        void onAddOnsFragmentResume();
    }
}
