/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="SystemInfo")
public class SystemInfo implements ResultData {

    @Element(name="Model", required=false)
    private String mModel;
    
    @Element(name="Serial", required=false) 
    private String mSerial;
    
    @Element(name="Firmware_Name", required=false)  
    private String mFirmwareName;   
    
    @Element(name="Firmware_Version", required=false)   
    private String mFirmwareVersion;    
    
    @Element(name="Memory", required=false) 
    private Integer mMemory;    
    
    @Element(name="CPU_Number", required=false) 
    private Integer mNumberOfCpus;      
    
    @Element(name="CPU_Frequency", required=false)  
    private double mCpuFrequency;       
    
    @Element(name="MAC_Address", required=false)    
    private String mMacAddress;         
    
    @Element(name="Language", required=false)   
    private String mLanguage;
    
    @Element(name="Raid_Level", required=false) 
    private String mRaidLevel;
    
    @Element(name="HDD_Vendor", required=false) 
    private String mHddVendor;  
    
    @Element(name="HDD_Model", required=false)  
    private String mHddModel;
    
    @Element(name="HDD_Serial", required=false) 
    private String mHddSerial;
    
    @Element(name="HDD_Firmware", required=false)   
    private String mHddFirmware;
    
    @Element(name="Registered", required=false) 
    private Integer mRegistered;    
    
    @Element(name="Raid", required=false)   
    private String mRaid;   
    
    @Element(name="Anti-Virus-Def-Version", required=false) 
    private String mAntiVirusDefVersion;    
    
    @Element(name="Anti-Virus-Last-Updated", required=false)    
    private Long mAntiVirusLastUpdatedTimestamp;    
    
    @Element(name="Read-Only-Data-Volumes", required=false) 
    private Integer mReadOnlyDataVolumes;   
    
    public SystemInfo() {
        // necessary to keep xml deserialization happy
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getSerial() {
        return mSerial;
    }

    public void setSerial(String serial) {
        this.mSerial = serial;
    }

    public String getFirmwareName() {
        return mFirmwareName;
    }

    public void setFirmwareName(String firmwareName) {
        this.mFirmwareName = firmwareName;
    }

    public String getFirmwareVersion() {
        return mFirmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.mFirmwareVersion = firmwareVersion;
    }

    public Integer getMemory() {
        return mMemory;
    }

    public void setMemory(Integer memory) {
        this.mMemory = memory;
    }

    public Integer getNumberOfCpus() {
        return mNumberOfCpus;
    }

    public void setNumberOfCpus(Integer numberOfCpus) {
        this.mNumberOfCpus = numberOfCpus;
    }

    public double getCpuFrequency() {
        return mCpuFrequency;
    }

    public void setCpuFrequency(double cpuFrequency) {
        this.mCpuFrequency = cpuFrequency;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        this.mLanguage = language;
    }

    public String getRaidLevel() {
        return mRaidLevel;
    }

    public void setRaidLevel(String raidLevel) {
        this.mRaidLevel = raidLevel;
    }

    public String getHddVendor() {
        return mHddVendor;
    }

    public void setHddVendor(String hddVendor) {
        this.mHddVendor = hddVendor;
    }

    public String getHddModel() {
        return mHddModel;
    }

    public void setHddModel(String hddModel) {
        this.mHddModel = hddModel;
    }

    public String getHddSerial() {
        return mHddSerial;
    }

    public void setHddSerial(String hddSerial) {
        this.mHddSerial = hddSerial;
    }

    public String getHddFirmware() {
        return mHddFirmware;
    }

    public void setHddFirmware(String hddFirmware) {
        this.mHddFirmware = hddFirmware;
    }

    public Integer getRegistered() {
        return mRegistered;
    }

    public void setRegistered(Integer registered) {
        this.mRegistered = registered;
    }

    public String getRaid() {
        return mRaid;
    }

    public void setRaid(String raid) {
        this.mRaid = raid;
    }

    public String getAntiVirusDefVersion() {
        return mAntiVirusDefVersion;
    }

    public void setAntiVirusDefVersion(String antiVirusDefVersion) {
        this.mAntiVirusDefVersion = antiVirusDefVersion;
    }

    public Long getAntiVirusLastUpdatedTimestamp() {
        return mAntiVirusLastUpdatedTimestamp;
    }

    public void setAntiVirusLastUpdatedTimestamp(Long antiVirusLastUpdatedTimestamp) {
        this.mAntiVirusLastUpdatedTimestamp = antiVirusLastUpdatedTimestamp;
    }

    public Integer getReadOnlyDataVolumes() {
        return mReadOnlyDataVolumes;
    }

    public void setReadOnlyDataVolumes(Integer readOnlyDataVolumes) {
        this.mReadOnlyDataVolumes = readOnlyDataVolumes;
    }
}
