/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

/**
 * Domain object representing network interfaces.
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class NetworkInterface {
    
    @Transient
    private String mInterfaceName;
    
    // RAIDiator 4.2
    @Element(name="interface_status", required=false)
    private String mInterfaceStatus;
    
    // RAIDiator 4.1 
    @Element(name="status", required=false)
    private String mStatus;
    
    @Element(name="interface_type", required=false)
    private String mInterfaceType;
    
    @Element(name="mac_address", required=false)
    private String mMacAddress;
    
    @Element(name="ip_address", required=false)
    private String mIpAddress;
    
    @Element(name="ipv6_address", required=false)
    private String mIpv6Address;
    
    @Element(name="ip_address_type", required=false)
    private String mIpAddressType;
    
    @Element(name="ipv6_address_type", required=false)
    private String mIpv6AddressType;
    
    public NetworkInterface() {
        // necessary to keep xml deserialization happy
    }
    
    public String getInterfaceName() {
        return mInterfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.mInterfaceName = interfaceName;
    }

    public String getInterfaceStatus() {
        return mInterfaceStatus;
    }
    
    public void setInterfaceStatus(String interfaceStatus) {
        this.mInterfaceStatus = interfaceStatus;
    }
    
    public String getInterfaceType() {
        return mInterfaceType;
    }
    
    public void setInterfaceType(String interfaceType) {
        this.mInterfaceType = interfaceType;
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
    
    public String getIpv6Address() {
        return mIpv6Address;
    }
    
    public void setIpv6Address(String ipv6Address) {
        this.mIpv6Address = ipv6Address;
    }
    
    public String getIpAddressType() {
        return mIpAddressType;
    }
    
    public void setIpAddressType(String ipAddressType) {
        this.mIpAddressType = ipAddressType;
    }
    
    public String getIpv6AddressType() {
        return mIpv6AddressType;
    }
    
    public void setIpv6AddressType(String ipv6AddressType) {
        this.mIpv6AddressType = ipv6AddressType;
    }
    
    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
    
    /** 
     * <p>Post deserialization processing</p> 
     */
    @Commit
    public void postDeserialization() {
        
        // For RAIDiator 4.1 use Interface Type as IP Address Type for consistency
        if (mIpAddressType == null && mInterfaceType != null) {
            mIpAddressType = mInterfaceType;
            mInterfaceType = null;
        }
        
        // For RAIDiator 4.1 use Status as Interface Status
        if (mStatus != null) {
            mInterfaceStatus = mStatus;
        }
    }
}
