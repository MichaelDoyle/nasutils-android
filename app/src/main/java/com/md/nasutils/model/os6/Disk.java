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
@Root(name="Disk", strict=false)
public class Disk {
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType;
    
    @Attribute(name="ATAError", required=false)
    private Integer mAtaError;
    
    @Attribute(name="Capacity", required=false)
    private Long mCapacityBytesAttribute;
    
    @Attribute(name="Channel", required=false)  
    private Integer mChannel;
    
    @Attribute(name="DiskState", required=false)
    private String mDiskState;
    
    @Attribute(name="FirmwareVersion", required=false)
    private String mFirmwareVersion;
    
    @Attribute(name="HardwareInterface", required=false)
    private String mHardwareInterface;
    
    @Attribute(name="IsSignedByNetgear", required=false)
    private Integer mIsSignedByNetgear;
    
    @Attribute(name="Model", required=false)
    private String mModelAttribute;
    
    @Attribute(name="PoolHostId", required=false)
    private String mPoolHostId;
    
    @Attribute(name="PoolName", required=false)
    private String mPoolName;
    
    @Attribute(name="PoolState", required=false)
    private String mPoolState;
    
    @Attribute(name="PoolType", required=false)
    private String mPoolType;
    
    @Attribute(name="Present", required=false)
    private Integer mPresent;
    
    @Attribute(name="RPM", required=false)
    private Integer mRpm;
    
    @Attribute(name="Sectors", required=false)
    private Long mSectors;
    
    @Attribute(name="Serial", required=false)
    private String mSerialNumber;

    @Attribute(name="SlotName", required=false)
    private String mSlotName;
    
    @Attribute(name="Temperature", required=false)
    private Integer mTemperatureAttribute;
    
    @Element(name="device_name", required=false)
    private String mDeviceName;
    
    @Element(name="device_id", required=false)
    private String mDeviceId;
    
    @Element(name="disk_model", required=false)
    private String mModel;
    
    @Element(name="disk_temperature", required=false)
    private Integer mTemperature;
    
    @Element(name="disk_capacity", required=false)
    private Long mCapacityBytes;
    
    @Element(name="disk_status", required=false)
    private String mStatus;
    
    public Disk() {
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

    public Integer getAtaError() {
        return mAtaError;
    }

    public void setAtaError(Integer ataError) {
        this.mAtaError = ataError;
    }

    public Long getCapacityBytesAttribute() {
        return mCapacityBytesAttribute;
    }

    public void setCapacityBytesAttribute(Long capacityBytesAttribute) {
        this.mCapacityBytesAttribute = capacityBytesAttribute;
    }

    public Integer getChannel() {
        return mChannel;
    }

    public void setChannel(Integer channel) {
        this.mChannel = channel;
    }

    public String getDiskState() {
        return mDiskState;
    }

    public void setDiskState(String diskState) {
        this.mDiskState = diskState;
    }

    public String getFirmwareVersion() {
        return mFirmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.mFirmwareVersion = firmwareVersion;
    }

    public String getHardwareInterface() {
        return mHardwareInterface;
    }

    public void setHardwareInterface(String hardwareInterface) {
        this.mHardwareInterface = hardwareInterface;
    }

    public Integer getIsSignedByNetgear() {
        return mIsSignedByNetgear;
    }

    public void setIsSignedByNetgear(Integer isSignedByNetgear) {
        this.mIsSignedByNetgear = isSignedByNetgear;
    }

    public String getModelAttribute() {
        return mModelAttribute;
    }

    public void setModelAttribute(String modelAttribute) {
        this.mModelAttribute = modelAttribute;
    }

    public String getPoolHostId() {
        return mPoolHostId;
    }

    public void setPoolHostId(String poolHostId) {
        this.mPoolHostId = poolHostId;
    }

    public String getPoolName() {
        return mPoolName;
    }

    public void setPoolName(String poolName) {
        this.mPoolName = poolName;
    }

    public String getPoolState() {
        return mPoolState;
    }

    public void setPoolState(String poolState) {
        this.mPoolState = poolState;
    }

    public String getPoolType() {
        return mPoolType;
    }

    public void setPoolType(String poolType) {
        this.mPoolType = poolType;
    }

    public Integer getPresent() {
        return mPresent;
    }

    public void setPresent(Integer present) {
        this.mPresent = present;
    }

    public Integer getRpm() {
        return mRpm;
    }

    public void setRpm(Integer rpm) {
        this.mRpm = rpm;
    }

    public Long getSectors() {
        return mSectors;
    }

    public void setSectors(Long sectors) {
        this.mSectors = sectors;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.mSerialNumber = serialNumber;
    }

    public String getSlotName() {
        return mSlotName;
    }

    public void setSlotName(String slotName) {
        this.mSlotName = slotName;
    }

    public Integer getTemperatureAttribute() {
        return mTemperatureAttribute;
    }

    public void setTemperatureAttribute(Integer temperatureAttribute) {
        this.mTemperatureAttribute = temperatureAttribute;
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

    public Long getCapacityBytes() {
        return mCapacityBytes;
    }

    public void setCapacityBytes(Long capacityBytes) {
        this.mCapacityBytes = capacityBytes;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
}
