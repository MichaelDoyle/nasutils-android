/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="Log")
public class Log {
    
    @Element(name="severity", required=false)
    private String mSeverity;
    
    @Element(name="date", required=false)
    private String mLocalizedDateString;
    
    @Element(name="message", required=false)
    private String mMessage;

    public Log() {
        // necessary to keep xml deserialization happy
    }

    public String getSeverity() {
        return mSeverity;
    }

    public void setSeverity(String severity) {
        this.mSeverity = severity;
    }
    
    public String getLocalizedDateString() {
        return mLocalizedDateString;
    }

    public void setLocalizedDateString(String localizedDateString) {
        this.mLocalizedDateString = localizedDateString;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
