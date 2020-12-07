/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Fan", strict=false)
public class Fan {
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType; 
    
    @Attribute(name="normal-min", required=false)   
    private Integer mNormalMin;
    
    @Attribute(name="normal-max", required=false)   
    private Integer mNormalMax;
    
    @Element(name="device_name", required=false)
    private String mDeviceName;
    
    @Element(name="device_id", required=false)
    private String mDeviceId;
    
    @Element(name="label", required=false)
    private String mLabel;
    
    @Element(name="value", required=false)
    private Integer mValue;
    
    @Element(name="status", required=false)
    private String mStatus;
    
    @Element(name="fan_speed", required=false)
    private Integer mSpeed;
    
    @Element(name="fan_status", required=false)
    private String mFanStatus;
    
    public Fan() {
        // necessary to keep xml deserialization happy
    }

    public String getResourceId() {
        return mResourceId;
    }

    public void setResourceId(String resourceId) {
        this.mResourceId = resourceId;
    }

    public String getResourceType() {
        return mResourceType;
    }

    public void setResourceType(String resourceType) {
        this.mResourceType = resourceType;
    }

    public Integer getNormalMin() {
        return mNormalMin;
    }

    public void setNormalMin(Integer normalMin) {
        this.mNormalMin = normalMin;
    }

    public Integer getNormalMax() {
        return mNormalMax;
    }

    public void setNormalMax(Integer normalMax) {
        this.mNormalMax = normalMax;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        this.mDeviceId = deviceId;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public Integer getValue() {
        return mValue;
    }

    public void setValue(Integer value) {
        this.mValue = value;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public Integer getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Integer speed) {
        this.mSpeed = speed;
    }

    public String getFanStatus() {
        return mFanStatus;
    }

    public void setFanStatus(String fanStatus) {
        this.mFanStatus = fanStatus;
    }
}
