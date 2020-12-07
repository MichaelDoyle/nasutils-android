/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.NasUtilsApplication;

import androidx.fragment.app.Fragment;

/**
 * Base fragment that will send views to Google Analytics
 *  
 * @author michaeldoyle
 */
public class NasUtilsTrackedFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics.getInstance(NasUtilsApplication.getContext())
                .setCurrentScreen(getActivity(), getClass().getSimpleName(), null);
    }
}
