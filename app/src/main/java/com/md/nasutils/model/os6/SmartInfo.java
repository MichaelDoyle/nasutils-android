/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="SMARTInfo", strict=false)
public class SmartInfo implements ResultData {
    
    @Element(name="smartinfo")
    private String mSmartInfo;
    
    public SmartInfo() {
        // necessary to keep xml deserialization happy
    }

    public String getSmartInfo() {
        return mSmartInfo;
    }

    public void setSmartInfo(String smartInfo) {
        this.mSmartInfo = smartInfo;
    }

}
