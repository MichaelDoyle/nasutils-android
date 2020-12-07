/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.db.NasDeviceTable;
import com.md.nasutils.ui.fragment.ReadyNasReceiverFragment;
import com.md.nasutils.ui.fragment.ReadyNasStatusReceiverFragment;
import com.md.nasutils.ui.widget.NasUtilsWidgetService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class NasUtilsWidgetConfigureActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    static final String TAG = NasUtilsWidgetConfigureActivity.class.getSimpleName();

    public static final String PREFS_NAME = "com.md.nasutils.ui.activity.NasUtilsWidgetConfigureActivity";
    public static final String PREF_DEVICE_ID_KEY = "device_id_";
    private static final String READY_NAS_STATUS_RECEIVER = "ReadyNasStatusReceiver";

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private SimpleCursorAdapter mAdapter;
    private Spinner mNasDevices;

    public NasUtilsWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_widget_config);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        findViewById(R.id.button_save).setOnClickListener(mOnClickListener);

        mNasDevices = (Spinner) findViewById(R.id.spinner_nas);
        String[] from = new String[] { NasDeviceTable.COLUMN_NAME };
        int[] to = new int[]{ android.R.id.text1 } ;
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, null, from, to, 0);
        mNasDevices.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
        initReceivers();
    }

    public void initReceivers() {
        FragmentManager fm = getSupportFragmentManager();
        ReadyNasReceiverFragment receiver = (ReadyNasStatusReceiverFragment) fm
                .findFragmentByTag(READY_NAS_STATUS_RECEIVER);

        if (receiver == null) {
            FragmentTransaction ft = fm.beginTransaction();
            receiver = new ReadyNasStatusReceiverFragment();
            ft.add(receiver, READY_NAS_STATUS_RECEIVER);
            ft.commit();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = NasUtilsWidgetConfigureActivity.this;

            long id = mNasDevices.getSelectedItemId();
            saveDeviceId(context, mAppWidgetId, id);

            // refresh the widget via service
            Intent intent = new Intent(context.getApplicationContext(), NasUtilsWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {mAppWidgetId});
            context.startService(intent);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    private void saveDeviceId(Context context, int appWidgetId, long id) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putLong(PREF_DEVICE_ID_KEY + appWidgetId, id);
        prefs.commit();
    }

    private long loadDeviceId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        long id = prefs.getLong(PREF_DEVICE_ID_KEY + appWidgetId, 0);
        return id;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                NasDeviceTable.COLUMN_ID,
                NasDeviceTable.COLUMN_NAME
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                DatabaseContentProvider.CONTENT_URI_NAS_DEVICE,
                projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
