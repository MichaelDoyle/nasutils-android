/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.md.nasutils.R;
import com.md.nasutils.model.Disk;
import com.md.nasutils.model.Response;
import com.md.nasutils.model.raidiator4.SmartDiskInfo;
import com.md.nasutils.service.ReadyNasIntentService;
import com.md.nasutils.service.ReadyNasServiceName;

import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ACTION;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_DEVICE;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_DISK;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS_CONFIG;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_RESULT_RECEIVER;

/**
 * <p>Fragment that will initiate the request Intent for retrieving 
 * NAS SMART+ disk details, and receive and process the response.</p>
 * 
 * <p>The host Activity must implement {@link OnRetrieveSmartDiskInfo} 
 * or a ClassCastException will be thrown.</p>
 * 
 * <p>After the response is received and processed, the 
 * {@link OnRetrieveSmartDiskInfo#onSuccess(SmartDiskInfo)} callback will be
 * made to the host activity.</p>
 * 
 * @author michaeldoyle
 *
 */
public class ReadyNasSmartDiskInfoReceiverFragment extends ReadyNasReceiverFragment {
    
    public static final String SAVED_INSTANCE_STATE_RESULT = "result";

    @SuppressWarnings("unused")
    private static final String TAG = ReadyNasSmartDiskInfoReceiverFragment.class.getSimpleName();
    
    private OnRetrieveSmartDiskInfo mCallBack;
    private Parcelable mResult;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState); 
        
        if(savedInstanceState != null) {
            this.mResult = savedInstanceState.getParcelable(SAVED_INSTANCE_STATE_RESULT);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_INSTANCE_STATE_RESULT, mResult);
    }

    public void refreshDetails(String disk, String deviceName) {
        mResult = null;
        setDetails(disk, deviceName);
    }
    
    private void setDetails(String disk, String deviceName) {
        Activity activity = getActivity();
        if(activity != null) {
            if (mResult == null) {
                Intent intent = new Intent(activity, ReadyNasIntentService.class);
                intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
                intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.GET_SMART_DISK_INFO.getCode());
                intent.putExtra(EXTRA_DISK, disk);
                intent.putExtra(EXTRA_DEVICE, deviceName);
                intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
                ReadyNasIntentService.enqueueWork(this.getContext(), intent);
            } else if (mResult instanceof Disk) {
                mCallBack.onRetrieveSmartDiskInfoSuccess((Disk) mResult);
            } else if (mResult instanceof Response) {
                mCallBack.onRetrieveSmartDiskInfoFailure((Response) mResult);
            }
        }
    }

    @Override
    public void onResult(Object result) {
        if (result == null) {
            result = new Response(getResources().getString(R.string.error_status_generic));
        }
        
        if (result instanceof Parcelable) {
            mResult = (Parcelable) result;
            setDetails(null, null);
        }
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnRetrieveSmartDiskInfo) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRetrieveSmartDiskInfo");
        }
    }
    
    /**
     * Activities that host this fragment must implement this interface
     * in order to receive the response callback.
     */
    public interface OnRetrieveSmartDiskInfo {
        /**
         * Callback to the Activity when status is retrieved.
         */
        void onRetrieveSmartDiskInfoSuccess(Disk disk);
        
        /**
         * Callback to the Activity when status retrieval has failed
         */
        void onRetrieveSmartDiskInfoFailure(Response error);
    }
}

