/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michaeldoyle
 */
public class Enclosure implements Parcelable {
    
    private List<Disk> mDisks;
    private List<Fan> mFans;
    private List<Temperature> mTemperatures;

    public Enclosure() {
    }
    
    public Enclosure(Parcel in) {
        
        mDisks = new ArrayList<>();
        in.readList(mDisks, Enclosure.class.getClassLoader());
        
        mFans = new ArrayList<>();
        in.readList(mFans, Enclosure.class.getClassLoader());
        
        mTemperatures = new ArrayList<>();
        in.readList(mTemperatures, Enclosure.class.getClassLoader());
    }
    
    public List<Disk> getDisks() {
        return mDisks;
    }

    public void setDisks(List<Disk> disks) {
        this.mDisks = disks;
    }

    public List<Fan> getFans() {
        return mFans;
    }

    public void setFans(List<Fan> fans) {
        this.mFans = fans;
    }

    public List<Temperature> getTemperatures() {
        return mTemperatures;
    }

    public void setTemperatures(List<Temperature> temperatures) {
        this.mTemperatures = temperatures;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(mDisks);
        out.writeList(mFans);
        out.writeList(mTemperatures);
    }

    public static final Parcelable.Creator<Enclosure> CREATOR = 
            new Parcelable.Creator<Enclosure>() {
                public Enclosure createFromParcel(Parcel in) {
                    return new Enclosure(in);
                }
        
                public Enclosure[] newArray(int size) {
                    return new Enclosure[size];
                }
            };
}
