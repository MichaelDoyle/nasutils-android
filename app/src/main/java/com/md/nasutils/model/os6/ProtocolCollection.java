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
@Root(strict=false, name="Protocol_Collection")
public class ProtocolCollection implements ResultData {

    @ElementList(entry="Protocol", required=false, inline=true, empty=false)
    public List<Protocol> mProtocols;
    
    public ProtocolCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<Protocol> getProtocols() {
        return mProtocols;
    }

    public void setProtocol(List<Protocol> protocols) {
        this.mProtocols = protocols;
    }
    
    /**
     * Convenience method to retrieve a Protocol by name
     * 
     * @param name
     * @return
     */
    public Protocol getProtocol(String name) {
        for (Protocol p : mProtocols) {
            if (p.getId() != null && p.getId().equals(name)) {
                return p;
            }
        }
        return null;
    }
}
