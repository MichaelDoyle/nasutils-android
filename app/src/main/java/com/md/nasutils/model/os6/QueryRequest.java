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
@Root(strict=false, name="query")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class QueryRequest extends Request {

    @Path("xs:qualifier")
    @ElementUnion({ 
        @Element(name="page", type=PageQualifier.class, required=false)})
    private Qualifier mQualifier;
    
    public QueryRequest() {
        // necessary to keep xml deserialization happy
    }

    public Qualifier getQualifier() {
        return mQualifier;
    }

    public void setQualifier(Qualifier qualifier) {
        this.mQualifier = qualifier;
    }
}
