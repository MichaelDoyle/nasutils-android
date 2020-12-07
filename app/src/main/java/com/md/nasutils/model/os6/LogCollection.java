/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="Log_Collection")
public class LogCollection implements ResultData {

    @ElementList(entry="Log", required=false, inline=true, empty=false)
    public List<Log> mLogs;
    
    public LogCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<Log> getLogs() {
        return mLogs;
    }

    public void setLogs(List<Log> logs) {
        this.mLogs = logs;
    }
}
