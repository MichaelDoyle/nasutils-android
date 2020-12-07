/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Interface", strict=false)
public class NetworkLink {
    
    @Element(name="Link_Status", required=false)
    private String mStatus;
    
    @Element(name="IP_Assignment", required=false)
    private String mIpType;
    
    @Element(name="Name", required=false)
    private String mName;
    
    @Element(name="Net_Mask", required=false)
    private String mNetmask;
    
    @Element(name="Broadcast", required=false)
    private String mBroadcast;
    
    @Element(name="MTU", required=false)
    private String mMtu;
    
    @Element(name="HW_Addr", required=false)
    private String mMacAddress;
    
    @Element(name="IP_Address", required=false)
    private String mIpAddress;
    
    public NetworkLink() {
        // necessary to keep xml deserialization happy
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getIpType() {
        return mIpType;
    }

    public void setIpType(String ipType) {
        this.mIpType = ipType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNetmask() {
        return mNetmask;
    }

    public void setNetmask(String netmask) {
        this.mNetmask = netmask;
    }

    public String getBroadcast() {
        return mBroadcast;
    }

    public void setBroadcast(String broadcast) {
        this.mBroadcast = broadcast;
    }

    public String getMtu() {
        return mMtu;
    }

    public void setMtu(String mtu) {
        this.mMtu = mtu;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.mIpAddress = ipAddress;
    }
}
