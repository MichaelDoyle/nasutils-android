/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Root domain object representing basic NAS status details.
 *
 * Currently fetched from:
 * <pre>POST /get_handler?PAGE=BackupStatus&OUTER_TAB=tab_backup_jobs&INNER_TAB=NONE&l=NONE&OPERATION=get</pre>
 *
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class BackupStatus {

    @ElementList(name="tb", entry="row", required=false, empty=false)
    @Path("content")
    private List<BackupJob> mBackupJobs;

    public BackupStatus() {
        // necessary to keep xml deserialization happy
    }

    public void setBackupJobs(List<BackupJob> jobs) {
        this.mBackupJobs = jobs;
    }
    
    public List<BackupJob> getBackupJobs() {
        return mBackupJobs;
    }
}
