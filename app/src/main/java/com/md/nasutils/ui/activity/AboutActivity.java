/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.md.nasutils.R;
import com.md.nasutils.ui.fragment.AboutWebViewFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Displays application information and open source licenses.
 * 
 * @author michaeldoyle
 *
 */
public class AboutActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = AboutActivity.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      
        setContentView(R.layout.activity_fragment);
  
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        if (fm.findFragmentById(R.id.fragment_content) == null) {
            ft.replace(R.id.fragment_content, new AboutWebViewFragment());
        }
        
        ft.commit();
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
    
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
