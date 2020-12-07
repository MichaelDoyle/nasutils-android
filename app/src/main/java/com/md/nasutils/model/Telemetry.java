/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Telemetry implements Parcelable {

    private long mRequestId;
    private long mUptime;
    private BigInteger mDiskSid;
    private BigInteger mTempSid;
    private BigInteger mFanSid;
    
    private boolean mAuthorized;

    private TelemetryLoad mLoad;
    private TelemetryMemory mMemory;
    
    private List<TelemetryCpu> mCpuTelemetry;
    private List<TelemetryNetwork> mNetworkTelemetry;
    
    public Telemetry() {
    }
    
    public Telemetry(Parcel in) {
        mRequestId = in.readLong();
        mUptime = in.readLong();
        mDiskSid = (BigInteger) in.readSerializable();
        mTempSid = (BigInteger) in.readSerializable();
        mFanSid = (BigInteger) in.readSerializable();

        mLoad = in.readParcelable(Telemetry.class.getClassLoader());
        mMemory = in.readParcelable(Telemetry.class.getClassLoader());
        
        mCpuTelemetry = new ArrayList<>();
        in.readList(mCpuTelemetry, Telemetry.class.getClassLoader());
        
        mNetworkTelemetry = new ArrayList<>();
        in.readList(mNetworkTelemetry, Telemetry.class.getClassLoader());
        
        mAuthorized = in.readByte() == 1;
    }

    public long getRequestId() {
        return mRequestId;
    }
    
    public void setRequestId(long requestId) {
        this.mRequestId = requestId;
    }
    
    public long getUptime() {
        return mUptime;
    }
    
    public void setUptime(long uptime) {
        this.mUptime = uptime;
    }
    
    public BigInteger getDiskSid() {
        return mDiskSid;
    }
    
    public void setDiskSid(BigInteger diskSid) {
        this.mDiskSid = diskSid;
    }
    
    public BigInteger getTempSid() {
        return mTempSid;
    }
    
    public void setTempSid(BigInteger tempSid) {
        this.mTempSid = tempSid;
    }
    
    public BigInteger getFanSid() {
        return mFanSid;
    }
    
    public void setFanSid(BigInteger fanSid) {
        this.mFanSid = fanSid;
    }
    
    public TelemetryLoad getLoad() {
        return mLoad;
    }
    
    public void setLoad(TelemetryLoad load) {
        this.mLoad = load;
    }
    
    public TelemetryMemory getMemory() {
        return mMemory;
    }
    
    public void setMemory(TelemetryMemory memory) {
        this.mMemory = memory;
    }
    
    public List<TelemetryCpu> getCpuTelemetry() {
        return mCpuTelemetry;
    }
    
    public void setCpuTelemetry(List<TelemetryCpu> cpuTelemetry) {
        this.mCpuTelemetry = cpuTelemetry;
    }
    
    public List<TelemetryNetwork> getNetworkTelemetry() {
        return mNetworkTelemetry;
    }
    
    public void setNetworkTelemetry(List<TelemetryNetwork> networkTelemetry) {
        this.mNetworkTelemetry = networkTelemetry;
    }
        
    public boolean isAuthorized() {
        return mAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        this.mAuthorized = authorized;
    }

    public String getDisplayUptime() {
        long c0 = 1L;
        long c1 = c0 * 1000L;
        long c2 = c1 * 1000L;
        long c3 = c2 * 1000L;
        long c4 = c3 * 60L;
        long c5 = c4 * 60L;
        long c6 = c5 * 24L;
        
        long day = mUptime / (c6 / c3);
        long hours = mUptime / (c5 / c3) - (day * 24);
        long minute = mUptime / (c4 / c3) - (mUptime / (c5 / c3) * 60);
        
        return day + " day(s), " + hours + " hour(s), " + minute + " minute(s)";
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(mRequestId);
        out.writeLong(mUptime);
        out.writeSerializable(mDiskSid);
        out.writeSerializable(mTempSid);
        out.writeSerializable(mFanSid);
        out.writeParcelable(mLoad, flags);
        out.writeParcelable(mMemory, flags);
        out.writeList(mCpuTelemetry);
        out.writeList(mNetworkTelemetry);
        out.writeByte((byte) (mAuthorized ? 1 : 0));
    }

    public static final Parcelable.Creator<Telemetry> CREATOR = 
            new Parcelable.Creator<Telemetry>() {
                public Telemetry createFromParcel(Parcel in) {
                    return new Telemetry(in);
                }
        
                public Telemetry[] newArray(int size) {
                    return new Telemetry[size];
                }
            };
}
