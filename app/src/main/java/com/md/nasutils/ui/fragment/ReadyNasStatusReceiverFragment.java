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
 * NAS status details, and receive and process the response.</p>
 * 
 * <p>The host Activity must implement {@link OnRetrieveStatus} 
 * or a ClassCastException will be thrown.</p>
 * 
 * <p>After the response is received and processed, the 
 * {@link OnRetrieveStatus#onRetrieveStatusSuccess(Nas status)} callback will be
 * made to the host activity.</p>
 * 
 * @author michaeldoyle
 *
 */
public class ReadyNasStatusReceiverFragment extends ReadyNasReceiverFragment {
    
    public static final String SAVED_INSTANCE_STATE_RESULT = "result";

    @SuppressWarnings("unused")
    private static final String TAG = ReadyNasStatusReceiverFragment.class.getSimpleName();
    
    private OnRetrieveStatus mCallBack;
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
    
    private void setDetails() {
        Activity activity = getActivity();
        if(activity != null) {
            if (mResult == null) {
                Intent intent = new Intent(activity, ReadyNasIntentService.class);
                intent.putExtra(EXTRA_NAS_CONFIG, getNasConfigurationFromActivity(activity));
                intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.GET_STATUS.getCode());
                intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
                ReadyNasIntentService.enqueueWork(this.getContext(), intent);
            } else if (mResult instanceof Nas) {
                mCallBack.onRetrieveStatusSuccess((Nas) mResult);
            } else if (mResult instanceof Response) {
                mCallBack.onRetrieveStatusFailure((Response) mResult);
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
                mCallBack.onRetrieveStatusFailure((Response) result);
            } else if (result instanceof Nas) {
                setDetails();
            }
        }
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnRetrieveStatus) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRetrieveStatus");
        }
    }
    
    /**
     * Activities that host this fragment must implement this interface
     * in order to receive the response callback.
     */
    public interface OnRetrieveStatus {
        /**
         * Callback to the Activity when status is retrieved.
         */
        void onRetrieveStatusSuccess(Nas status);
        
        /**
         * Callback to the Activity when status retrieval has failed
         */
        void onRetrieveStatusFailure(Response error);
    }
}

