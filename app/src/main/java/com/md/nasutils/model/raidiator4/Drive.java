/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

/**
 * Domain object representing a disk drive.
 *  
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class Drive {
    
    @Element(name="drive_name", required=false)
    private String mDriveName;
    
    @Element(name="model", required=false)
    private String mModel;
    
    @Element(name="serial", required=false)
    private String mSerial;
    
    @Element(name="firmware", required=false)
    private String mFirmware;
    
    @Element(name="mode", required=false)
    private String mMode;
    
    @Element(name="capacity", required=false)
    private int mCapacity;
    
    @Element(name="channel", required=false)
    private int mChannel;
    
    @Element(name="alt", required=false)
    private String mAlt;
    
    @Element(name="free_size", required=false)
    private long mFreeSize;
    
    @Element(name="pending_spare", required=false)
    private String mPendingSpare;

    public Drive() {
        // necessary to keep xml deserialization happy
    }
    
    public String getDriveName() {
        return mDriveName;
    }

    public void setDriveName(String driveName) {
        this.mDriveName = driveName;
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

    public String getFirmware() {
        return mFirmware;
    }

    public void setFirmware(String firmware) {
        this.mFirmware = firmware;
    }

    public String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        this.mMode = mode;
    }

    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int capacity) {
        this.mCapacity = capacity;
    }

    public int getChannel() {
        return mChannel;
    }

    public void setChannel(int channel) {
        this.mChannel = channel;
    }

    public String getAlt() {
        return mAlt;
    }

    public void setAlt(String alt) {
        this.mAlt = alt;
    }

    public long getFreeSize() {
        return mFreeSize;
    }

    public void setFreeSize(long freeSize) {
        this.mFreeSize = freeSize;
    }

    public String getPendingSpare() {
        return mPendingSpare;
    }

    public void setPendingSpare(String pendingSpare) {
        this.mPendingSpare = pendingSpare;
    }
    
    /** 
     * <p>Post deserialization processing</p> 
     */
    @Commit
    public void postDeserialization() {     
        // serial number is comma separated list of ASCII codes on some devices
        if (mSerial != null && mSerial.contains(",")) {
            String[] splits = mSerial.split(",");
            StringBuffer sb = new StringBuffer();
            for (String s : splits) {
                int i = Integer.parseInt(s);
                sb.append(String.valueOf(Character.toChars(i)));
            }
            mSerial = sb.toString();
        }
    }
}
