/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="custom")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class CustomRequest extends Request {

    @Attribute(name="name", required=false)
    private String mName;
    
    @Element(name="Shutdown", required=false)
    private Shutdown mShutdown;

    public CustomRequest() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Shutdown getShutdown() {
        return mShutdown;
    }

    public void setShutdown(Shutdown shutdown) {
        this.mShutdown = shutdown;
    }
}
