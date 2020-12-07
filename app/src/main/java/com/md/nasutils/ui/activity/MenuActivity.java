/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.model.MenuItem;
import com.md.nasutils.model.Response;
import com.md.nasutils.model.Service;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.MainNavigationListFragment;
import com.md.nasutils.ui.fragment.MainNavigationListFragment.MainNavigation;
import com.md.nasutils.ui.fragment.PreferenceConstants;
import com.md.nasutils.ui.fragment.ReadyNasFtpServiceDetailsReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasFtpServiceDetailsReceiverFragment.OnRetrieveFtpServiceDetails;
import com.md.nasutils.ui.widget.MultiViewAdaptable;
import com.md.nasutils.ui.widget.MultiViewArrayAdapter;

import java.util.ArrayList;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_FORCE_FTPS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_FTPS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MODE;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_PORT;


/**
 * Provides the primary navigation menu
 * 
 * @author michaeldoyle
 *
 */
public class MenuActivity extends NasUtilsFragmentActivity implements MainNavigation, OnRetrieveFtpServiceDetails {

    private static final String TAG = MenuActivity.class.getSimpleName();

    private static final String MARKET_CONNECT_BOT = "market://details?id=org.connectbot";
    private static final String MARKET_WEB_CONNECT_BOT = "http://play.google.com/store/apps/details?id=org.connectbot";
    private static final String WEB_CONNECT_BOT = "https://code.google.com/p/connectbot/";
    private static final String MARKET_AND_FTP = "market://details?id=lysesoft.andftp";
    private static final String MARKET_WEB_AND_FTP = "http://play.google.com/store/apps/details?id=lysesoft.andftp";
    private static final String WEB_AND_FTP = "http://www.lysesoft.com/products/andftp/";
    private static final String FTP_SERVICE_DETAILS_RECEIVER = "FtpServiceDetailsReceiver";

