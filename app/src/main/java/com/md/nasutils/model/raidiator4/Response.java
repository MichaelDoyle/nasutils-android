/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

import java.util.List;

/**
 * Domain object representing the generic Response 
 * structure returned by ReadyNAS FrontView.
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class Response {
    
    @Element(name="status", required=false)
    private String mStatus;
    
    @ElementList(entry="message", data=true, inline=true, required=false, empty=false)
    @Path("log_alerts")
    private List<String> mLogAlerts;

    @ElementList(entry="message", data=true, inline=true, required=false, empty=false)
    @Path("normal_alerts")
    private List<String> mNormalAlerts;
    
    @Transient
    private String mMessage;

    public Response() {
        // necessary to keep xml deserialization happy
    }
        
    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
    
    public List<String> getLogAlerts() {
        return mLogAlerts;
    }

    public void setLogAlerts(List<String> logAlerts) {
        this.mLogAlerts = logAlerts;
    }

    public List<String> getNormalAlerts() {
        return mNormalAlerts;
    }

    public void setNormalAlerts(List<String> normalAlerts) {
        this.mNormalAlerts = normalAlerts;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    /** 
     * <p>Post deserialization processing</p> 
     * 
     */
    @Commit
    public void postDeserialization() {
        StringBuffer alerts = new StringBuffer();

        if (mLogAlerts != null) {
            for (String s : mLogAlerts) {
                if (!"NO_ALERTS".equals(s)) {
                    alerts.append(s.trim()).append("\n");
                }
            }
        } 
        
        if (mNormalAlerts != null) {
            for (String s : mNormalAlerts) {
                if (!"NO_ALERTS".equals(s)) {
                    alerts.append(s.trim()).append("\n");
                }
            }
        }
            
        mMessage = alerts.toString();
    }
}
