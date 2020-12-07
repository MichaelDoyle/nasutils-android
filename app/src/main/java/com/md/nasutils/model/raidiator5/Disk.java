/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Disk", strict=false)
public class Disk {
    
    @Attribute(name="id", required=false)
    private String mId;
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType;
 
    @Element(name="device_name", required=false)
    private String mDeviceName;
    
    @Element(name="device_id", required=false)
    private Integer mDeviceId;
    
    @Element(name="disk_model", required=false)
    private String mModel;
    
    @Element(name="disk_temperature", required=false)
    private Integer mTemperature;
        
    @Element(name="disk_status", required=false)
    private String mStatus;
    
    @Transient
    private int mCapacity;
    
    public Disk() {
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

    public Integer getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.mDeviceId = deviceId;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public Integer getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Integer temperature) {
        this.mTemperature = temperature;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
    
    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int capacity) {
        this.mCapacity = capacity;
    }

    /** <p>Post deserialization processing</p> */
    @Commit
    public void postDeserialization() {
        if (mModel != null) {
            parseModel();
        }
    }

    /**
     * <p>Will strip out capacity from model 
     * name, assuming the model name is in the format:</p>
     * 
     * <p>ST2000DL003-9VT166 1863 GB</p>
     */
    private void parseModel() {
        String[] splits = mModel.split(" ");
        if (splits.length-2 > 0) {
            try {
                mCapacity = Integer.parseInt(splits[splits.length-2]);
            } catch (NumberFormatException e) {
                mCapacity = 0;
                reportParseError(e);
            }
        }
    }
    
    private void reportParseError(Exception e) {
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.log("Could not parse model for disk size: " + mModel);
        crashlytics.recordException(e);
    }
}
