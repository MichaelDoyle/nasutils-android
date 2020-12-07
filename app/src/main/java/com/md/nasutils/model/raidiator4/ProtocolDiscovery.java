/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**  
 * Annotated for parsing the XML output returned by ReadyNAS FrontView using the
 * Simple XML Framework.
 * 
 * PAGE=Services&OUTER_TAB=tab_discovery&INNER_TAB=NONE&OPERATION=get
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class ProtocolDiscovery {
    
    // ------------------------ Bonjour ------------------------ //

    @Element(name="value", required=false)
    @Path("content/bonjour_enabled")
    private int mIsBonjourEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/bonjour_enabled")
    private String mIsBonjourEnabledOnUi;
        
    @Element(name="value", required=false)
    @Path("content/bonjour_advertise_printer")
    private String mIsBonjourAdvertisePrinterEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/bonjour_advertise_printer")
    private String mIsBonjourAdvertisePrinterEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/bonjour_advertise_afp")
    private String mIsBonjourAdvertiseAfpEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/bonjour_advertise_afp")
    private String mIsBonjourAdvertiseAfpEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/bonjour_advertise_frontview")
    private String mIsBonjourAdvertiseFrontviewEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/bonjour_advertise_frontview")
    private String mIsBonjourAdvertiseFrontviewEnabledOnUi;

    // ------------------------ UPnP ------------------------ //

    @Element(name="value", required=false)
    @Path("content/upnp_enabled")
    private int mIsUpnpEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/upnp_enabled")
    private String mIsUpnpEnabledOnUi;
    
    public ProtocolDiscovery() {
        // necessary to keep xml deserialization happy
    }

    public int getIsBonjourEnabled() {
        return mIsBonjourEnabled;
    }

    public void setIsBonjourEnabled(int isBonjourEnabled) {
        this.mIsBonjourEnabled = isBonjourEnabled;
    }

    public String getIsBonjourEnabledOnUi() {
        return mIsBonjourEnabledOnUi;
    }

    public void setIsBonjourEnabledOnUi(String isBonjourEnabledOnUi) {
        this.mIsBonjourEnabledOnUi = isBonjourEnabledOnUi;
    }

    public String getIsBonjourAdvertisePrinterEnabled() {
        return mIsBonjourAdvertisePrinterEnabled;
    }

    public void setIsBonjourAdvertisePrinterEnabled(
            String isBonjourAdvertisePrinterEnabled) {
        this.mIsBonjourAdvertisePrinterEnabled = isBonjourAdvertisePrinterEnabled;
    }

    public String getIsBonjourAdvertisePrinterEnabledOnUi() {
        return mIsBonjourAdvertisePrinterEnabledOnUi;
    }

    public void setIsBonjourAdvertisePrinterEnabledOnUi(
            String isBonjourAdvertisePrinterEnabledOnUi) {
        this.mIsBonjourAdvertisePrinterEnabledOnUi = isBonjourAdvertisePrinterEnabledOnUi;
    }

    public String getIsBonjourAdvertiseAfpEnabled() {
        return mIsBonjourAdvertiseAfpEnabled;
    }

    public void setIsBonjourAdvertiseAfpEnabled(String isBonjourAdvertiseAfpEnabled) {
        this.mIsBonjourAdvertiseAfpEnabled = isBonjourAdvertiseAfpEnabled;
    }

    public String getIsBonjourAdvertiseAfpEnabledOnUi() {
        return mIsBonjourAdvertiseAfpEnabledOnUi;
    }

    public void setIsBonjourAdvertiseAfpEnabledOnUi(
            String isBonjourAdvertiseAfpEnabledOnUi) {
        this.mIsBonjourAdvertiseAfpEnabledOnUi = isBonjourAdvertiseAfpEnabledOnUi;
    }

    public String getIsBonjourAdvertiseFrontviewEnabled() {
        return mIsBonjourAdvertiseFrontviewEnabled;
    }

    public void setIsBonjourAdvertiseFrontviewEnabled(
            String isBonjourAdvertiseFrontviewEnabled) {
        this.mIsBonjourAdvertiseFrontviewEnabled = isBonjourAdvertiseFrontviewEnabled;
    }

    public String getIsBonjourAdvertiseFrontviewEnabledOnUi() {
        return mIsBonjourAdvertiseFrontviewEnabledOnUi;
    }

    public void setIsBonjourAdvertiseFrontviewEnabledOnUi(
            String isBonjourAdvertiseFrontviewEnabledOnUi) {
        this.mIsBonjourAdvertiseFrontviewEnabledOnUi = isBonjourAdvertiseFrontviewEnabledOnUi;
    }

    public int getIsUpnpEnabled() {
        return mIsUpnpEnabled;
    }

    public void setIsUpnpEnabled(int isUpnpEnabled) {
        this.mIsUpnpEnabled = isUpnpEnabled;
    }

    public String getIsUpnpEnabledOnUi() {
        return mIsUpnpEnabledOnUi;
    }

    public void setIsUpnpEnabledOnUi(String isUpnpEnabledOnUi) {
        this.mIsUpnpEnabledOnUi = isUpnpEnabledOnUi;
    }
}
