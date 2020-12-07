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
@Root(name="RAID", strict=false)
public class Raid {
    
    @Attribute(name="LEVEL", required=false)    
    private String mLevel;
    
    @Attribute(name="ID", required=false)   
    private String mId;
    
    @ElementList(entry="Disk", required=false, inline=true, empty=false)
    private List<Disk> mDisks;
    
    public Raid() {
        // necessary to keep xml deserialization happy
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        this.mLevel = level;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public List<Disk> getDisks() {
        return mDisks;
    }

    public void setDisks(List<Disk> disks) {
        this.mDisks = disks;
    }
}
