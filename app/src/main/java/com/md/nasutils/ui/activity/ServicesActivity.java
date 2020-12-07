/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.md.nasutils.R;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.model.Service;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasCommandReceiverFragment.OnCommandExecuted;
import com.md.nasutils.ui.fragment.ReadyNasServicesReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasServicesReceiverFragment.OnRetrieveServices;
import com.md.nasutils.ui.fragment.ServicesFragment;
import com.md.nasutils.ui.fragment.ServicesFragment.OnServicesFragmentResume;
import com.md.nasutils.util.ServiceEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_AFP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ANTIVIRUS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_AFP_ADVERTISE_BONJOUR;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_BONJOUR_ADVERTISE_AFP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_BONJOUR_ADVERTISE_AFP_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DAAP_DIRECTORY;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DAAP_DIRECTORY_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DAAP_PASSWORD;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DAAP_PASSWORD_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DAAP_SHARE_NAME;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DAAP_SHARE_NAME_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_FORCE_FTPS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_FORCE_FTPS_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_FTPS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_FTPS_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MASQUERADE_ADDRESS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MASQUERADE_ADDRESS_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MAX_DOWNLOAD_RATE;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MAX_DOWNLOAD_RATE_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MAX_UPLOAD_RATE;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MAX_UPLOAD_RATE_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MODE;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_MODE_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_PASSIVE_END;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_PASSIVE_END_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_PASSIVE_START;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_PASSIVE_START_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_PORT;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_PORT_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_SERVER_TRANSFER_LOG;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_SERVER_TRANSFER_LOG_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_UPLOAD_RESUME;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_FTP_UPLOAD_RESUME_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_PORT;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_PORT_1;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_PORT_1_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_PORT_2;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_PORT_2_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_PORT_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_SSL_KEY_HOST;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTPS_SSL_KEY_HOST_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTP_SHARE_NAMES;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTP_WEBSERVER_SHARE;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTP_WEBSERVER_SHARE_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_NFS_NFSV4_DOMAIN;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_NFS_NFSV4_DOMAIN_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_NFS_NFSV4_ENABLED;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_NFS_THREADS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_NFS_THREADS_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_ALLOW_HOST;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_ALLOW_HOST_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_COMMUNITY;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_COMMUNITY_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_CONTACT;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_CONTACT_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_LOCATION;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_LOCATION_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_TRAP_DESTINATION;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_TRAP_DESTINATION_ENABLED_ON_UI;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_BONJOUR;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_CIFS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_DAAP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_DLNA;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_FTP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_HTTP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_HTTPS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_NFS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_RSYNC;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_SMART_NETWORK;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_SNMP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_SSH;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_UPNP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SUFFIX_ENABLED_ON_UI;

/**
 * Activity for enabling/disabling ReadyNAS services
 * 
 * @author michaeldoyle
 *
 */
