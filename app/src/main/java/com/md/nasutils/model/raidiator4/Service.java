/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Domain object representing a ReadyNAS service
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class Service {

    @Element(name="name", required=false)
    private String mName;
    
    @Element(name="status", required=false)
    private String mStatus;
    
    @Element(name="version", required=false)
    private String mVersion;

    public Service() {
        // necessary to keep xml deserialization happy
    }
    
    public String getName() {
        return mName;
    }
    
    public void setName(String name) {
        this.mName = name;
    }
    
    public String getStatus() {
        return mStatus;
    }
    
    public void setStatus(String status) {
        this.mStatus = status;
    }
    
    public String getVersion() {
        return mVersion;
    }
    
    public void setVersion(String version) {
        this.mVersion = version;
    }
}
