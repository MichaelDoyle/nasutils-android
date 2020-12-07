/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.md.nasutils.R;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.service.ReadyNasIntentService;
import com.md.nasutils.service.ReadyNasServiceName;

import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ACTION;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS_CONFIG;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_RESULT_RECEIVER;

/**
 * <p>Fragment that will initiate the request Intent for retrieving 
 * NAS backup job details, and receive and process the response.</p>
 * 
 * <p>The host Activity must implement {@link OnRetrieveBackups}
 * or a ClassCastException will be thrown.</p>
 *
 * <p>After the response is received and processed, the
 * {@link OnRetrieveBackups#onSuccess(com.md.nasutils.model.Nas)} callback will be
 * made to the host activity.</p>
 *
 * @author michaeldoyle
 *
 */
public class ReadyNasBackupsReceiverFragment extends ReadyNasReceiverFragment {
    
    public static final String SAVED_INSTANCE_STATE_RESULT = "result";

    @SuppressWarnings("unused")
    private static final String TAG = ReadyNasBackupsReceiverFragment.class.getSimpleName();
    
    private OnRetrieveBackups mCallBack;
    private Parcelable mResult;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState); 
        
        if(savedInstanceState != null) {
            this.mResult = savedInstanceState.getParcelable(SAVED_INSTANCE_STATE_RESULT);
        }
        
        setDetails();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_INSTANCE_STATE_RESULT, mResult);
    }

    public void refreshDetails() {
        mResult = null;
        setDetails();
    }
    
    public void setDetails() {
        Activity activity = getActivity();
        if(activity != null) {
            if (mResult == null) {
                Intent intent = new Intent(activity, ReadyNasIntentService.class);
                intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
                intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.GET_BACKUPS.getCode());
                intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
                ReadyNasIntentService.enqueueWork(this.getContext(), intent);
            } else if (mResult instanceof Nas) {
                mCallBack.onSuccess((Nas) mResult);
            } else if (mResult instanceof Response) {
                mCallBack.onFailure((Response) mResult);
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
            if (result instanceof Response) {
                mCallBack.onFailure((Response) result);
            } else if (result instanceof Nas) {
                setDetails();
            }
        }
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnRetrieveBackups) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRetrieveBackups");
        }
    }
    
    /**
     * Activities that host this fragment must implement this interface
     * in order to receive the response callback.
     */
    public interface OnRetrieveBackups {
        /**
         * Callback to the Activity when services is retrieved.
         */
        void onSuccess(Nas status);
        
        /**
         * Callback to the Activity when services retrieval has failed
         */
        void onFailure(Response error);
    }
}
