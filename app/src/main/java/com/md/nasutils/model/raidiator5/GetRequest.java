/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="get")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class GetRequest extends Request {

    @Element(name="request", required=false)
    private FsBrokerRequest mFsBrokerRequest;
    
    public GetRequest() {
    }

    public FsBrokerRequest getFsBrokerRequest() {
        return mFsBrokerRequest;
    }

    public void setFsBrokerRequest(FsBrokerRequest fsBrokerRequest) {
        this.mFsBrokerRequest = fsBrokerRequest;
    }
}
