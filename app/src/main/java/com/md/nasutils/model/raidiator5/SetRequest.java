/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="set")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class SetRequest extends Request {

    @ElementUnion({ 
        @Element(name = "Protocol_Collection", type = ProtocolCollection.class, required = false) 
        })
    private ResultData mDocument;
    
    public SetRequest() {
    }

    public ResultData getDocument() {
        return mDocument;
    }

    public void setDocument(ResultData document) {
        this.mDocument = document;
    }
}
