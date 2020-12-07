/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.R;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.NetworkInterface;
import com.md.nasutils.model.Response;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.NasDeviceFragment;
import com.md.nasutils.ui.fragment.ReadyNasTestConnectionReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasTestConnectionReceiverFragment.OnTestConnection;
import com.md.nasutils.ui.fragment.TestConnectionDialogFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NasDeviceActivity extends AppCompatActivity implements OnTestConnection {
    
    @SuppressWarnings("unused")
    private static final String TAG = NasDeviceListActivity.class.getSimpleName();
    
    private static final String RECEIVER = "RECEIVER";
    
    private static final List<String> STATUS_ONLINE = 
            Collections.unmodifiableList(Arrays.asList("ONLINE", "UP"));
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  
        setContentView(R.layout.activity_fragment);
  
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        if (fm.findFragmentById(R.id.fragment_content) == null) {
            ft.replace(R.id.fragment_content, new NasDeviceFragment());
        }
        
        ReadyNasTestConnectionReceiverFragment receiver = (ReadyNasTestConnectionReceiverFragment) fm
                .findFragmentByTag(RECEIVER);

        if (receiver == null) {
            receiver = new ReadyNasTestConnectionReceiverFragment();
            ft.add(receiver, RECEIVER);
        }
        
        ft.commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        case R.id.menu_settings:
            viewSettings();
            return true;
        case R.id.menu_about:
            viewAbout();
            return true;
        case R.id.menu_rate:
            viewRate();
            return true;
        case R.id.menu_twitter:
            viewTwitter();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_nas_device, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    
    private void viewSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    private void viewAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    protected void viewRate() {
        String packageName = getPackageName();
        String market = getPackageManager().getInstallerPackageName(packageName);
                
        String marketUri = null;
        if ("com.amazon.venezia".equals(market)) {
            marketUri = "amzn://apps/android?p=" + packageName;
        } else {
            marketUri = "market://details?id=" + packageName;
        }
        
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(marketUri));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i(TAG, "No market installed? How'd you get NAS Utils, anyway?");
        }
    }
    
    private void viewTwitter() {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/intent/user?screen_name=NASUtils"));
        startActivity(intent);
    }
    
    @Override
    public void onTestConnectionSuccess(Nas status) {
        FragmentManager fm = getSupportFragmentManager();
        TestConnectionDialogFragment fragmentDialog = 
                (TestConnectionDialogFragment) fm.findFragmentByTag("Dialog");

        if (fragmentDialog != null) {
            TextView textView = (TextView) fragmentDialog.getDialog().findViewById(R.id.fragment_test_connection);
            textView.setText(R.string.success);
            
            LinearLayout progressBar = (LinearLayout) fragmentDialog
                    .getDialog().findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            
            // capture mac address from the first online interface if
            // the user didn't already set one
            if (status.getNetworkInterfaces() != null) {
                for (NetworkInterface i : status.getNetworkInterfaces()) {
                    if (STATUS_ONLINE.contains(i.getStatus().toUpperCase(Locale.US))) {
                        NasDeviceFragment nasDeviceFragment = 
                                (NasDeviceFragment) fm.findFragmentById(R.id.fragment_content);
                        nasDeviceFragment.setMacAddress(i.getMacAddress());
                    }
                }
            }
        }
    }

    @Override
    public void onTestConnectionFailure(Response error) {
        FragmentManager fm = getSupportFragmentManager();

        TestConnectionDialogFragment fragmentDialog = 
                (TestConnectionDialogFragment) fm.findFragmentByTag("Dialog");

        if (fragmentDialog != null) {
            TextView textView = (TextView) fragmentDialog.getDialog().findViewById(R.id.fragment_test_connection);
            textView.setText(error.getMessage());
            
            LinearLayout progressBar = (LinearLayout) fragmentDialog
                    .getDialog().findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        }
    }
    
    public void onClickDone(View view) {
        FragmentManager fm = getSupportFragmentManager();
        NasDeviceFragment nasDeviceFragment = (NasDeviceFragment) fm.findFragmentById(R.id.fragment_content);

        NasConfiguration config = nasDeviceFragment.getNasConfiguration();
        Bundle params = new Bundle();
        params.putString("model", config.getModel());
        params.putString("os_version", Integer.toString(config.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("nas_device_save", params);

        if (nasDeviceFragment.validate()) {
            nasDeviceFragment.saveState();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
    
    public void onClickTestConnection(View view) {
        FragmentManager fm = getSupportFragmentManager();
        NasDeviceFragment nasDeviceFragment = (NasDeviceFragment) fm.findFragmentById(R.id.fragment_content);

        NasConfiguration config = nasDeviceFragment.getNasConfiguration();
        Bundle params = new Bundle();
        params.putString("model", config.getModel());
        params.putString("os_version", Integer.toString(config.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("nas_device_test", params);

        if (nasDeviceFragment.validate()) {
            TestConnectionDialogFragment dialog = TestConnectionDialogFragment.newInstance();
            dialog.show(getSupportFragmentManager(), "Dialog");
            
            ReadyNasTestConnectionReceiverFragment receiver = 
                    (ReadyNasTestConnectionReceiverFragment) fm.findFragmentByTag(RECEIVER);
            
            receiver.testConnection(config);
        }
    }
}
