/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Dictionary", strict=false)
public class Dictionary implements ResultData {

    @ElementList(entry="Language", inline=true, required=false, empty=false)
    private List<Language> mLanguages;
    
    public Dictionary() {
        // necessary to keep xml deserialization happy
    }

    public List<Language> getLanguages() {
        return mLanguages;
    }

    public void setLanguages(List<Language> languages) {
        this.mLanguages = languages;
    }
}
