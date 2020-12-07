/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.widget;

import android.content.Context;
import android.view.View;

/**
 * Represents a list item for use with {@link MultiViewArrayAdapter}.
 * The list item defines its own layout.
 * 
 * @author michaeldoyle
 */
public interface MultiViewAdaptable {

    /**
     * Returns the View to display for this list item.
     * 
     * @param context context for which to retrieve the View
     * @return the View to display
     */
    public View getView(Context context);
    
}
