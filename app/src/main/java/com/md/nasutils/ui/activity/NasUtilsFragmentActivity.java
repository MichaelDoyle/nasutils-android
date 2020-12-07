/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.db.NasDeviceTable;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.ReadyNasReceiverFragment.NasConfigurationProvider;
import com.md.nasutils.util.ScreenUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * <p>Base class providing standard functionality across
 * NAS Utils Activities.</p>
 * 
 * <p>Retrieves the URI for the current NAS Device, 
 * from either the savedInstanceState or the Intent extras,
 * and sets the ActionBar subtitle accordingly.</p>
 * 
 * @author michaeldoyle
 *
 */
public abstract class NasUtilsFragmentActivity extends AppCompatActivity
        implements NasConfigurationProvider {

    private static final String TAG = NasUtilsFragmentActivity.class.getSimpleName();

    private static final List<String> STATUS_OK =
            Collections.unmodifiableList(Arrays.asList("OK", "ONLINE", "Redundant", "REDUNDANT", "UP", "READY"));

    private static final List<String> STATUS_NA =
            Collections.unmodifiableList(Arrays.asList("NA", "OFFLINE"));

    protected Uri mUri;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE);
        }

        Bundle extras = getIntent().getExtras();
        if (mUri == null && extras != null) {
            mUri = extras.getParcelable(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE);
        }
        
        setSubtitle();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE, mUri);
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
    public void onStart() {
        super.onStart();
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCustomKey("ReadyNAS Model", getNasConfiguration().getModel());
        crashlytics.setCustomKey("ReadyNAS Firmware", getNasConfiguration().getOsVersion());
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public NasConfiguration getNasConfiguration() {
        String hostname = null;
        String user = null;
        String password = null;
        String mac = null;
        int port = 0;
        int osVersion = 0;
        String sshUser = null;
        int sshPort = 0;
        String istatPasscode = null;
        int istatPort = 0;
        int wolPort = 0;
        int wolPackets = 0;
        int wolSendAsBroadcast = 0;
        String model = null;
        
        String[] projection = { 
                NasDeviceTable.COLUMN_DNS_NAME,
                NasDeviceTable.COLUMN_MODEL,
                NasDeviceTable.COLUMN_OS_VERSION,
                NasDeviceTable.COLUMN_USERNAME,
                NasDeviceTable.COLUMN_PASSWORD,
                NasDeviceTable.COLUMN_MAC_ADDRESS,
                NasDeviceTable.COLUMN_PORT,
                NasDeviceTable.COLUMN_SSH_USERNAME,
                NasDeviceTable.COLUMN_SSH_PORT,
                NasDeviceTable.COLUMN_ISTAT_PASSCODE,
                NasDeviceTable.COLUMN_ISTAT_PORT,
                NasDeviceTable.COLUMN_WOL_PORT,
                NasDeviceTable.COLUMN_WOL_PACKETS,
                NasDeviceTable.COLUMN_WOL_SEND_AS_BCAST,
                NasDeviceTable.COLUMN_ISTAT_PORT};
        
        Cursor cursor = getContentResolver().query(mUri,
                projection, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            osVersion = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_OS_VERSION));
            hostname = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_DNS_NAME));
            port = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_PORT));
            mac = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_MAC_ADDRESS));
            user = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_USERNAME));
            password = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_PASSWORD));
            sshUser = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_SSH_USERNAME));
            sshPort = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_SSH_PORT));
            istatPasscode = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_ISTAT_PASSCODE));
            model = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_MODEL));
            
            wolPort = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_WOL_PORT));
            if (wolPort == 0) {
                wolPort = 9;
            }
            
            wolPackets = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_WOL_PACKETS));
            if (wolPackets == 0) {
                wolPackets = 3;
            }
            
            istatPort = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_ISTAT_PORT));
            if (istatPort == 0) {
                istatPort = 5109;
            }
            
            wolSendAsBroadcast = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_WOL_SEND_AS_BCAST));
            cursor.close();
        }
        
        return new NasConfiguration("https", hostname, port, user, password,
                mac, osVersion, sshUser, sshPort, istatPasscode, istatPort, 
                wolPort, wolPackets, wolSendAsBroadcast, model);
    }
    
    protected void setSubtitle() {
        String[] projection = { NasDeviceTable.COLUMN_NAME, NasDeviceTable.COLUMN_DNS_NAME };

        Cursor cursor = getContentResolver().query(mUri, projection, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_NAME));
            String hostname = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_DNS_NAME));
            getSupportActionBar().setSubtitle(name + " (" + hostname + ")");
            cursor.close();
        }
    }
    
    protected void viewSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    protected void viewAbout() {
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
    
    protected void viewTwitter() {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/intent/user?screen_name=NASUtils"));
        startActivity(intent);
    }

    protected void refreshTextViewStatus(ViewGroup root, int id, String text) {
        int color = Color.BLACK;

        if (STATUS_OK.contains(text)) {
            color = Color.rgb(34, 139, 34);
        } else if (STATUS_NA.contains(text)) {
            color = Color.BLACK;
        } else {
            color = Color.RED;
        }

        refreshTextView(root, id, text, color);
    }

    /**
     * Find a given TextView by id and set the text to a new value
     * 
     * @param root
     *            optional ViewGroup in which to search for the TextView
     * @param id
     *            id of the TextView to search
     * @param text
     *            new text to be displayed by the TextView
     */
    protected void refreshTextView(ViewGroup root, int id, String text, Integer color) {
        TextView textView = null;

        if (root != null) {
            textView = (TextView) root.findViewById(id);
        } else {
            textView = (TextView) findViewById(id);
        }

        textView.setText(text);
        
        if (color !=  null) {
            textView.setTextColor(color);
        }
    }
    
    protected void refreshTextView(ViewGroup root, int id, String text) {
        refreshTextView(root, id, text, null);
    }

    protected RelativeLayout inflateRelativeLayout(int id) {
        RelativeLayout layout = new RelativeLayout(this);
        getLayoutInflater().inflate(id, layout);
        return layout;
    }
    
    protected View createSpacer() {
        int pad = (int) getResources().getDimension(R.dimen.padding_10px);
        TextView header = new TextView(this);
        header.setHeight(pad);
        header.setWidth(LayoutParams.MATCH_PARENT);
        return header;
    }

    /**
     * Convenience method for creating a TextView
     * 
     * @param text
     *            the text to display
     * @return a TextView padded by 5 pixels
     */
    protected TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        textView.setText(text);
        return textView;
    }
    
    public void setMaxWidth(View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float dpWidth  = metrics.widthPixels / metrics.density;
        
        if (dpWidth > 400) {
            view.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils
                    .dipToPx(this, 400), LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
