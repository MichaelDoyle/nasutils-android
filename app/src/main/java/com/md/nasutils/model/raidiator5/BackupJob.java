/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Path("BackupJob_Collection")
@Root(name="BackupJob", strict=false)
public class BackupJob {

    @Attribute(name="resource-id", required=false)
    private String mResourceId;

    @Attribute(name="resource-type", required=false)
    private String mResourceType;

    @Element(name="Job_Id", required=false)
    private String mJobId;

    @Element(name="Job_Name", required=false, data=true)
    private String mJobName;

    @Element(name="Status", required=false)
    private String mStatus;

    @Element(name="Status_Date", required=false)
    private String mStatusDate;

    @Element(name="Status_Result", required=false)
    private String mStatusResult;

    @ElementList(entry="BackupJob_Resource", inline=true, required=false, empty=false)
    private List<BackupJobResource> mBackupJobResources;

    @Element(name="Schedule")
    private BackupSchedule mSchedule;

    @Element(name="Option")
    private BackupOption mOption;

    public BackupJob() {
        // necessary to keep xml deserialization happy
    }

    public String getResourceId() {
        return mResourceId;
    }

    public void setResourceId(String resourceId) {
        this.mResourceId = resourceId;
    }

    public String getResourceType() {
        return mResourceType;
    }

    public void setResourceType(String resourceType) {
        this.mResourceType = resourceType;
    }

    public String getJobId() {
        return mJobId;
    }

    public void setJobId(String jobId) {
        this.mJobId = jobId;
    }

    public String getJobName() {
        return mJobName;
    }

    public void setJobName(String jobName) {
        this.mJobName = jobName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getStatusDate() {
        return mStatusDate;
    }

    public void setStatusDate(String statusDate) {
        this.mStatusDate = statusDate;
    }

    public String getStatusResult() {
        return mStatusResult;
    }

    public void setStatusResult(String statusResult) {
        this.mStatusResult = statusResult;
    }

    public List<BackupJobResource> getBackupJobResources() {
        return mBackupJobResources;
    }

    public void setBackupJobResources(List<BackupJobResource> backupJobResources) {
        this.mBackupJobResources = backupJobResources;
    }

    public BackupSchedule getSchedule() {
        return mSchedule;
    }

    public void setSchedule(BackupSchedule schedule) {
        this.mSchedule = schedule;
    }

    public BackupOption getOption() {
        return mOption;
    }

    public void setOption(BackupOption option) {
        this.mOption = option;
    }

    public String getSource() {
        return getBackupResource("Source");
    }

    public String getDest() {
        return getBackupResource("Destination");
    }

    public String getBackupResource(String key) {
        if (mBackupJobResources != null && key != null) {
            for (BackupJobResource br : mBackupJobResources) {
                if (key.equals(br.getItem())) {
                    return br.getShareNameOrProtocol();
                }
            }
        }
        return null;
    }
}
