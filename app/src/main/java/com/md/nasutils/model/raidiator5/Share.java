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
@Root(name="Share", strict=false)
public class Share {
    
    @Attribute(name="id", required=false)   
    private String mId;
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType; 
    
    @Element(name="Name", required=false)
    private String mName;
    
    public Share() {
        // necessary to keep xml deserialization happy
    }

    public String getId() {
        return mId;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
