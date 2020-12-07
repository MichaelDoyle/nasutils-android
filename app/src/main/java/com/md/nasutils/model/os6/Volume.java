/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Volume_Collection", strict=false)
public class Volume {
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType;
    
    @Attribute(name="ejectable", required=false)
    private String mEjectable;
    
    @Path("Property_List")
    @Element(name="Volume_Name", required=false)
    private String mVolumeName;
    
    @Path("Property_List")
    @Element(name="Volume_Mode", required=false)
    private Integer mVolumeMode;
    
    @Path("Property_List")
    @Element(name="AltRoot", required=false)
    private String mAltRoot;
    
    @Path("Property_List")
    @Element(name="DeDup", required=false)
    private String mDeDup;
    
    @Path("Property_List")
    @Element(name="AutoExpand", required=false)
    private String mAutoExpand;
    
    @Path("Property_List")
    @Element(name="AutoReplace", required=false)
    private String mAutoReplace;
    
    @Path("Property_List")
    @Element(name="CacheFile", required=false)
    private String mCacheFile;
    
    @Path("Property_List")
    @Element(name="Delegation", required=false)
    private String mDelegation;
    
    @Path("Property_List")
    @Element(name="ListSnapshots", required=false)
    private String mListSnapshots;
    
    @Path("Property_List")
    @Element(name="Version", required=false)
    private String mVersion;
    
    @Path("Property_List")
    @Element(name="Allocated", required=false)
    private double mAllocatedMegabytes;
    
    @Path("Property_List")
    @Element(name="Capacity", required=false)
    private double mCapacityMegabytes;
    
    @Path("Property_List")
    @Element(name="Free", required=false)
    private double mFreeMegabytes;
    
    @Path("Property_List")
    @Element(name="Available", required=false)
    private double mAvailableMegabytes;

    @Path("Property_List")
    @Element(name="GUID", required=false)
    private String mGuid;
    
    @Path("Property_List")
    @Element(name="Health", required=false)
    private String mHealth;
    
    @Path("Property_List")
    @Element(name="Checksum", required=false)
    private String mChecksum;
    
    @Path("vdev_list")
    @ElementList(entry="RAID", required=false, inline=true, empty=false)
    private List<Raid> mVirtualDevices;
    
    public Volume() {
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

    public String getEjectable() {
        return mEjectable;
    }

    public void setEjectable(String ejectable) {
        this.mEjectable = ejectable;
    }

    public String getVolumeName() {
        return mVolumeName;
    }

    public void setVolumeName(String volumeName) {
        this.mVolumeName = volumeName;
    }

    public Integer getVolumeMode() {
        return mVolumeMode;
    }

    public void setVolumeMode(Integer volumeMode) {
        this.mVolumeMode = volumeMode;
    }

    public String getAltRoot() {
        return mAltRoot;
    }

    public void setAltRoot(String altRoot) {
        this.mAltRoot = altRoot;
    }

    public String getDeDup() {
        return mDeDup;
    }

    public void setDeDup(String deDup) {
        this.mDeDup = deDup;
    }

    public String getAutoExpand() {
        return mAutoExpand;
    }

    public void setAutoExpand(String autoExpand) {
        this.mAutoExpand = autoExpand;
    }

    public String getAutoReplace() {
        return mAutoReplace;
    }

    public void setAutoReplace(String autoReplace) {
        this.mAutoReplace = autoReplace;
    }

    public String getCacheFile() {
        return mCacheFile;
    }

    public void setCacheFile(String cacheFile) {
        this.mCacheFile = cacheFile;
    }

    public String getDelegation() {
        return mDelegation;
    }

    public void setDelegation(String delegation) {
        this.mDelegation = delegation;
    }

    public String getListSnapshots() {
        return mListSnapshots;
    }

    public void setListSnapshots(String listSnapshots) {
        this.mListSnapshots = listSnapshots;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public double getAllocatedMegabytes() {
        return mAllocatedMegabytes;
    }

    public void setAllocatedMegabytes(double allocatedMegabytes) {
        this.mAllocatedMegabytes = allocatedMegabytes;
    }

    public double getCapacityMegabytes() {
        return mCapacityMegabytes;
    }

    public void setCapacityMegabytes(double capacityMegabytes) {
        this.mCapacityMegabytes = capacityMegabytes;
    }

    public double getFreeMegabytes() {
        return mFreeMegabytes;
    }

    public void setFreeMegabytes(double freeMegabytes) {
        this.mFreeMegabytes = freeMegabytes;
    }

    public double getAvailableMegabytes() {
        return mAvailableMegabytes;
    }

    public void setAvailableMegabytes(double availableMegabytes) {
        this.mAvailableMegabytes = availableMegabytes;
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String guid) {
        this.mGuid = guid;
    }

    public String getHealth() {
        return mHealth;
    }

    public void setHealth(String health) {
        this.mHealth = health;
    }

    public String getChecksum() {
        return mChecksum;
    }

    public void setChecksum(String checksum) {
        this.mChecksum = checksum;
    }

    public List<Raid> getVirtualDevices() {
        return mVirtualDevices;
    }

    public void setVirtualDevices(List<Raid> virtualDevices) {
        this.mVirtualDevices = virtualDevices;
    }
}
