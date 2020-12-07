/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.md.nasutils.R;

/**
 * Activity where the user will set up NAS details and other
 * preferences
 * 
 * @author michaeldoyle
 *
 */
public class SettingsActivity extends AbstractPreferenceActivity {

    @SuppressWarnings("unused")
    private static final String TAG = SettingsActivity.class.getSimpleName();
    
    @SuppressWarnings("deprecation") // legacy android support
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppCompatDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.pref_general);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, true);

        initSummaries(getPreferenceScreen());       
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
