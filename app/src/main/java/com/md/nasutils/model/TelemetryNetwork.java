/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TelemetryNetwork implements Parcelable{
    
    private static final BigDecimal DIVISOR_KB = new BigDecimal(1000);

    private BigInteger mId;
    private BigInteger mDownloadBytes;
    private BigInteger mUploadBytes;
    private Date mTime;
    
    public TelemetryNetwork() {
    }
    
    public TelemetryNetwork(Parcel in) {
        mId = (BigInteger) in.readSerializable();
        mDownloadBytes = (BigInteger) in.readSerializable();
        mUploadBytes = (BigInteger) in.readSerializable();
        mTime = new Date(in.readLong());
    }

    public BigInteger getId() {
        return mId;
    }
    
    public void setId(BigInteger id) {
        this.mId = id;
    }
    
    public BigInteger getDownloadBytes() {
        return mDownloadBytes;
    }
    
    public void setDownloadBytes(BigInteger downloadBytes) {
        this.mDownloadBytes = downloadBytes;
    }
    
    public BigInteger getUploadBytes() {
        return mUploadBytes;
    }
    
    public void setUploadBytes(BigInteger uploadBytes) {
        this.mUploadBytes = uploadBytes;
    }
    
    public Date getTime() {
        return mTime;
    }
    
    public void setTime(Date time) {
        this.mTime = time;
    }
    
    public BigDecimal getDownloadKb() {
        return new BigDecimal(mDownloadBytes).divide(DIVISOR_KB);
    }
    
    public BigDecimal getUploadKb() {
        return new BigDecimal(mUploadBytes).divide(DIVISOR_KB);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mId);
        out.writeSerializable(mDownloadBytes);
        out.writeSerializable(mUploadBytes);
        out.writeLong(mTime != null ? mTime.getTime() : 0);
    }

    public static final Parcelable.Creator<TelemetryNetwork> CREATOR = 
            new Parcelable.Creator<TelemetryNetwork>() {
                public TelemetryNetwork createFromParcel(Parcel in) {
                    return new TelemetryNetwork(in);
                }
        
                public TelemetryNetwork[] newArray(int size) {
                    return new TelemetryNetwork[size];
                }
            };
}
