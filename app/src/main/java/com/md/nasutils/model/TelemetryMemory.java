/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

public class TelemetryMemory implements Parcelable {

    private BigInteger mWired;
    private BigInteger mActive;
    private BigInteger mInactive;
    private BigInteger mFree;
    private BigInteger mTotal;
    private BigInteger mSwapUsed;
    private BigInteger mSwapTotal;
    private BigInteger mPageIns;
    private BigInteger mPageOuts;
    
    public TelemetryMemory() {
    }

    public TelemetryMemory(Parcel in) {
        mWired = (BigInteger) in.readSerializable();
        mActive = (BigInteger) in.readSerializable();
        mInactive = (BigInteger) in.readSerializable();
        mFree = (BigInteger) in.readSerializable();
        mTotal = (BigInteger) in.readSerializable();
        mSwapUsed = (BigInteger) in.readSerializable();
        mSwapTotal = (BigInteger) in.readSerializable();
        mPageIns = (BigInteger) in.readSerializable();
        mPageOuts = (BigInteger) in.readSerializable();
    }

    public BigInteger getWired() {
        return mWired;
    }

    public void setWired(BigInteger wired) {
        this.mWired = wired;
    }

    public BigInteger getActive() {
        return mActive;
    }

    public void setActive(BigInteger active) {
        this.mActive = active;
    }

    public BigInteger getInactive() {
        return mInactive;
    }

    public void setInactive(BigInteger inactive) {
        this.mInactive = inactive;
    }

    public BigInteger getFree() {
        return mFree;
    }

    public void setFree(BigInteger free) {
        this.mFree = free;
    }

    public BigInteger getTotal() {
        return mTotal;
    }

    public void setTotal(BigInteger total) {
        this.mTotal = total;
    }

    public BigInteger getSwapUsed() {
        return mSwapUsed;
    }

    public void setSwapUsed(BigInteger swapUsed) {
        this.mSwapUsed = swapUsed;
    }

    public BigInteger getSwapTotal() {
        return mSwapTotal;
    }

    public void setSwapTotal(BigInteger swapTotal) {
        this.mSwapTotal = swapTotal;
    }

    public BigInteger getPageIns() {
        return mPageIns;
    }

    public void setPageIns(BigInteger pageIns) {
        this.mPageIns = pageIns;
    }

    public BigInteger getPageOuts() {
        return mPageOuts;
    }

    public void setPageOuts(BigInteger pageOuts) {
        this.mPageOuts = pageOuts;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mWired);
        out.writeSerializable(mActive);
        out.writeSerializable(mInactive);
        out.writeSerializable(mFree);
        out.writeSerializable(mTotal);
        out.writeSerializable(mSwapUsed);
        out.writeSerializable(mSwapTotal);
        out.writeSerializable(mPageIns);
        out.writeSerializable(mPageOuts);
    }

    public static final Parcelable.Creator<TelemetryMemory> CREATOR = 
            new Parcelable.Creator<TelemetryMemory>() {
                public TelemetryMemory createFromParcel(Parcel in) {
                    return new TelemetryMemory(in);
                }
        
                public TelemetryMemory[] newArray(int size) {
                    return new TelemetryMemory[size];
                }
            };
}
