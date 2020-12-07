/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.util;

import com.md.nasutils.NasUtilsApplication;
import com.md.nasutils.R;

/**
 * Display values for Teaming Modes returned by ReadyNAS OS 6
 * 
 * @author michaeldoyle
 */
public enum TeamingMode {

    //        (ReadyNAS,    Display Value)
    LACP      ("lacp",      R.string.teaming_mode_lacp), 
    BACKUP    ("backup",    R.string.teaming_mode_backup), 
    TRANSMIT  ("transmit",  R.string.teaming_mode_transmit), 
    ADAPTIVE  ("adaptive",  R.string.teaming_mode_adaptive), 
    ROBIN     ("robin",     R.string.teaming_mode_robin), 
    XOR       ("xor",       R.string.teaming_mode_xor), 
    BROADCAST ("broadcast", R.string.teaming_mode_broadcast);

    private String mName;
    private int mStringId;
    
    private TeamingMode(String mName, int stringId) {
        this.mName = mName;
        this.mStringId = stringId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getStringId() {
        return mStringId;
    }

    public void setStringId(int stringId) {
        this.mStringId = stringId;
    }
    
    public String getDisplayName() {
        return NasUtilsApplication.getContext().getString(mStringId);
    }
    
    public static TeamingMode fromName(String name) {
        for (TeamingMode value : TeamingMode.values()) {
            if (name.equalsIgnoreCase(value.mName)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No TeamingMode for name " + name);
    }
}
