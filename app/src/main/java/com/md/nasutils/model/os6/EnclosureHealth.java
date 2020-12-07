/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Enclosure_Health", strict=false)
public class EnclosureHealth {
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType; 
            
    @Attribute(name="port-label", required=false)
    private String mPortLabel;
            
    @Attribute(name="nr-cpu", required=false)   
    private Integer mNumberOfCpus;
            
    @Attribute(name="nr-fan", required=false)   
    private Integer mNumberOfFans;
            
    @Attribute(name="nr-psu", required=false)   
    private Integer mNumberOfPsus;
            
    @Attribute(name="nr-volt", required=false)  
    private Integer mNumberOfVolts;
    
    @ElementList(entry="Fan", inline=true, required=false, empty=false)
    private List<Fan> mFans;
    
    @ElementList(entry="Temperature", inline=true, required=false, empty=false)
    private List<Temperature> mTemperatures;
    
    @ElementList(entry="Disk", inline=true, required=false, empty=false)
    private List<Disk> mDisks;
    
    public EnclosureHealth() {
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

    public String getPortLabel() {
        return mPortLabel;
    }

    public void setPortLabel(String portLabel) {
        this.mPortLabel = portLabel;
    }

    public Integer getNumberOfCpus() {
        return mNumberOfCpus;
    }

    public void setNumberOfCpus(Integer numberOfCpus) {
        this.mNumberOfCpus = numberOfCpus;
    }

    public Integer getNumberOfFans() {
        return mNumberOfFans;
    }

    public void setNumberOfFans(Integer numberOfFans) {
        this.mNumberOfFans = numberOfFans;
    }

    public Integer getNumberOfPsus() {
        return mNumberOfPsus;
    }

    public void setNumberOfPsus(Integer numberOfPsus) {
        this.mNumberOfPsus = numberOfPsus;
    }

    public Integer getNumberOfVolts() {
        return mNumberOfVolts;
    }

    public void setNumberOfVolts(Integer numberOfVolts) {
        this.mNumberOfVolts = numberOfVolts;
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
    
    /**
     * Convenience method to retrieve a disk by resource-id
     * 
     * @param resourceId
     * @return
     */
    public Disk getDisk(String resourceId) {
        for (Disk disk : mDisks) {
            if (disk.getResourceId().equals(resourceId)) {
                return disk;
            }
        }
        return null;
    }
}
