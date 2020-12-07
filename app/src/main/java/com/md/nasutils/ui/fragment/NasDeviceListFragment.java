/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.md.nasutils.R;
import com.md.nasutils.db.DatabaseContentProvider;
import com.md.nasutils.db.NasDeviceTable;
import com.md.nasutils.ui.activity.MenuActivity;
import com.md.nasutils.ui.activity.NasDeviceActivity;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

public class NasDeviceListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;

    private SimpleCursorAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addHostFromPrefs();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        
        return inflater.inflate(R.layout.fragment_nas_device_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillData();
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_NAS_DEVICE + "/" + info.id);

        switch (item.getItemId()) {
        case DELETE_ID:
            getActivity().getContentResolver().delete(uri, null, null);
            fillData();
            return true;
        case EDIT_ID:
            Intent intent = new Intent(getActivity(), NasDeviceActivity.class);
            intent.putExtra(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE, uri);
            startActivity(intent);
            return true;
        }
        
        return super.onContextItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_NAS_DEVICE + "/" + id);
        intent.putExtra(DatabaseContentProvider.TYPE_ITEM_NAS_DEVICE, uri);
        startActivity(intent);
    }

    private void fillData() {
        String[] from = new String[] { NasDeviceTable.COLUMN_NAME,
                NasDeviceTable.COLUMN_DNS_NAME, NasDeviceTable.COLUMN_MODEL };
        int[] to = new int[] { R.id.label, R.id.hostname, R.id.model };

        getLoaderManager().initLoader(0, null, this);
        
        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_nas_device, null, from, to, 0);

        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 1, "Edit");
        menu.add(0, DELETE_ID, 2, "Delete");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { NasDeviceTable.COLUMN_ID,
                NasDeviceTable.COLUMN_NAME, NasDeviceTable.COLUMN_DNS_NAME,
                NasDeviceTable.COLUMN_MODEL };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                DatabaseContentProvider.CONTENT_URI_NAS_DEVICE, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

        Bundle params = new Bundle();
        params.putInt("size", adapter.getCount());
        FirebaseAnalytics.getInstance(getActivity()).logEvent("load_device_list", params);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
    
    /**
     * If the user is upgrading from a previous version,
     * add their configured host automatically to the database
     * and clean up the old preferences
     */
    private void addHostFromPrefs() {
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        
        if (!"".equals(prefs.getString(PreferenceConstants.SERVER_HOSTNAME, ""))) {
            ContentValues values = new ContentValues();
            values.put(NasDeviceTable.COLUMN_NAME, prefs.getString(PreferenceConstants.SERVER_HOSTNAME, null));
            values.put(NasDeviceTable.COLUMN_DNS_NAME, prefs.getString(PreferenceConstants.SERVER_HOSTNAME, null));
            values.put(NasDeviceTable.COLUMN_PORT, Integer.parseInt(prefs.getString(PreferenceConstants.SERVER_PORT, "443")));
            values.put(NasDeviceTable.COLUMN_MAC_ADDRESS, prefs.getString(PreferenceConstants.SERVER_MAC, null));
            values.put(NasDeviceTable.COLUMN_USERNAME, prefs.getString(PreferenceConstants.SERVER_USERNAME, null));
            values.put(NasDeviceTable.COLUMN_PASSWORD, prefs.getString(PreferenceConstants.SERVER_PASSWORD, null));
            
            // new fields for make/model and os version - just default to the first option
            String[] firmwareVersions = getResources().getStringArray(R.array.nasModelFirmwareVersions);
            values.put(NasDeviceTable.COLUMN_MODEL, getResources().getStringArray(R.array.nasModels)[0]);
            values.put(NasDeviceTable.COLUMN_OS_VERSION, Integer.parseInt(firmwareVersions[0]));
    
            // new fields for SSH
            values.put(NasDeviceTable.COLUMN_SSH_USERNAME, prefs.getString(PreferenceConstants.SERVER_USERNAME, null));
            values.put(NasDeviceTable.COLUMN_SSH_PORT, 22);

            // add host to database
            getActivity().getContentResolver().insert(DatabaseContentProvider.CONTENT_URI_NAS_DEVICE, values);
            
            // clean up preferences
            Editor editor = prefs.edit();
            editor.remove(PreferenceConstants.SERVER_HOSTNAME);
            editor.remove(PreferenceConstants.SERVER_PORT);
            editor.remove(PreferenceConstants.SERVER_MAC);
            editor.remove(PreferenceConstants.SERVER_USERNAME);
            editor.remove(PreferenceConstants.SERVER_PASSWORD);
            editor.commit();
        }
    }
}