public class ServicesActivity extends NasUtilsFragmentActivity implements
        OnRetrieveServices, OnServicesFragmentResume, OnCommandExecuted {

    @SuppressWarnings("unused")
    private static final String TAG = ServicesActivity.class.getSimpleName();

    private static final String READY_NAS_COMMAND_RECEIVER = "ReadyNasCommandReceiver";
    private static final String READY_NAS_SERVICES_RECEIVER = "ReadyNasServicesReceiver";
    
    private Nas mNas;
    private Response mError;
    private ServicesFragment mServicesFragment;
    private NasConfiguration mNasConfiguration;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        mServicesFragment = new ServicesFragment();
        ft.replace(R.id.fragment_content, mServicesFragment);
        mServicesFragment.setRetainInstance(true);
        
        ReadyNasServicesReceiverFragment receiver = 
                (ReadyNasServicesReceiverFragment) fm.findFragmentByTag(READY_NAS_SERVICES_RECEIVER);
        if (receiver == null) {
            receiver = new ReadyNasServicesReceiverFragment();
            ft.add(receiver, READY_NAS_SERVICES_RECEIVER);
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
        getMenuInflater().inflate(R.menu.activity_services, menu);
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
        case R.id.menu_save:
            saveServices();
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
    
    public void refreshServices() {
        FragmentManager fm = getSupportFragmentManager();
        
        ReadyNasServicesReceiverFragment receiver = 
                (ReadyNasServicesReceiverFragment) fm.findFragmentByTag(READY_NAS_SERVICES_RECEIVER);
        
        if (receiver != null) {
            LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            TextView message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.GONE);
            LinearLayout view = (LinearLayout) mServicesFragment.getView().findViewById(R.id.fragment_content);
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
        onServicesFragmentResume();
        setProgressBar(View.GONE);
    }
    
    @Override
    public void onServicesFragmentResume() {
        if (mNas != null) {
            setServices();
            setProgressBar(View.GONE);
            setMessage(View.GONE, null);
        } else if (mError != null) {
            setProgressBar(View.GONE);
            setMessage(View.VISIBLE, mError.getMessage());
        } else {
            FragmentManager fm = getSupportFragmentManager();
            ReadyNasServicesReceiverFragment receiver = 
                    (ReadyNasServicesReceiverFragment) fm.findFragmentByTag(READY_NAS_SERVICES_RECEIVER);
            
            if (receiver != null) {
                receiver.setDetails();
            }
        }
    }
    
    private void setServices() {
        if (mServicesFragment.isAdded() && mServicesFragment.getView() != null) {
            LinearLayout view = (LinearLayout) mServicesFragment.getView().findViewById(R.id.fragment_content);
            view.removeAllViews();
            
            for (Service s : mNas.getServices().values()) {
                try {
                    RelativeLayout listItemService = null;
                    CompoundButton toggle = null;

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        listItemService = inflateRelativeLayout(R.layout.list_item_service);
                        toggle = (CompoundButton) listItemService.findViewById(R.id.toggle);
                    } else {
                        listItemService = inflateRelativeLayout(R.layout.list_item_service_legacy);
                        toggle = (CompoundButton) listItemService.findViewById(R.id.toggle);
                    }

                    listItemService.setId(ServiceEnum.fromName(s.getName()).getId());
                    toggle.setEnabled(s.isEnabledOnUi());
                    toggle.setChecked(s.isEnabled());
                    toggle.setTag(R.id.service, s.getName());

                    toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            onToggleClicked(buttonView);
                        }
                    });

                    String label = ServiceEnum.fromName(s.getName()).getDisplayName();
                    refreshTextView(listItemService, R.id.label, label);

                    LinearLayout ll = (LinearLayout) listItemService.findViewById(R.id.properties);
                    setServiceOptions(s, ll);

                    setMaxWidth(listItemService);
                    view.addView(listItemService);
                    view.addView(createSpacer());
                } catch (IllegalArgumentException e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }
        }
    }
    
    private void setServiceOptions(Service s, LinearLayout ll) {
        if (SERVICE_NFS.equals(s.getName())) {
            setServiceOptionsNfs(s, ll);
        } else if (SERVICE_FTP.equals(s.getName())) {
            setServiceOptionsFtp(s, ll);
        } else if (SERVICE_HTTP.equals(s.getName())) {
            setServiceOptionsHttp(s, ll);
        } else if (SERVICE_HTTPS.equals(s.getName())) {
            setServiceOptionsHttps(s, ll);
        } else if (SERVICE_DLNA.equals(s.getName())) {
            setServiceOptionsDlna(s, ll);
        } else if (SERVICE_BONJOUR.equals(s.getName())) {
            setServiceOptionsBonjour(s, ll);
        } else if (SERVICE_DAAP.equals(s.getName())) {
            setServiceOptionsDaap(s, ll);
        } else if (SERVICE_SNMP.equals(s.getName())) {
            setServiceOptionsSnmp(s, ll);
        }
    }
    
    private void setServiceOptionsNfs(Service s, LinearLayout ll) {
        RelativeLayout form = inflateRelativeLayout(R.layout.form_nfs);
        
        Spinner spinner = (Spinner) form.findViewById(R.id.spinner_nfs_threads);
        Boolean nfsThreadsEnabledOnUi = (Boolean) s.getMetaDataByName(SERVICE_ATTR_NFS_THREADS_ENABLED_ON_UI);
        if (nfsThreadsEnabledOnUi != null) {
            TextView nfsThreads = (TextView) form.findViewById(R.id.textview_nfs_threads);
            nfsThreads.setVisibility(View.VISIBLE);
            
            spinner.setVisibility(View.VISIBLE);

            List<String> options = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_NFS_THREADS_ENABLED_ON_UI));
            spinner.setOnItemSelectedListener(null);
            
            String threads = s.getOptionByName(SERVICE_ATTR_NFS_THREADS);
            if (mNasConfiguration.getOsVersion() >= 6) {
                List<String> opts = Arrays.asList(new String[] {"8", "12", "16", "24", "32"});
                options.addAll(opts);
                for (int i = 0; i < opts.size(); i ++) {
                    if (opts.get(i).equals(threads)) {
                        adapter.notifyDataSetChanged();
                        spinner.setSelection(i);                            
                    }
                }
            } else {                
                for(int i = 1; i <17; i++) {
                    options.add(i + "");
                }
                adapter.notifyDataSetChanged();
                
                int threadsInt;
                try {
                    threadsInt = Integer.parseInt(threads);
                } catch (NumberFormatException e) {
                    threadsInt = 1;
                }
                spinner.setSelection(threadsInt-1);
            }
            spinner.setOnItemSelectedListener(new SpinnerListener(SERVICE_NFS, SERVICE_ATTR_NFS_THREADS));
        }
                
        Boolean nfsv4CheckBoxEnabledOnUi = (Boolean) s.getMetaDataByName(SERVICE_ATTR_NFS_NFSV4_DOMAIN_ENABLED_ON_UI);
        String nfsv4Enabled = s.getOptionByName(SERVICE_ATTR_NFS_NFSV4_ENABLED);
        if (nfsv4CheckBoxEnabledOnUi != null) {
            CheckBox checkBoxEnableNfsv4 = (CheckBox) form.findViewById(R.id.checkbox_enable_nfsv4);
            checkBoxEnableNfsv4.setVisibility(View.VISIBLE);
            checkBoxEnableNfsv4.setEnabled(s.isEnabled() && nfsv4CheckBoxEnabledOnUi);
            checkBoxEnableNfsv4.setChecked("1".equals(nfsv4Enabled) ? true : false);
        }
        
        Boolean nfsv4DomainEnabledOnUi = (Boolean) s.getMetaDataByName(SERVICE_ATTR_NFS_NFSV4_DOMAIN_ENABLED_ON_UI);
        if (nfsv4DomainEnabledOnUi != null) {
            TextView textViewNfsv4Domain = (TextView) form.findViewById(R.id.textview_nfsv4_domain);
            textViewNfsv4Domain.setVisibility(View.VISIBLE);
                        
            EditText editTextNfsv4Domain = (EditText) form.findViewById(R.id.edittext_nfsv4_domain);
            editTextNfsv4Domain.setText(s.getOptionByName(SERVICE_ATTR_NFS_NFSV4_DOMAIN));
            editTextNfsv4Domain.setEnabled(s.isEnabled() && nfsv4DomainEnabledOnUi);
            editTextNfsv4Domain.setVisibility(View.VISIBLE);
            editTextNfsv4Domain.addTextChangedListener(new EditTextWatcher(SERVICE_NFS, SERVICE_ATTR_NFS_NFSV4_DOMAIN));
            editTextNfsv4Domain.setEnabled("1".equals(nfsv4Enabled) ? true : false);
            
            if (nfsThreadsEnabledOnUi != null) {
                // align of the UI
                RelativeLayout.LayoutParams layoutParams = 
                        (RelativeLayout.LayoutParams) spinner.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.edittext_nfsv4_domain);
                spinner.setLayoutParams(layoutParams);
            }
        }
        
        ll.addView(form);
    }
    
    private void setServiceOptionsFtp(Service s, LinearLayout ll) {
        RelativeLayout form = inflateRelativeLayout(R.layout.form_ftp);
        
        EditText editTextPort = (EditText) form.findViewById(R.id.edittext_port);
        editTextPort.setText(s.getOptionByName(SERVICE_ATTR_FTP_PORT));
        editTextPort.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_PORT_ENABLED_ON_UI));
        editTextPort.addTextChangedListener(new EditTextWatcher(SERVICE_FTP, SERVICE_ATTR_FTP_PORT));
        
        Spinner spinnerAuthMode = (Spinner) form.findViewById(R.id.spinner_auth_mode);
        List<String> options = Arrays.asList(new String[] {"anonymous", "user"});
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAuthMode.setAdapter(adapter);
        spinnerAuthMode.setOnItemSelectedListener(null);
        
        String authMode = s.getOptionByName(SERVICE_ATTR_FTP_MODE);

        for (int i = 0; i < options.size(); i++) {
            if (authMode != null && authMode.equals(options.get(i))) {
                spinnerAuthMode.setSelection(i);    
            }
        }
        
        spinnerAuthMode.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_MODE_ENABLED_ON_UI));
        spinnerAuthMode.setOnItemSelectedListener(new SpinnerListener(SERVICE_FTP, SERVICE_ATTR_FTP_MODE));
        
        EditText editTextPassivePortStart = (EditText) form.findViewById(R.id.edittext_passive_port_start);
        editTextPassivePortStart.setText(s.getOptionByName(SERVICE_ATTR_FTP_PASSIVE_START));
        editTextPassivePortStart.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_PASSIVE_START_ENABLED_ON_UI));
        editTextPassivePortStart.addTextChangedListener(new EditTextWatcher(SERVICE_FTP, SERVICE_ATTR_FTP_PASSIVE_START));
        
        EditText editTextPassivePortEnd = (EditText) form.findViewById(R.id.edittext_passive_port_end);
        editTextPassivePortEnd.setText(s.getOptionByName(SERVICE_ATTR_FTP_PASSIVE_END));
        editTextPassivePortEnd.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_PASSIVE_END_ENABLED_ON_UI));
        editTextPassivePortEnd.addTextChangedListener(new EditTextWatcher(SERVICE_FTP, SERVICE_ATTR_FTP_PASSIVE_END));
        
        EditText editTextMasqueradeAddress = (EditText)form.findViewById(R.id.edittext_masquerade_address);
        editTextMasqueradeAddress.setText(s.getOptionByName(SERVICE_ATTR_FTP_MASQUERADE_ADDRESS));
        editTextMasqueradeAddress.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_MASQUERADE_ADDRESS_ENABLED_ON_UI));
        editTextMasqueradeAddress.addTextChangedListener(new EditTextWatcher(SERVICE_FTP, SERVICE_ATTR_FTP_MASQUERADE_ADDRESS));
        
        CheckBox checkBoxAllowUpResume = (CheckBox) form.findViewById(R.id.checkbox_allow_upload_resume);
        String allowUpResume = s.getOptionByName(SERVICE_ATTR_FTP_UPLOAD_RESUME);
        checkBoxAllowUpResume.setChecked("1".equals(allowUpResume) ? true : false);
        checkBoxAllowUpResume.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_UPLOAD_RESUME_ENABLED_ON_UI));
                    
        String maxUpRate = s.getOptionByName(SERVICE_ATTR_FTP_MAX_UPLOAD_RATE);
        if (maxUpRate != null) {
            TextView textViewMaxUploadRate = (TextView) form.findViewById(R.id.textview_max_upload_rate);
            textViewMaxUploadRate.setVisibility(View.VISIBLE);
            EditText editTextMaxUploadRate = (EditText) form.findViewById(R.id.edittext_max_upload_rate);
            TextView textViewMaxUploadRateKbs = (TextView) form.findViewById(R.id.textview_max_upload_rate_kbs);
            textViewMaxUploadRateKbs.setVisibility(View.VISIBLE);
            editTextMaxUploadRate.setText(maxUpRate);
            editTextMaxUploadRate.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_MAX_UPLOAD_RATE_ENABLED_ON_UI));
            editTextMaxUploadRate.setVisibility(View.VISIBLE);
            editTextMaxUploadRate.addTextChangedListener(new EditTextWatcher(SERVICE_FTP, SERVICE_ATTR_FTP_MAX_UPLOAD_RATE));               
        }
        
        String maxDownRate = s.getOptionByName(SERVICE_ATTR_FTP_MAX_DOWNLOAD_RATE);
        if (maxUpRate != null) {
            TextView textViewMaxDownloadRate = (TextView) form.findViewById(R.id.textview_max_download_rate);
            textViewMaxDownloadRate.setVisibility(View.VISIBLE);
            TextView textViewMaxDownloadRateKbs = (TextView) form.findViewById(R.id.textview_max_download_rate_kbs);
            textViewMaxDownloadRateKbs.setVisibility(View.VISIBLE);
            EditText editTextMaxDownloadRate = (EditText) form.findViewById(R.id.edittext_max_download_rate);
            editTextMaxDownloadRate.setText(maxDownRate);
            editTextMaxDownloadRate.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_MAX_DOWNLOAD_RATE_ENABLED_ON_UI));
            editTextMaxDownloadRate.setVisibility(View.VISIBLE);
            editTextMaxDownloadRate.addTextChangedListener(new EditTextWatcher(SERVICE_FTP, SERVICE_ATTR_FTP_MAX_DOWNLOAD_RATE));
        }
        
        String ftps = s.getOptionByName(SERVICE_ATTR_FTP_FTPS);
        if (ftps != null) {
            CheckBox checkBoxFtps = (CheckBox) form.findViewById(R.id.checkbox_ftps);
            checkBoxFtps.setChecked("1".equals(ftps) ? true : false);
            checkBoxFtps.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_FTPS_ENABLED_ON_UI));
            checkBoxFtps.setVisibility(View.VISIBLE);
        }
        
        String forceFtps = s.getOptionByName(SERVICE_ATTR_FTP_FORCE_FTPS);
        if (forceFtps != null) {
            CheckBox checkBoxForceFtps = (CheckBox) form.findViewById(R.id.checkbox_force_ftps);
            checkBoxForceFtps.setChecked("1".equals(forceFtps) ? true : false);
            checkBoxForceFtps.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_FORCE_FTPS_ENABLED_ON_UI));
            checkBoxForceFtps.setVisibility(View.VISIBLE);
        }
        
        String logTrans = s.getOptionByName(SERVICE_ATTR_FTP_SERVER_TRANSFER_LOG);
        if (logTrans != null) {
            CheckBox checkBoxLogTrans = (CheckBox) form.findViewById(R.id.checkbox_server_transfer_log);
            checkBoxLogTrans.setChecked("1".equals(logTrans) ? true : false);
            checkBoxLogTrans.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_FTP_SERVER_TRANSFER_LOG_ENABLED_ON_UI));
            checkBoxLogTrans.setVisibility(View.VISIBLE);
        }
        
        ll.addView(form);
    }
    
    private void setServiceOptionsHttp(Service s, LinearLayout ll) {
        RelativeLayout form = inflateRelativeLayout(R.layout.form_http);
        
        Spinner spinnerWebAccess = (Spinner) form.findViewById(R.id.spinner_redirect_web_access);
        List<String> options = new ArrayList<>();
        String selection = s.getOptionByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE);
        
        if (mNasConfiguration.getOsVersion() >= 5) {
            options.add("None Selected");
        }
        @SuppressWarnings("unchecked")
        List<String> opts = (List<String>) s.getMetaDataByName(SERVICE_ATTR_HTTP_SHARE_NAMES);
        options.addAll(opts);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWebAccess.setAdapter(adapter);
        spinnerWebAccess.setOnItemSelectedListener(null);
        
        for (int i = 0; i < options.size(); i++) {
            if (selection != null && selection.equals(options.get(i))) {
                spinnerWebAccess.setSelection(i);   
            }
        }
        
        spinnerWebAccess.setOnItemSelectedListener(new SpinnerListener(SERVICE_HTTP, SERVICE_ATTR_HTTP_WEBSERVER_SHARE));
        spinnerWebAccess.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE_ENABLED_ON_UI));
        
        CheckBox checkBoxLoginAuth = (CheckBox) form.findViewById(R.id.checkbox_login_authentication);
        String loginAuth = s.getOptionByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD);
        checkBoxLoginAuth.setChecked("1".equals(loginAuth) ? true : false);
        checkBoxLoginAuth.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD_ENABLED_ON_UI));
        
        ll.addView(form);
    }
    
    private void setServiceOptionsHttps(Service s, LinearLayout ll) {
        RelativeLayout form = inflateRelativeLayout(R.layout.form_https);
        
        EditText editTextPort1 = (EditText) form.findViewById(R.id.edittext_port_1);
        editTextPort1.addTextChangedListener(new EditTextWatcher(SERVICE_HTTPS, SERVICE_ATTR_HTTPS_PORT_1));
        
        EditText editTextPort2 = (EditText) form.findViewById(R.id.edittext_port_2);
                            
        if (mNasConfiguration.getOsVersion() >= 5) {
            editTextPort1.setText("443");
            editTextPort1.setEnabled(false);
            
            editTextPort2.setText(s.getOptionByName(SERVICE_ATTR_HTTPS_PORT));
            editTextPort2.addTextChangedListener(new EditTextWatcher(SERVICE_HTTPS, SERVICE_ATTR_HTTPS_PORT));
            editTextPort2.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_HTTPS_PORT_ENABLED_ON_UI));
        } else {
            editTextPort1.setText(s.getOptionByName(SERVICE_ATTR_HTTPS_PORT_1));
            editTextPort1.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_HTTPS_PORT_1_ENABLED_ON_UI));
            
            editTextPort2.setText(s.getOptionByName(SERVICE_ATTR_HTTPS_PORT_2));
            editTextPort2.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_HTTPS_PORT_2_ENABLED_ON_UI));
            editTextPort2.addTextChangedListener(new EditTextWatcher(SERVICE_HTTPS, SERVICE_ATTR_HTTPS_PORT_2));
        }
        
        EditText editTextMasqueradeAddress = (EditText)form.findViewById(R.id.edittext_ssl_key_host);
        editTextMasqueradeAddress.addTextChangedListener(new EditTextWatcher(SERVICE_HTTPS, SERVICE_ATTR_HTTPS_SSL_KEY_HOST));
        editTextMasqueradeAddress.setText(s.getOptionByName(SERVICE_ATTR_HTTPS_SSL_KEY_HOST));
        editTextMasqueradeAddress.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_HTTPS_SSL_KEY_HOST_ENABLED_ON_UI));
        
        ll.addView(form);
    }
    
    private void setServiceOptionsDlna(Service s, LinearLayout ll) {
        RelativeLayout form = inflateRelativeLayout(R.layout.form_readydlna);
        
        CheckBox checkBoxDatabase = (CheckBox) form.findViewById(R.id.checkbox_auto_update_database);
        String database = s.getOptionByName(SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED);
        checkBoxDatabase.setChecked("1".equals(database) ? true : false);
        checkBoxDatabase.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED_ENABLED_ON_UI));
        
        CheckBox checkBoxTivo = (CheckBox) form.findViewById(R.id.checkbox_tivo_support);
        String tivo = s.getOptionByName(SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED);
        checkBoxTivo.setChecked("1".equals(tivo) ? true : false);
        checkBoxTivo.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED_ENABLED_ON_UI));

        Button button = (Button) form.findViewById(R.id.button_rescan_dlna);
        button.setEnabled(s.isEnabled());

        ll.addView(form);
    }
    
    private void setServiceOptionsBonjour(Service s, LinearLayout ll) {
        RelativeLayout form = inflateRelativeLayout(R.layout.form_bonjour);
        
        CheckBox checkBoxFrontView = (CheckBox) form.findViewById(R.id.checkbox_advertise_frontview);
        String frontView = s.getOptionByName(SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW);
        checkBoxFrontView.setChecked("1".equals(frontView) ? true : false);
        checkBoxFrontView.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW_ENABLED_ON_UI));
        
        CheckBox checkBoxPrinters = (CheckBox) form.findViewById(R.id.checkbox_advertise_printers);
        String printers = s.getOptionByName(SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER);
        checkBoxPrinters.setChecked("1".equals(printers) ? true : false);
        checkBoxPrinters.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER_ENABLED_ON_UI));
        
        CheckBox checkAfp = (CheckBox) form.findViewById(R.id.checkbox_advertise_afp);
        String afp = s.getOptionByName(SERVICE_ATTR_BONJOUR_ADVERTISE_AFP);
        checkAfp.setChecked("1".equals(afp) ? true : false);
        checkAfp.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_BONJOUR_ADVERTISE_AFP_ENABLED_ON_UI));
        
        ll.addView(form);
    }
    
    private void setServiceOptionsDaap(Service s, LinearLayout ll) {
        if (mNasConfiguration.getOsVersion() >= 5) {
            RelativeLayout form = inflateRelativeLayout(R.layout.form_itunes);
                        
            EditText editTextShareName = (EditText) form.findViewById(R.id.edittext_sharename);
            String sharename = s.getOptionByName(SERVICE_ATTR_DAAP_SHARE_NAME);
            sharename.replace("%h", mNas.getHostname());
            editTextShareName.setText(sharename);
            editTextShareName.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_DAAP_SHARE_NAME_ENABLED_ON_UI));
            editTextShareName.addTextChangedListener(new EditTextWatcher(SERVICE_DAAP, SERVICE_ATTR_DAAP_SHARE_NAME));
            
            EditText editTextPassword = (EditText) form.findViewById(R.id.edittext_password);
            editTextPassword.setText(s.getOptionByName(SERVICE_ATTR_DAAP_PASSWORD));
            editTextPassword.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_DAAP_PASSWORD_ENABLED_ON_UI));
            editTextPassword.addTextChangedListener(new EditTextWatcher(SERVICE_DAAP, SERVICE_ATTR_DAAP_PASSWORD));
            
            EditText editTextDirectory = (EditText)form.findViewById(R.id.edittext_directory);
            editTextDirectory.setText(s.getOptionByName(SERVICE_ATTR_DAAP_DIRECTORY));
            editTextDirectory.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_DAAP_DIRECTORY_ENABLED_ON_UI));
            editTextDirectory.addTextChangedListener(new EditTextWatcher(SERVICE_DAAP, SERVICE_ATTR_DAAP_DIRECTORY));
            
            ll.addView(form);
        }
    }
    
    private void setServiceOptionsSnmp(Service s, LinearLayout ll) {
        if (mNasConfiguration.getOsVersion() >= 5) {
            RelativeLayout form = inflateRelativeLayout(R.layout.form_snmp);

            EditText editTextHostsAllowed = (EditText) form.findViewById(R.id.edittext_allowhost);
            String hostsallowed = s.getOptionByName(SERVICE_ATTR_SNMP_ALLOW_HOST);
            editTextHostsAllowed.setText(hostsallowed);
            editTextHostsAllowed.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_SNMP_ALLOW_HOST_ENABLED_ON_UI));
            editTextHostsAllowed.addTextChangedListener(new EditTextWatcher(SERVICE_SNMP, SERVICE_ATTR_SNMP_ALLOW_HOST));

            EditText editTextCommunity = (EditText) form.findViewById(R.id.edittext_community);
            editTextCommunity.setText(s.getOptionByName(SERVICE_ATTR_SNMP_COMMUNITY));
            editTextCommunity.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_SNMP_COMMUNITY_ENABLED_ON_UI));
            editTextCommunity.addTextChangedListener(new EditTextWatcher(SERVICE_SNMP, SERVICE_ATTR_SNMP_COMMUNITY));

            EditText editTextDirectory = (EditText) form.findViewById(R.id.edittext_trap_dest);
            editTextDirectory.setText(s.getOptionByName(SERVICE_ATTR_SNMP_TRAP_DESTINATION));
            editTextDirectory.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_SNMP_TRAP_DESTINATION_ENABLED_ON_UI));
            editTextDirectory.addTextChangedListener(new EditTextWatcher(SERVICE_SNMP, SERVICE_ATTR_SNMP_TRAP_DESTINATION));

            if (mNasConfiguration.getOsVersion() == 6) {
                TextView textViewLocation = (TextView) form.findViewById(R.id.textview_location);
                textViewLocation.setVisibility(View.VISIBLE);

                EditText editTextLocation = (EditText) form.findViewById(R.id.edittext_location);
                editTextLocation.setText(s.getOptionByName(SERVICE_ATTR_SNMP_LOCATION));
                editTextLocation.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_SNMP_LOCATION_ENABLED_ON_UI));
                editTextLocation.addTextChangedListener(new EditTextWatcher(SERVICE_SNMP, SERVICE_ATTR_SNMP_LOCATION));
                editTextLocation.setVisibility(View.VISIBLE);

                TextView textViewContact = (TextView) form.findViewById(R.id.textview_contact);
                textViewContact.setVisibility(View.VISIBLE);

                EditText editTextContact = (EditText) form.findViewById(R.id.edittext_contact);
                editTextContact.setText(s.getOptionByName(SERVICE_ATTR_SNMP_CONTACT));
                editTextContact.setEnabled(s.isEnabled() && (Boolean) s.getMetaDataByName(SERVICE_ATTR_SNMP_CONTACT_ENABLED_ON_UI));
                editTextContact.addTextChangedListener(new EditTextWatcher(SERVICE_SNMP, SERVICE_ATTR_SNMP_CONTACT));
                editTextContact.setVisibility(View.VISIBLE);
            }

            ll.addView(form);
        }
    }
    
    private void saveServices() {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("save_services", params);
        
        // user could press save, without having first retrieved service data
        if (mServicesFragment.isAdded() && mServicesFragment.getView() != null && mNas != null) {
            LinearLayout view = (LinearLayout) mServicesFragment.getView().findViewById(R.id.fragment_content);
            
            captureServices(view);
            setProgressBar(View.VISIBLE);
            
            FragmentManager fm = getSupportFragmentManager();
            ReadyNasCommandReceiverFragment commandReceiver = 
                    (ReadyNasCommandReceiverFragment) fm.findFragmentByTag(READY_NAS_COMMAND_RECEIVER);
            commandReceiver.sendSetServices(mNas);
        }
    }

    private CompoundButton getButtonById(LinearLayout view, int id) {
        return (CompoundButton) view.findViewById(id).findViewById(R.id.toggle);
    }
    
    private void captureServices(LinearLayout view) {
        Service serviceCifs = mNas.getServices().get(SERVICE_CIFS);
        if (serviceCifs != null) {
            CompoundButton buttonCifs = getButtonById(view, ServiceEnum.CIFS.getId());
            serviceCifs.setEnabled(buttonCifs.isChecked());
        }
        
        Service serviceAfp = mNas.getServices().get(SERVICE_AFP);
        if (serviceAfp != null) {
            CompoundButton buttonAfp = getButtonById(view, ServiceEnum.AFP.getId());
            serviceAfp.setEnabled(buttonAfp.isChecked());
        }
        
        Service serviceNfs = mNas.getServices().get(SERVICE_NFS);
        if (serviceNfs != null) {
            CompoundButton buttonNfs = getButtonById(view, ServiceEnum.NFS.getId());
            serviceNfs.setEnabled(buttonNfs.isChecked());
        }
        
        Service serviceFtp = mNas.getServices().get(SERVICE_FTP);
        if (serviceFtp != null) {
            CompoundButton buttonFtp = getButtonById(view, ServiceEnum.FTP.getId());
            serviceFtp.setEnabled(buttonFtp.isChecked());
        }
        
        Service serviceHttp = mNas.getServices().get(SERVICE_HTTP);
        if (serviceHttp != null) {
            if (serviceHttp != null) {
                CompoundButton buttonHttp = getButtonById(view, ServiceEnum.HTTP.getId());
                serviceHttp.setEnabled(buttonHttp.isChecked());
                
                if (mNasConfiguration.getOsVersion() >= 5) {
                    String shareName = serviceHttp.getOptionByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE);
                    if ("None Selected".equals(shareName)) {
                        serviceHttp.setOptionByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE, "");
                    }
                }
            }
        }
        
        Service serviceHttps = mNas.getServices().get(SERVICE_HTTPS);
        if (serviceHttps != null) {
            CompoundButton buttonHttps = getButtonById(view, ServiceEnum.HTTPS.getId());
            serviceHttps.setEnabled(buttonHttps.isChecked());
        }
        
        Service serviceRsync = mNas.getServices().get(SERVICE_RSYNC);
        if (serviceRsync != null) {
            CompoundButton buttonRsync = getButtonById(view, ServiceEnum.RSYNC.getId());
            serviceRsync.setEnabled(buttonRsync.isChecked());
        }
        
        Service serviceUpnp = mNas.getServices().get(SERVICE_UPNP);
        if (serviceUpnp != null) {
            CompoundButton buttonUpnp = getButtonById(view, ServiceEnum.UPNPD.getId());
            serviceUpnp.setEnabled(buttonUpnp.isChecked());
        }
        
        Service serviceDlna = mNas.getServices().get(SERVICE_DLNA);
        if (serviceDlna != null) {
            CompoundButton buttonDlna = getButtonById(view, ServiceEnum.DLNA.getId());
            serviceDlna.setEnabled(buttonDlna.isChecked());
        }

        Service serviceItunes = mNas.getServices().get(SERVICE_DAAP);
        if (serviceItunes != null) {
            CompoundButton buttonItunes = getButtonById(view, ServiceEnum.DAAP.getId());
            serviceItunes.setEnabled(buttonItunes.isChecked());
        }
        
        Service serviceBonjour = mNas.getServices().get(SERVICE_BONJOUR);
        if (serviceBonjour != null) {
            CompoundButton buttonBonjour = getButtonById(view, ServiceEnum.BONJOUR.getId());
            serviceBonjour.setEnabled(buttonBonjour.isChecked());
        }
        
        Service serviceSnmp = mNas.getServices().get(SERVICE_SNMP);
        if (serviceSnmp != null) {
            CompoundButton buttonSnmp = getButtonById(view, ServiceEnum.SNMP.getId());
            serviceSnmp.setEnabled(buttonSnmp.isChecked());             
        }

        Service serviceSmartNetwork = mNas.getServices().get(SERVICE_SMART_NETWORK);
        if (serviceSmartNetwork != null) {
            CompoundButton buttonSmartNetwork = getButtonById(view, ServiceEnum.SMART_NETWORK.getId());
            serviceSmartNetwork.setEnabled(buttonSmartNetwork.isChecked());             
        }

        Service serviceAv = mNas.getServices().get(SERVICE_ANTIVIRUS);
        if (serviceAv != null) {
            CompoundButton buttonAv = getButtonById(view, ServiceEnum.ANTI_VIRUS.getId());
            serviceAv.setEnabled(buttonAv.isChecked());             
        }

        Service serviceSsh = mNas.getServices().get(SERVICE_SSH);
        if (serviceSsh != null) {
            CompoundButton buttonSsh = getButtonById(view, ServiceEnum.SSH.getId());
            serviceSsh.setEnabled(buttonSsh.isChecked());               
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
    
    public void onToggleClicked(View view) {
        String service = (String) view.getTag(R.id.service);
        CompoundButton tb = (CompoundButton) view;
        Log.i(TAG, "Service " + service + " enabled: " + tb.isChecked());

        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        params.putString("name", service);
        FirebaseAnalytics.getInstance(this).logEvent("toggle_service", params);

        Service s = mNas.getServices().get(service);
        s.setEnabled(tb.isChecked());
        for (Map.Entry<String, Object> md : s.getMetaData().entrySet()) {
            if (md.getKey().endsWith(SUFFIX_ENABLED_ON_UI)) {
                md.setValue(tb.isChecked());
            }
        }
        
        setServices();
    }
    
    private void setOptionValue(String service, String option, CheckBox view) {
        Service s = mNas.getServices().get(service);
        String value = getBinary(view.isChecked());
        s.setOptionByName(option, value);
    }
    
    public void onEnableNfsv4(View view) {
        View fragmentView = mServicesFragment.getView();
        EditText editText = (EditText) fragmentView.findViewById(R.id.edittext_nfsv4_domain);
        editText.setEnabled(((CheckBox) view).isChecked());
        
        setOptionValue(SERVICE_NFS, SERVICE_ATTR_NFS_NFSV4_ENABLED, (CheckBox) view);
    }
    
    public void onClickAdvertiseFrontview(View view) {
        setOptionValue(SERVICE_BONJOUR, SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW, (CheckBox) view);
    }
    
    public void onClickAdvertisePrinters(View view) {
        setOptionValue(SERVICE_BONJOUR, SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER, (CheckBox) view);
    }

    public void onClickAdvertiseAfp(View view) {
        setOptionValue(SERVICE_BONJOUR, SERVICE_ATTR_BONJOUR_ADVERTISE_AFP, (CheckBox) view);
    }
    
    public void onClickAutoUpdateDatabase(View view) {
        setOptionValue(SERVICE_DLNA, SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED, (CheckBox) view);
    }

    public void onClickTivoSupport(View view) {
        setOptionValue(SERVICE_DLNA,
                SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED, (CheckBox) view);
    }
    
    public void onClickAdvertiseAfpOverBonjour(View view) {
        setOptionValue(SERVICE_AFP, SERVICE_ATTR_AFP_ADVERTISE_BONJOUR, (CheckBox) view);
    }
    
    public void onClickAllowUploadResume(View view) {
        setOptionValue(SERVICE_FTP, SERVICE_ATTR_FTP_UPLOAD_RESUME, (CheckBox) view);       
    }
    
    public void onClickFtps(View view) {
        setOptionValue(SERVICE_FTP, SERVICE_ATTR_FTP_FTPS, (CheckBox) view);
    }
    
    public void onClickForceFtps(View view) {
        setOptionValue(SERVICE_FTP, SERVICE_ATTR_FTP_FORCE_FTPS, (CheckBox) view);
    }
    
    public void onClickServerTransferLog(View view) {
        setOptionValue(SERVICE_FTP, SERVICE_ATTR_FTP_SERVER_TRANSFER_LOG, (CheckBox) view);
    }
    
    public void onClickLoginAuthentication(View view) {
        setOptionValue(SERVICE_HTTP, SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD, (CheckBox) view);
    }
    
    public void onClickRescanDlna(View view) {
        Bundle params = new Bundle();
        params.putString("model", mNasConfiguration.getModel());
        params.putString("os_version", Integer.toString(mNasConfiguration.getOsVersion()));
        FirebaseAnalytics.getInstance(this).logEvent("rescan_dlna", params);

        FragmentManager fm = getSupportFragmentManager();
        ReadyNasCommandReceiverFragment commandReceiver = 
                (ReadyNasCommandReceiverFragment) fm.findFragmentByTag(READY_NAS_COMMAND_RECEIVER);
                
        if (commandReceiver != null) {
            LinearLayout progressBar = (LinearLayout) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            TextView message = (TextView) findViewById(R.id.message);
            message.setVisibility(View.GONE);
            commandReceiver.rescanDlna();
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
    
    private class SpinnerListener implements OnItemSelectedListener {
        
        private String mService;
        private String mAttribute;
        
        public SpinnerListener(String service, String attribute) {
            mService = service;
            mAttribute = attribute;
        }
        
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String value = (String) parent.getItemAtPosition(position);
            Service s = mNas.getServices().get(mService);
            s.setOptionByName(mAttribute, value);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
        }
    }
    
    private class EditTextWatcher implements TextWatcher {

        private String mService;
        private String mAttribute;
        
        public EditTextWatcher(String service, String attribute) {
            mService = service;
            mAttribute = attribute;
        }
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // do nothing
        }
        
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing
        }
        
        @Override
        public void afterTextChanged(Editable s) {
            Service service = mNas.getServices().get(mService);
            service.setOptionByName(mAttribute, s.toString());;
        }
    }
 }
