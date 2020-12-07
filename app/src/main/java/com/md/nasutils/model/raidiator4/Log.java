/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Domain object representing a Log entry. 
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class Log {
    
    @Element(name="id", required=false)
    private int mId;
    
    @Element(name="value", data=true, required=false)
    @Path("description")
    private String mDescription;
    
    @Element(name="value", data=true, required=false)
    @Path("severity_image")
    private String mSeverityImage;
    
    @Element(name="value", data=true, required=false)
    @Path("time")
    private String mTime;

    public Log() {
        // necessary to keep xml deserialization happy
    }
    
    public String getSeverityImage() {
        return mSeverityImage;
    }

    public void setSeverityImage(String severityImage) {
        this.mSeverityImage = severityImage;
    }

    public String getTime() {
        return mTime;
    }
    
    public void setTime(String time) {
        this.mTime = time;
    }
    
    public String getDescription() {
        return mDescription;
    }
    
    public void setDescription(String description) {
        this.mDescription = description;
    }
    
    public int getId() {
        return mId;
    }
    
    public void setId(int id) {
        this.mId = id;
    }
}
