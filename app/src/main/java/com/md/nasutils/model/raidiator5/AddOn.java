/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Path("addon_Collection")
@Root(name="addon", strict=false)
public class AddOn {

    @Element(name="name", required=false)
    private String mName;

    @Element(name="launch_url", required=false)
    private String mLaunchUrl;

    @Element(name="status", required=false)
    private String mStatus;

    @Element(name="friendly_name", required=false)
    private String mFriendlyName;

    @Element(name="version", required=false)
    private String mVersion;

    @Element(name="current_url", required=false)
    private String mCurrentUrl;

    @Element(name="detail_url", required=false)
    private String mDetailUrl;

    @Element(name="description", required=false)
    private String mDescription;

    @Element(name="icon", required=false)
    private String mIcon;

    public AddOn() {
        // necessary to keep xml deserialization happy
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLaunchUrl() {
        return mLaunchUrl;
    }

    public void setLaunchUrl(String launchUrl) {
        this.mLaunchUrl = launchUrl;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getFriendlyName() {
        return mFriendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.mFriendlyName = friendlyName;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }
}
