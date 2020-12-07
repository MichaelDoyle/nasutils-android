/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.db.DictionaryTable;
import com.md.nasutils.model.AddOn;
import com.md.nasutils.model.Dictionary;
import com.md.nasutils.model.Disk;
import com.md.nasutils.model.Language;
import com.md.nasutils.model.Logs;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.service.http.HttpClientException;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.service.istat.ReadyNasIstatService;
import com.md.nasutils.service.istat.ReadyNasIstatServiceException;
import com.md.nasutils.service.istat.ReadyNasIstatServiceImpl;
import com.md.nasutils.service.network.WakeOnLanService;
import com.md.nasutils.service.network.WakeOnLanServiceImpl;
import com.md.nasutils.service.readynas.Raidiator4ReadyNasService;
import com.md.nasutils.service.readynas.ReadyNasService;
import com.md.nasutils.service.readynas.ReadyNasServiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.app.JobIntentService;

import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ACTION;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ADDONS;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_BACKUP_JOB;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_DEVICE;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_DISK;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_FORCE_FSCK;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_FORCE_QUOTA_CHECK;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS_CONFIG;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NUMBER_OF_PACKETS;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_PORT;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_RESULT_RECEIVER;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_SEND_AS_BROADCAST;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_SINCE;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_WIDGET_ID;
import static com.md.nasutils.service.ReadyNasServiceConstants.READY_NAS_RESULT;

/**
 * <p>IntentService that will handle all calls to {@link Raidiator4ReadyNasService}</p>
 * 
 * <p>Calls are typically made by creating an Intent with the following extras:</p>
 * 
 * <p>{@link ReadyNasServiceConstants#EXTRA_ACTION} := 
 * int representing the action to take<br />
 * {@link ReadyNasServiceConstants#EXTRA_RESULT_RECEIVER} := 
 * ResultReceiver which will handle the response</p>
 * 
 * <p>The Bundle sent to the ResultReceiver will contain the results under the key:
 * {@link ReadyNasServiceConstants#READY_NAS_RESULT}</p>
 * 
 * <p>Additional Extras may be required for certain actions. Result Bundles may
 * also vary.</p>
 * 
 * @author michaeldoyle
 */
public class ReadyNasIntentService extends JobIntentService {

    private static final String TAG = ReadyNasIntentService.class.getSimpleName();

    /**
     * Incoming requests will specify an action to take by providing an
     * integer key. The ServiceExecutor at the corresponding index
     * in the array will execute the action.
     */
    private SparseArray<ServiceExecutor> mServiceExecutors;

    private ReadyNasService mReadyNasService;
    private WakeOnLanService mWakeOnLanService;
    private ReadyNasIstatService mIstatService;

    static final int JOB_ID = 1000;

