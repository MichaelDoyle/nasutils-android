/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author michaeldoyle
 */
public class Nas implements Parcelable {

    private String mHostname;
    private String mSerialNumber;
    private String mModel;
    private String mArchitecture;
    private String mFirmware;
    private String mMemory;
    
    private List<NetworkInterface> mNetworkInterfaces;
    private List<Enclosure> mEnclosures;
    private List<Ups> mUps;
    private List<Volume> mVolumes;
    private List<BackupJob> mBackupJobs;

    private Map<String, AddOn> mAddOns;
    private Map<String, Service> mServices;
    
    public Nas() {
    }
    
    public Nas(Parcel in) {
        mHostname = in.readString();
        mSerialNumber = in.readString();
        mModel = in.readString();
        mArchitecture = in.readString();
        mFirmware = in.readString();
        mMemory = in.readString();
        
        mNetworkInterfaces = new ArrayList<>();
        in.readList(mNetworkInterfaces, Nas.class.getClassLoader());
        
        mEnclosures = new ArrayList<>();
        in.readList(mEnclosures, Nas.class.getClassLoader());
        
        mVolumes = new ArrayList<>();
        in.readList(mVolumes, Nas.class.getClassLoader());
        
        mUps = new ArrayList<>();
        in.readList(mUps, Nas.class.getClassLoader());
        
        mServices = new HashMap<>();
        in.readMap(mServices, Nas.class.getClassLoader());

        mBackupJobs = new ArrayList<>();
        in.readList(mBackupJobs, Nas.class.getClassLoader());

        mAddOns = new HashMap<>();
        in.readMap(mAddOns, Nas.class.getClassLoader());
    }

    public String getHostname() {
        return mHostname;
    }

    public void setHostname(String hostname) {
        this.mHostname = hostname;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.mSerialNumber = serialNumber;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getArchitecture() {
        return mArchitecture;
    }

    public void setArchitecture(String architecture) {
        this.mArchitecture = architecture;
    }

    public String getFirmware() {
        return mFirmware;
    }

    public void setFirmware(String firmware) {
        this.mFirmware = firmware;
    }

    public String getMemory() {
        return mMemory;
    }

    public void setMemory(String memory) {
        this.mMemory = memory;
    }
    
    public List<NetworkInterface> getNetworkInterfaces() {
        return mNetworkInterfaces;
    }

    public void setNetworkInterfaces(List<NetworkInterface> networkInterfaces) {
        this.mNetworkInterfaces = networkInterfaces;
    }
    
    public List<Enclosure> getEnclosures() {
        return mEnclosures;
    }

    public void setEnclosures(List<Enclosure> enclosures) {
        this.mEnclosures = enclosures;
    }

    public List<Volume> getVolumes() {
        return mVolumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.mVolumes = volumes;
    }
    
    public List<Ups> getUps() {
        return mUps;
    }

    public void setUps(List<Ups> ups) {
        this.mUps = ups;
    }

    public List<BackupJob> getBackupJobs() {
        return mBackupJobs;
    }

    public void setBackupJobs(List<BackupJob> backupJobs) {
        this.mBackupJobs = backupJobs;
    }

    public Map<String, AddOn> getAddOns() {
        return mAddOns;
    }

    public void setAddOns(Map<String, AddOn> addOns) {
        this.mAddOns = addOns;
    }
    
    public Map<String, Service> getServices() {
        return mServices;
    }

    public void setServices(Map<String, Service> services) {
        this.mServices = services;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mHostname);
        out.writeString(mSerialNumber);
        out.writeString(mModel);
        out.writeString(mArchitecture);
        out.writeString(mFirmware);
        out.writeString(mMemory);
        out.writeList(mNetworkInterfaces);
        out.writeList(mEnclosures);
        out.writeList(mVolumes);
        out.writeList(mUps);
        out.writeMap(mServices);
        out.writeList(mBackupJobs);
        out.writeMap(mAddOns);
    }

    public static final Parcelable.Creator<Nas> CREATOR = 
            new Parcelable.Creator<Nas>() {
                public Nas createFromParcel(Parcel in) {
                    return new Nas(in);
                }
        
                public Nas[] newArray(int size) {
                    return new Nas[size];
                }
            };
}
