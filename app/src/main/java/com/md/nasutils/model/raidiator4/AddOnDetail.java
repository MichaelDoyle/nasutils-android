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
@Root(name="addon", strict=false)
public class AddOnDetail {

    @Element(name="name", required=false)
    private String mName;

    @Element(name="friendly_name", required=false)
    private String mFriendlyName;

    @Element(name="author", required=false)
    private String mAuthor;

    @Element(name="version", required=false)
    private String mVersion;

    @Element(name="get_url", required=false)
    private String mGetUrl;

    @Element(name="set_url", required=false)
    private String mSetUrl;

    @Element(name="preaction", required=false)
    private String mPreAction;

    @Element(name="onloadaction", required=false)
    private String mOnLoadAction;

    @Element(name="current_url", required=false)
    private String mCurrentUrl;

    public AddOnDetail() {
        // necessary to keep xml deserialization happy
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getFriendlyName() {
        return mFriendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.mFriendlyName = friendlyName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getGetUrl() {
        return mGetUrl;
    }

    public void setGetUrl(String getUrl) {
        this.mGetUrl = getUrl;
    }

    public String getSetUrl() {
        return mSetUrl;
    }

    public void setSetUrl(String setUrl) {
        this.mSetUrl = setUrl;
    }

    public String getPreAction() {
        return mPreAction;
    }

    public void setPreAction(String preAction) {
        this.mPreAction = preAction;
    }

    public String getOnLoadAction() {
        return mOnLoadAction;
    }

    public void setOnLoadAction(String onLoadAction) {
        this.mOnLoadAction = onLoadAction;
    }

    public String getCurrentUrl() {
        return mCurrentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.mCurrentUrl = currentUrl;
    }
}
