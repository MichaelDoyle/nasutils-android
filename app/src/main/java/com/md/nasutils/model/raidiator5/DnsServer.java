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
@Root(name="DNS", strict=false)
public class DnsServer {

    @Element(name="Priority", required=false)
    private Integer mPriority;
    
    @Element(name="IP_Address", required=false)
    private String mIpAddress;
    
    public DnsServer() {
        // necessary to keep xml deserialization happy
    }

    public Integer getPriority() {
        return mPriority;
    }

    public void setPriority(Integer priority) {
        this.mPriority = priority;
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.mIpAddress = ipAddress;
    }
}
