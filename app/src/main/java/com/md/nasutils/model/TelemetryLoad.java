/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TelemetryLoad implements Parcelable{

    private double mOneMinuteAverage;
    private double mFiveMinuteAverage;
    private double mFifteenMinuteAverage;

    public TelemetryLoad() {
    }

    public TelemetryLoad(Parcel in) {
        mOneMinuteAverage = in.readDouble();
        mFiveMinuteAverage = in.readDouble();
        mFifteenMinuteAverage = in.readDouble();
    }
    
    public double getOneMinuteAverage() {
        return mOneMinuteAverage;
    }

    public void setOneMinuteAverage(double oneMinuteAverage) {
        this.mOneMinuteAverage = oneMinuteAverage;
    }

    public double getFiveMinuteAverage() {
        return mFiveMinuteAverage;
    }

    public void setFiveMinuteAverage(double fiveMinuteAverage) {
        this.mFiveMinuteAverage = fiveMinuteAverage;
    }

    public double getFifteenMinuteAverage() {
        return mFifteenMinuteAverage;
    }

    public void setFifteenMinuteAverage(double fifteenMinuteAverage) {
        this.mFifteenMinuteAverage = fifteenMinuteAverage;
    }
    
    public String getDisplayAverages() {
        return mOneMinuteAverage + ", " + mFiveMinuteAverage + ", " + mFifteenMinuteAverage;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(mOneMinuteAverage);
        out.writeDouble(mFiveMinuteAverage);
        out.writeDouble(mFifteenMinuteAverage);
    }

    public static final Parcelable.Creator<TelemetryLoad> CREATOR = 
            new Parcelable.Creator<TelemetryLoad>() {
                public TelemetryLoad createFromParcel(Parcel in) {
                    return new TelemetryLoad(in);
                }
        
                public TelemetryLoad[] newArray(int size) {
                    return new TelemetryLoad[size];
                }
            };
}
