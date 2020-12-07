/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
public abstract class Request {
    
    @Attribute(name="id", required=false)
    private String mId;
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType;

    public Request() {
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
}
