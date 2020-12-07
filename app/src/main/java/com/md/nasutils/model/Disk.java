/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author michaeldoyle
 */
public class Disk extends Device implements Parcelable {

    private String mModel;
    private int mTemperature;
    private int mTemperatureMin;
    private int mTemperatureMax;
    private double mCapacityGb;
    private int mChannel;
    private String mFirmwareVersion;
    private String mSerialNumber;
    
    private Map<String, String> mSmartAttributes;
    
    public Disk() {
    }
    
    public Disk(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLabel = in.readString();
        mStatus = in.readString();
        mModel = in.readString();
        mTemperature = in.readInt();
        mTemperatureMin = in.readInt();
        mTemperatureMax = in.readInt();
        mCapacityGb = in.readDouble();
        mChannel = in.readInt();
        mFirmwareVersion = in.readString();
        mSerialNumber = in.readString();

        mSmartAttributes = new HashMap<>();
        in.readMap(mSmartAttributes, Disk.class.getClassLoader());
    }
    
    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
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

    public double getCapacityGb() {
        return mCapacityGb;
    }

    public void setCapacityGb(double capacityGb) {
        this.mCapacityGb = capacityGb;
    }

    public int getChannel() {
        return mChannel;
    }

    public void setChannel(int channel) {
        this.mChannel = channel;
    }

    public String getFirmwareVersion() {
        return mFirmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.mFirmwareVersion = firmwareVersion;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.mSerialNumber = serialNumber;
    }

    public Map<String, String> getSmartAttributes() {
        return mSmartAttributes;
    }

    public void setSmartAttributes(Map<String, String> smartAttributes) {
        this.mSmartAttributes = smartAttributes;
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
        out.writeInt(mTemperature);
        out.writeInt(mTemperatureMin);
        out.writeInt(mTemperatureMax);
        out.writeDouble(mCapacityGb);
        out.writeInt(mChannel);
        out.writeString(mFirmwareVersion);
        out.writeString(mSerialNumber);
        out.writeMap(mSmartAttributes);
    }

    public static final Parcelable.Creator<Disk> CREATOR = 
            new Parcelable.Creator<Disk>() {
                public Disk createFromParcel(Parcel in) {
                    return new Disk(in);
                }
        
                public Disk[] newArray(int size) {
                    return new Disk[size];
                }
            };
}
