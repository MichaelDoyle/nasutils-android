/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Health_Collection", strict=false)
public class HealthCollection implements ResultData {
    
    @ElementList(entry="Fan", inline=true, required=false, empty=false)
    private List<Fan> mFans;
    
    @ElementList(entry="Temperature_Summary", inline=true, required=false, empty=false)
    private List<Temperature> mTemperatures;
    
    @ElementList(entry="Disk", inline=true, required=false, empty=false)
    private List<Disk> mDisks;
    
    @ElementList(entry="UPS", inline=true, required=false, empty=false)
    private List<Ups> mUpses;
    
    public HealthCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<Fan> getFans() {
        return mFans;
    }

    public void setFans(List<Fan> fans) {
        this.mFans = fans;
    }

    public List<Temperature> getTemperatures() {
        return mTemperatures;
    }

    public void setTemperatures(List<Temperature> temperatures) {
        this.mTemperatures = temperatures;
    }

    public List<Disk> getDisks() {
        return mDisks;
    }

    public void setDisks(List<Disk> disks) {
        this.mDisks = disks;
    }
    
    public List<Ups> getUpses() {
        return mUpses;
    }

    public void setUpses(List<Ups> upses) {
        this.mUpses = upses;
    }
}
