/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Root domain object representing comprehensive NAS status details.
 *
 * Currently fetched from:
 * <pre>/get_handler/?OPERATION=get&PAGE=Nasstate&OUTER_TAB=NONE&INNER_TAB=NONE&SECTION=ALL</pre>
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class NasStatus {
    
    @Element(name="nas_serial", required=false)
    @Path("content")
    private String mNasSerial;
    
    @Element(name="hostname", required=false)
    @Path("content")
    private String mHostname;
    
    @Element(name="gateway", required=false)
    @Path("content")
    private String mGateway;
    
    @Element(name="dns_server_0", required=false)
    @Path("content")
    private String mDnsServer0;
    
    @Element(name="dns_server_1", required=false)
    @Path("content")
    private String mDnsServer1;
    
    @Element(name="raid_type", required=false)
    @Path("content")
    private String mRaidType;
    
    @Element(name="model_info", required=false)
    @Path("content")
    private ModelInfo mModelInfo;
    
    // Netgear puts these in the xml payload
    // as first class elements <eth0>, <eth1> etc
    private NetworkInterface mEth0;
    private NetworkInterface mEth1;
    private NetworkInterface mEth2;
    private NetworkInterface mEth3;
    private NetworkInterface mBond0;
    private NetworkInterface mBond1;
    
    @Transient
    private List<NetworkInterface> mNetworkInterfaces;
    
    @ElementList(name="services", required=false, empty=false)
    @Path("content")
    private List<Service> mServices;
    
    @ElementList(name="volumes", inline=true, required=false, empty=false)
    @Path("content/available_volumes")
    private List<Volume> mVolumes;
    
    @ElementList(name="drives", inline=true, required=false, empty=false)
    @Path("content/drives")
    private List<Drive> mDrives;
    
    @ElementList(name="fans", entry="fan", inline=true, required=false, empty=false)
    @Path("content/fans")
    private List<ComponentStatus> mFans;
    
    @ElementList(name="temps", entry="temp", inline=true, required=false, empty=false)
    @Path("content/temps")
    private List<ComponentStatus> mTemps;

    public NasStatus() {
        // necessary to keep xml deserialization happy
    }
    
    public String getNasSerial() {
        return mNasSerial;
    }
    
    public void setNasSerial(String nasSerial) {
        this.mNasSerial = nasSerial;
    }
    
    public String getHostname() {
        return mHostname;
    }
    
    public void setHostname(String hostname) {
        this.mHostname = hostname;
    }
    
    public String getGateway() {
        return mGateway;
    }
    
    public void setGateway(String gateway) {
        this.mGateway = gateway;
    }
    
    public String getDnsServer0() {
        return mDnsServer0;
    }
    
    public void setDnsServer0(String dnsServer0) {
        this.mDnsServer0 = dnsServer0;
    }
    
    public String getDnsServer1() {
        return mDnsServer1;
    }
    
    public void setDnsServer1(String dnsServer1) {
        this.mDnsServer1 = dnsServer1;
    }
    
    public String getRaidType() {
        return mRaidType;
    }
    
    public void setRaidType(String raidType) {
        this.mRaidType = raidType;
    }
    
    public ModelInfo getModelInfo() {
        return mModelInfo;
    }
    
    public void setModelInfo(ModelInfo modelInfo) {
        this.mModelInfo = modelInfo;
    }

    @Element(name="eth0", required=false)
    public NetworkInterface getEth0() {
        return mEth0;
    }

    /**
     * Note that this method will also enrich {@link NetworkInterface} eth0
     * by setting its interfaceName with the String "eth0".
     * 
     * @param eth0
     */
    @Element(name="eth0", required=false)
    @Path("content/network_interfaces")
    public void setEth0(NetworkInterface eth0) {
        this.mEth0 = eth0;
        eth0.setInterfaceName("eth0");
    }

    @Element(name="eth1", required=false)
    public NetworkInterface getEth1() {
        return mEth1;
    }

    /**
     * Note that this method will also enrich {@link NetworkInterface} eth1
     * by setting its interfaceName with the String "eth1". 
     * 
     * @param eth1
     */
    @Element(name="eth1", required=false)
    @Path("content/network_interfaces")
    public void setEth1(NetworkInterface eth1) {
        this.mEth1 = eth1;
        eth1.setInterfaceName("eth1");
    }

    @Element(name="eth2", required=false)
    public NetworkInterface getEth2() {
        return mEth2;
    }

    /**
     * Note that this method will also enrich {@link NetworkInterface} eth2
     * by setting its interfaceName with the String "eth2". 
     * 
     * @param eth2
     */
    @Element(name="eth2", required=false)
    @Path("content/network_interfaces")
    public void setEth2(NetworkInterface eth2) {
        this.mEth2 = eth2;
        this.mEth2.setInterfaceName("eth2");

    }
    
    @Element(name="eth3", required=false)
    public NetworkInterface getEth3() {
        return mEth3;
    }

    /**
     * Note that this method will also enrich {@link NetworkInterface} eth3
     * by setting its interfaceName with the String "eth3".
     * 
     * @param eth3
     */
    @Element(name="eth3", required=false)
    @Path("content/network_interfaces")
    public void setEth3(NetworkInterface eth3) {
        this.mEth3 = eth3;
        this.mEth3.setInterfaceName("eth3");
    }
    
    @Element(name="bond0", required=false)
    public NetworkInterface getBond0() {
        return mBond0;
    }

    /**
     * Note that this method will also enrich {@link NetworkInterface} bond0
     * by setting its interfaceName with the String "bond0".
     * 
     * @param bond0
     */
    @Element(name="bond0", required=false)
    @Path("content/network_interfaces")
    public void setBond0(NetworkInterface bond0) {
        this.mBond0 = bond0;
        this.mBond0.setInterfaceName("bond0");
    }
    
    @Element(name="bond1", required=false)
    public NetworkInterface getBond1() {
        return mBond1;
    }

    /**
     * Note that this method will also enrich {@link NetworkInterface} bond1
     * by setting its interfaceName with the String "bond1".
     * 
     * @param bond1
     */
    @Element(name="bond1", required=false)
    @Path("content/network_interfaces")
    public void setBond1(NetworkInterface bond1) {
        this.mBond1 = bond1;
        this.mBond1.setInterfaceName("bond1");
    }
        
    public List<Service> getServices() {
        return mServices;
    }
    
    public void setServices(List<Service> services) {
        this.mServices = services;
    }
    
    public List<Volume> getVolumes() {
        return mVolumes;
    }
    
    public void setVolumes(List<Volume> volumes) {
        this.mVolumes = volumes;
    }
    
    public List<Drive> getDrives() {
        return mDrives;
    }
    
    public void setDrives(List<Drive> drives) {
        this.mDrives = drives;
    }
    
    public List<ComponentStatus> getFans() {
        return mFans;
    }

    public void setFans(List<ComponentStatus> fans) {
        this.mFans = fans;
    }

    public List<ComponentStatus> getTemps() {
        return mTemps;
    }

    public void setTemps(List<ComponentStatus> temps) {
        this.mTemps = temps;
    }
    
    /** <p>Post deserialization processing</p> */
    @Commit
    public void postDeserialization() {
        sortDrivesByChannel();
        buildNetworkInterfacesList();
    }
    
    public void sortDrivesByChannel() { 
        Collections.sort(mDrives, new Comparator<Drive>() {
            @Override
            public int compare(Drive lhs, Drive rhs) {
                return lhs.getChannel() - rhs.getChannel();
            }
        });
    }
    
    /**
     * Netgear makes it a pain to get a nice list of network interfaces
     * since each will be in the XML payload as a first class element: 
     * {@code<eth0>, <eth1>} etc. This method creates a nice list so that 
     * we can iterate over the interfaces in a more generic fashion.
     */
    private void buildNetworkInterfacesList() {
        mNetworkInterfaces = new ArrayList<>();
        addIfNotNull(mNetworkInterfaces, mEth0);
        addIfNotNull(mNetworkInterfaces, mEth1);
        addIfNotNull(mNetworkInterfaces, mEth2);
        addIfNotNull(mNetworkInterfaces, mEth3);
        addIfNotNull(mNetworkInterfaces, mBond0);
        addIfNotNull(mNetworkInterfaces, mBond1);
    }
    
    private <T> void addIfNotNull(List<T> list, T obj) {
        if(obj!=null) {
            list.add(obj);
        }
    }

    /**
     * Convenience method to get statuses for all components
     * 
     * @return list of ComponentStatus sorted by index
     */
    public List<ComponentStatus> getComponentStatuses() {
        List<ComponentStatus> statuses = new ArrayList<>();
        statuses.addAll(mFans);
        statuses.addAll(mTemps);
        
        Collections.sort(statuses, new Comparator<ComponentStatus>() {
            @Override
            public int compare(ComponentStatus lhs, ComponentStatus rhs) {
                return lhs.getIndex() - rhs.getIndex();
            }
        });
        
        return statuses;
    }

    public List<NetworkInterface> getNetworkInterfaces() {
        return mNetworkInterfaces;
    }
}
