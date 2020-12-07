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
@Root(name="UPS", strict=false)
public class Ups {
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType; 
    
    @Element(name="device_name", required=false)
    private String mDeviceName;
    
    @Element(name="ups_model", required=false)
    private String mModel;

    @Element(name="ups_status", required=false)
    private String mStatus;
    
    @Element(name="ups_raw_status", required=false)
    private String mRawStatus;
    
    @Element(name="battery_charge", required=false)
    private String mBatteryCharge;
    
    @Element(name="battery_runtime", required=false)
    private String mBatteryRuntime;
    
    public Ups() {
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

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getRawStatus() {
        return mRawStatus;
    }

    public void setRawStatus(String rawStatus) {
        this.mRawStatus = rawStatus;
    }
    
    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getBatteryCharge() {
        return mBatteryCharge;
    }

    public void setBatteryCharge(String batteryCharge) {
        this.mBatteryCharge = batteryCharge;
    }

    public String getBatteryRuntime() {
        return mBatteryRuntime;
    }

    public void setBatteryRuntime(String batteryRuntime) {
        this.mBatteryRuntime = batteryRuntime;
    }
}
