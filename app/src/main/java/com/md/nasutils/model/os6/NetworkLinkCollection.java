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
@Root(strict=false, name="network_link_collection")
public class NetworkLinkCollection implements ResultData {

    @ElementList(entry="network_link", inline=true, required=false, empty=false)
    private List<NetworkLink> mNetworkLinks;
    
    public NetworkLinkCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<NetworkLink> getNetworkLinks() {
        return mNetworkLinks;
    }

    public void setNetworkLinks(List<NetworkLink> networkLinks) {
        this.mNetworkLinks = networkLinks;
    }
}
