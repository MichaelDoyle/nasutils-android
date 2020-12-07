/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="result")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class Result {

    @Path("xs:get-s")
    @ElementUnion({
        @Element(name="BackupJob_Collection", type=BackupJobCollection.class, required=false),
        @Element(name="Dictionary", type=Dictionary.class, required=false),
        @Element(name="Health_Collection", type=HealthCollection.class, required=false),
        @Element(name="Network", type=Network.class, required=false),
        @Element(name="Protocol_Collection", type=ProtocolCollection.class, required=false),
        @Element(name="SMARTInfo", type=SmartInfo.class, required=false),
        @Element(name="SystemInfo", type=SystemInfo.class, required=false),
        @Element(name="Volume_Collection", type=VolumeCollection.class, required=false),
        @Element(name="Log_Collection", type=LogCollection.class, required=false),
        @Element(name="addon_Collection", type=AddonCollection.class, required=false)})
    private ResultData mGetResults;
    
    @Element(name="error", required=false)
    private Error mError;
    
    // We may also get set output - ignore for now:
    //
    // <xs:set-s resource-id="Protocols" resource-type="Protocol"/>
    
    public Result() {
        // necessary to keep xml deserialization happy
    }

    public ResultData getGetResults() {
        return mGetResults;
    }

    public void setGetResults(ResultData getResults) {
        this.mGetResults = getResults;
    }

    public Error getError() {
        return mError;
    }

    public void setError(Error error) {
        this.mError = error;
    }
}
