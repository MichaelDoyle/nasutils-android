/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import com.md.nasutils.service.readynas.ArgumentsConverter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="Log")
public class Log {
    
    @Attribute(name="id", required=false)
    private Long mId;
    
    @Attribute(name="timestamp", required=false)
    private Long mTimestamp;
    
    @Attribute(name="localized_ts", required=false)
    private String mLocalizedDateString;
    
    @Attribute(name="severity", required=false)
    private String mSeverity;
    
    @Attribute(name="category", required=false)
    private String mCategory;
    
    @Attribute(name="message-tag", required=false)
    private String mMessageTag;
    
    @Element(name="args", required=false)
    @Convert(ArgumentsConverter.class)
    private Arguments mArgs;

    public Log() {
        // necessary to keep xml deserialization happy
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public Long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.mTimestamp = timestamp;
    }

    public String getLocalizedDateString() {
        return mLocalizedDateString;
    }

    public void setLocalizedDateString(String localizedDateString) {
        this.mLocalizedDateString = localizedDateString;
    }

    public String getSeverity() {
        return mSeverity;
    }

    public void setSeverity(String severity) {
        this.mSeverity = severity;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public String getMessageTag() {
        return mMessageTag;
    }

    public void setMessageTag(String messageTag) {
        this.mMessageTag = messageTag;
    }

    public Arguments getArgs() {
        return mArgs;
    }

    public void setArgs(Arguments args) {
        this.mArgs = args;
    }
}
