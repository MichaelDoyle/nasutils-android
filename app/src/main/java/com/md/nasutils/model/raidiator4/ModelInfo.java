/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Domain object representing the NAS details.
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class ModelInfo {
    
    @Element(name="display_model", required=false)
    private String mDisplayModel;
    
    @Element(name="model", required=false)
    private String mModel;
    
    @Element(name="firmware", required=false)
    private String mFirmware;
    
    @Element(name="display_firmware", required=false)
    private String mDisplayFirmware;
    
    @Element(name="memory", required=false)
    private String mMemory;
    
    @Element(name="architecture", required=false)
    private String mArchitecture;
    
    @Element(name="channels", required=false)
    private int mChannels;
    
    @Element(name="ads_support", required=false)
    private String mAdsSupport;
    
    @Element(name="teaming_support", required=false)
    private String mTeamingSupport;

    public ModelInfo() {
        // necessary to keep xml deserialization happy
    }
    
    public String getDisplayModel() {
        return mDisplayModel;
    }
    
    public void setDisplayModel(String displayModel) {
        this.mDisplayModel = displayModel;
    }
    
    public String getModel() {
        return mModel;
    }
    
    public void setModel(String model) {
        this.mModel = model;
    }
    
    public String getFirmware() {
        return mFirmware;
    }
    
    public void setFirmware(String firmware) {
        this.mFirmware = firmware;
    }
    
    public String getDisplayFirmware() {
        return mDisplayFirmware;
    }
    
    public void setDisplayFirmware(String displayFirmware) {
        this.mDisplayFirmware = displayFirmware;
    }
    
    public String getMemory() {
        return mMemory;
    }
    
    public void setMemory(String memory) {
        this.mMemory = memory;
    }
    
    public String getArchitecture() {
        return mArchitecture;
    }
    
    public void setArchitecture(String architecture) {
        this.mArchitecture = architecture;
    }
    
    public int getChannels() {
        return mChannels;
    }
    
    public void setChannels(int channels) {
        this.mChannels = channels;
    }
    
    public String getAdsSupport() {
        return mAdsSupport;
    }
    
    public void setAdsSupport(String adsSupport) {
        this.mAdsSupport = adsSupport;
    }
    
    public String getTeamingSupport() {
        return mTeamingSupport;
    }
    
    public void setTeamingSupport(String teamingSupport) {
        this.mTeamingSupport = teamingSupport;
    }
}
