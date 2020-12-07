/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="Network", strict=false)
public class Network implements ResultData {
  
    @Path("General_Settings")
    @Element(name="Hostname", required=false)
    private String mHostname;
    
    @Path("General_Settings")
    @Element(name="Workgroup", required=false)
    private String mWorkgroup;
    
    @Path("General_Settings")
    @Element(name="Gateway", required=false)
    private String mGateway;
    
    @Path("DNS_Settings")
    @Element(name="Domain", required=false)
    private String mDnsDomain;
    
    @Path("DNS_Settings")
    @ElementList(name="DNS_List", entry="DNS", required=false, empty=false)
    private List<DnsServer> mDnsServers;
    
    @Path("Interface_Settings")
    @ElementList(name="Interface_List", entry="Interface", required=false, empty=false)
    private List<NetworkLink> mNetworkLinks;
    
    public Network() {
        // necessary to keep xml deserialization happy
    }

    public String getHostname() {
        return mHostname;
    }

    public void setHostname(String hostname) {
        this.mHostname = hostname;
    }

    public String getWorkgroup() {
        return mWorkgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.mWorkgroup = workgroup;
    }

    public String getGateway() {
        return mGateway;
    }

    public void setGateway(String gateway) {
        this.mGateway = gateway;
    }

    public String getDnsDomain() {
        return mDnsDomain;
    }

    public void setDnsDomain(String dnsDomain) {
        this.mDnsDomain = dnsDomain;
    }

    public List<DnsServer> getDnsServers() {
        return mDnsServers;
    }

    public void setDnsServers(List<DnsServer> dnsServers) {
        this.mDnsServers = dnsServers;
    }

    public List<NetworkLink> getNetworkLinks() {
        return mNetworkLinks;
    }

    public void setNetworkLinks(List<NetworkLink> networkLinks) {
        this.mNetworkLinks = networkLinks;
    }
}
