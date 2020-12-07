/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/** 
 * @author michaeldoyle
 */
public class Logs implements Parcelable  {
    
    private List<Log> mLogs;

    public Logs() {
    }

    public Logs(Parcel in) {
        mLogs = new ArrayList<>();
        in.readList(mLogs, Logs.class.getClassLoader());
    }
    
    public List<Log> getLogs() {
        return mLogs;
    }
    
    public void setLogs(List<Log> logs) {
        this.mLogs = logs;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(mLogs);
    }

    public static final Parcelable.Creator<Logs> CREATOR = 
            new Parcelable.Creator<Logs>() {
                public Logs createFromParcel(Parcel in) {
                    return new Logs(in);
                }

                public Logs[] newArray(int size) {
                    return new Logs[size];
                }
            };

}
