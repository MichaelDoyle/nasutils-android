/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.md.nasutils.R;

import androidx.fragment.app.DialogFragment;

public class TestConnectionDialogFragment extends DialogFragment {

    public static TestConnectionDialogFragment newInstance() {
        return new TestConnectionDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();

        LayoutInflater inflater = LayoutInflater.from(activity);

        String title = getResources().getString(R.string.dialog_testing_settings);
        String ok = getResources().getString(R.string.button_ok);

        final View v = inflater.inflate(R.layout.fragment_test_connection, null);

        return new AlertDialog.Builder(activity).setTitle(title).setView(v)
                .setPositiveButton(ok, null).create();
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
