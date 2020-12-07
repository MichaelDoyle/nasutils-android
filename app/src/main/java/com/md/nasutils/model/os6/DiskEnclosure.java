/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Path("DiskEnclosure_Collection")
@Root(name="DiskEnclosure", strict=false)
public class DiskEnclosure {

    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType;
    
    @Attribute(name="name", required=false)
    private String mName;
    
    @Attribute(name="PortName", required=false)
    private String mPortName;
    
    @Element(name="Disk_Collection", required=false)
    private DiskCollection mDiskCollection;
    
    public DiskEnclosure() {
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

    public String getPortName() {
        return mPortName;
    }

    public void setPortName(String portName) {
        this.mPortName = portName;
    }

    public DiskCollection getDiskCollection() {
        return mDiskCollection;
    }

    public void setDiskCollection(DiskCollection diskCollection) {
        this.mDiskCollection = diskCollection;
    }   
}
