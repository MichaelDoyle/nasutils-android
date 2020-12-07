/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="Shutdown")
public class Shutdown {

    @Attribute(name="fsck", required=false)
    private String mFsck;
    
    @Attribute(name="halt", required=false)
    private String mHalt;
    
    public Shutdown() {
    }

    public String getFsck() {
        return mFsck;
    }

    public void setFsck(String fsck) {
        this.mFsck = fsck;
    }

    public String getHalt() {
        return mHalt;
    }

    public void setHalt(String halt) {
        this.mHalt = halt;
    }
}
