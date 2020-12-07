/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Disk_Collection", strict=false)
public class DiskCollection {
    
    @Attribute(name="IsSlotHorizontal", required=false)
    private Integer mIsSlotHorizontal;
    
    @Attribute(name="columns", required=false)
    private Integer mColumns;
    
    @Attribute(name="rows", required=false)
    private Integer mRows;
    
    @ElementList(entry="Disk", inline=true, required=false, empty=false)
    private List<Disk> mDisks;
    
    public DiskCollection() {
        // necessary to keep xml deserialization happy
    }

    public Integer getIsSlotHorizontal() {
        return mIsSlotHorizontal;
    }

    public void setIsSlotHorizontal(Integer isSlotHorizontal) {
        this.mIsSlotHorizontal = isSlotHorizontal;
    }

    public Integer getColumns() {
        return mColumns;
    }

    public void setColumns(Integer columns) {
        this.mColumns = columns;
    }

    public Integer getRows() {
        return mRows;
    }

    public void setRows(Integer rows) {
        this.mRows = rows;
    }

    public List<Disk> getDisks() {
        return mDisks;
    }

    public void setDisks(List<Disk> disks) {
        this.mDisks = disks;
    }
    
    /**
     * Convenience method to retrieve a disk by resource-id
     * 
     * @param resourceId
     * @return
     */
    public Disk getDisk(String resourceId) {
        for (Disk disk : mDisks) {
            if (disk.getResourceId().equals(resourceId)) {
                return disk;
            }
        }
        return null;
    }
} 
