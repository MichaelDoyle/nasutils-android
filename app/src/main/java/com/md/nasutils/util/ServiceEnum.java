/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.util;

import com.md.nasutils.NasUtilsApplication;
import com.md.nasutils.R;

/**
 * Display values for Services returned by ReadyNAS
 * 
 * @author michaeldoyle
 */
public enum ServiceEnum {

    //            (Service,        Display Value,        ID)
    CIFS          ("cifs",         R.string.service_cifs,               R.id.service_cifs),
    AFP           ("afp",          R.string.service_afp,                R.id.service_afp),
    NFS           ("nfs",          R.string.service_nfs,                R.id.service_nfs),
    FTP           ("ftp",          R.string.service_ftp,                R.id.service_ftp),
    DAAP          ("daap",         R.string.service_daap,             R.id.service_daap),
    SNMP          ("snmp",         R.string.service_snmp,               R.id.service_snmp),
    SSH           ("ssh",          R.string.service_ssh,                R.id.service_ssh),
    REPLICATE     ("replicate",    R.string.service_replicate,          R.id.service_replicate),
    SMART_NETWORK ("smartnetwork", R.string.service_smartnetwork, R.id.service_smartnetwork),
    UPNPD         ("upnpd",        R.string.service_upnpd,               R.id.service_upnpd),
    RSYNC         ("rsync",        R.string.service_rsync,              R.id.service_rsync),
    DLNA          ("dlna",         R.string.service_dlna,          R.id.service_dlna),
    HTTP          ("http",         R.string.service_http,               R.id.service_http),
    HTTPS         ("https",        R.string.service_https,              R.id.service_https),
    ANTI_VIRUS    ("anti-virus",   R.string.service_antivirus,         R.id.service_antivirus),
    READY_CLOUD   ("readycloud",   R.string.service_readycloud,        R.id.service_readycloud),
    REPLISYNC     ("replisync",    R.string.service_replisync,          R.id.service_replisync),
    BONJOUR       ("bonjour",      R.string.service_bonjour,            R.id.service_bonjour);
  
    private String mName;
    private int mStringId;
    private int mId;
    
    private ServiceEnum(String mName, int stringId, int id) {
        this.mName = mName;
        this.mStringId = stringId;
        this.mId = id;
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
    
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getDisplayName() {
        return NasUtilsApplication.getContext().getString(mStringId);
    }
    
    public static ServiceEnum fromName(String name) {
        for (ServiceEnum value : ServiceEnum.values()) {
            if (name.equalsIgnoreCase(value.mName)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No Service for name " + name);
    }
}
