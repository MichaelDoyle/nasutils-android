/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Temperature_Summary", strict=false)
public class Temperature {
    
    @Attribute(name="id", required=false)
    private String mId;
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType;
                
    @Element(name="value", required=false)
    private Double mValue;
    
    @Element(name="status", required=false)
    private String mStatus;
    
    public Temperature() {
        // necessary to keep xml deserialization happy
    }

    public String getId() {
        return mResourceId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public Double getValue() {
        return mValue;
    }

    public void setValue(Double value) {
        this.mValue = value;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
}
