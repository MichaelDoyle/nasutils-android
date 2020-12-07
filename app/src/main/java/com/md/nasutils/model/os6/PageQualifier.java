/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="page", strict=false)
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class PageQualifier implements Qualifier {

    @Attribute(name="direction", required=false)
    int mDirection;
    
    @Attribute(name="start-index", required=false)
    int mStartIndex;
    
    @Attribute(name="count", required=false)
    int mCount;
    
    public PageQualifier() {
        // necessary to keep xml deserialization happy
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int direction) {
        this.mDirection = direction;
    }

    public int getStartIndex() {
        return mStartIndex;
    }

    public void setStartIndex(int startIndex) {
        this.mStartIndex = startIndex;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }
}
