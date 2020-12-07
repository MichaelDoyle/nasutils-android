/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.md.nasutils.R;
import com.md.nasutils.ui.widget.MultiViewAdaptable;

/**
 * Standard menu item for the main navigation
 * 
 * @author michaeldoyle
 */
public class MenuItem implements MultiViewAdaptable {

    private String mLabel;
    private int mDrawable;
    
    public MenuItem() {
    }
    
    public MenuItem(String label, int drawable) {
        this.mLabel = label;
        this.mDrawable = drawable;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }
    
    public int getDrawable() {
        return mDrawable;
    }

    public void setDrawable(int drawable) {
        this.mDrawable = drawable;
    }

    /**
     * Returns a LinearLayout with the menu item text
     */
    @Override
    public View getView(Context context) {
        LayoutInflater layoutInflator = LayoutInflater.from(context);
    
        FrameLayout view = new FrameLayout(context);
        layoutInflator.inflate(R.layout.list_item_main, view);
        
        TextView mainText = (TextView) view.findViewById(R.id.list_item_text);
        mainText.setText(mLabel);
        Drawable iconImage = context.getResources().getDrawable(mDrawable);
        mainText.setCompoundDrawablesWithIntrinsicBounds(iconImage, null, null, null);  
        mainText.setCompoundDrawablePadding(10);
        return view;
    }
}
