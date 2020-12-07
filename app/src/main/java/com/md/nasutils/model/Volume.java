/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author michaeldoyle
 */
public class Volume implements Parcelable {

    private String mName;
    private String mStatus;
    private String mAutoExpand;
    private String mAutoReplace;
    private String mDelegation;
    private String mListSnapshots;
    private int mVersion;
    private double mAllocatedGb;
    private double mCapacityGb;
    private double mFreeGb;
    private double mAvailableGb;
    private String mGuid;
    private String mHealth;
    private String mChecksum;
    private String mRaidLevel;
    private long mSnapshotSize;
    private int mPercentageUsed;
    private String mUsageText;
    
    private List<Disk> mDisks;

    public Volume() {
    }

    public Volume(Parcel in) {
        mName = in.readString();
        mStatus = in.readString();
        mAutoExpand = in.readString();
        mAutoReplace = in.readString();
        mDelegation = in.readString();
        mListSnapshots = in.readString();
        mVersion = in.readInt();
        mAllocatedGb = in.readDouble();
        mCapacityGb = in.readDouble();
        mFreeGb = in.readDouble();
        mAvailableGb = in.readDouble();
        mGuid = in.readString();
        mHealth = in.readString();
        mChecksum = in.readString();
        mSnapshotSize = in.readLong();
        mPercentageUsed = in.readInt();
        mUsageText = in.readString();
    }
    
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getAutoExpand() {
        return mAutoExpand;
    }

    public void setAutoExpand(String autoExpand) {
        this.mAutoExpand = autoExpand;
    }

    public String getAutoReplace() {
        return mAutoReplace;
    }

    public void setAutoReplace(String autoReplace) {
        this.mAutoReplace = autoReplace;
    }

    public String getDelegation() {
        return mDelegation;
    }

    public void setDelegation(String delegation) {
        this.mDelegation = delegation;
    }

    public String getListsnapshots() {
        return mListSnapshots;
    }

    public void setListsnapshots(String listsnapshots) {
        this.mListSnapshots = listsnapshots;
    }

    public int getVersion() {
        return mVersion;
    }

    public void setVersion(int version) {
        this.mVersion = version;
    }

    public double getAllocatedGb() {
        return mAllocatedGb;
    }

    public void setAllocatedGb(double allocatedGb) {
        this.mAllocatedGb = allocatedGb;
    }

    public double getCapacityGb() {
        return mCapacityGb;
    }

    public void setCapacityGb(double capacityGb) {
        this.mCapacityGb = capacityGb;
    }

    public double getFreeGb() {
        return mFreeGb;
    }

    public void setFreeGb(double freeGb) {
        this.mFreeGb = freeGb;
    }

    public double getAvailableGb() {
        return mAvailableGb;
    }

    public void setAvailableGb(double availableGb) {
        this.mAvailableGb = availableGb;
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String guid) {
        this.mGuid = guid;
    }

    public String getHealth() {
        return mHealth;
    }

    public void setHealth(String health) {
        this.mHealth = health;
    }

    public String getChecksum() {
        return mChecksum;
    }

    public void setChecksum(String checksum) {
        this.mChecksum = checksum;
    }

    public String getRaidLevel() {
        return mRaidLevel;
    }

    public void setRaidLevel(String raidLevel) {
        this.mRaidLevel = raidLevel;
    }

    public List<Disk> getDisks() {
        return mDisks;
    }

    public void setDisks(List<Disk> disks) {
        this.mDisks = disks;
    }

    public long getSnapshotSize() {
        return mSnapshotSize;
    }

    public void setSnapshotSize(long snapshotSize) {
        this.mSnapshotSize = snapshotSize;
    }

    public int getPercentageUsed() {        
        if (mPercentageUsed == 0) {
            if (mCapacityGb > 0) {
                mPercentageUsed = (int) ((mCapacityGb - mFreeGb) / mCapacityGb);
            }
        }
        
        return mPercentageUsed;
    }

    public String getUsageText() {
        return mUsageText;
    }

    public void setUsageText(String usageText) {
        this.mUsageText = usageText;
    }

    public void setPercentageUsed(int percentageUsed) {
        this.mPercentageUsed = percentageUsed;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeString(mStatus);
        out.writeString(mAutoExpand);
        out.writeString(mAutoReplace);
        out.writeString(mDelegation);
        out.writeString(mListSnapshots);
        out.writeInt(mVersion);
        out.writeDouble(mAllocatedGb);
        out.writeDouble(mCapacityGb);
        out.writeDouble(mFreeGb);
        out.writeDouble(mAvailableGb);
        out.writeString(mGuid);
        out.writeString(mHealth);
        out.writeString(mChecksum);
        out.writeLong(mSnapshotSize);
        out.writeInt(mPercentageUsed);
        out.writeString(mUsageText);
    }

    public static final Parcelable.Creator<Volume> CREATOR = 
            new Parcelable.Creator<Volume>() {
                public Volume createFromParcel(Parcel in) {
                    return new Volume(in);
                }
        
                public Volume[] newArray(int size) {
                    return new Volume[size];
                }
            };
}
