/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="error")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class Error {
    
    @Element(name="error-code")
    @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9")
    private Long mErrorCode;
    
    @Element(name="error-cause")
    @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9")
    private String mErrorCause;
    
    @Element(name="error-details")
    @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9")
    private String mErrorDetails;
    
    public Error() {
        // necessary to keep xml deserialization happy
    }

    public Long getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.mErrorCode = errorCode;
    }

    public String getErrorCause() {
        return mErrorCause;
    }

    public void setErrorCause(String errorCause) {
        this.mErrorCause = errorCause;
    }

    public String getErrorDetails() {
        return mErrorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.mErrorDetails = errorDetails;
    }
}
