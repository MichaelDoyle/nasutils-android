/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

/**
 * Domain object representing a Backup Job
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class BackupJob {

    @Element(name="id", required=false)
    private String mId;

    @Element(name="value", required=false)
    @Path("job")
    private String mJob;

    // <![CDATA[ <img src="/images/LED/OK.gif">&nbsp;Completed<br>Thu Sep  4 22:38 ]]>
    @Element(name="value", required=false, data=true)
    @Path("status")
    private String mStatus;

    @Transient
    private String mStatusDescription;

    // ![CDATA[ Daily <br> Every 24 hr <br> between 20-23]]>
    @Element(name="value", required=false, data=true)
    @Path("when")
    private String mWhen;

    // <![CDATA[[dropzone]//Boston Pictures <br> [backup]//Test]]>
    @Element(name="value", required=false, data=true)
    @Path("source_dest")
    private String mSourceDest;

    @Element(name="value", required=false)
    @Path("enable")
    private int mEnable;

    @Transient
    private String mSource;

    @Transient
    private String mDest;

    public BackupJob() {
        // necessary to keep xml deserialization happy
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getJob() {
        return mJob;
    }

    public void setJob(String job) {
        this.mJob = job;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getWhen() {
        return mWhen;
    }

    public void setWhen(String when) {
        this.mWhen = when;
    }

    public String getSourceDest() {
        return mSourceDest;
    }

    public void setSourceDest(String sourceDest) {
        this.mSourceDest = sourceDest;
    }

    public int getEnable() {
        return mEnable;
    }

    public void setEnable(int enable) {
        this.mEnable = enable;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        this.mSource = source;
    }

    public String getDest() {
        return mDest;
    }

    public void setDest(String dest) {
        this.mDest = dest;
    }

    public String getStatusDescription() {
        return mStatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.mStatusDescription = statusDescription;
    }

    /** <p>Post deserialization processing</p> */
    @Commit
    public void postDeserialization() {
        mStatusDescription = cleanHtml(mStatus);
        mWhen = cleanHtml(mWhen);

        if (mSourceDest != null) {
            String[] splits = mSourceDest.split("<br>");
            if (splits.length > 1) {
                mSource = splits[0].trim();
                mDest = splits[1].trim();
            }
        }
    }

    private String cleanHtml(String in) {
        if (in == null) {
            return null;
        }

        return in.replace("&nbsp;", "")
                .replace("<br>", " ")
                .replaceAll("\\<.*?>","")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
