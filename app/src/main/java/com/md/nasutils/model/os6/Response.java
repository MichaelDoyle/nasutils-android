/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="response")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class Response {
    
    @Attribute(name="ref-id", required=false)
    private String mRefId;
    
    @Attribute(name="status", required=false)
    private String mStatus;
    
    @Element(name="result", required=false)
    private Result mResult;
    
    @Element(name="error", required=false)
    private Error mError;
    
    public Response() {
        // necessary to keep xml deserialization happy
    }
    
    public String getRefId() {
        return mRefId;
    }

    public void setRefId(String refId) {
        this.mRefId = refId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        this.mResult = result;
    }
    
    public Error getError() {
        return mError;
    }

    public void setError(Error error) {
        this.mError = error;
    }
}
