/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.appcompat.app.AppCompatDelegate;

import static com.md.nasutils.ui.fragment.PreferenceConstants.ANALYTICS;

/**
 * <p>Provides base functionality for preference activities.</p>
 *  
 * <p>Particularly useful functionality includes the ability 
 * to update all preference summaries based on their current values.
 * Passwords will be masked appropriately.</p>
 * 
 * @author michaeldoyle
 *
 */
public abstract class AbstractPreferenceActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    private static final int TEXT_PASSWORD = (android.text.InputType.TYPE_CLASS_TEXT | 
            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

    private AppCompatDelegate appCompatDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    
    @SuppressWarnings("deprecation") // legacy android support
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation") // legacy android support
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    
    @SuppressWarnings("deprecation") // legacy android support
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        Preference pref = findPreference(key);
        this.setSummary(pref);
        
        if (key.equals(ANALYTICS)) {
            boolean optIn = sp.getBoolean(key, false);
            FirebaseAnalytics.getInstance(getApplicationContext()).setAnalyticsCollectionEnabled(!optIn);
        }
    }
    
    /**
     * Sets all preference summaries based on the preferences' current value.
     * Ensures that passwords are always masked.
     * 
     * @param pg
     */
    protected void initSummaries(PreferenceGroup pg) {
        for (int i=0; i<pg.getPreferenceCount(); i++) {
            Preference p = pg.getPreference(i);
            if (p instanceof PreferenceGroup) {
                initSummaries((PreferenceGroup) p);
            }
            else {
                setSummary(p);
            }
        }
    }

    protected AppCompatDelegate getAppCompatDelegate() {
        if (appCompatDelegate == null) {
            appCompatDelegate = AppCompatDelegate.create(this, null);
        }
        return appCompatDelegate;
    }

    /**
     * For a given preference, set its summary based on its current value.
     * Ensures that passwords are always masked.
     * 
     * @param pref
     */
    private void setSummary(Preference pref) {
        String summaryText = null;

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            if (listPref.getEntry() != null) {
                summaryText = listPref.getEntry().toString();
            }
        }
        else if (pref instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) pref;
            int inputType = editTextPref.getEditText().getInputType();
            
            if (editTextPref.getText() != null) {
                summaryText = editTextPref.getText();
                
                // if we have a password, don't show it!
                if (inputType == TEXT_PASSWORD) {
                    summaryText = summaryText.replaceAll(".", "*");
                }
            }
        }
        
        if (summaryText == null || summaryText == "") {
            // if we didn't find any value set, just keep the summary text
            summaryText = pref.getSummary().toString();
        }
        
        pref.setSummary(summaryText); 
    }
}
