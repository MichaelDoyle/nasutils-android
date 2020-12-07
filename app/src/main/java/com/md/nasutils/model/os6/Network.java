/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Network", strict=false)
public class Network implements ResultData {

    @Path("General_Settings")
    @Element(name="Hostname")
    private String mHostname;
    
    public Network() {
        // necessary to keep xml deserialization happy
    }

    public String getHostname() {
        return mHostname;
    }

    public void setHostname(String hostname) {
        this.mHostname = hostname;
    }
}
