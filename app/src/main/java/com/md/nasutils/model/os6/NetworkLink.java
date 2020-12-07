/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="NetworkLink", strict=false)
public class NetworkLink {
    
    @Element(name="link", required=false)
    private String mLink;
    
    @Element(name="speed", required=false)
    private Integer mSpeed;
    
    @Element(name="path", required=false)
    private String mPath;
    
    @Element(name="duplex", required=false)
    private String mDuplex;
    
    @Element(name="vid", required=false)
    private String mVid;
    
    @Element(name="iptype", required=false)
    private String mIpAddressType;
    
    @Element(name="ipv6type", required=false)
    private String mIpv6Addresstype;
    
    @Element(name="ip", required=false)
    private String mIpAddress;
    
    @Element(name="ipv6", required=false)
    private String mIpv6Address;
    
    //sic (ReadyNASOS 6)
    @Element(name="prefixlenght", required=false)
    private Integer mPrefixLength;
    
    // spelled correctly in ReadyDATAOS and 
    // bonded interfaces in ReadyNASOS 6
    @Element(name="prefixlength", required=false)
    private Integer mPrefixLengthBonded;
    
    @Element(name="mac", required=false)
    private String mMacAddress;
    
    @Element(name="subnet", required=false)
    private String mSubnet;
    
    @Element(name="mtu", required=false)
    private Integer mMtu;
    
    @Element(name="router", required=false)
    private String mRouter;
    
    @Element(name="router6", required=false)
    private String mRouter6;

    @Element(name="state", required=false)
    private String mState;
    
    @ElementList(name="dnscollection", entry="DNS", required=false, empty=false)
    private List<DnsServer> mDnsServers;
    
    /** Fields exclusive to bonded interfaces */
    
    @Element(name="jumbo", required=false)
    private String mJumbo;
    
    @Element(name="mode", required=false)
    private String mMode;
    
    @Element(name="policy", required=false)
    private String mPolicy;
    
    @Element(name="primary", required=false)
    private String mPrimary;
    
    @Element(name="over", required=false)
    private String mOver;
    
    @Element(name="ipv6_link", required=false)
    private String mIpv6Link;
    
    @Element(name="prefixlength_link", required=false)
    private String mPrefixLengthLink;
    
    /** ReadyDATA Fields */
    
    @Element(name="vnic_collection", required=false)
    private VirtualNetworkLinkCollection mVirtualNetworkLinkCollection;
    
    public NetworkLink() {
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

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getDuplex() {
        return mDuplex;
    }

    public void setDuplex(String duplex) {
        this.mDuplex = duplex;
    }

    public String getVid() {
        return mVid;
    }

    public void setVid(String vid) {
        this.mVid = vid;
    }

    public String getIpType() {
        return mIpAddressType;
    }

    public void setIpType(String ipType) {
        this.mIpAddressType = ipType;
    }

    public String getIpv6type() {
        return mIpv6Addresstype;
    }

    public void setIpv6type(String ipv6type) {
        this.mIpv6Addresstype = ipv6type;
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
        return mPrefixLength != null ? mPrefixLength : mPrefixLengthBonded;
    }

    public void setPrefixLength(Integer prefixLength) {
        this.mPrefixLength = prefixLength;
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

    public String getRouter6() {
        return mRouter6;
    }

    public void setRouter6(String router6) {
        this.mRouter6 = router6;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        this.mState = state;
    }

    public List<DnsServer> getDnsServers() {
        return mDnsServers;
    }

    public void setDnsServers(List<DnsServer> dnsServers) {
        this.mDnsServers = dnsServers;
    }

    /** Fields exclusive to bonded interfaces */

    public String getJumbo() {
        return mJumbo;
    }

    public void setJumbo(String jumbo) {
        this.mJumbo = jumbo;
    }

    public String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        this.mMode = mode;
    }

    public String getPolicy() {
        return mPolicy;
    }

    public void setPolicy(String policy) {
        this.mPolicy = policy;
    }

    public String getPrimary() {
        return mPrimary;
    }

    public void setPrimary(String primary) {
        this.mPrimary = primary;
    }

    public String getOver() {
        return mOver;
    }

    public void setOver(String over) {
        this.mOver = over;
    }

    public String getIpv6Link() {
        return mIpv6Link;
    }

    public void setIpv6Link(String opv6Link) {
        this.mIpv6Link = opv6Link;
    }

    public Integer getPrefixLengthBonded() {
        return mPrefixLengthBonded;
    }

    public void setPrefixLengthBonded(Integer prefixLengthBonded) {
        this.mPrefixLengthBonded = prefixLengthBonded;
    }

    public String getPrefixLengthLink() {
        return mPrefixLengthLink;
    }

    public void setPrefixLengthLink(String prefixLengthLink) {
        this.mPrefixLengthLink = prefixLengthLink;
    }

    public VirtualNetworkLinkCollection getVirtualNetworkLinkCollection() {
        return mVirtualNetworkLinkCollection;
    }

    public void setVirtualNetworkLinkCollection(VirtualNetworkLinkCollection virtualNetworkLinkCollection) {
        this.mVirtualNetworkLinkCollection = virtualNetworkLinkCollection;
    }
}
