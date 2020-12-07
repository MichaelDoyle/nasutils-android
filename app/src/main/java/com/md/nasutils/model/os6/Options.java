/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import java.util.Map;

/**
 * @author michaeldoyle
 */
public class Options {

    private String mContact;
    private String mLocation;

    public Options() {
        // necessary to keep xml deserialization happy
    }

    private Map<String, String> mOptions;

    public Map<String, String> getOptions() {
        return mOptions;
    }

    public void setOptions(Map<String, String> options) {
        this.mOptions = options;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        this.mContact = contact;
    }
}
