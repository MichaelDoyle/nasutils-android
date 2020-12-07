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
@Root(strict=false, name="BackupJob_Collection")
public class BackupJobCollection implements ResultData {

    @ElementList(entry="BackupJob", inline=true, required=false, empty=false)
    private List<BackupJob> mBackupJobs;

    public BackupJobCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<BackupJob> getBackupJobs() {
        return mBackupJobs;
    }

    public void setBackupJobs(List<BackupJob> backupJobs) {
        this.mBackupJobs = backupJobs;
    }
    
    /**
     * Convenience method to retrieve a backup job by id
     * 
     * @param resourceId
     * @return
     */
    public BackupJob getBackUpJob(String resourceId) {
        for (BackupJob bj : mBackupJobs) {
            if (bj.getResourceId() != null && bj.getResourceId().equals(resourceId)) {
                return bj;
            }
        }
        return null;
    }
}
