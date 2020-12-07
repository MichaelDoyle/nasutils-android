/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Path("LocalApp_Collection")
@Root(name="Application", strict=false)
public class Application {

    @Attribute(name="resource-id", required=false)
    private String mResourceId;

    @Attribute(name="resource-type", required=false)
    private String mResourceType;

    @Attribute(name="state", required=false)
    private String mState;

    @Attribute(name="is-preinstalled", required=false)
    private int mIsPreinstalled;

    @Attribute(name="is-localapp", required=false)
    private int mIsLocalApp;

    @Attribute(name="is-uninitialized", required=false)
    private int mIsUninitialized;

    @Attribute(name="on-off", required=false)
    private String mOnOff;

    @Element(name="AppID", required=false)
    private String mAppId;

    @Element(name="Category", required=false)
    private String mCategory;

    @Element(name="Name", required=false)
    private String mName;

    @Element(name="Author", required=false)
    private String mAuthor;

    @Element(name="Version", required=false)
    private String mVersion;

    @Element(name="RequireReboot", required=false)
    private String mRequireReboot;

    @Element(name="ConfigURL", required=false)
    private String mConfigUrl;

    @Element(name="LaunchURL", required=false)
    private String mLaunchUrl;

    @ElementList(entry="ReservePort", inline=true, required=false, empty=false)
    private List<String> mReservePort;

    @Element(name="DebianPackage", required=false)
    private String mDebianPackage;

    @Element(name="ServiceName", required=false)
    private String mServiceName;

    @Element(name="Description", required=false)
    private String mDescription;

    public Application() {
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

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public int getIsPreinstalled() {
        return mIsPreinstalled;
    }

    public void setIsPreinstalled(int isPreinstalled) {
        this.mIsPreinstalled = isPreinstalled;
    }

    public int getIsLocalApp() {
        return mIsLocalApp;
    }

    public void setIsLocalApp(int isLocalApp) {
        this.mIsLocalApp = isLocalApp;
    }

    public int getIsUninitialized() {
        return mIsUninitialized;
    }

    public void setIsUninitialized(int isUninitialized) {
        this.mIsUninitialized = isUninitialized;
    }

    public String getOnOff() {
        return mOnOff;
    }

    public void setOnOff(String onOff) {
        this.mOnOff = onOff;
    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
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

    public String getRequireReboot() {
        return mRequireReboot;
    }

    public void setRequireReboot(String requireReboot) {
        this.mRequireReboot = requireReboot;
    }

    public String getConfigUrl() {
        return mConfigUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.mConfigUrl = configUrl;
    }

    public String getLaunchUrl() {
        return mLaunchUrl;
    }

    public void setLaunchUrl(String launchUrl) {
        this.mLaunchUrl = launchUrl;
    }

    public List<String> getReservePort() {
        return mReservePort;
    }

    public void setReservePort(List<String> reservePort) {
        this.mReservePort = reservePort;
    }

    public String getDebianPackage() {
        return mDebianPackage;
    }

    public void setDebianPackage(String debianPackage) {
        this.mDebianPackage = debianPackage;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        this.mServiceName = serviceName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
