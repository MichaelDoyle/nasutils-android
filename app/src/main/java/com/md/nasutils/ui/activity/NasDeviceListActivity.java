/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.md.nasutils.R;
import com.md.nasutils.ui.fragment.NasDeviceListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NasDeviceListActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = NasDeviceListActivity.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
  
        getSupportActionBar().setSubtitle(getResources().getString(R.string.select_device));
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        if (fm.findFragmentById(R.id.fragment_content) == null) {
            ft.replace(R.id.fragment_content, new NasDeviceListFragment());
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
        inflater.inflate(R.menu.activity_nas_device_list, menu);
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
    
    public void addNasDevice(View view) {
        Intent intent = new Intent(this, NasDeviceActivity.class);
        startActivity(intent);
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
}
