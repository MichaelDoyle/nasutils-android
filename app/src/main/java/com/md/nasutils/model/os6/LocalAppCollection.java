/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="LocalApp_Collection")
public class LocalAppCollection implements ResultData {

    @ElementList(entry="Application", inline=true, required=false, empty=false)
    private List<Application> mApplications;

    public LocalAppCollection() {
        // necessary to keep xml deserialization happy
    }

    public List<Application> getApplications() {
        return mApplications;
    }

    public void setApplications(List<Application> applications) {
        this.mApplications = applications;
    }
    
    /**
     * Convenience method to retrieve an app by resourceId
     * 
     * @param resourceId
     * @return
     */
    public Application getBackUpJob(String resourceId) {
        for (Application a : mApplications) {
            if (a.getResourceId() != null && a.getResourceId().equals(resourceId)) {
                return a;
            }
        }
        return null;
    }
}
