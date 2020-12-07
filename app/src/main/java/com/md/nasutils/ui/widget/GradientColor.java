/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.widget;

import android.graphics.Color;

public enum GradientColor {

    RED(Color.rgb(70, 0, 0), Color.rgb(230, 0, 0)),
    YELLOW(Color.rgb(122, 130, 0), Color.rgb(255, 240, 41)),
    GREEN(Color.rgb(0, 60, 7), Color.rgb(0, 230, 27)),
    BLUE(Color.rgb(0, 0, 204), Color.rgb(102, 102, 255)),
    VIOLET(Color.rgb(102, 0, 204), Color.rgb(178, 102, 255));

    private int mStart;
    private int mEnd;
    
    GradientColor(int start, int end) {
        this.mStart = start;
        this.mEnd = end;
    }

    public int getStart() {
        return mStart;
    }

    public void setStart(int start) {
        this.mStart = start;
    }

    public int getEnd() {
        return mEnd;
    }

    public void setEnd(int end) {
        this.mEnd = end;
    }
}
