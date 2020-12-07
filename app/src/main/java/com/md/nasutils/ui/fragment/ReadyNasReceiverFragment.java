/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.md.nasutils.service.http.NasConfiguration;

import androidx.fragment.app.Fragment;

import static com.md.nasutils.service.ReadyNasServiceConstants.READY_NAS_RESULT;

/**
 * Base class which all Receiver fragments must extend
 * 
 * @author michaeldoyle
 *
 */
public abstract class ReadyNasReceiverFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = ReadyNasReceiverFragment.class.getSimpleName();

    private ResultReceiver mReceiver;
    
    public ResultReceiver getResultReceiver() {
        return mReceiver;
    }
    
    public ReadyNasReceiverFragment() {
        mReceiver = new ResultReceiver(new Handler()) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    if (resultData != null && resultData.containsKey(READY_NAS_RESULT)) {
                        onResult(resultData.getParcelable(READY_NAS_RESULT));
                    }
                    else {
                        onResult(null);
                    }
                } 
            };
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    
    protected NasConfiguration getNasConfigurationFromActivity(Activity context) {  
        NasConfiguration nasConfiguration = null;
        
        if (context instanceof NasConfigurationProvider) {
            nasConfiguration = ((NasConfigurationProvider) context).getNasConfiguration();
        }
        
        return nasConfiguration;
    }
    
    protected NasConfiguration getNasConfigurationFromPrefs(Activity context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        String hostname = prefs.getString(PreferenceConstants.SERVER_HOSTNAME, null);
        String portString = prefs.getString(PreferenceConstants.SERVER_PORT, null);
        String user = prefs.getString(PreferenceConstants.SERVER_USERNAME, null);
        String password = prefs.getString(PreferenceConstants.SERVER_PASSWORD, null);
        String mac = prefs.getString(PreferenceConstants.SERVER_MAC, null);
        String osVersionString = prefs.getString(PreferenceConstants.READYNAS_MODEL, null);
        
        int port;
        
        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            port = 443; // default
        }
        
        int osVersion;
        
        try {
            osVersion = Integer.parseInt(osVersionString);
        } catch (NumberFormatException e) {
            osVersion = 400; // default
        }
        
        return new NasConfiguration("https", hostname, port, user, password, mac, osVersion);
    }
    
    protected void makeToast(String message) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }

    abstract public void onResult(Object result);
    
    public interface NasConfigurationProvider {
       NasConfiguration getNasConfiguration();
    }
}
