/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Domain object representing SMART+ disk information
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class SmartDiskInfo {
    
    @Element(name="serial", required=false)
    @Path("content")
    private String mSerial;
    
    @ElementList(name="smart", entry="row", required=false, empty=false)
    @Path("content")    
    private List<SmartDiskInfoAttribute> mAttributes;
    
    public SmartDiskInfo() {
        // necessary to keep xml deserialization happy
    }
    
    public String getSerial() {
        return mSerial;
    }

    public void setSerial(String serial) {
        this.mSerial = serial;
    }

    public List<SmartDiskInfoAttribute> getAttributes() {
        return mAttributes;
    }

    public void setmAttributes(List<SmartDiskInfoAttribute> attributes) {
        this.mAttributes = attributes;
    }
}
