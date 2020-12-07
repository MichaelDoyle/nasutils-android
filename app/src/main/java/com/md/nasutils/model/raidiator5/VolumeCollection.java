/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Volume_Collection", strict=false)
public class VolumeCollection implements ResultData {

    @Path("Property_List")
    @Element(name = "Type", required=false)
    private String mType;
    
    @Path("Property_List")
    @Element(name = "Expandable", required=false)
    private String mExpandable;
    
    @Path("Property_List")
    @Element(name = "Permisible_Disks", required=false)
    private Integer mPermissableDisks;
    
    @Path("Property_List")
    @Element(name = "Installed_Disks", required=false)
    private Integer mInstalledDisks;
    
    @ElementList(entry="Volume", required=false, inline=true, empty=false)
    private List<Volume> mVolumes;
    
    public VolumeCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<Volume> getVolumes() {
        return mVolumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.mVolumes = volumes;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getExpandable() {
        return mExpandable;
    }

    public void setExpandable(String expandable) {
        this.mExpandable = expandable;
    }

    public Integer getPermissableDisks() {
        return mPermissableDisks;
    }

    public void setPermissableDisks(Integer permissableDisks) {
        this.mPermissableDisks = permissableDisks;
    }

    public Integer getInstalledDisks() {
        return mInstalledDisks;
    }

    public void setInstalledDisks(Integer installedDisks) {
        this.mInstalledDisks = installedDisks;
    }
}
