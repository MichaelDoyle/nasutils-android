/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author michaeldoyle
 */
public class Fan extends Device implements Parcelable {
    
    private int mSpeed;
    private int mSpeedMin;
    private int mSpeedMax;
    
    public Fan() {
    }
    
    public Fan(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLabel = in.readString();
        mStatus = in.readString();
        mSpeed = in.readInt();
        mSpeedMin = in.readInt();
        mSpeedMax = in.readInt();

    }
    
    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    public int getSpeedMin() {
        return mSpeedMin;
    }

    public void setSpeedMin(int speedMin) {
        this.mSpeedMin = speedMin;
    }

    public int getSpeedMax() {
        return mSpeedMax;
    }

    public void setSpeedMax(int speedMax) {
        this.mSpeedMax = speedMax;
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
        out.writeInt(mSpeed);
        out.writeInt(mSpeedMin);
        out.writeInt(mSpeedMax);
    }

    public static final Parcelable.Creator<Fan> CREATOR = 
            new Parcelable.Creator<Fan>() {
                public Fan createFromParcel(Parcel in) {
                    return new Fan(in);
                }
        
                public Fan[] newArray(int size) {
                    return new Fan[size];
                }
            };
}
