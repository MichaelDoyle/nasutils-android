/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Root domain object representing installed Add Ons
 *
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class AddOns {

    @ElementList(name="content", entry="other_addon", required=false, empty=false)
    private List<AddOn> mAddOns;

    public AddOns() {
        // necessary to keep xml deserialization happy
    }

    public void setAddOns(List<AddOn> jobs) {
        this.mAddOns = jobs;
    }
    
    public List<AddOn> getAddOns() {
        return mAddOns;
    }
}
