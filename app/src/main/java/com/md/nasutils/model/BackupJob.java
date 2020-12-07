/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author michaeldoyle
 */
public class BackupJob implements Parcelable {

    private String mBackupId;
    private String mName;
    private String mStatusDescription;
    private String mSchedule;
    private String mSource;
    private String mDestination;
    private String mStatus;
    private String mStatusDate;

    public BackupJob() {
    }

    public BackupJob(Parcel in) {
        mBackupId = in.readString();
        mName = in.readString();
        mStatusDescription = in.readString();
        mSchedule = in.readString();
        mSource = in.readString();
        mDestination = in.readString();
        mStatus = in.readString();
        mStatusDate = in.readString();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getStatusDate() {
        return mStatusDate;
    }

    public void setStatusDate(String statusDate) {
        this.mStatusDate = statusDate;
    }

    public String getStatusDescription() {
        return mStatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.mStatusDescription = statusDescription;
    }

    public String getSchedule() {
        return mSchedule;
    }

    public void setSchedule(String schedule) {
        this.mSchedule = schedule;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        this.mSource = source;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        this.mDestination = destination;
    }

    public String getBackupId() {
        return mBackupId;
    }

    public void setBackupId(String backupId) {
        this.mBackupId = backupId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mBackupId);
        out.writeString(mName);
        out.writeString(mStatusDescription);
        out.writeString(mSchedule);
        out.writeString(mSource);
        out.writeString(mDestination);
        out.writeString(mStatus);
        out.writeString(mStatusDate);
    }

    public static final Creator<BackupJob> CREATOR =
            new Creator<BackupJob>() {
                public BackupJob createFromParcel(Parcel in) {
                    return new BackupJob(in);
                }
        
                public BackupJob[] newArray(int size) {
                    return new BackupJob[size];
                }
            };

}
