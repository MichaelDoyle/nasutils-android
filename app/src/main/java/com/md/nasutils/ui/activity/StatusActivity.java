/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.R;
import com.md.nasutils.model.Disk;
import com.md.nasutils.model.Enclosure;
import com.md.nasutils.model.Fan;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.NetworkInterface;
import com.md.nasutils.model.Response;
import com.md.nasutils.model.Temperature;
import com.md.nasutils.model.Ups;
import com.md.nasutils.model.Volume;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.DeviceFragment;
import com.md.nasutils.ui.fragment.DeviceFragment.OnDeviceFragmentResume;
import com.md.nasutils.ui.fragment.NetworkFragment;
import com.md.nasutils.ui.fragment.NetworkFragment.OnNetworkFragmentResume;
import com.md.nasutils.ui.fragment.PreferenceConstants;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasSmartDiskInfoReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasSmartDiskInfoReceiverFragment.OnRetrieveSmartDiskInfo;
import com.md.nasutils.ui.fragment.ReadyNasStatusReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasStatusReceiverFragment.OnRetrieveStatus;
import com.md.nasutils.ui.fragment.SmartDiskInfoDialogFragment;
import com.md.nasutils.ui.fragment.SmartDiskInfoDialogFragment.SmartInfoDialogOnCLickListener;
import com.md.nasutils.ui.fragment.StorageFragment;
import com.md.nasutils.ui.fragment.StorageFragment.OnStorageFragmentResume;
import com.md.nasutils.ui.widget.ArcMeterView;
import com.md.nasutils.ui.widget.PieChartView;
import com.md.nasutils.util.ScreenUtils;
import com.md.nasutils.util.TeamingMode;
import com.md.nasutils.util.UnitConversionUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Displays NAS device details and status
 * 
 * @author michaeldoyle
 */