    public ReadyNasIntentService() {
        super();
        mServiceExecutors = new SparseArray<>();
        initServiceExecutors();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWakeOnLanService = new WakeOnLanServiceImpl();
        mIstatService = new ReadyNasIstatServiceImpl();
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ReadyNasIntentService.class, JOB_ID, work);
    }

    /**
     * Handle all incoming Intents. Retrieves a {@link ServiceExecutor}
     * based on the index sent via the Intent Extra {@link ReadyNasServiceConstants#EXTRA_ACTION}
     * to execute the request. Sends the result back to the ResultReceiver identified
     * by the Extra {@link ReadyNasServiceConstants#EXTRA_RESULT_RECEIVER}.
     * Each {@link ServiceExecutor} may expect additional Extras or SharedPreferences to be set.
     */
    @Override
    protected void onHandleWork(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean isValidRequest = true;
        Parcelable parcel = null;

        // validation
        if (!extras.containsKey(EXTRA_NAS_CONFIG) || !extras.containsKey(EXTRA_ACTION) ||
                !extras.containsKey(EXTRA_RESULT_RECEIVER)) {
            Log.e(TAG, "Either action, receiver or config was not included with the Intent.");
            isValidRequest = false;
            parcel = new Response(getResources().getString(R.string.error_generic));
        }

        // create execution context
        ServiceExecutionContext context = new ServiceExecutionContext();
        context.setExtras(extras);
        context.setPreferences(PreferenceManager.getDefaultSharedPreferences(this));

        NasConfiguration config = (NasConfiguration) extras.getParcelable(EXTRA_NAS_CONFIG);
        mReadyNasService = ReadyNasServiceFactory.getService(config);

        // decide what to do, and let it do what it does
        int action = extras.getInt(EXTRA_ACTION);
        ServiceExecutor executor = mServiceExecutors.get(action);

        if (executor == null) {
            Log.e(TAG, "Action not supported");
            isValidRequest = false;
            parcel = new Response(getResources().getString(R.string.error_generic));
        }

        if (isValidRequest) {
            try {

                FirebaseCrashlytics.getInstance()
                        .setCustomKey("request_name",
                                ReadyNasServiceName.fromCode(action).getDisplayName());

                long start = System.nanoTime();
                parcel = executor.execute(context);
                long requestTime = System.nanoTime() - start;

                Bundle params = new Bundle();
                params.putString("model", config.getModel());
                params.putString("os_version", Integer.toString(config.getOsVersion()));
                params.putString("name", ReadyNasServiceName.fromCode(action).getDisplayName());
                params.putLong("duration", requestTime / 1000000);
                FirebaseAnalytics.getInstance(this).logEvent("request", params);
            } catch (HttpClientException e) {
                parcel = new Response(e.getMessage());
            } catch (ReadyNasIstatServiceException e) {
                parcel = new Response(getResources().getString(R.string.error_istat));
            } catch (com.md.nasutils.service.readynas.UnsupportedOperationException e) {
                parcel = new Response(e.getMessage());
            } catch (Exception e) {
                Log.i(TAG, "Unexpected exception: ", e);
                // catch remaining exceptions so that the app does not crash
                // return a friendly message for the user
                parcel = new Response(getResources().getString(R.string.error_generic));
                sendExceptionReport(e);
            }
        }

        // send the result back as a bundle
        Bundle bundle = null;
        if (parcel instanceof Bundle) {
            bundle = (Bundle) parcel;
        } else {
            bundle = new Bundle();
            bundle.putParcelable(READY_NAS_RESULT, parcel);
        }

        // pass back any provided widgetId
        int widgetId = extras.getInt(EXTRA_WIDGET_ID);
        bundle.putInt(EXTRA_WIDGET_ID, widgetId);

        ResultReceiver receiver = extras.getParcelable(EXTRA_RESULT_RECEIVER);
        receiver.send(0, bundle);
    }

    private void sendExceptionReport(Exception e) {
        FirebaseCrashlytics.getInstance().recordException(e);
    }

    private void initServiceExecutors() {
        createGetLogsExecutor();
        createGetSmartDiskInfoExecutor();
        createGetStatusExecutor();
        createLocateDiskExecutor();
        createSendShutdownExecutor();
        createSendRestartExecutor();
        createSendWolExecutor();
        createGetServicesExecutor();
        createSetServicesExecutor();
        createGetFtpServiceExecutor();
        createRecalibrateFanExecutor();
        createGetTelemetryExecutor();
        createRescanDlnaExecutor();
        createGetBackupsExecutor();
        createStartBackupExecutor();
        createGetAddOnsExecutor();
        createToggleAddOnsExecutor();
    }

    private boolean dictionaryExists() {
        String[] projection = {DictionaryTable.COLUMN_ID};

        Uri uri = DatabaseContentProvider.CONTENT_URI_DICTIONARY;
        Builder builder = uri.buildUpon();
        builder.appendQueryParameter("limit", "1");
        Cursor cursor = getContentResolver().query(
                builder.build(), projection,
                null, null, null);

        boolean dictionaryExists = false;
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                dictionaryExists = true;
            }
            cursor.close();
        }

        return dictionaryExists;
    }

    private void populateDictionary(NasConfiguration config) {
        Dictionary dictionary = mReadyNasService.getDictionary(config);

        Uri uri = DatabaseContentProvider.CONTENT_URI_DICTIONARY;

        List<ContentValues> contentValues = new ArrayList<>();

        for (Language l : dictionary.getLanguages()) {
            for (Map.Entry<String, String> e : l.getItems().entrySet()) {
                ContentValues c = new ContentValues();
                c.put(DictionaryTable.COLUMN_KEY, e.getKey());
                c.put(DictionaryTable.COLUMN_VALUE, e.getValue());
                c.put(DictionaryTable.COLUMN_LANGUAGE_CODE, l.getCode());
                contentValues.add(c);
            }
        }

        ContentValues[] cv = contentValues
                .toArray(new ContentValues[contentValues.size()]);

        getContentResolver().bulkInsert(uri, cv);
    }

    private Map<String, String> getDictionary() {
        Uri uri = DatabaseContentProvider.CONTENT_URI_DICTIONARY;

        Cursor cursor = getContentResolver().query(
                uri, null, null, null, null);

        Map<String, String> dictionary = new HashMap<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String key = cursor.getString(cursor.getColumnIndexOrThrow(DictionaryTable.COLUMN_KEY));
                String value = cursor.getString(cursor.getColumnIndexOrThrow(DictionaryTable.COLUMN_VALUE));
                dictionary.put(key, value);
            }
            cursor.close();
        }

        return dictionary;
    }

    private void replaceLogText(Logs logs) {
        Map<String, String> dictionary = getDictionary();

        for (com.md.nasutils.model.Log log : logs.getLogs()) {
            String desc = dictionary.get(log.getDescription());
            if (log.getArgs() != null && desc != null) {
                for (Map.Entry<String, String> e : log.getArgs().entrySet()) {
                    String token = "{" + e.getKey() + "}";
                    desc = desc.replace(token, e.getValue());
                }
                log.setDescription(desc);
            }
        }
    }

    private void replaceBackupStatus(Nas nas) {
        Map<String, String> dictionary = getDictionary();
        for (com.md.nasutils.model.BackupJob b : nas.getBackupJobs()) {
            b.setStatusDescription(dictionary.get(b.getStatusDescription()));
        }
    }
    
    /**
     * Create {@link ServiceExecutor} that will send a request for NAS logs.
     * 
     * Returns a {@link com.md.nasutils.model.Logs}
     */
    private void createGetLogsExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_LOGS.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                            
                Logs logs = mReadyNasService.getStatusLogs(config);
                
                if (config.getOsVersion() >= 6) {
                    if (!dictionaryExists()) {
                        populateDictionary(config);
                    }
                    replaceLogText(logs);
                }
                                
                return logs;
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will send a request for 
     * comprehensive NAS status details.
     * 
     * Returns a {@link com.md.nasutils.model.Nas}
     */
    private void createGetStatusExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_STATUS.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                
                return mReadyNasService.getStatus(config);
            }
        });
    };
    
    /**
     * <p>Create {@link ServiceExecutor} that will send a shutdown request. 
     * Expects two extras:</p>
     * 
     * <p>{@link ReadyNasServiceConstants#EXTRA_FORCE_FSCK} := 
     * Boolean - Force volume scan on next boot<br />
     * {@link ReadyNasServiceConstants#EXTRA_FORCE_QUOTA_CHECK} := 
     * Boolean - Force quota check on next boot</p>
     * 
     * Returns a {@link com.md.nasutils.model.Response}
     */
    private void createSendShutdownExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.SEND_SHUTDOWN.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                boolean forceFsck = extras.getBoolean(EXTRA_FORCE_FSCK, false);
                boolean forceQuotaCheck = extras.getBoolean(EXTRA_FORCE_QUOTA_CHECK, false);
                
                return mReadyNasService.shutdown(config, forceFsck, forceQuotaCheck);
            }
        });
    };
    
    /**
     * <p>Create {@link ServiceExecutor} that will send a restart request. 
     * Expects two extras:</p>
     * 
     * <p>{@link ReadyNasServiceConstants#EXTRA_FORCE_FSCK} := 
     * Boolean - Force volume scan on next boot<br />
     * {@link ReadyNasServiceConstants#EXTRA_FORCE_QUOTA_CHECK} := 
     * Boolean - Force quota check on next boot</p>
     * 
     * Returns a {@link com.md.nasutils.model.Response}
     */
    private void createSendRestartExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.SEND_RESTART.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                boolean forceFsck = extras.getBoolean(EXTRA_FORCE_FSCK, false);
                boolean forceQuotaCheck = extras.getBoolean(EXTRA_FORCE_QUOTA_CHECK, false);
                return mReadyNasService.restart(config, forceFsck, forceQuotaCheck);
            }
        });
    };
    
    /**
     * <p>Create {@link ServiceExecutor} that will send a number 
     * of WOL packets to wake up the NAS using the MAC and hostname 
     * set in shared user preferences.</p>
     * 
     * <p>Expects three extras:</p>
     * 
     * <p>{@link ReadyNasServiceConstants#EXTRA_SEND_AS_BROADCAST} := 
     * Boolean - send the request over the broadcast address?<br />
     * {@link ReadyNasServiceConstants#EXTRA_NUMBER_OF_PACKETS} := 
     * int - number of packets to send<br/>
     * {@link ReadyNasServiceConstants#EXTRA_PORT} := 
     * int - port on which to send WOL packets</p>
     * 
     * <p>Returns an empty {@link android.os.Bundle}</p>
     */
    private void createSendWolExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.SEND_WOL.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateWol();
                if (error!=null) {
                    return new Response(error);
                }
                boolean sendAsBroadcast = extras.getBoolean(EXTRA_SEND_AS_BROADCAST);
                int numberOfPackets = extras.getInt(EXTRA_NUMBER_OF_PACKETS);
                int port = extras.getInt(EXTRA_PORT);

                // This logic should probably be moved into the service
                if (port < 0 || port > 65535) {
                    Response response = new Response(
                            getResources().getString(R.string.wol_port_validation));
                    return response;
                }
                
                if (sendAsBroadcast) {
                    mWakeOnLanService.sendWakeOnLan(config.getMacAddress(),
                            port, numberOfPackets);
                } else {
                    mWakeOnLanService.sendWakeOnLan(config.getHostname(),
                            config.getMacAddress(), port, numberOfPackets);
                }
                
                Response response = new Response(
                        getResources().getString(R.string.wol_success));
                return response;
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will recalibrate the system fan
     * 
     * Returns a {@link com.md.nasutils.model.Response}
     */
    private void createRecalibrateFanExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.RECALIBRATE_FAN.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                return mReadyNasService.recalibrateFan(config);
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will blink the LCD for a given disk
     * 
     * Returns a {@link com.md.nasutils.model.Response}
     */
    private void createLocateDiskExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.SEND_LOCATE_DISK.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                String disk = extras.getString(EXTRA_DISK);
                return mReadyNasService.locateDisk(config, disk);
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will send a request for 
     * SMART+ disk information.
     * 
     * Returns a {@link com.md.nasutils.model.Disk}
     */
    private void createGetSmartDiskInfoExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_SMART_DISK_INFO.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                String disk = extras.getString(EXTRA_DISK);
                String deviceName = extras.getString(EXTRA_DEVICE);
                Disk smartDisk = mReadyNasService.getSmartDiskInfo(config, disk, deviceName);
                if(smartDisk!= null) {
                    return smartDisk;
                }
                return new Response(getResources().getString(R.string.error_unavailable));
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will send a request for 
     * NAS Service details.
     * 
     * Returns a {@link com.md.nasutils.model.Nas}
     */
    private void createGetServicesExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_SERVICES.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                
                return mReadyNasService.getServices(config);
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will send a request to update 
     * NAS Service details.
     * 
     * Returns a {@link com.md.nasutils.model.Response}
     */
    private void createSetServicesExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.SET_SERVICES.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                Nas nas = (Nas) extras.getParcelable(EXTRA_NAS);
                return mReadyNasService.setServices(config, nas);
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will send a request for 
     * FTP Service details.
     * 
     * Returns a {@link com.md.nasutils.model.Nas}
     */
    private void createGetFtpServiceExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_FTP_SERVICE.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                
                return mReadyNasService.getFtpServiceDetails(config);
            }
        });
    };
    
    private void createGetTelemetryExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_TELEMETRY.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                long since = extras.getLong(EXTRA_SINCE);
                String error = config.validateIstat();
                if (error!=null) {
                    return new Response(error, true);
                }
                
                return mIstatService.getTelemetrySince(config, since);
            }
        });
    };
    
    /**
     * Create {@link ServiceExecutor} that will rescan DLNA media folders
     * 
     * Returns a {@link com.md.nasutils.model.Response}
     */
    private void createRescanDlnaExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.RESCAN_DLNA.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                return mReadyNasService.rescanDlna(config);
            }
        });
    };

    /**
     * Create {@link ServiceExecutor} that will send a request for
     * backup job details.
     *
     * Returns a {@link com.md.nasutils.model.Nas}
     */
    private void createGetBackupsExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_BACKUPS.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);

                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }

                Nas nas = mReadyNasService.getBackups(config);

                if (config.getOsVersion() >= 6) {
                    if (!dictionaryExists()) {
                        populateDictionary(config);
                    }
                    replaceBackupStatus(nas);
                }

                return nas;
            }
        });
    };

    /**
     * Create {@link ServiceExecutor} that will start a backup job
     *
     * Returns a {@link com.md.nasutils.model.Response}
     */
    private void createStartBackupExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.START_BACKUP.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                String backupJob = extras.getString(EXTRA_BACKUP_JOB);
                return mReadyNasService.startBackup(config, backupJob);
            }
        });
    };

    private void createGetAddOnsExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.GET_ADDONS.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                return mReadyNasService.getAddOns(config);
            }
        });
    };

    private void createToggleAddOnsExecutor() {
        mServiceExecutors.put(ReadyNasServiceName.TOGGLE_ADDONS.getCode(), new ServiceExecutor() {
            @Override
            public Parcelable execute(ServiceExecutionContext context) {
                Bundle extras = context.getExtras();
                NasConfiguration config = extras.getParcelable(EXTRA_NAS_CONFIG);
                String error = config.validateHttp();
                if (error!=null) {
                    return new Response(error);
                }
                List<AddOn> addons = extras.getParcelableArrayList(EXTRA_ADDONS);
                return mReadyNasService.toggleAddOns(config, addons);
            }
        });
    };
}
