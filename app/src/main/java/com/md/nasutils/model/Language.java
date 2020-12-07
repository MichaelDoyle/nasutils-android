/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author michaeldoyle
 */
public class Language implements Parcelable{

    private String mCode;
    private String mName;
    private Map<String, String> mItems;

    public Language() {
    }

    public Language(Parcel in) {
        mCode = in.readString();
        mName = in.readString();
        mItems = new HashMap<>();
        in.readMap(mItems, Language.class.getClassLoader());
    }
    
    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        this.mCode = code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Map<String, String> getItems() {
        return mItems;
    }

    public void setItems(Map<String, String> items) {
        this.mItems = items;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mCode);
        out.writeString(mName);
        out.writeMap(mItems);
    }

    public static final Parcelable.Creator<Language> CREATOR = 
            new Parcelable.Creator<Language>() {
                public Language createFromParcel(Parcel in) {
                    return new Language(in);
                }
        
                public Language[] newArray(int size) {
                    return new Language[size];
                }
            };
}
