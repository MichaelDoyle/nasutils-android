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
@Root(name="Volume_Collection", strict=false)
public class VolumeCollection implements ResultData {

    @ElementList(entry="Volume", required=false, inline=true, empty=false)
    private List<Volume> mVolumes;
    
    public VolumeCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<Volume> getVolumes() {
        return mVolumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.mVolumes = volumes;
    }
}
