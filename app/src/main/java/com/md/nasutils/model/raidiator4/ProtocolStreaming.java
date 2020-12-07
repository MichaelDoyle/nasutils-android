/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/** 
 *  
 * Annotated for parsing the XML output returned by ReadyNAS FrontView using the
 * Simple XML Framework.
 * 
 * PAGE=Services&OUTER_TAB=tab_streaming&INNER_TAB=NONE&OPERATION=get
 * 
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class ProtocolStreaming {

    // ------------------------ ReadyDLNA ------------------------ //

    @Element(name="value", required=false)
    @Path("content/upnpav_enabled")
    private int mIsDlnaEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/upnpav_enabled")
    private String mIsDlnaEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/upnpav_database_update_enabled")
    private String mIsDlnaDatabaseUpdateEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/upnpav_database_update_enabled")
    private String mIsDlnaDatabaseUpdateEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/upnpav_tivo_support_enabled")
    private String mIsDlnaTivoSupportEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/upnpav_tivo_support_enabled")
    private String mIsDlnaTivoSupportEnabledOnUi;
    
    // ------------------------ iTunes ------------------------ //

    @Element(name="value", required=false)
    @Path("content/daapd_enabled")
    private int mIsItunesStreamingEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/daapd_enabled")
    private String mIsItunesStreamingEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/daapd_anchor")
    private String mItunesStreamingServer;
    
    @Element(name="enable", required=false)
    @Path("content/daapd_anchor")
    private String mIsItunesStreamingServerEnabledOnUi;
    
    public ProtocolStreaming() {
        // necessary to keep xml deserialization happy
    }

    public int getIsDlnaEnabled() {
        return mIsDlnaEnabled;
    }

    public void setIsDlnaEnabled(int isDlnaEnabled) {
        this.mIsDlnaEnabled = isDlnaEnabled;
    }

    public String getIsDlnaEnabledOnUi() {
        return mIsDlnaEnabledOnUi;
    }

    public void setIsDlnaEnabledOnUi(String isDlnaEnabledOnUi) {
        this.mIsDlnaEnabledOnUi = isDlnaEnabledOnUi;
    }

    public String getIsDlnaDatabaseUpdateEnabled() {
        return mIsDlnaDatabaseUpdateEnabled;
    }

    public void setIsDlnaDatabaseUpdateEnabled(String isDlnaDatabaseUpdateEnabled) {
        this.mIsDlnaDatabaseUpdateEnabled = isDlnaDatabaseUpdateEnabled;
    }

    public String getIsDlnaDatabaseUpdateEnabledOnUi() {
        return mIsDlnaDatabaseUpdateEnabledOnUi;
    }

    public void setIsDlnaDatabaseUpdateEnabledOnUi(
            String isDlnaDatabaseUpdateEnabledOnUi) {
        this.mIsDlnaDatabaseUpdateEnabledOnUi = isDlnaDatabaseUpdateEnabledOnUi;
    }

    public String getIsDlnaTivoSupportEnabled() {
        return mIsDlnaTivoSupportEnabled;
    }

    public void setIsDlnaTivoSupportEnabled(String isDlnaTivoSupportEnabled) {
        this.mIsDlnaTivoSupportEnabled = isDlnaTivoSupportEnabled;
    }

    public String getIsDlnaTivoSupportEnabledOnUi() {
        return mIsDlnaTivoSupportEnabledOnUi;
    }

    public void setIsDlnaTivoSupportEnabledOnUi(String isDlnaTivoSupportEnabledOnUi) {
        this.mIsDlnaTivoSupportEnabledOnUi = isDlnaTivoSupportEnabledOnUi;
    }

    public int getIsItunesStreamingEnabled() {
        return mIsItunesStreamingEnabled;
    }

    public void setIsItunesStreamingEnabled(int isItunesStreamingEnabled) {
        this.mIsItunesStreamingEnabled = isItunesStreamingEnabled;
    }

    public String getIsItunesStreamingEnabledOnUi() {
        return mIsItunesStreamingEnabledOnUi;
    }

    public void setIsItunesStreamingEnabledOnUi(String isItunesStreamingEnabledOnUi) {
        this.mIsItunesStreamingEnabledOnUi = isItunesStreamingEnabledOnUi;
    }

    public String getItunesStreamingServer() {
        return mItunesStreamingServer;
    }

    public void setItunesStreamingServer(String itunesStreamingServer) {
        this.mItunesStreamingServer = itunesStreamingServer;
    }

    public String getIsItunesStreamingServerEnabledOnUi() {
        return mIsItunesStreamingServerEnabledOnUi;
    }

    public void setIsItunesStreamingServerEnabledOnUi(
            String isItunesStreamingServerEnabledOnUi) {
        this.mIsItunesStreamingServerEnabledOnUi = isItunesStreamingServerEnabledOnUi;
    }
}
