/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * ArrayAdapter that allows each list item to provide
 * its own Layout. List items implement {@link MultiViewAdaptable}.
 * 
 * @author michaeldoyle
 *
 */
public class MultiViewArrayAdapter extends ArrayAdapter<MultiViewAdaptable> {

    @SuppressWarnings("unused")
    private static final String TAG = MultiViewArrayAdapter.class.getSimpleName();

    public MultiViewArrayAdapter(Context context, List<MultiViewAdaptable> items) {
        super(context, 0, items);
    }
    
    /**
     * Delegates to the list item to provide its own View to display
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MultiViewAdaptable item = (MultiViewAdaptable) getItem(position);
        return item.getView(getContext());
    }
    
    @Override
    public int getViewTypeCount() {
        // TODO: actually return the correct number
        return super.getViewTypeCount();
    }
}
