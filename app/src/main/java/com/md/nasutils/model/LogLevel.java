/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.md.nasutils.R;
import com.md.nasutils.service.readynas.Raidiator4Constants;
import com.md.nasutils.service.readynas.XmlReadyNasConstants;

public enum LogLevel implements Parcelable {

    ERROR (R.drawable.ic_log_error),
    WARNING (R.drawable.ic_log_warning), 
    INFO (R.drawable.ic_log_info);
    
    private int drawable;

    LogLevel(int drawable) {
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
    
    public static LogLevel getLogLevel(String string) {
        if (Raidiator4Constants.LED_ERROR.contains(string)
                || XmlReadyNasConstants.LOG_LEVEL_ERROR.contains(string)) {
            return ERROR;
        } else if (Raidiator4Constants.LED_WARNING.contains(string)
                || XmlReadyNasConstants.LOG_LEVEL_WARNING.contains(string)) {
            return WARNING;
        } else {
            return INFO;
        }
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<LogLevel> CREATOR = new Creator<LogLevel>() {
        @Override
        public LogLevel createFromParcel(final Parcel source) {
            return LogLevel.values()[source.readInt()];
        }

        @Override
        public LogLevel[] newArray(final int size) {
            return new LogLevel[size];
        }
    };
}
