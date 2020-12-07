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
public class Dictionary implements Parcelable {

    private List<Language> mLanguages;
    
    public Dictionary() {
    }

    public Dictionary(Parcel in) {
        mLanguages = new ArrayList<>();
        in.readList(mLanguages, Dictionary.class.getClassLoader());
    }
    
    public List<Language> getLanguages() {
        return mLanguages;
    }

    public void setLanguages(List<Language> languages) {
        this.mLanguages = languages;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(mLanguages);
    }

    public static final Parcelable.Creator<Dictionary> CREATOR = 
            new Parcelable.Creator<Dictionary>() {
                public Dictionary createFromParcel(Parcel in) {
                    return new Dictionary(in);
                }
        
                public Dictionary[] newArray(int size) {
                    return new Dictionary[size];
                }
            };
}
