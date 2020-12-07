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
 * Root domain object representing the NAS logs
 *
 * Currently fetched from:
 * <pre>/get_handler/?OPERATION=get&PAGE=Status&OUTER_TAB=tab_log&INNER_TAB=NONE</pre>
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class StatusLogs {

    @Element(name="status", required=false)
    @Path("content")
    public String mStatus;
    
    @ElementList(name="lul", entry="row", required=false, empty=false)
    @Path("content")    
    public List<Log> mLogs;

    public StatusLogs() {
        // necessary to keep xml deserialization happy
    }
    
    public String getStatus() {
        return mStatus;
    }
    
    public void setStatus(String status) {
        this.mStatus = status;
    }
    
    public List<Log> getLogs() {
        return mLogs;
    }
    
    public void setLogs(List<Log> logs) {
        this.mLogs = logs;
    }
}
