/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.md.nasutils.R;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.activity.PowerActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author michaeldoyle
 *
 */
public class PowerFragment extends Fragment {
    
    @SuppressWarnings("unused")
    private static final String TAG = PowerFragment.class.getSimpleName();
    
    private static final List<String> NON_WOL_MODELS = Collections.unmodifiableList(
            Arrays.asList("ReadyNAS NV+ v1", "ReadyNAS Duo v1", "ReadyNAS 1100"));
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            
            return null;
        }
        
        PowerActivity activity = (PowerActivity) getActivity();
        NasConfiguration config = activity.getNasConfiguration();
        
        View v = inflater.inflate(R.layout.fragment_power, container, false);

        activity.setMaxWidth(v.findViewById(R.id.shutdown_form));
        activity.setMaxWidth(v.findViewById(R.id.wol_form));
        
        if (!isWolSupported(config)) {
            View wolForm = v.findViewById(R.id.wol_form);
            wolForm.setVisibility(View.INVISIBLE);
        }
        
        EditText port = (EditText) v.findViewById(R.id.edittext_wol_port);
        port.setText(String.valueOf(config.getWolPort()));
        
        EditText packets = (EditText) v.findViewById(R.id.edittext_num_of_packets);
        packets.setText(String.valueOf(config.getWolPackets()));
        
        CheckBox sendAsBroadCast = (CheckBox) v.findViewById(R.id.checkbox_send_as_broadcast);
        sendAsBroadCast.setChecked(config.getWolSendAsBroadcast() == 1 ? true : false);
        
        return v;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
        PowerActivity activity = (PowerActivity) getActivity();
        activity.saveWolSettings();
    }
    
    public boolean isWolSupported(NasConfiguration config) {
        return !NON_WOL_MODELS.contains(config.getModel());
    }
}
