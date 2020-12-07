/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Domain object representing a disk volume
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */@Root(strict=false)
public class Volume {
    
    @Element(name="name", required=false)
    private String mName;
    
    @Element(name="usage", required=false)
    private String mUsage;
    
    @Element(name="pct_used", required=false)
    private int mPctUsed;
    
    @Element(name="expand_volume", required=false)
    private String mExpandVolume;
    
    @Element(name="limit", required=false)
    private String mLimit;
    
    @Element(name="migrate5to6", required=false)
    private String mMigrate5to6;
    
    @Element(name="migrate5to6_expand_volume", required=false)
    private String mMigrate5to6ExpandVolume;
    
    @Element(name="raid10_ifwarn", required=false)
    private String mRaid10Ifwarn;
    
    @Element(name="snapshot_size", required=false)
    private long mSnapshotSize;
    
    @Element(name="snapshot_message", required=false)
    private String mSnapshotMessage;
    
    @Element(name="raid_level", required=false)
    private String mRaidLevel;
    
    @Element(name="raid_status", required=false)
    private String mRaidStatus;
    
    @Element(name="raid_status_description", required=false)
    private String mRaidStatusDescription;
    
    @Element(name="description", required=false)
    private String mDescription;
    
    @Element(name="mode", required=false)
    private String mMode;

    public Volume() {
        // necessary to keep xml deserialization happy
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(String name) {
        this.mName = name;
    }
    
    public String getUsage() {
        return mUsage;
    }
    
    public void setUsage(String usage) {
        this.mUsage = usage;
    }
    
    public int getPctUsed() {
        return mPctUsed;
    }
    
    public void setPctUsed(int pctUsed) {
        this.mPctUsed = pctUsed;
    }
    
    public String getExpandVolume() {
        return mExpandVolume;
    }
    
    public void setExpandVolume(String expandVolume) {
        this.mExpandVolume = expandVolume;
    }
    
    public String getLimit() {
        return mLimit;
    }
    
    public void setLimit(String limit) {
        this.mLimit = limit;
    }
    
    public String getMigrate5to6() {
        return mMigrate5to6;
    }
    
    public void setMigrate5to6(String migrate5to6) {
        this.mMigrate5to6 = migrate5to6;
    }
    
    public String getMigrate5to6ExpandVolume() {
        return mMigrate5to6ExpandVolume;
    }
    
    public void setMigrate5to6ExpandVolume(String migrate5to6ExpandVolume) {
        this.mMigrate5to6ExpandVolume = migrate5to6ExpandVolume;
    }
    
    public String getRaid10Ifwarn() {
        return mRaid10Ifwarn;
    }
    
    public void setRaid10Ifwarn(String raid10Ifwarn) {
        this.mRaid10Ifwarn = raid10Ifwarn;
    }
    
    public long getSnapshotSize() {
        return mSnapshotSize;
    }
    
    public void setSnapshotSize(int snapshotSize) {
        this.mSnapshotSize = snapshotSize;
    }
    
    public String getSnapshotMessage() {
        return mSnapshotMessage;
    }
    
    public void setSnapshotMessage(String snapshotMessage) {
        this.mSnapshotMessage = snapshotMessage;
    }
    
    public String getRaidLevel() {
        return mRaidLevel;
    }
    
    public void setRaidLevel(String raidLevel) {
        this.mRaidLevel = raidLevel;
    }
    
    public String getRaidStatus() {
        return mRaidStatus;
    }
    
    public void setRaidStatus(String raidStatus) {
        this.mRaidStatus = raidStatus;
    }
    
    public String getRaidStatusDescription() {
        return mRaidStatusDescription;
    }
    
    public void setRaidStatusDescription(String raidStatusDescription) {
        this.mRaidStatusDescription = raidStatusDescription;
    }
    
    public String getDescription() {
        return mDescription;
    }
    
    public void setDescription(String description) {
        this.mDescription = description;
    }
    
    public String getMode() {
        return mMode;
    }
    
    public void setMode(String mode) {
        this.mMode = mode;
    }
}
