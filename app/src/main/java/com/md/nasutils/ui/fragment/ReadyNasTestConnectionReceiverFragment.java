/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.md.nasutils.R;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.service.ReadyNasIntentService;
import com.md.nasutils.service.ReadyNasServiceName;
import com.md.nasutils.service.http.NasConfiguration;

import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ACTION;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS_CONFIG;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_RESULT_RECEIVER;

/**
 * @author michaeldoyle
 */
public class ReadyNasTestConnectionReceiverFragment extends ReadyNasReceiverFragment {
    
    public static final String SAVED_INSTANCE_STATE_RESULT = "result";

    @SuppressWarnings("unused")
    private static final String TAG = ReadyNasTestConnectionReceiverFragment.class.getSimpleName();
    
    private OnTestConnection mCallBack;
    
    public void testConnection(NasConfiguration nasConfig) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ReadyNasIntentService.class);
            intent.putExtra(EXTRA_NAS_CONFIG, nasConfig);
            intent.putExtra(EXTRA_ACTION, ReadyNasServiceName.GET_STATUS.getCode());
            intent.putExtra(EXTRA_RESULT_RECEIVER, getResultReceiver());
            ReadyNasIntentService.enqueueWork(this.getContext(), intent);
        }
    }

    @Override
    public void onResult(Object result) {
        if (result == null) {
            result = new Response(getResources().getString(R.string.error_status_generic));
        }
        
        if (result instanceof Parcelable) {
            if (result instanceof Response) {
                mCallBack.onTestConnectionFailure((Response) result);
            } else if (result instanceof Nas) {
                mCallBack.onTestConnectionSuccess((Nas) result);
            }
        }
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnTestConnection) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRetrieveStatus");
        }
    }
    
    /**
     * Activities that host this fragment must implement this interface
     * in order to receive the response callback.
     */
    public interface OnTestConnection {
        /**
         * Callback to the Activity when status is retrieved.
         */
        void onTestConnectionSuccess(Nas status);
        
        /**
         * Callback to the Activity when status retrieval has failed
         */
        void onTestConnectionFailure(Response error);
    }
}

