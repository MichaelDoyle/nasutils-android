/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.md.nasutils.R;
import com.md.nasutils.db.NasDeviceTable;

import androidx.cursoradapter.widget.CursorAdapter;

public class NasDeviceCursorAdapter extends CursorAdapter {

    public NasDeviceCursorAdapter(Context context, Cursor cursor,
            boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    public NasDeviceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String label = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_NAME));
        TextView labelTextView = (TextView) view.findViewById(R.id.label);
        labelTextView.setText(label);
        
        String dnsName = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_DNS_NAME));
        TextView dnsNameTextView = (TextView) view.findViewById(R.id.hostname);
        dnsNameTextView.setText(dnsName);
        
        String model = cursor.getString(cursor.getColumnIndexOrThrow(NasDeviceTable.COLUMN_MODEL));
        TextView modelTextView = (TextView) view.findViewById(R.id.model);
        modelTextView.setText(model);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflator = LayoutInflater.from(context);
        return layoutInflator.inflate(R.layout.list_item_nas_device, parent, false);
    }
}
