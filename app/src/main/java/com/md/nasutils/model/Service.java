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
public class Service implements Parcelable {
    
    private String mName;
    private boolean mIsEnabled;
    private boolean mIsEnabledOnUi;
    private Map<String, String> mOptions;
    private Map<String, Object> mMetaData;
    
    public Service() {
    }
    
    public Service(Parcel in) {
        mName = in.readString();
        mIsEnabled = in.readByte() == 1;
        mIsEnabledOnUi = in.readByte() == 1;
        
        mOptions = new HashMap<>();
        in.readMap(mOptions, getClass().getClassLoader());
        
        mMetaData = new HashMap<>();
        in.readMap(mMetaData, getClass().getClassLoader());
    }
    
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    public boolean isEnabledOnUi() {
        return mIsEnabledOnUi;
    }

    public void setEnabledOnUi(boolean isEnabledOnUi) {
        this.mIsEnabledOnUi = isEnabledOnUi;
    }

    public Map<String, String> getOptions() {
        return mOptions;
    }

    public void setOptions(Map<String, String> options) {
        this.mOptions = options;
    }

    public String getOptionByName(String name) {
        return mOptions.get(name);
    }
    
    public String setOptionByName(String name, String value) {
        return mOptions.put(name, value);
    }
    
    public Map<String, Object> getMetaData() {
        return mMetaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.mMetaData = metaData;
    }

    public Object getMetaDataByName(String name) {
        return mMetaData.get(name);
    }
    
    public Object setOptionByName(String name, Object value) {
        return mMetaData.put(name, value);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeByte((byte) (mIsEnabled ? 1 : 0));
        out.writeByte((byte) (mIsEnabledOnUi ? 1 : 0));
        out.writeMap(mOptions);
        out.writeMap(mMetaData);
    }

    public static final Parcelable.Creator<Service> CREATOR = 
            new Parcelable.Creator<Service>() {
                public Service createFromParcel(Parcel in) {
                    return new Service(in);
                }
        
                public Service[] newArray(int size) {
                    return new Service[size];
                }
            };

}
