/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

import java.util.ArrayList;
import java.util.List;

/**
 * Root domain object representing basic NAS status details.
 *
 * Currently fetched from:
 * <pre>/get_handler/?OPERATION=get&PAGE=Status&OUTER_TAB=tab_health&INNER_TAB=NONE</pre>
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class NasHealth {

    @ElementList(name="sts", entry="row", required=false, empty=false)
    @Path("content")
    private List<Device> mDevices;

    @Transient
    private List<Device> mDrives = new ArrayList<>();
    
    @Transient
    private List<Device> mFans = new ArrayList<>();
    
    @Transient
    private List<Device> mTemps = new ArrayList<>();
    
    @Transient
    private List<Device> mUps = new ArrayList<>();

    public NasHealth() {
        // necessary to keep xml deserialization happy
    }
    
    public List<Device> getDevices() {
        return mDevices;
    }

    public void setDevices(List<Device> devices) {
        this.mDevices = devices;
    }
    
    public List<Device> getDrives() {
        return mDrives;
    }

    public void setDrives(List<Device> drives) {
        this.mDrives = drives;
    }

    public List<Device> getFans() {
        return mFans;
    }

    public void setFans(List<Device> fans) {
        this.mFans = fans;
    }

    public List<Device> getTemps() {
        return mTemps;
    }

    public void setTemps(List<Device> temps) {
        this.mTemps = temps;
    }

    public List<Device> getUps() {
        return mUps;
    }

    public void setUps(List<Device> ups) {
        this.mUps = ups;
    }
    
    /** <p>Post deserialization processing</p> */
    @Commit
    public void postDeserialization() {
        for (Device device : mDevices) {
            String id = device.getId();
            if (id != null) {
                if (Device.FAN.equals(id)) {
                    mFans.add(device);
                } else if (Device.UPS.equals(id)) {
                    mUps.add(device);
                } else if (Device.CPU_SYS.equals(id)) {
                    mTemps.add(device);
                } else if (id.startsWith(Device.DISK)) {
                    mDrives.add(device);
                }
            }
        }
    }
}
