/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
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
    
    @Element(name="Name", required=false)
    private String mName;
    
    @Element(name="RaidLevel", required=false)
    private Integer mRaidLevel;
    
    @Element(name="Capacity", required=false)
    private Long mCapacityBytes;
    
    @Transient
    private Long mFreeBytes;
    
    // ignoring the state attributes for now
    
    // <State Percentage-Completed="15.0" Finish-Seconds="13740" Speed="120445">Recovery</State>
    @Element(name="State", required=false)
    private String mState;
    
    // ignoring the disks for now
    
    //    <Disk_collection>
    //      <Disk id="1">
    //        <Slot>1</Slot>
    //        <Type>Active</Type>
    //        <Usage></Usage>
    //      </Disk>
    //      <Disk id="2">
    //        <Type>Active</Type>
    //        <Usage></Usage>
    //      </Disk>
    //    </Disk_collection>
    
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Integer getRaidLevel() {
        return mRaidLevel;
    }

    public void setRaidLevel(Integer raidLevel) {
        this.mRaidLevel = raidLevel;
    }

    public Long getCapacityBytes() {
        return mCapacityBytes;
    }

    public void setCapacityBytes(Long capacityBytes) {
        this.mCapacityBytes = capacityBytes;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public Long getFreeBytes() {
        return mFreeBytes;
    }

    public void setFreeBytes(Long freeBytes) {
        this.mFreeBytes = freeBytes;
    }
}
