/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.R;
import com.md.nasutils.model.BackupJob;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.BackupsFragment;
import com.md.nasutils.ui.fragment.BackupsFragment.OnBackupsFragmentResume;
import com.md.nasutils.ui.fragment.ReadyNasBackupsReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasBackupsReceiverFragment.OnRetrieveBackups;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment.OnCommandExecuted;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity for viewing and configuring backups
 * 
 * @author michaeldoyle
 *
 */
public class BackupsActivity extends NasUtilsFragmentActivity implements
        OnRetrieveBackups, OnBackupsFragmentResume, OnCommandExecuted {

    @SuppressWarnings("unused")
    private static final String TAG = BackupsActivity.class.getSimpleName();

    private static final String READY_NAS_COMMAND_RECEIVER = "ReadyNasCommandReceiver";
    private static final String READY_NAS_BACKUPS_RECEIVER = "ReadyNasBackupsReceiver";

    private Nas mNas;
    private Response mError;
    private BackupsFragment mBackupsFragment;
    private NasConfiguration mNasConfiguration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        mBackupsFragment = new BackupsFragment();
        ft.replace(R.id.fragment_content, mBackupsFragment);
        mBackupsFragment.setRetainInstance(true);

        ReadyNasBackupsReceiverFragment receiver =
                (ReadyNasBackupsReceiverFragment) fm.findFragmentByTag(READY_NAS_BACKUPS_RECEIVER);
        if (receiver == null) {
            receiver = new ReadyNasBackupsReceiverFragment();
            ft.add(receiver, READY_NAS_BACKUPS_RECEIVER);
        }

        ReadyNasCommandReceiverFragment commandReceiver =
                (ReadyNasCommandReceiverFragment) fm.findFragmentByTag(READY_NAS_COMMAND_RECEIVER);
        if (commandReceiver == null) {
            commandReceiver = new ReadyNasCommandReceiverFragment();
            ft.add(commandReceiver, READY_NAS_COMMAND_RECEIVER);
        }

        mNasConfiguration = getNasConfiguration();

        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_backups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            break;
        case R.id.menu_refresh:
            refreshServices();
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void refreshServices() {
        FragmentManager fm = getSupportFragmentManager();

        ReadyNasBackupsReceiverFragment receiver =
                (ReadyNasBackupsReceiverFragment) fm.findFragmentByTag(READY_NAS_BACKUPS_RECEIVER);

        if (receiver != null) {
            LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            TextView message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.GONE);
            LinearLayout view = (LinearLayout) mBackupsFragment.getView().findViewById(R.id.fragment_content);
            view.removeAllViews();
            receiver.refreshDetails();
        }
    }

    @Override
    public void onFailure(Response error) {
        setMessage(View.VISIBLE, error.getMessage());
        setProgressBar(View.GONE);
    }

    @Override
    public void onSuccess(Nas nas) {
        mNas = nas;
        onBackupsFragmentResume();
        setProgressBar(View.GONE);
    }

    @Override
    public void onBackupsFragmentResume() {
        if (mNas != null) {
            setBackups();
            setProgressBar(View.GONE);
            setMessage(View.GONE, null);
        } else if (mError != null) {
            setProgressBar(View.GONE);
            setMessage(View.VISIBLE, mError.getMessage());
        } else {
            FragmentManager fm = getSupportFragmentManager();
            ReadyNasBackupsReceiverFragment receiver =
                    (ReadyNasBackupsReceiverFragment) fm.findFragmentByTag(READY_NAS_BACKUPS_RECEIVER);

            if (receiver != null) {
                receiver.setDetails();
            }
        }
    }

    private void setBackups() {
        if (mBackupsFragment.isAdded() && mBackupsFragment.getView() != null) {
            LinearLayout view = (LinearLayout) mBackupsFragment.getView().findViewById(R.id.fragment_content);
            view.removeAllViews();

            for (BackupJob b : mNas.getBackupJobs()) {
                RelativeLayout listItem = inflateRelativeLayout(R.layout.list_item_backup);
                refreshTextView(listItem, R.id.label, b.getName());
                refreshTextViewStatus(listItem, R.id.status_value, b.getStatus());
                refreshTextView(listItem, R.id.backups_table_sched_value, b.getSchedule());
                refreshTextView(listItem, R.id.backups_table_source_value, b.getSource());
                refreshTextView(listItem, R.id.backups_table_dest_value, b.getDestination());

                String date = b.getStatusDate() != null ? b.getStatusDate() : "";
                String statusDesc = b.getStatusDescription() + " " + date;
                refreshTextView(listItem, R.id.backups_table_status_value, statusDesc);

                Button button = (Button) listItem.findViewById(R.id.button_backup_now);
                button.setTag(b.getBackupId());

                setMaxWidth(listItem);
                view.addView(listItem);
                view.addView(createSpacer());
            }
        }
    }

    private void setProgressBar(int state) {
        LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);

        // ensure fragment is still visible
        if (progressBar != null) {
            progressBar.setVisibility(state);
        }
    }
    
    private void setMessage(int state, String message) {
        TextView messageText = (TextView) findViewById(R.id.message);

        // ensure fragment is still visible
        if (messageText != null) {
            messageText.setText(message);
            messageText.setVisibility(state);
        }
    }

    private String getBinary(boolean i) {
        return i == true ? "1" : "0";
    }

    @Override
    public void onCommandSuccess(Response response) {
        setProgressBar(View.GONE);
    }
    
    @Override
    public void onCommandFailure(Response response) {
        setProgressBar(View.GONE);
    }

    public void onClickBackupNow(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("backup_now", params);

        Button button = (Button) view;
        String backupJob = (String) button.getTag();

        ReadyNasCommandReceiverFragment receiver = (ReadyNasCommandReceiverFragment) getSupportFragmentManager()
                .findFragmentByTag(READY_NAS_COMMAND_RECEIVER);

        receiver.startBackup(backupJob);
    }
 }
