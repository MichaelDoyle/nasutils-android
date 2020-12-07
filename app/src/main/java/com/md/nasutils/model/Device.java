/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

/**
 * @author michaeldoyle
 */
public abstract class Device {
    
    protected String mId;
    protected String mName;
    protected String mLabel;
    protected String mStatus;
    
    public Device() {
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

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
}
