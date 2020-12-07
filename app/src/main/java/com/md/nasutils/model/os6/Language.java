/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Map;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Language", strict=false)
public class Language {

    @Attribute(name="code", required=false)
    private String mCode;
    
    @Attribute(name="spell", required=false)
    private String mName;
    
    @ElementMap(entry="item", key="key", attribute=true, inline=true, required=false, data=true)
    private Map<String, String> mItems;

    public Language() {
        // necessary to keep xml deserialization happy
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        this.mCode = code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Map<String, String> getItems() {
        return mItems;
    }

    public void setItems(Map<String, String> items) {
        this.mItems = items;
    }
}
