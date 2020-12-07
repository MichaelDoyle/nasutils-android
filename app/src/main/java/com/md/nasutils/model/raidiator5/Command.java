/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="command")
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class Command extends Request {

    @Element(name="command_name", required=false)
    private String mCommandName;
    
    // Commands also have arguments in the format:
    //
    // <xs:args>
    //   <xs:arg>
    //     <Stop_Scrub>false</Stop_Scrub>
    //   </xs:arg>
    // </xs:args>
    //
    // We'll ignore for now, as we don't yet have a use case

    public Command() {
    }

    public String getCommandName() {
        return mCommandName;
    }

    public void setCommandName(String commandName) {
        this.mCommandName = commandName;
    }
}
