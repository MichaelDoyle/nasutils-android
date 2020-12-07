/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import com.md.nasutils.service.readynas.Os6OptionsConverter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="Protocol")
public class Protocol {

    @Attribute(name="id", required=false)
    private String mId;
    
    @Attribute(name="resource-id", required=false)
    private String mResourceId;
    
    @Attribute(name="resource-type", required=false)
    private String mResourceType;
    
    @Attribute(name="enabled", required=false)
    private int mIsEnabled;
    
    @Attribute(name="raw-enabled", required=false)
    private int mIsEnabledRaw;
    
    @Attribute(name="status", required=false)
    private String mStatus;
    
    @Element(name="Options", required=false)
    @Convert(Os6OptionsConverter.class)
    private Options mOptions;
    
    @ElementList(entry="Share", inline=true, required=false, empty=false)
    @Path("Share_Collection")
    private List<Share> mShares;

    public Protocol() {
        // necessary to keep xml deserialization happy
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public int isEnabled() {
        return mIsEnabled;
    }

    public void setEnabled(int isEnabled) {
        this.mIsEnabled = isEnabled;
    }
    
    public int isEnabledRaw() {
        return mIsEnabledRaw;
    }

    public void setEnabledRaw(int isEnabledRaw) {
        this.mIsEnabledRaw = isEnabledRaw;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public Options getOptions() {
        return mOptions;
    }

    public void setOptions(Options options) {
        this.mOptions = options;
    }

    public List<Share> getShares() {
        return mShares;
    }

    public void setShares(List<Share> shares) {
        this.mShares = shares;
    }
}
