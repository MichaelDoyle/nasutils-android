/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.R;
import com.md.nasutils.db.NasDeviceTable;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.PowerFragment;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity where the user can start/shutdown
 * the ReadyNAS device
 * 
 * @author michaeldoyle
 *
 */
public class PowerActivity extends NasUtilsFragmentActivity {

    private static final String TAG = PowerActivity.class.getSimpleName();

    private static final String READY_NAS_COMMAND_RECEIVER = "ReadyNasCommandReceiver";

    private NasConfiguration mNasConfiguration;

    // common listener for the cancel button on a DialogInterface
    private static DialogInterface.OnClickListener CANCEL_ONCLICK_LISTENER = 
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        PowerFragment fragment = new PowerFragment();
        ft.replace(R.id.fragment_content, fragment);
        fragment.setRetainInstance(true);
        
        ReadyNasCommandReceiverFragment receiver = 
                (ReadyNasCommandReceiverFragment) fm.findFragmentByTag(READY_NAS_COMMAND_RECEIVER);
        if (receiver == null) {
            receiver = new ReadyNasCommandReceiverFragment();
            ft.add(receiver, READY_NAS_COMMAND_RECEIVER);
        }

        ft.commit();

        mNasConfiguration = getNasConfiguration();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_power, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onClickShutdown(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("power_shutdown", params);

        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ReadyNasCommandReceiverFragment receiver = (ReadyNasCommandReceiverFragment) getSupportFragmentManager()
                        .findFragmentByTag(READY_NAS_COMMAND_RECEIVER);                     
                boolean forceFsck = isForceVolumeScanChecked();
                boolean forceQuotaCheck = isForceQuotasChecked();
                receiver.sendPowerDown(forceFsck, forceQuotaCheck);
            }
        };

        showConfirmationDialog(
                R.string.dialog_title_shutdown,
                R.string.dialog_message_shutdown,
                R.string.dialog_positive_button_label_shutdown,
                positiveOnClickListener,
                R.string.dialog_negative_button_label_shutdown,
                CANCEL_ONCLICK_LISTENER);
    }
    
    public void onClickRestart(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("power_reboot", params);

        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ReadyNasCommandReceiverFragment receiver = (ReadyNasCommandReceiverFragment) getSupportFragmentManager()
                        .findFragmentByTag(READY_NAS_COMMAND_RECEIVER);
                boolean forceFsck = isForceVolumeScanChecked();
                boolean forceQuotaCheck = isForceQuotasChecked();
                receiver.sendRestart(forceFsck, forceQuotaCheck);
            }
        };
        
        showConfirmationDialog(
                R.string.dialog_title_restart,
                R.string.dialog_message_restart,
                R.string.dialog_positive_button_label_restart,
                positiveOnClickListener,
                R.string.dialog_negative_button_label_restart,
                CANCEL_ONCLICK_LISTENER);
    }

    public void onClickWol(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("power_wake_on_lan", params);

        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ReadyNasCommandReceiverFragment receiver = (ReadyNasCommandReceiverFragment) getSupportFragmentManager()
                        .findFragmentByTag(READY_NAS_COMMAND_RECEIVER);
                boolean sendAsBroadcast = isSendAsBroadcastChecked();
                int numberOfPackets = getIntFromEditText(R.id.edittext_num_of_packets, 3);
                int port = getIntFromEditText(R.id.edittext_wol_port, 9);
                receiver.sendWol(numberOfPackets, port, sendAsBroadcast);
            }
        };
        
        showConfirmationDialog(
                R.string.dialog_title_wol,
                R.string.dialog_message_wol,
                R.string.dialog_positive_button_label_wol,
                positiveOnClickListener,
                R.string.dialog_negative_button_label_wol,
                CANCEL_ONCLICK_LISTENER);
    }
    
    private void showConfirmationDialog(int titleId, int messageId,
            int positiveButtonLabelId,
            DialogInterface.OnClickListener positiveOnClickListener,
            int negativeButtonLabelId,
            DialogInterface.OnClickListener negativeOnClickListener) {
        
        String title = getResources().getString(titleId);
        String message = getResources().getString(messageId);
        String positiveButtonLabel = getResources().getString(positiveButtonLabelId);
        String negativeButtonLabel = getResources().getString(negativeButtonLabelId);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_log_warning);
        alertDialog.setPositiveButton(positiveButtonLabel, positiveOnClickListener);
        alertDialog.setNegativeButton(negativeButtonLabel, negativeOnClickListener);
        alertDialog.show();
    }
    
    private boolean isForceVolumeScanChecked() {
        return isChecked(R.id.checkbox_volume_scan);
    }
    
    private boolean isForceQuotasChecked() {
        return isChecked(R.id.checkbox_volume_scan);
    }
    
    private boolean isSendAsBroadcastChecked() {
        return isChecked(R.id.checkbox_send_as_broadcast);
    }
    
    private boolean isChecked(int id) {
        CheckBox checkBoxForceQuotaCheck = (CheckBox) findViewById(id);
        return checkBoxForceQuotaCheck.isChecked();
    }
    
    /**
     * Convenience method for safely parsing an int value
     * from an EditText field
     * 
     * @param id id to lookup the EditText View
     * @param defaultValue default value to return if something goes wrong
     * @return the int value contained by the EditText, otherwise the defaultValue
     */
    private int getIntFromEditText(int id, int defaultValue) {
        EditText editText = (EditText) findViewById(id);
        String text = editText.getText().toString();
        
        int retval = 0;
        
        try {
            retval = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            Log.i(TAG, "Couldn't parse number of packets, using default");
            retval = defaultValue;
        }
        
        return retval;
    }
    
    public void saveWolSettings() {
        
        int sendAsBroadcast = isSendAsBroadcastChecked() ? 1 : 0;
        int numberOfPackets = getIntFromEditText(R.id.edittext_num_of_packets, 3);
        int port = getIntFromEditText(R.id.edittext_wol_port, 9);
        
        ContentValues values = new ContentValues();
        values.put(NasDeviceTable.COLUMN_WOL_PORT, port);
        values.put(NasDeviceTable.COLUMN_WOL_PACKETS, numberOfPackets);
        values.put(NasDeviceTable.COLUMN_WOL_SEND_AS_BCAST, sendAsBroadcast);

        getContentResolver().update(mUri, values, null, null);
    }
}
