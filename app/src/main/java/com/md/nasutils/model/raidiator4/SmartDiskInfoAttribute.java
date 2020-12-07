/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Domain object representing SMART+ disk attributes
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class SmartDiskInfoAttribute {
    
    @Element(name="id", required=false)
    private String mId;
    
    @Element(name="value", required=false)
    @Path("val")
    private String mValue;
    
    @Element(name="value", data=true, required=false)
    @Path("attribute")
    private String mName;
    
    public SmartDiskInfoAttribute() {
        // necessary to keep xml deserialization happy
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
