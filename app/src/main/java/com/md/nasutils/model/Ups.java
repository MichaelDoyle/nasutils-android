/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author michaeldoyle
 */
public class Ups extends Device implements Parcelable {
    
    private String mModel;
    private String mBatteryCharge;
    private String mBatteryRuntime;

    public Ups() {
    }
    
    public Ups(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLabel = in.readString();
        mStatus = in.readString();
        mModel = in.readString();
        mBatteryCharge = in.readString();
        mBatteryRuntime = in.readString();
    }
    
    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }
    
    public String getBatteryCharge() {
        return mBatteryCharge;
    }

    public void setBatteryCharge(String batteryCharge) {
        this.mBatteryCharge = batteryCharge;
    }

    public String getBatteryRuntime() {
        return mBatteryRuntime;
    }

    public void setBatteryRuntime(String batteryRuntime) {
        this.mBatteryRuntime = batteryRuntime;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mId);
        out.writeString(mName);
        out.writeString(mLabel);
        out.writeString(mLabel);
        out.writeString(mModel);
        out.writeString(mBatteryCharge);
        out.writeString(mBatteryRuntime);
    }

    public static final Parcelable.Creator<Ups> CREATOR = 
            new Parcelable.Creator<Ups>() {
                public Ups createFromParcel(Parcel in) {
                    return new Ups(in);
                }
        
                public Ups[] newArray(int size) {
                    return new Ups[size];
                }
            };
}
