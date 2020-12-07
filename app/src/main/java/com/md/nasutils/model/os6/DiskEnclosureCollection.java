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
@Root(strict=false, name="DiskEnclosure_Collection")
public class DiskEnclosureCollection implements ResultData {
    
    @ElementList(entry="DiskEnclosure", inline=true, required=false, empty=false)
    private List<DiskEnclosure> mDiskEnclosures;
    
    public DiskEnclosureCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<DiskEnclosure> getDiskEnclosures() {
        return mDiskEnclosures;
    }

    public void setDiskEnclosures(List<DiskEnclosure> diskEnclosures) {
        this.mDiskEnclosures = diskEnclosures;
    }
    
    /**
     * Convenience method to retrieve a disk enclosure by id
     * 
     * @param refId
     * @return
     */
    public DiskEnclosure getDiskEnclosure(String resourceId) {
        for (DiskEnclosure de : mDiskEnclosures) {
            if (de.getResourceId() != null && de.getResourceId().equals(resourceId)) {
                return de;
            }
        }
        return null;
    }
}
