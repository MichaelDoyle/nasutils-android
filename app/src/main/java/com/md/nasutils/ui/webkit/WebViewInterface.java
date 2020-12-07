/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.webkit;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.JavascriptInterface;

/**
 * Provides JavaScript API for WebView
 * 
 * @author michaeldoyle
 *
 */
public class WebViewInterface {
    
    private static final String TAG = WebViewInterface.class.getSimpleName();
    
    private Context mContext;

    public WebViewInterface(Context c) {
        mContext = c;
    }
    
    @JavascriptInterface
    public String getAppVersion() {
        String version = "";
        
        try {
            version = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        
        return version;
    }

    /**
     * Get the background color from the currently applied theme
     * 
     * @return RGB color in the format #RRGGBB
     */
    @JavascriptInterface
    public String getBackgroundColor() {
        return getColor(android.R.attr.colorBackground);

    }
    
    /**
     * Get the primary text color from the currently applied theme
     * 
     * @return RGB color in the format #RRGGBB
     */
    @JavascriptInterface
    public String getTextColor() {
        return getColor(android.R.attr.textColorPrimary);
    }
    
    private String getColor(int id) {
        TypedValue tv = new TypedValue();
        mContext.getTheme().resolveAttribute(id, tv, true);
        int color = mContext.getResources().getColor(tv.resourceId);
        // strip alpha and append hash to get a legal css rgb color string
        return "#" + Integer.toHexString(color).substring(2);
    }
}
