/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.R;
import com.md.nasutils.model.AddOn;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.AddOnsFragment;
import com.md.nasutils.ui.fragment.AddOnsFragment.OnAddOnsFragmentResume;
import com.md.nasutils.ui.fragment.ReadyNasAddOnsReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasAddOnsReceiverFragment.OnRetrieveAddOns;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment.OnCommandExecuted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity for viewing and configuring addons
 * 
 * @author michaeldoyle
 *
 */
public class AddOnsActivity extends NasUtilsFragmentActivity implements
        OnRetrieveAddOns, OnAddOnsFragmentResume, OnCommandExecuted {

    @SuppressWarnings("unused")
    private static final String TAG = AddOnsActivity.class.getSimpleName();

    private static final String READY_NAS_COMMAND_RECEIVER = "ReadyNasCommandReceiver";
    private static final String READY_NAS_ADDONS_RECEIVER = "ReadyNasAddOnsReceiver";

    private Nas mNas;
    private Response mError;
    private AddOnsFragment mAddOnsFragment;
    private NasConfiguration mNasConfiguration;
    private Map<String, String> mAddOnStatus = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        mAddOnsFragment = new AddOnsFragment();
        ft.replace(R.id.fragment_content, mAddOnsFragment);
        mAddOnsFragment.setRetainInstance(true);

        ReadyNasAddOnsReceiverFragment receiver =
                (ReadyNasAddOnsReceiverFragment) fm.findFragmentByTag(READY_NAS_ADDONS_RECEIVER);
        if (receiver == null) {
            receiver = new ReadyNasAddOnsReceiverFragment();
            ft.add(receiver, READY_NAS_ADDONS_RECEIVER);
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
        getMenuInflater().inflate(R.menu.activity_addons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            break;
        case R.id.menu_save:
            save();
            break;
        case R.id.menu_refresh:
            refresh();
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void refresh() {
        FragmentManager fm = getSupportFragmentManager();

        ReadyNasAddOnsReceiverFragment receiver =
                (ReadyNasAddOnsReceiverFragment) fm.findFragmentByTag(READY_NAS_ADDONS_RECEIVER);

        if (receiver != null) {
            LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            TextView message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.GONE);
            LinearLayout view = (LinearLayout) mAddOnsFragment.getView().findViewById(R.id.fragment_content);
            view.removeAllViews();
            mAddOnStatus.clear();
            receiver.refreshDetails();
        }
    }

    private void save() {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("save_addons", params);

        // user could press save, without having first retrieved addon data
        if (mAddOnsFragment.isAdded() && mAddOnsFragment.getView() != null && mNas != null) {
            LinearLayout view = (LinearLayout) mAddOnsFragment.getView().findViewById(R.id.fragment_content);

            setProgressBar(View.VISIBLE);

            FragmentManager fm = getSupportFragmentManager();
            ReadyNasCommandReceiverFragment commandReceiver =
                    (ReadyNasCommandReceiverFragment) fm.findFragmentByTag(READY_NAS_COMMAND_RECEIVER);

            ArrayList<AddOn> changed = new ArrayList<>();
            for (Map.Entry<String, String> e : mAddOnStatus.entrySet()) {
                String k = e.getKey();
                String status = e.getValue();
                AddOn a = mNas.getAddOns().get(k);
                if (!status.equals(a.getStatus())) {
                    // make a copy - we'll set the status of the
                    // cached add-on only after a successful change
                    AddOn copy = new AddOn();
                    copy.setId(a.getId());
                    copy.setStatus(status);
                    changed.add(copy);
                }
            }
            commandReceiver.toggleAddOns(changed);
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
        onAddOnsFragmentResume();
        setProgressBar(View.GONE);
    }

    @Override
    public void onAddOnsFragmentResume() {
        if (mNas != null) {
            setAddOns();
            setProgressBar(View.GONE);
            setMessage(View.GONE, null);
        } else if (mError != null) {
            setProgressBar(View.GONE);
            setMessage(View.VISIBLE, mError.getMessage());
        } else {
            FragmentManager fm = getSupportFragmentManager();
            ReadyNasAddOnsReceiverFragment receiver =
                    (ReadyNasAddOnsReceiverFragment) fm.findFragmentByTag(READY_NAS_ADDONS_RECEIVER);

            if (receiver != null) {
                receiver.setDetails();
            }
        }
    }

    private void setAddOns() {
        if (mAddOnsFragment.isAdded() && mAddOnsFragment.getView() != null) {
            LinearLayout view = (LinearLayout) mAddOnsFragment.getView().findViewById(R.id.fragment_content);
            view.removeAllViews();

            for (AddOn a : mNas.getAddOns().values()) {
                RelativeLayout listItem = null;
                CompoundButton toggle = null;

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    listItem = inflateRelativeLayout(R.layout.list_item_addon);
                    toggle = (CompoundButton) listItem.findViewById(R.id.toggle);
                } else {
                    listItem = inflateRelativeLayout(R.layout.list_item_addon_legacy);
                    toggle = (CompoundButton) listItem.findViewById(R.id.toggle);
                }

                toggle.setChecked("ON".equals(a.getStatus()));
                toggle.setTag(R.id.addon, a.getId());
                if (!isToggleEnabled(a)) {
                    toggle.setEnabled(false);
                }

                toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        onToggleClicked(buttonView);
                    }
                });

                refreshTextView(listItem, R.id.label, a.getName());
                refreshTextView(listItem, R.id.addons_table_name_value, a.getName());
                refreshTextView(listItem, R.id.addons_table_version_value, a.getVersion());

                setMaxWidth(listItem);
                view.addView(listItem);
                view.addView(createSpacer());
            }
        }
    }

    private boolean isToggleEnabled(AddOn a) {
        if (getNasConfiguration().getOsVersion() == 6
                && a.getServiceName() == null) {
            // no service to start/stop
            return false;
        }
        return true;
    }

    public void onToggleClicked(View view) {
        String id = (String) view.getTag(R.id.addon);
        CompoundButton tb = (CompoundButton) view;
        Log.i(TAG, "Add On " + id + " enabled: " + tb.isChecked());

        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        params.putString("name", id.toLowerCase());
        FirebaseAnalytics.getInstance(this).logEvent("toggle_addon", params);

        String status = tb.isChecked() ? "ON" : "OFF";
        mAddOnStatus.put(id, status);
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
        for (Map.Entry<String, String> e : mAddOnStatus.entrySet()) {
            String k = e.getKey();
            String status = e.getValue();
            AddOn a = mNas.getAddOns().get(k);
            if (!status.equals(a.getStatus())) {
                // we got a success, so update our cache
                a.setStatus(status);
            }
        }
        mAddOnStatus.clear();

        setProgressBar(View.GONE);
    }
    
    @Override
    public void onCommandFailure(Response response) {
        // it would be better if we knew which operations
        // succeeded, and which failed so we could update
        // the cache for those that succeeded
        setProgressBar(View.GONE);
    }
 }
