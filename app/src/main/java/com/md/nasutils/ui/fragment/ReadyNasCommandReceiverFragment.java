/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.md.nasutils.R;
import com.md.nasutils.model.AddOn;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.service.ReadyNasIntentService;
import com.md.nasutils.service.ReadyNasServiceName;

import java.util.ArrayList;

import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ACTION;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ADDONS;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_BACKUP_JOB;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_DISK;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_FORCE_FSCK;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_FORCE_QUOTA_CHECK;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS_CONFIG;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NUMBER_OF_PACKETS;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_PORT;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_RESULT_RECEIVER;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_SEND_AS_BROADCAST;

/**
 * <p>Fragment that will initiate the request Intents for start up
 * and shutdown events, and receive and process the response.</p>
 * 
 * <p>Currently, no callback is made to the host Activity. Just a simple Toast.</p>
 * 
 * @author michaeldoyle
 *
 */
public class ReadyNasCommandReceiverFragment extends ReadyNasReceiverFragment {

    @SuppressWarnings("unused")
    private static final String TAG = ReadyNasCommandReceiverFragment.class.getSimpleName();
    
    private OnCommandExecuted mCallBack;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);        
    }

    public void sendPowerDown(boolean forceFsck, boolean forceQuotaCheck) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.SEND_SHUTDOWN.getCode());
            intent.putExtra(EXTRA_FORCE_FSCK, forceFsck);
            intent.putExtra(EXTRA_FORCE_QUOTA_CHECK, forceQuotaCheck);
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }
    
    public void sendRestart(boolean forceFsck, boolean forceQuotaCheck) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.SEND_RESTART.getCode());
            intent.putExtra(EXTRA_FORCE_FSCK, forceFsck);
            intent.putExtra(EXTRA_FORCE_QUOTA_CHECK, forceQuotaCheck);
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }

    public void sendWol(int numberOfPackets, int port, boolean sendAsBroadast) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.SEND_WOL.getCode());
            intent.putExtra(EXTRA_NUMBER_OF_PACKETS, numberOfPackets);
            intent.putExtra(EXTRA_PORT, port);
            intent.putExtra(EXTRA_SEND_AS_BROADCAST, sendAsBroadast);
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }
 
    public void sendLocateDisk(String disk) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.SEND_LOCATE_DISK.getCode());
            intent.putExtra(EXTRA_DISK, disk);
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }
    
    public void recalibrateFan() {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.RECALIBRATE_FAN.getCode());
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }
    
    public void sendSetServices(Nas nas) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.SET_SERVICES.getCode());
            intent.putExtra(EXTRA_NAS, nas);
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }
    
    public void rescanDlna() {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.RESCAN_DLNA.getCode());
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }

    public void startBackup(String backupJob) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.START_BACKUP.getCode());
            intent.putExtra(EXTRA_BACKUP_JOB, backupJob);
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }

    public void toggleAddOns(ArrayList<AddOn> addOns) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.TOGGLE_ADDONS.getCode());
            intent.putParcelableArrayListExtra(EXTRA_ADDONS, addOns);
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnCommandExecuted) {
            mCallBack = (OnCommandExecuted) activity;
        }
    }
    
    @Override
    public void onResult(Object result) {
        
        String message = "";
        
        if (result != null) {
            if (result instanceof Response) {
                Response response = (Response) result;
                message = response.getMessage();
                
                if (mCallBack != null) {
                    mCallBack.onCommandSuccess(response);
                }
            }
        }
        else {
            message = getResources().getString(R.string.error_send_generic);
            
            if (mCallBack != null) {
                mCallBack.onCommandFailure(new Response(message));
            }
        }
        
        makeToast(message);
    }
    
    /**
     * Activities that host this fragment must implement this interface
     * in order to receive the response callback.
     */
    public interface OnCommandExecuted {
        /**
         * Callback to the Activity when services is retrieved.
         */
        void onCommandSuccess(Response response);
        
        /**
         * Callback to the Activity when services retrieval has failed
         */
        void onCommandFailure(Response error);
    }
}