public class StatusActivity extends NasUtilsFragmentActivity implements
        OnRetrieveStatus, OnRetrieveSmartDiskInfo, OnDeviceFragmentResume,
        OnNetworkFragmentResume, OnStorageFragmentResume,
        SmartInfoDialogOnCLickListener {

    private static final String TAG = StatusActivity.class.getSimpleName();

    private static final String READY_NAS_COMMAND_RECEIVER = "ReadyNasCommandReceiver";
    private static final String READY_NAS_STATUS_RECEIVER = "ReadyNasStatusReceiver";
    private static final String READY_NAS_SMART_RECEIVER = "ReadyNasSmartReceiver";

    private static final String SAVED_STATE_TAB = "tab";
    private static final String SAVED_STATE_CURRENT_SMART_DIALOG = "currentSmartDialog";
    private static final String SAVED_STATE_SMART_INFO = "smartInfo";
    private static final String SAVED_STATE_ERROR = "error";

    private static final String TAB_DEVICE = "Device";
    private static final String TAB_NETWORK = "Network";
    private static final String TAB_STORAGE = "Storage";

    private Nas mStatus;
    private Disk mSmartDiskInfo;
    private Response mError;
    private ActionBar mActionBar;
    private int mTempUnits;
    private String mCurrentSmartDialog;
    private NasConfiguration mNasConfiguration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // prefs
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        mTempUnits = Integer.parseInt(prefs.getString(
                PreferenceConstants.TEMPERATURE_UNIT, "0"));

        // initialize the ActionBar tabs
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        String device = getResources().getString(R.string.tab_label_device);
        mActionBar.addTab(createFragmentTab(DeviceFragment.class, device,
                TAB_DEVICE));
        String network = getResources().getString(R.string.tab_label_network);
        mActionBar.addTab(createFragmentTab(NetworkFragment.class, network,
                TAB_NETWORK));
        String storage = getResources().getString(R.string.tab_label_storage);
        mActionBar.addTab(createFragmentTab(StorageFragment.class, storage,
                TAB_STORAGE));

        initReceivers();

        if (savedInstanceState != null) {
            // activity is being resumed, so restore the tab the user was last
            // viewing
            mActionBar.setSelectedNavigationItem(savedInstanceState.getInt(
                    SAVED_STATE_TAB, 0));
            mCurrentSmartDialog = savedInstanceState
                    .getString(SAVED_STATE_CURRENT_SMART_DIALOG);
            mSmartDiskInfo = savedInstanceState
                    .getParcelable(SAVED_STATE_SMART_INFO);
            mError = savedInstanceState
                    .getParcelable(SAVED_STATE_ERROR);
        }

        mNasConfiguration = getNasConfiguration();
    }

    /**
     * Initialize fragments that responsible for receiving and processing
     * responses returned from ReadyNasIntentService
     */
    public void initReceivers() {
        FragmentManager fm = getSupportFragmentManager();

        ReadyNasReceiverFragment receiver = (ReadyNasStatusReceiverFragment) fm
                .findFragmentByTag(READY_NAS_STATUS_RECEIVER);

        if (receiver == null) {
            FragmentTransaction ft = fm.beginTransaction();
            receiver = new ReadyNasStatusReceiverFragment();
            ft.add(receiver, READY_NAS_STATUS_RECEIVER);
            ft.commit();
        }

        receiver = (ReadyNasCommandReceiverFragment) fm
                .findFragmentByTag(READY_NAS_COMMAND_RECEIVER);

        if (receiver == null) {
            FragmentTransaction ft = fm.beginTransaction();
            receiver = new ReadyNasCommandReceiverFragment();
            ft.add(receiver, READY_NAS_COMMAND_RECEIVER);
            ft.commit();
        }

        receiver = (ReadyNasSmartDiskInfoReceiverFragment) fm
                .findFragmentByTag(READY_NAS_SMART_RECEIVER);

        if (receiver == null) {
            FragmentTransaction ft = fm.beginTransaction();
            receiver = new ReadyNasSmartDiskInfoReceiverFragment();
            ft.add(receiver, READY_NAS_SMART_RECEIVER);
            ft.commit();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // if we get focus back (perhaps after orientation change)
            // redisplay the data
            onRetrieveStatus();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_STATE_TAB, getSupportActionBar()
                .getSelectedNavigationIndex());
        outState.putString(SAVED_STATE_CURRENT_SMART_DIALOG,
                mCurrentSmartDialog);
        outState.putParcelable(SAVED_STATE_SMART_INFO, mSmartDiskInfo);
        outState.putParcelable(SAVED_STATE_ERROR, mError);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            break;
        case R.id.menu_refresh:
            refreshStatus();
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void getSmartDiskInfo(String disk, String deviceName) {
        FragmentManager fm = getSupportFragmentManager();

        ReadyNasSmartDiskInfoReceiverFragment receiver = (ReadyNasSmartDiskInfoReceiverFragment) fm
                .findFragmentByTag(READY_NAS_SMART_RECEIVER);

        receiver.refreshDetails(disk, deviceName);
    }

    public void refreshStatus() {
        FragmentManager fm = getSupportFragmentManager();

        ReadyNasStatusReceiverFragment receiver = (ReadyNasStatusReceiverFragment) fm
                .findFragmentByTag(READY_NAS_STATUS_RECEIVER);

        if (receiver != null) {
            setProgressBar(View.VISIBLE);
            receiver.refreshDetails();
        }
    }

    private void setProgressBar(int state) {
        FragmentManager fm = getSupportFragmentManager();

        int tabs = getSupportActionBar().getTabCount();
        String tab = "";
        Fragment f = null;

        for (int i = 0; i < tabs; i++) {
            tab = (String) getSupportActionBar().getTabAt(i).getText();
            f = fm.findFragmentByTag(tab);
            if (isFragmentRefreshable(f)) {
                LinearLayout progressBar = (LinearLayout) f.getView()
                        .findViewById(R.id.progress_bar);
                if (progressBar != null) {
                    progressBar.setVisibility(state);
                }
            }
        }
    }

    private void setMessage(int state, String message) {
        FragmentManager fm = getSupportFragmentManager();

        int tabs = getSupportActionBar().getTabCount();
        String tab = "";
        Fragment f = null;

        for (int i = 0; i < tabs; i++) {
            tab = (String) getSupportActionBar().getTabAt(i).getText();
            f = fm.findFragmentByTag(tab);
            if (isFragmentRefreshable(f)) {
                if (message != null) {
                    TextView messageText = (TextView) f.getView().findViewById(
                            R.id.message);
                    messageText.setText(message);
                    messageText.setVisibility(state);
                }
            }
        }
    }

    @Override
    public void onRetrieveStatusSuccess(Nas status) {
        mStatus = status;
        onRetrieveStatus();
    }

    @Override
    public void onRetrieveSmartDiskInfoSuccess(Disk disk) {
        mSmartDiskInfo = disk;
        onRetrieveSmartDiskInfo();
    }

    @Override
    public void onRetrieveStatusFailure(Response error) {
        mError = error;
        setProgressBar(View.GONE);
        setMessage(View.VISIBLE, error.getMessage());
    }

    @Override
    public void onRetrieveSmartDiskInfoFailure(Response error) {
        mError = error;
        onRetrieveSmartDiskInfo();
    }

    public void onRetrieveStatus() {
        onDeviceFragmentResume();
        onNetworkFragmentResume();
        onStorageFragmentResume();
    }

    public void onRetrieveSmartDiskInfo() {
        if (mCurrentSmartDialog != null) {
            SmartDiskInfoDialogFragment fragmentDialog = (SmartDiskInfoDialogFragment) getSupportFragmentManager()
                    .findFragmentByTag(mCurrentSmartDialog);

            if (fragmentDialog != null) {
                LinearLayout layout = (LinearLayout) fragmentDialog.getDialog()
                        .findViewById(R.id.fragment_smart);
                layout.removeAllViews();
    
                LinearLayout progressBar = (LinearLayout) fragmentDialog
                        .getDialog().findViewById(R.id.progress_bar);
                
                if (mSmartDiskInfo != null) {
                    if(mSmartDiskInfo.getSmartAttributes() != null) {
                        for (Entry<String, String> attr : mSmartDiskInfo
                                .getSmartAttributes().entrySet()) {
                            layout.addView(createTextView(attr.getKey() + ": "
                                    + attr.getValue()));
                        }
                    } else {
                        layout.addView(createTextView(getResources().getString(
                                R.string.error_unavailable)));
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (mError != null) {
                    layout.addView(createTextView(mError.getMessage()));
                    progressBar.setVisibility(View.GONE);
                }           
            }
        }
    }

    /**
     * Populate the Device tab with the appropriate data
     */
    @Override
    public void onDeviceFragmentResume() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(TAB_DEVICE);

        if (isFragmentRefreshable(f)) {
            LinearLayout view = (LinearLayout) f.getView().findViewById(
                    R.id.fragment_content);
            if (mStatus != null) {
                view.removeAllViews();
                setHardwareFirmwareDetails(view);
                setSystemHealthDetails(view);
                setProgressBar(View.GONE);
                setMessage(View.GONE, null);
            } else if (mError != null) {
                setProgressBar(View.GONE);
                setMessage(View.VISIBLE, mError.getMessage());
            }
        }
    }

    /**
     * Populate the Network tab with the appropriate data
     */
    @Override
    public void onNetworkFragmentResume() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(TAB_NETWORK);

        if (isFragmentRefreshable(f)) {
            LinearLayout view = (LinearLayout) f.getView().findViewById(
                    R.id.fragment_content);
            if (mStatus != null) {
                view.removeAllViews();
                setNetworkInterfaceDetails(view);
                setProgressBar(View.GONE);
                setMessage(View.GONE, null);
            } else if (mError != null) {
                setProgressBar(View.GONE);
                setMessage(View.VISIBLE, mError.getMessage());
            }
        }
    }

    /**
     * Populate the Storage tab with the appropriate data
     */
    @Override
    public void onStorageFragmentResume() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(TAB_STORAGE);

        if (isFragmentRefreshable(f)) {
            LinearLayout view = (LinearLayout) f.getView().findViewById(
                    R.id.fragment_content);
            if (mStatus != null) {
                view.removeAllViews();
                setVolumeDetails(view);
                setDriveDetails(view);
                setProgressBar(View.GONE);
                setMessage(View.GONE, null);
            } else if (mError != null) {
                setProgressBar(View.GONE);
                setMessage(View.VISIBLE, mError.getMessage());
            }
        }
    }

    /**
     * Determine if we can safely update a UI fragment
     * 
     * @param fragment
     *            the fragment to be tested
     * @return true if the fragment is visible and we have a copy of the NAS
     *         Status details. false otherwise.
     */
    private boolean isFragmentRefreshable(Fragment fragment) {
        return fragment != null && fragment.getView() != null;
    }

    /**
     * Convenience method for creating an ActionBar Tab
     * 
     * @param clazz
     *            UI fragment to display when the tab is selected
     * @param label
     *            display label for the tab
     * @param tag
     *            tag for which to retrieve/store the fragment
     * @return an ActionBar Tab that will display its corresponding fragment
     *         when the tab is selected
     */
    private <T extends Fragment> ActionBar.Tab createFragmentTab(Class<T> clazz,
                                                                 String label, String tag) {
        ActionBar.Tab tab = mActionBar.newTab();
        tab.setText(label);
        tab.setTabListener(new StatusTabListener<T>(this, tag, clazz));
        return tab;
    }
    
    private ArcMeterView createArcMeterView(Context context,
            String label, String value, float percentFull) {

        ArcMeterView meter = new ArcMeterView(this, percentFull, label, value);
        meter.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        return meter;
    }
    
    private void setHardwareFirmwareDetails(ViewGroup view) {
        RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_nas);
        setMaxWidth(rl);
        view.addView(rl);
        
        refreshTextView(rl, R.id.label,
                getResources().getString(R.string.device_details));
        refreshTextView(rl, R.id.device_table_hostname_value,
                mStatus.getHostname());
        refreshTextView(rl, R.id.device_table_serial_value,
                mStatus.getSerialNumber());
        refreshTextView(rl, R.id.device_table_model_value, mStatus.getModel());
        refreshTextView(rl, R.id.device_table_arch_value,
                mStatus.getArchitecture());
        refreshTextView(rl, R.id.device_table_firmware_value,
                mStatus.getFirmware());
        refreshTextView(rl, R.id.device_table_memory_value, mStatus.getMemory());
    }

    private void setSystemHealthDetails(ViewGroup view) {

        for (Enclosure enc : mStatus.getEnclosures()) {
            // TEMP - Name, Temp, Status
            for (Temperature comp : enc.getTemperatures()) {
                view.addView(createSpacer());
                
                RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_health_temp);
                setMaxWidth(rl);
                LinearLayout meterLayout = (LinearLayout) rl.findViewById(R.id.meter);
                
                float pct = (float) comp.getTemperature()
                        / comp.getTemperatureMax();
    
                Integer temp = (mTempUnits == 0 ? comp.getTemperature()
                        : UnitConversionUtils.celciusToFahrenheit(comp
                                .getTemperature()));
                
                String label = "\u00B0" + (mTempUnits == 0 ? "C" : "F");
    
                ArcMeterView meter = createArcMeterView(this, label, temp.toString(), pct);
                meterLayout.addView(meter);
                
                refreshTextView(rl, R.id.label, comp.getLabel());
                refreshTextViewStatus(rl, R.id.comp_status_value, comp.getStatus());
    
                view.addView(rl);
            }
    
            // FAN - Name, RPM, Status
            for (Fan comp : enc.getFans()) {
                view.addView(createSpacer());
                
                RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_health_fan);
                setMaxWidth(rl);
                LinearLayout meterLayout = (LinearLayout) rl.findViewById(R.id.meter);
                
                int max = comp.getSpeedMax() > 0 ? comp.getSpeedMax() : 1;
                float pct = (float) comp.getSpeed() / max;
                ArcMeterView meter = createArcMeterView(this, "RPM", Integer.toString(comp.getSpeed()), pct);
                meterLayout.addView(meter);
                
                refreshTextView(rl, R.id.label, comp.getLabel());
                refreshTextViewStatus(rl, R.id.comp_status_value, comp.getStatus());
    
                view.addView(rl);
            }
        }
    
        // UPS - Name, Desc, Status
        for (Ups comp : mStatus.getUps()) {
            view.addView(createSpacer());
            RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_health_ups);
            setMaxWidth(rl);
            refreshTextView(rl, R.id.label, comp.getLabel());
            refreshTextView(rl, R.id.comp_desc_value, comp.getModel());
            refreshTextView(rl, R.id.ups_table_battery_charge_value, comp.getBatteryCharge());
            refreshTextView(rl, R.id.ups_table_battery_runtime_value, comp.getBatteryRuntime());
            refreshTextViewStatus(rl, R.id.comp_status_value, comp.getStatus());
            view.addView(rl);
        }

        for (Enclosure enc : mStatus.getEnclosures()) {
            // DISK - Name, Desc, Temp, Status
            for (Disk comp : enc.getDisks()) {
                view.addView(createSpacer());
                RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_health_disk);
                setMaxWidth(rl);
                LinearLayout meterLayout = (LinearLayout) rl.findViewById(R.id.meter);
                
                float pct = (float) comp.getTemperature()
                        / comp.getTemperatureMax();
    
                Integer temp = (mTempUnits == 0 ? comp.getTemperature()
                        : UnitConversionUtils.celciusToFahrenheit(comp
                                .getTemperature()));
    
                String label = "\u00B0" + (mTempUnits == 0 ? "C" : "F");
    
                ArcMeterView meter = createArcMeterView(this, label, temp.toString(), pct);
                meterLayout.addView(meter);
                
                refreshTextView(rl, R.id.label, comp.getLabel());
                refreshTextView(rl, R.id.comp_desc_value, comp.getModel());
                refreshTextViewStatus(rl, R.id.comp_status_value, comp.getStatus());
                
                view.addView(rl);
            }
        }
    }

    private void setNetworkInterfaceDetails(LinearLayout view) {

        boolean first = true;

        for (NetworkInterface iface : mStatus.getNetworkInterfaces()) {
            if (!first) {
                view.addView(createSpacer());
            } else {
                first = false;
            }

            RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_network_interface);
            setMaxWidth(rl);
            view.addView(rl);
            refreshTextView(rl, R.id.label, iface.getInterfaceName());
            refreshTextViewStatus(rl, R.id.network_status_value,
                    iface.getStatus());
            refreshTextView(rl, R.id.network_table_type_value,
                    iface.getInterfaceType());
            refreshTextView(rl, R.id.network_table_mac_value,
                    iface.getMacAddress());
            refreshTextView(rl, R.id.network_table_ip_value,
                    iface.getIpAddress());
            refreshTextView(rl, R.id.network_table_ip_type_value,
                    iface.getIpAddressType());
            refreshTextView(rl, R.id.network_table_ipv6_value,
                    iface.getIpv6Address());
            
            if (iface.getOver() != null) {
                TableRow trOver = (TableRow) rl.findViewById(R.id.network_table_row_over);
                trOver.setVisibility(View.VISIBLE);
                refreshTextView(rl, R.id.network_table_over_value, iface.getOver());
            }
            
            if (iface.getMode() != null) {
                TableRow trMode = (TableRow) rl.findViewById(R.id.network_table_row_mode);
                trMode.setVisibility(View.VISIBLE);
                String teamingMode = TeamingMode.fromName(iface.getMode()).getDisplayName();
                refreshTextView(rl, R.id.network_table_mode_value, teamingMode);
            }
        }
    }

    private void setVolumeDetails(LinearLayout view) {
        boolean first = true;

        for (Volume vol : mStatus.getVolumes()) {
            if (!first) {
                view.addView(createSpacer());
            } else {
                first = false;
            }
            
            String volName = vol.getName() != null ? vol.getName().toUpperCase(Locale.US) : "";
            StringBuffer headerLabel = new StringBuffer();
            headerLabel.append(getResources().getString(R.string.volume))
                    .append(" ").append(volName);

            RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_volume);
            setMaxWidth(rl);
            refreshTextView(rl, R.id.label, headerLabel.toString());
            refreshTextViewStatus(rl, R.id.volume_status_value, vol.getStatus());
            view.addView(rl);

            LinearLayout usageValueLayout = (LinearLayout) rl
                    .findViewById(R.id.volume_table_usage_value);

            PieChartView chart = new PieChartView(this,
                    vol.getPercentageUsed() / 100.0f);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            chart.setLayoutParams(lp);
            chart.setMinimumHeight(ScreenUtils.dipToPx(this, 75));
            usageValueLayout.addView(chart);
            
            TextView usageText = createTextView(vol.getUsageText());
            usageValueLayout.addView(usageText);
            
            refreshTextView(rl, R.id.volume_table_raid_level_value,
                    vol.getRaidLevel());
            refreshTextView(rl, R.id.volume_table_raid_status_value,
                    vol.getHealth());
        }
    }

    private void setDriveDetails(LinearLayout view) {

        // TODO: should disks be organized within volume by vdev?

        for (Enclosure enc : mStatus.getEnclosures()) {

            List<Disk> drives = enc.getDisks();
    
            boolean zeroIndexed = false;
            if (drives.size() > 0) {
                zeroIndexed = drives.get(0).getChannel() == 0 ? true : false;
            }
    
            for (Disk drive : drives) {
                view.addView(createSpacer());
    
                int channel = zeroIndexed ? drive.getChannel() + 1 : drive
                        .getChannel();

                RelativeLayout rl = inflateRelativeLayout(R.layout.list_item_disk);
                setMaxWidth(rl);
                view.addView(rl);
                refreshTextView(rl, R.id.label, drive.getLabel());
                refreshTextViewStatus(rl, R.id.disk_status_value, drive.getStatus());
                refreshTextView(rl, R.id.disk_table_channel_value,
                        Integer.toString(channel));
                refreshTextView(rl, R.id.disk_table_device_name_value, drive.getName());
                refreshTextView(rl, R.id.disk_table_model_value, drive.getModel());
                refreshTextView(rl, R.id.disk_table_serial_value,
                        drive.getSerialNumber());
                refreshTextView(rl, R.id.disk_table_firmware_value,
                        drive.getFirmwareVersion());
                refreshTextView(rl, R.id.disk_table_capacity_value,
                        Double.toString(drive.getCapacityGb()) + " GB");
    
                View buttonLocate = rl.findViewById(R.id.button_locate);
                buttonLocate.setTag(Integer.toString(drive.getChannel()));
    
                View buttonSmart = rl.findViewById(R.id.button_smart);
                buttonSmart.setTag(R.id.disk_index,
                        Integer.toString(drive.getChannel()));
                buttonSmart.setTag(R.id.serial_number, drive.getSerialNumber());
                buttonSmart.setTag(R.id.device_name, drive.getName());
            }
        }
    }

    public void onClickRecalibrateFan(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("recalibrate_fan", params);

        ReadyNasCommandReceiverFragment receiver = (ReadyNasCommandReceiverFragment) getSupportFragmentManager()
                .findFragmentByTag(READY_NAS_COMMAND_RECEIVER);

        receiver.recalibrateFan();
    }

    public void onClickLocate(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("disk_locate", params);

        Button button = (Button) view;
        String disk = (String) button.getTag();

        ReadyNasCommandReceiverFragment receiver = (ReadyNasCommandReceiverFragment) getSupportFragmentManager()
                .findFragmentByTag(READY_NAS_COMMAND_RECEIVER);

        receiver.sendLocateDisk(disk);
    }

    public void onClickSmart(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("disk_smart", params);

        Button button = (Button) view;
        String disk = (String) button.getTag(R.id.disk_index);
        String deviceName = (String) button.getTag(R.id.device_name);
        mCurrentSmartDialog = (String) button.getTag(R.id.serial_number);

        Log.i(TAG, "Get SMART+ for disk " + disk);

        String smartInfo = getResources().getString(R.string.dialog_title_smart_info);
        
        SmartDiskInfoDialogFragment dialog = SmartDiskInfoDialogFragment
                .newInstance(deviceName + " " + smartInfo);

        dialog.show(getSupportFragmentManager(), mCurrentSmartDialog);

        getSmartDiskInfo(disk, deviceName);
    }

    @Override
    public void onSmartDialogClicked() {
        mCurrentSmartDialog = null;
        mSmartDiskInfo = null;
        mError = null;
    }
}
