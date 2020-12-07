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
@Root(name="Health_Collection", strict=false)
public class HealthCollection implements ResultData {
    
    @ElementList(entry="Enclosure_Health", inline=true, required=false, empty=false)
    private List<EnclosureHealth> mEnclosures;
    
    @ElementList(entry="Ups_Health", inline=true, required=false, empty=false)
    private List<UpsHealth> mUpsHealths;
    
    public HealthCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<EnclosureHealth> getEnclosureHealths() {
        return mEnclosures;
    }

    public void setEnclosureHealths(List<EnclosureHealth> enclosures) {
        this.mEnclosures = enclosures;
    }

    public List<UpsHealth> getUpsHealths() {
        return mUpsHealths;
    }

    public void setUpsHealths(List<UpsHealth> mUpsHealths) {
        this.mUpsHealths = mUpsHealths;
    }
}
