/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author michaeldoyle
 */
public class AddOn implements Parcelable {

    private String mId;
    private String mName;
    private String mServiceName;
    private String mVersion;
    private String mStatus;

    public AddOn() {
    }

    public AddOn(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mServiceName = in.readString();
        mVersion = in.readString();
        mStatus = in.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        this.mServiceName = serviceName;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
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
        out.writeString(mId);
        out.writeString(mName);
        out.writeString(mServiceName);
        out.writeString(mVersion);
        out.writeString(mStatus);
    }

    public static final Creator<AddOn> CREATOR =
            new Creator<AddOn>() {
                public AddOn createFromParcel(Parcel in) {
                    return new AddOn(in);
                }
        
                public AddOn[] newArray(int size) {
                    return new AddOn[size];
                }
            };

}
