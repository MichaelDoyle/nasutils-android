/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.md.nasutils.R;
import com.md.nasutils.ui.activity.StatusActivity;

import androidx.fragment.app.DialogFragment;

public class SmartDiskInfoDialogFragment extends DialogFragment {
    
    private final static String TITLE = "title";
    
    private String mTitle;

    public static SmartDiskInfoDialogFragment newInstance(String title) {
        SmartDiskInfoDialogFragment f = new SmartDiskInfoDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        Activity a = getActivity();
         if (a instanceof StatusActivity) {
             StatusActivity activity = (StatusActivity) a;
             activity.onRetrieveSmartDiskInfo();
         }
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        
        LayoutInflater inflater = LayoutInflater.from(activity);
        
        String ok = getResources().getString(R.string.button_ok);
        final View v = inflater.inflate(R.layout.fragment_smart, null);
        
        return new AlertDialog.Builder(activity)
                .setTitle(mTitle)
                .setView(v)
                .setPositiveButton(ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         Activity a = getActivity();
                         if (a instanceof SmartInfoDialogOnCLickListener) {
                             SmartInfoDialogOnCLickListener activity = (SmartInfoDialogOnCLickListener) a;
                             activity.onSmartDialogClicked();
                         }
                    }
                }).create();
    }
    
    /**
     * Activities that host this fragment must implement this interface
     */
    public interface SmartInfoDialogOnCLickListener {
        /**
         * Callback to the Activity when dialog is dismissed
         */
        void onSmartDialogClicked();

    }
}
