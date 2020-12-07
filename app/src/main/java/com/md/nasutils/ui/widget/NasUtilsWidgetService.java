/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.db.NasDeviceTable;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Temperature;
import com.md.nasutils.service.ReadyNasIntentService;
import com.md.nasutils.service.ReadyNasServiceName;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.ui.fragment.PreferenceConstants;
import com.md.nasutils.util.ScreenUtils;
import com.md.nasutils.util.UnitConversionUtils;

import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_ACTION;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_NAS_CONFIG;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_RESULT_RECEIVER;
import static com.md.nasutils.service.ReadyNasServiceConstants.EXTRA_WIDGET_ID;
import static com.md.nasutils.service.ReadyNasServiceConstants.READY_NAS_RESULT;
import static com.md.nasutils.ui.activity.NasUtilsWidgetConfigureActivity.PREFS_NAME;
import static com.md.nasutils.ui.activity.NasUtilsWidgetConfigureActivity.PREF_DEVICE_ID_KEY;

public class NasUtilsWidgetService extends Service {

    private static final String TAG = NasUtilsWidgetService.class.getSimpleName();

    private ResultReceiver mReceiver;
    private int mTempUnits = 0;

    public NasUtilsWidgetService() {
        mReceiver = new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultData != null && resultData.containsKey(READY_NAS_RESULT)) {
                    onResult(resultData.getInt(EXTRA_WIDGET_ID),
                            resultData.getParcelable(READY_NAS_RESULT));
                }
                else {
                    onResult(0, null);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {
            NasConfiguration config = getNasConfiguration(widgetId);
            if (config.getOsVersion() > 0) {
                Intent intentService = new Intent(this, ReadyNasIntentService.class);
                intentService.putExtra(EXTRA_NAS_CONFIG, config);
                intentService.putExtra(EXTRA_WIDGET_ID, widgetId);
                intentService.putExtra(EXTRA_ACTION, ReadyNasServiceName.GET_STATUS.getCode());
                intentService.putExtra(EXTRA_RESULT_RECEIVER, mReceiver);
                startService(intentService);
            }
        }

        stopSelf();

        super.onStartCommand(intent, flags, startId);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ArcMeterView createArcMeterView(String label, String value, float percentFull) {
        ArcMeterView meter = new ArcMeterView(this, percentFull, label, value);
        meter.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return meter;
    }

    private void onResult(int widgetId, Parcelable result) {
        if (result instanceof Nas) {
            Nas nas = (Nas) result;

            SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            mTempUnits = Integer.parseInt(appPrefs.getString(PreferenceConstants.TEMPERATURE_UNIT, "0"));

            AppWidgetManager appWidgetManager =
                    AppWidgetManager.getInstance(this.getApplicationContext());

            // quick and dirty for demo purposes
            Temperature temp = nas.getEnclosures().get(0).getTemperatures().get(0);
            float pct = (float) temp.getTemperature() / temp.getTemperatureMax();

            Integer t = (mTempUnits == 0 ? temp.getTemperature() :
                    UnitConversionUtils.celciusToFahrenheit(temp.getTemperature()));

            RemoteViews remoteViews = new RemoteViews(
                    this.getApplicationContext().getPackageName(),
                    R.layout.widget_layout);

            String label = "\u00B0" + (mTempUnits == 0 ? "C" : "F");
            int width = ScreenUtils.dipToPx(this, 120);
            int height = ScreenUtils.dipToPx(this, 60);

            ArcMeterView meter = createArcMeterView(label, t.toString(), pct);
            meter.measure(width, height);
            meter.layout(0, 0, width, height);
            meter.setDrawingCacheEnabled(true);

            Bitmap bitmap = meter.getDrawingCache();

            remoteViews.setImageViewBitmap(R.id.meter, bitmap);
            remoteViews.setTextViewText(R.id.header, nas.getModel());
            remoteViews.setTextViewText(R.id.sub_header, nas.getHostname());

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private NasConfiguration getNasConfiguration(int widgetId) {
        String[] projection = {
                NasDeviceTable.COLUMN_DNS_NAME,
                NasDeviceTable.COLUMN_MODEL,
                NasDeviceTable.COLUMN_OS_VERSION,
                NasDeviceTable.COLUMN_USERNAME,
                NasDeviceTable.COLUMN_PASSWORD,
                NasDeviceTable.COLUMN_MAC_ADDRESS,
                NasDeviceTable.COLUMN_PORT,
                NasDeviceTable.COLUMN_SSH_USERNAME,
                NasDeviceTable.COLUMN_SSH_PORT,
                NasDeviceTable.COLUMN_ISTAT_PASSCODE,
                NasDeviceTable.COLUMN_ISTAT_PORT,
                NasDeviceTable.COLUMN_WOL_PORT,
                NasDeviceTable.COLUMN_WOL_PACKETS,
                NasDeviceTable.COLUMN_WOL_SEND_AS_BCAST,
                NasDeviceTable.COLUMN_ISTAT_PORT};

        SharedPreferences widgetPrefs = getSharedPreferences(PREFS_NAME, 0);
        long id = widgetPrefs.getLong(PREF_DEVICE_ID_KEY + widgetId, 0);

        Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_NAS_DEVICE + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        String hostname = null;
        String user = null;
        String password = null;
        String mac = null;
        int port = 0;
        int osVersion = 0;
        String sshUser = null;
        int sshPort = 0;
        String istatPasscode = null;
        int istatPort = 0;
        int wolPort = 0;
        int wolPackets = 0;
        int wolSendAsBroadcast = 0;
        String model = null;

        if (cursor != null && cursor.moveToFirst()) {
            osVersion = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_OS_VERSION));
            hostname = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_DNS_NAME));
            port = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_PORT));
            mac = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_MAC_ADDRESS));
            user = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_USERNAME));
            password = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_PASSWORD));
            sshUser = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_SSH_USERNAME));
            sshPort = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_SSH_PORT));
            istatPasscode = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_ISTAT_PASSCODE));
            model = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_MODEL));

            wolPort = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_WOL_PORT));
            if (wolPort == 0) {
                wolPort = 9;
            }

            wolPackets = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_WOL_PACKETS));
            if (wolPackets == 0) {
                wolPackets = 3;
            }

            istatPort = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_ISTAT_PORT));
            if (istatPort == 0) {
                istatPort = 5109;
            }

            wolSendAsBroadcast = cursor.getInt(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_WOL_SEND_AS_BCAST));
            cursor.close();
        }

        return new NasConfiguration("https", hostname, port, user, password,
                mac, osVersion, sshUser, sshPort, istatPasscode, istatPort,
                wolPort, wolPackets, wolSendAsBroadcast, model);
    }
}
