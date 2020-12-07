/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

public class TelemetryCpu implements Parcelable {

    private BigInteger mId;
    private int mUser;
    private int mSystem;
    private int mNice;
    
    public TelemetryCpu() {
    }
    
    public TelemetryCpu(Parcel in) {
        mId = (BigInteger) in.readSerializable();
        mUser = in.readInt();
        mSystem = in.readInt();
        mNice = in.readInt();
    }

    public BigInteger getId() {
        return mId;
    }
    
    public void setId(BigInteger id) {
        this.mId = id;
    }
    
    public int getUser() {
        return mUser;
    }
    
    public void setUser(int user) {
        this.mUser = user;
    }
    
    public int getSystem() {
        return mSystem;
    }
    
    public void setSystem(int system) {
        this.mSystem = system;
    }
    
    public int getNice() {
        return mNice;
    }
    
    public void setNice(int nice) {
        this.mNice = nice;
    }
    
    public int getIdle() {
        return 100 - mNice - mSystem - mUser;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mId);
        out.writeInt(mUser);
        out.writeInt(mSystem);
        out.writeInt(mNice);
    }

    public static final Parcelable.Creator<TelemetryCpu> CREATOR = 
            new Parcelable.Creator<TelemetryCpu>() {
                public TelemetryCpu createFromParcel(Parcel in) {
                    return new TelemetryCpu(in);
                }
        
                public TelemetryCpu[] newArray(int size) {
                    return new TelemetryCpu[size];
                }
            };
}
