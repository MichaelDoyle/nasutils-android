/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.util;

import android.content.Context;

public class ScreenUtils {
    
    public static int dipToPx(Context context, int i) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) Math.ceil(i * density);
    }

    public static int pxToDip(Context context, int i) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) Math.ceil(i / density);
    }
}
