/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


/**
 * <p>TabListener, for use with ActionBar, that will take care of 
 * instantiating, adding and removing UI fragments as the tab 
 * selection is changed by the user.</p>
 * 
 * <p>Based on the Google provided Android examples.</p>
 * 
 * @author michaeldoyle
 *
 * @param <T> fragment class
 */
public class StatusTabListener<T extends Fragment> implements ActionBar.TabListener {

    @SuppressWarnings("unused")
    private static final String TAG = StatusTabListener.class.getSimpleName();

    private final AppCompatActivity mActivity;
    private final String mTag;
    private final Bundle mArgs;
    private final Class<T> mClass;
    
    private Fragment mFragment;

    public StatusTabListener(AppCompatActivity activity, String tag, Class<T> clz) {
        this(activity, tag, clz, null);
    }

    public StatusTabListener(AppCompatActivity activity, String tag, Class<T> clz, Bundle args) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
        mArgs = args;

        mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
        
        if (mFragment != null && !mFragment.isDetached()) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.detach(mFragment);
            ft.commit();
        }
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment == null) {
            mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
            ft.add(android.R.id.content, mFragment, mTag);
        } else {
            ft.attach(mFragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment != null) {
            ft.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // do nothing.
    }
}
