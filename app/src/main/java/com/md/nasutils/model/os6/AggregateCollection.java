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
@Root(strict=false, name="aggregate_collection")
public class AggregateCollection implements ResultData {

    @ElementList(entry="aggregate", inline=true, required=false, empty=false)
    private List<NetworkLink> mAggregateNetworkLinks;
    
    public AggregateCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<NetworkLink> getAggregateNetworkLinks() {
        return mAggregateNetworkLinks;
    }

    public void setAggregateNetworkLinks(List<NetworkLink> aggregateNetworkLinks) {
        this.mAggregateNetworkLinks = aggregateNetworkLinks;
    }
}
