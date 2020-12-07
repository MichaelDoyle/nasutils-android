/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

/**
 * Domain object representing components such as CPUs and Fans.
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class ComponentStatus {
    
    @Element(name="unit", required=false)
    private int mIndex;

    @Element(name="mo", required=false)
    private String mStatusDescription;

    @Transient
    private String mName;
    
    @Transient
    private String mStatus;

    public ComponentStatus() {
        // necessary to keep xml deserialization happy
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
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

    public String getStatusDescription() {
        return mStatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.mStatusDescription = statusDescription;
    }
    
    /** 
     * <p>Post deserialization processing</p>
     * 
     * <p>This method will parse the status description 
     * and set separate fields for component name and status.</p>
     * 
     * <p>The status (captured in the xml element {@code<mo>})
     * should typically be in the format "Component Name: Status".
     * e.g.) Fan SYS2: 914 RPM</p>
     * 
     */
    @Commit
    public void postDeserialization() {
        String[] splits = mStatusDescription.split(":");
        
        if(splits.length == 2) {
            this.mName = splits[0].trim();
            this.mStatus = splits[1].trim();
        }
    }
}
