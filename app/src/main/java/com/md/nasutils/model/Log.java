/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.nasutils.R;
import com.md.nasutils.ui.widget.MultiViewAdaptable;
import com.md.nasutils.ui.widget.MultiViewArrayAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Domain object representing a Log entry. Implements {@link MultiViewAdaptable}
 * to support use with {@link MultiViewArrayAdapter}
 * 
 * @author michaeldoyle
 */
public class Log implements Parcelable, MultiViewAdaptable {
    
    private long mId;
    private String mDescription;
    private Date mTime;
    private LogLevel mLogLevel;
    private Map<String, String> mArgs;
    private String mDateString;

    public Log() {
    }
    
    public Log(Parcel in) {
        mId = in.readLong();
        mDescription = in.readString();
        mTime = new Date(in.readLong());
        mLogLevel = in.readParcelable(Log.class.getClassLoader());
        mArgs = new HashMap<>();
        in.readMap(mArgs, Log.class.getClassLoader());
        mDateString = in.readString();
    }
    
    public Date getTime() {
        return mTime;
    }
    
    public void setTime(Date time) {
        this.mTime = time;
    }
    
    public String getDescription() {
        return mDescription;
    }
    
    public void setDescription(String description) {
        this.mDescription = description;
    }
    
    public long getId() {
        return mId;
    }
    
    public void setId(long id) {
        this.mId = id;
    }
    
    public LogLevel getLogLevel() {
        return mLogLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.mLogLevel = logLevel;
    }

    public Map<String, String> getArgs() {
        return mArgs;
    }

    public void setArgs(Map<String, String> args) {
        this.mArgs = args;
    }
    
    public String getDateString() {
        return mDateString;
    }

    public void setDateString(String dateString) {
        this.mDateString = dateString;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(mId);
        out.writeString(mDescription);
        out.writeLong(mTime != null ? mTime.getTime() : 0);
        out.writeParcelable(mLogLevel, 0);
        out.writeMap(mArgs);
        out.writeString(mDateString);
    }

    public static final Parcelable.Creator<Log> CREATOR = 
            new Parcelable.Creator<Log>() {
                public Log createFromParcel(Parcel in) {
                    return new Log(in);
                }
        
                public Log[] newArray(int size) {
                    return new Log[size];
                }
            };

    /**
     * Returns a LinearLayout including the log date and message
     */
    @Override
    public View getView(Context context) {
        LayoutInflater layoutInflator = LayoutInflater.from(context);

        LinearLayout view = new LinearLayout(context);
        layoutInflator.inflate(R.layout.list_item_logs, view);
        
        Drawable iconImage = context.getResources().getDrawable(mLogLevel.getDrawable());
        ImageView icon = (ImageView) view.findViewById(R.id.list_item_icon);
        icon.setImageDrawable(iconImage);
        
        TextView mainText = (TextView) view.findViewById(R.id.list_item_text);
        mainText.setText(mDescription);
        
        TextView subText = (TextView) view.findViewById(R.id.list_item_subtext);
        
        // mTime is parsed from a String for RAIDiator 4
        // if we didn't succeed in parsing the date, fall back to the original string
        if (mTime != null) {
            subText.setText(mTime.toString());
        } else {
            subText.setText(mDateString);
        }
        
        return view;
    }

}
