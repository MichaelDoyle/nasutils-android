/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="result")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class Result {

    @Path("xs:get-s")
    @ElementUnion({
        @Element(name="aggregate_collection", type=AggregateCollection.class, required=false),
        @Element(name="BackupJob_Collection", type=BackupJobCollection.class, required=false),
        @Element(name="Dictionary", type=Dictionary.class, required=false),
        @Element(name="DiskEnclosure_Collection", type=DiskEnclosureCollection.class, required=false),
        @Element(name="Health_Collection", type=HealthCollection.class, required=false),
        @Element(name="Network", type=Network.class, required=false),
        @Element(name="network_link_collection", type=NetworkLinkCollection.class, required=false),
        @Element(name="Protocol_Collection", type=ProtocolCollection.class, required=false),
        @Element(name="SMARTInfo", type=SmartInfo.class, required=false),
        @Element(name="SystemInfo", type=SystemInfo.class, required=false),
        @Element(name="Volume_Collection", type=VolumeCollection.class, required=false),
        @Element(name="LocalApp_Collection", type=LocalAppCollection.class, required=false)})
    private ResultData mGetResults;

    @Path("xs:query-s")
    @ElementUnion({ 
        @Element(name="Log_Collection", type=LogCollection.class, required=false)})
    private ResultData mQueryResults;

    // We may also get command output - ignore for now:
    //
    // <xs:command-s resource-id="Protocols" resource-type="Protocol">
    //   <xs:command-output/>
    //   <xs:command-error/>
    // </xs:command-s>
    
    @Element(name="error", required=false)
    private Error mError;
    
    public Result() {
        // necessary to keep xml deserialization happy
    }

    public ResultData getGetResults() {
        return mGetResults;
    }

    public void setGetResults(ResultData getResults) {
        this.mGetResults = getResults;
    }

    public ResultData getQueryResults() {
        return mQueryResults;
    }

    public void setQueryResults(ResultData queryResults) {
        this.mQueryResults = queryResults;
    }

    public Error getError() {
        return mError;
    }

    public void setError(Error error) {
        this.mError = error;
    }
    
    /**
     * Convenience method to return the resulting data, no matter
     * which original request type (get, query, etc) was made
     * 
     * @return ResultData
     */
    public ResultData getData() {
        
        if(mGetResults != null) {
            return mGetResults;
        } else if (mQueryResults != null) {
            return mQueryResults;
        }

        return null;
    }
}
