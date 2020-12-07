/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Domain object representing an Addon
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class AddOn {

    @Element(name="name", required=false, data=true)
    private String mName;

    @Element(name="name_lc", required=false, data=true)
    private String mDisplayName;

    @Element(name="start", required=false, data=true)
    private String mStart;

    @Element(name="stop", required=false, data=true)
    private String mStop;

    // off, on
    @Element(name="status", required=false)
    private String mStatus;

    // OK, not_present
    @Element(name="running", required=false)
    private String mRunning;

    @Element(name="version", required=false, data=true)
    private String mVersion;

    @Element(name="current_url", required=false, data=true)
    private String mCurrentUrl;

    @Element(name="detail_url", required=false, data=true)
    private String mDetailUrl;

    @Element(name="icon_url", required=false, data=true)
    private String mIconlUrl;

    public AddOn() {
        // necessary to keep xml deserialization happy
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        this.mStart = start;
    }

    public String getStop() {
        return mStop;
    }

    public void setStop(String stop) {
        this.mStop = stop;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getRunning() {
        return mRunning;
    }

    public void setRunning(String running) {
        this.mRunning = running;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getCurrentUrl() {
        return mCurrentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.mCurrentUrl = currentUrl;
    }

    public String getDetailUrl() {
        return mDetailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.mDetailUrl = detailUrl;
    }

    public String getIconlUrl() {
        return mIconlUrl;
    }

    public void setIconlUrl(String iconlUrl) {
        this.mIconlUrl = iconlUrl;
    }
}
