/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Fan", strict=false)
public class Fan {

    @Attribute(name="id", required=false)
    private String mId;
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType; 
        
    @Element(name="device_name", required=false)
    private String mDeviceName;
    
    @Element(name="device_id", required=false)
    private String mDeviceId;
    
    @Element(name="fan_status", required=false)
    private String mStatus;
    
    @Element(name="fan_speed", required=false)
    private Integer mSpeed;
    
    public Fan() {
        // necessary to keep xml deserialization happy
    }

    public String getId() {
        return mResourceId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        this.mDeviceId = deviceId;
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
}
