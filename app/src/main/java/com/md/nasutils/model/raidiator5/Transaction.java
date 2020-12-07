/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="transaction")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class Transaction {

    // for requests
    @Attribute(name="id", required=false)
    private String mId;
    
    // for responses
    @Attribute(name="ref-id", required=false)
    private String mRefId;
    
    @Attribute(name="ref-type", required=false)
    private Integer mType;
                
    @ElementList(entry="response", required=false, inline=true, empty=false)
    @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9")
    private List<Response> mResponses;
    
    @ElementListUnion({
          @ElementList(entry="command", type=Command.class, required=false, inline=true, empty=false),
          @ElementList(entry="custom", type=CustomRequest.class, required=false, inline=true, empty=false),
          @ElementList(entry="get", type=GetRequest.class, required=false, inline=true, empty=false),
          @ElementList(entry="set", type=SetRequest.class, required=false, inline=true, empty=false)})
    @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9")
    private List<Request> mRequests;
    
    public Transaction() {
        // necessary to keep xml deserialization happy
    }
    
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getRefId() {
        return mRefId;
    }

    public void setRefId(String refId) {
        this.mRefId = refId;
    }

    public Integer getType() {
        return mType;
    }

    public void setType(Integer type) {
        this.mType = type;
    }

    public List<Response> getResponses() {
        return mResponses;
    }

    public void setResponses(List<Response> responses) {
        this.mResponses = responses;
    }

    public List<Request> getRequests() {
        return mRequests;
    }

    public void setRequests(List<Request> requests) {
        this.mRequests = requests;
    }
    
    /**
     * Convenience method to retrieve a response by id
     * irrespective of the order in which the responses were received
     * 
     * @param refId
     * @return
     */
    public Response getResponse(String refId) {
        for (Response response : mResponses) {
            if (response.getRefId() != null && response.getRefId().equals(refId)) {
                return response;
            }
        }
        return null;
    }
}
