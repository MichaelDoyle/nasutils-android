/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author michaeldoyle
 */
public class Temperature extends Device implements Parcelable {
    
    private int mTemperature;
    private int mTemperatureMin;
    private int mTemperatureMax;

    public Temperature() {
    }

    public Temperature(Parcel in) {     
        mId = in.readString();
        mName = in.readString();
        mLabel = in.readString();
        mStatus = in.readString();
        mTemperature = in.readInt();
        mTemperatureMin = in.readInt();
        mTemperatureMax = in.readInt();
    }
    
    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(int temperature) {
        this.mTemperature = temperature;
    }

    public int getTemperatureMin() {
        return mTemperatureMin;
    }

    public void setTemperatureMin(int temperatureMin) {
        this.mTemperatureMin = temperatureMin;
    }

    public int getTemperatureMax() {
        return mTemperatureMax;
    }

    public void setTemperatureMax(int temperatureMax) {
        this.mTemperatureMax = temperatureMax;
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
        out.writeInt(mTemperature);
        out.writeInt(mTemperatureMin);
        out.writeInt(mTemperatureMax);
    }

    public static final Parcelable.Creator<Temperature> CREATOR = 
            new Parcelable.Creator<Temperature>() {
                public Temperature createFromParcel(Parcel in) {
                    return new Temperature(in);
                }
        
                public Temperature[] newArray(int size) {
                    return new Temperature[size];
                }
            };
}
