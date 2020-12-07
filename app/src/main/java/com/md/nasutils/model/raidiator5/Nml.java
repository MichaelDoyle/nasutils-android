/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="nml")
@NamespaceList({
        @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs"),
        @Namespace(reference="urn:netgear:nas:readynasd") })
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class Nml {
    
    @Attribute(name="src", required=false)
    private String mSrc;
    
    @Attribute(name="dst", required=false)
    private String mDst;
    
    @Attribute(name="locale", required=false)
    private String mLocale;
                
    @Element(name="transaction", required=false)
    @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9")
    private Transaction mTransaction;
    
    public Nml() {
        // necessary to keep xml deserialization happy
    }
    
    public String getSrc() {
        return mSrc;
    }

    public void setSrc(String src) {
        this.mSrc = src;
    }

    public String getDst() {
        return mDst;
    }

    public void setDst(String dst) {
        this.mDst = dst;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        this.mLocale = locale;
    }

    public Transaction getTransaction() {
        return mTransaction;
    }

    public void setTransaction(Transaction transaction) {
        this.mTransaction = transaction;
    }
}
