/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyDATA OS
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="vnic_collection")
public class VirtualNetworkLinkCollection {

    @ElementList(entry="vnic", inline=true, required=false, empty=false)
    private List<VirtualNetworkLink> mVirtualNetworkLinks;
    
    public VirtualNetworkLinkCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<VirtualNetworkLink> getVirtualNetworkLinks() {
        return mVirtualNetworkLinks;
    }

    public void setVirtualNetworkLinks(List<VirtualNetworkLink> virtualNetworkLinks) {
        this.mVirtualNetworkLinks = virtualNetworkLinks;
    }
}
