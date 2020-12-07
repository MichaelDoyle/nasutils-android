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
@Root(name="Temperature", strict=false)
public class Temperature {
    
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
    
    @Element(name="temp_value", required=false)
    private Integer mTemperature;
    
    @Element(name="min_value", required=false)  
    private Integer mMinTemperature;
    
    @Element(name="max_value", required=false)  
    private Integer mMaxTemperature;
    
    public Temperature() {
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

    public Integer getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Integer temperature) {
        this.mTemperature = temperature;
    }

    public Integer getMinTemperature() {
        return mMinTemperature;
    }

    public void setMinTemperature(Integer minTemperature) {
        this.mMinTemperature = minTemperature;
    }

    public Integer getMaxTemperature() {
        return mMaxTemperature;
    }

    public void setMaxTemperature(Integer maxTemperature) {
        this.mMaxTemperature = maxTemperature;
    }
}
