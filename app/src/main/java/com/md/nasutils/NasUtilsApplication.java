/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.ui.fragment.PreferenceConstants;

public class NasUtilsApplication extends Application {
    
    private static final String TAG = NasUtilsApplication.class.getSimpleName();

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        // User opt in/out
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean optIn = prefs.getBoolean(PreferenceConstants.ANALYTICS, false);
        FirebaseAnalytics.getInstance(mContext).setAnalyticsCollectionEnabled(optIn);
    }

    public static Context getContext() {
        return mContext;
    }

}
