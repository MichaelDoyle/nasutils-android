/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="addon_Collection")
public class AddonCollection implements ResultData {

    @ElementList(entry="addon", inline=true, required=false, empty=false)
    private List<AddOn> mAddOns;

    public AddonCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<AddOn> getAddOns() {
        return mAddOns;
    }

    public void setApplications(List<AddOn> addOns) {
        this.mAddOns = addOns;
    }
}
