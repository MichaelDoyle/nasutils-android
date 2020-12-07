/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyDATA OS
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="vnic", strict=false)
public class VirtualNetworkLink {
    
    @Element(name="link", required=false)
    private String mLink;
    
    @Element(name="speed", required=false)
    private Integer mSpeed;
    
    @Element(name="macaddrtype", required=false)
    private String mMacAdressType;
    
    @Element(name="vid", required=false)
    private String mVid;
    
    @Element(name="priority", required=false)
    private Integer mPriority;
    
    @Element(name="state", required=false)
    private String mState;
    
    @Element(name="over", required=false)
    private String mOver;
    
    @Element(name="iptype", required=false)
    private String mIpType;
    
    @Element(name="ip", required=false)
    private String mIpAddress;

    @Element(name="ipv6", required=false)
    private String mIpv6Address;
    
    @Element(name="prefixlength", required=false)
    private Integer mPrefixLength;
    
    @Element(name="ipv6global", required=false)
    private String mIpv6global;
    
    @Element(name="prefixglobal", required=false)
    private Integer mPrefixglobal;
    
    @Element(name="mac", required=false)
    private String mMacAddress;
    
    @Element(name="subnet", required=false)
    private String mSubnet;
    
    @Element(name="mtu", required=false)
    private Integer mMtu;
    
    @Element(name="router", required=false)
    private String mRouter;
    
    @ElementList(name="dnscollection", entry="DNS", required=false, empty=false)
    private List<DnsServer> mDnsServers;
    
    public VirtualNetworkLink() {
        // necessary to keep xml deserialization happy
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public Integer getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Integer speed) {
        this.mSpeed = speed;
    }

    public String getMacAdressType() {
        return mMacAdressType;
    }

    public void setMacAdressType(String macAdressType) {
        this.mMacAdressType = macAdressType;
    }

    public String getVid() {
        return mVid;
    }

    public void setVid(String vid) {
        this.mVid = vid;
    }

    public Integer getPriority() {
        return mPriority;
    }

    public void setPriority(Integer priority) {
        this.mPriority = priority;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public String getOver() {
        return mOver;
    }

    public void setOver(String over) {
        this.mOver = over;
    }

    public String getIpType() {
        return mIpType;
    }

    public void setIpType(String ipAddressType) {
        this.mIpType = ipAddressType;
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

    public Integer getPrefixLength() {
        return mPrefixLength;
    }

    public void setPrefixLength(Integer prefixLength) {
        this.mPrefixLength = prefixLength;
    }

    public String getIpv6global() {
        return mIpv6global;
    }

    public void setIpv6global(String ipv6global) {
        this.mIpv6global = ipv6global;
    }

    public Integer getPrefixglobal() {
        return mPrefixglobal;
    }

    public void setPrefixglobal(Integer prefixglobal) {
        this.mPrefixglobal = prefixglobal;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }

    public String getSubnet() {
        return mSubnet;
    }

    public void setSubnet(String subnet) {
        this.mSubnet = subnet;
    }

    public Integer getMtu() {
        return mMtu;
    }

    public void setMtu(Integer mtu) {
        this.mMtu = mtu;
    }

    public String getRouter() {
        return mRouter;
    }

    public void setRouter(String router) {
        this.mRouter = router;
    }

    public List<DnsServer> getDnsServers() {
        return mDnsServers;
    }

    public void setDnsServers(List<DnsServer> dnsServers) {
        this.mDnsServers = dnsServers;
    }
}
