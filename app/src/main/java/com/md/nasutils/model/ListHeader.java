/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.md.nasutils.ui.widget.MultiViewAdaptable;

/**
 * Represents a list heading/separator for use with
 * {@link MultiViewAdaptable}
 * 
 * @author michaeldoyle
 */
public class ListHeader implements MultiViewAdaptable {

    private String mText;
    
    public ListHeader(String text) {
        super();
        this.mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    /**
     * Returns a TextView header/separator with the 
     * android.R.attr.listSeparatorTextViewStyle style
     */
    @Override
    public View getView(Context context) {      
        TextView view = new TextView(context, null, android.R.attr.listSeparatorTextViewStyle);
        view.setPadding(5, 5, 0, 0);
        view.setGravity(Gravity.LEFT);
        view.setWidth(LayoutParams.MATCH_PARENT);
        view.setText(mText);
        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
        view.setLongClickable(false);
        
        return view;
    }

}