    private MultiViewArrayAdapter mListAdapter;
    private ArrayList<MultiViewAdaptable> mBackingList;
    private Service mFtpService;
    private NasConfiguration mNasConfiguration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main_menu);
        
        Resources r = getResources();       
        
        mBackingList = new ArrayList<>();

        if (!getNasConfiguration().isReadyData()) {
            mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_addons), R.drawable.ic_addon));
            mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_backups), R.drawable.ic_backups));
        }

        mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_ftp), R.drawable.ic_ftp));
        mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_status), R.drawable.ic_health));
        mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_logs), R.drawable.ic_logs));
        mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_power), R.drawable.ic_power));
        mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_services), R.drawable.ic_settings));
        mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_ssh), R.drawable.ic_ssh));
        mBackingList.add(new MenuItem(r.getString(R.string.main_navigation_istat), R.drawable.ic_telemetry));

        mListAdapter = new MultiViewArrayAdapter(this, mBackingList);
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        ListFragment list = new MainNavigationListFragment();
        ft.replace(R.id.fragment_content, list);
        list.setListAdapter(mListAdapter);
        list.setRetainInstance(true);
                
        ReadyNasFtpServiceDetailsReceiverFragment receiver = 
                (ReadyNasFtpServiceDetailsReceiverFragment) fm.findFragmentByTag(FTP_SERVICE_DETAILS_RECEIVER);
        if (receiver == null) {
            receiver = new ReadyNasFtpServiceDetailsReceiverFragment();
            ft.add(receiver, FTP_SERVICE_DETAILS_RECEIVER);
        }
        
        ft.commit();

        mNasConfiguration = getNasConfiguration();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE, mUri);
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_power, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEvent(String label) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent(label, params);
    }
    
    @Override
    public void viewLogs() {
        sendEvent("main_menu_logs");
        createIntent(LogsActivity.class);
    }
    
    @Override
    public void viewStatus() {
        sendEvent("main_menu_status");
        createIntent(StatusActivity.class);
    }
    
    @Override
    public void viewServices() {
        sendEvent("main_menu_services");
        createIntent(ServicesActivity.class);
    }
    
    @Override
    public void viewPower() {
        sendEvent("main_menu_power");
        createIntent(PowerActivity.class);
    }

    @Override
    public void viewBackups() {
        sendEvent("main_menu_backups");
        createIntent(BackupsActivity.class);
    }

    @Override
    public void viewAddOns() {
        sendEvent("main_menu_addons");
        createIntent(AddOnsActivity.class);
    }
    
    @Override
    public void viewTelemetry() {
        sendEvent("main_menu_istat");
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dialogStatus = prefs.getBoolean(PreferenceConstants.SHOW_ISTAT_CHECKBOX, false);
        
        if (!dialogStatus) {
            showTelemetryAlertDialog();
        }
        else {
            createIntent(TelemetryActivity.class);
        }
    }
    
    @Override
    public void launchSsh() {
        sendEvent("main_menu_ssh");

        NasConfiguration nasConfig = getNasConfiguration();
        String message = nasConfig.validateSsh();
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "SSH via ConnectBot to: " + nasConfig.getSshUri());
            Intent intent = new Intent(Intent.ACTION_VIEW, nasConfig.getSshUri());
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                showMarketAlertDialog(R.string.ssh_dialog_header, 
                        R.string.ssh_dialog_message, 
                        MARKET_CONNECT_BOT, MARKET_WEB_CONNECT_BOT, WEB_CONNECT_BOT);
            }
        }
    }
    
    @Override
    public void launchFtp() {
        sendEvent("main_menu_ftp");

        // We ask the NAS to tell us what port is open for FTP, 
        // among other details to avoid troubling the user
        if (mFtpService == null) {
            FragmentManager fm = getSupportFragmentManager();
        
            ReadyNasFtpServiceDetailsReceiverFragment receiver = 
                    (ReadyNasFtpServiceDetailsReceiverFragment) fm.findFragmentByTag(FTP_SERVICE_DETAILS_RECEIVER);
            
            if (receiver != null) {
                LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                receiver.fetchFtpServiceDetails();
            }
        } else {
            startFtpIntent();
        }
    }
    
    private void startFtpIntent() {
        NasConfiguration nasConfig = getNasConfiguration();
        String message = nasConfig.validateFtp();
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            boolean ftps = "1".equals(mFtpService.getOptionByName(SERVICE_ATTR_FTP_FTPS)) ? true : false;
            boolean forceFtps = "1".equals(mFtpService.getOptionByName(SERVICE_ATTR_FTP_FORCE_FTPS)) ? true : false;
            boolean ftpsEnabled = ftps || forceFtps;
            boolean anonymous = "anonymous".equals(mFtpService.getOptionByName(SERVICE_ATTR_FTP_MODE)) ? true : false;
            Uri uri = nasConfig.getFtpUri(ftpsEnabled, mFtpService.getOptionByName(SERVICE_ATTR_FTP_PORT));
            Log.i(TAG, "FTP via AndFTP to: " + uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            String username = "";
            if (anonymous) {
                username = "anonymous";
            } else {
                username = nasConfig.getFtpUserName();
            }
            intent.putExtra("ftp_username", username);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                showMarketAlertDialog(R.string.ftp_dialog_header, 
                        R.string.ftp_dialog_message, 
                        MARKET_AND_FTP, MARKET_WEB_AND_FTP, WEB_AND_FTP);
            }
        }
    }
    
    private void createIntent(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE, mUri);
        startActivity(intent);
    }
    
    private void showMarketAlertDialog(int header, int message,
            final String googleMarketUri, final String googleMarketWebUri,
            final String appWebUri) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(header));
        alertDialog.setMessage(getResources().getString(message));
        alertDialog.setIcon(R.drawable.ic_log_warning);
        
        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                
                // special handling to ensure we only launch market intent for Google Play
                String market = getPackageManager().getInstallerPackageName(getPackageName());
                if (!"com.amazon.venezia".equals(market)) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(googleMarketUri));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(googleMarketWebUri));
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(appWebUri));
                    startActivity(intent);
                }
            }
        };
        
        String positiveLabel = getResources().getString(R.string.install_dialog_positive);
        alertDialog.setPositiveButton(positiveLabel, positiveOnClickListener);
        
        DialogInterface.OnClickListener negativeOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
        
        String negativeLabel = getResources().getString(R.string.install_dialog_negative);
        alertDialog.setNegativeButton(negativeLabel, negativeOnClickListener);
        
        alertDialog.show();
    }
    
    private void showTelemetryAlertDialog() {   
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        View dialogContent = getLayoutInflater().inflate(R.layout.dialog_telemetry, null);
        
        final CheckBox checkBox = (CheckBox) dialogContent.findViewById(R.id.checkbox);
        
        // make links clickable
        TextView textView = (TextView) dialogContent.findViewById(R.id.textbox);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        
        String positiveLabel = getResources().getString(R.string.button_ok);
        
        DialogInterface.OnClickListener positiveOnClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PreferenceConstants.SHOW_ISTAT_CHECKBOX, checkBox.isChecked());
                editor.commit();
                createIntent(TelemetryActivity.class);
                dialog.dismiss();
            }
        };
        
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton(positiveLabel, positiveOnClickListener)
                .setTitle(getResources().getString(R.string.dialog_title_istat))
                .setIcon(R.drawable.ic_log_warning).setView(dialogContent).create();
        
        alertDialog.show();
        
    }

    @Override
    public void onFailure(Response error) {
        setProgressBar(View.GONE);
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onSuccess(Service ftpService) {
        mFtpService = ftpService;
        setProgressBar(View.GONE);
        startFtpIntent();
    }
    
    private void setProgressBar(int state) {
        LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
        progressBar.setVisibility(state);
    }
}
