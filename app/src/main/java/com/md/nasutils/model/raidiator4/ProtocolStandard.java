/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.Map;
import java.util.TreeMap;

/** 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView using the
 * Simple XML Framework.
 * 
 * PAGE=Services&OUTER_TAB=tab_standard&INNER_TAB=NONE&OPERATION=get
 *
 * @author michaeldoyle
 */
@Root(name="payload", strict=false)
public class ProtocolStandard {

    // ------------------------ CIFS ------------------------ //

    @Element(name="value", required=false)
    @Path("content/cifs_enabled")
    private int mIsCifsEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/cifs_enabled")
    private String mIsCifsEnabledOnUi;
    
    // ------------------------ NFS ------------------------ //

    @Element(name="value", required=false)
    @Path("content/nfs_enabled")
    private int mIsNfsEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/nfs_enabled")
    private String mIsNfsEnabledOnUi;
    
    @Element(name="selectedValue", required=false)
    @Path("content/nfs_threads/value/value")
    private String mNfsThreads;
    
    @Element(name="enable", required=false)
    @Path("content/nfs_threads")
    private String mIsNfsThreadsEnabledOnUi;
    
    // ------------------------ AFP ------------------------ //
    
    @Element(name="value", required=false)
    @Path("content/afp_enabled")
    private int mIsAfpEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/afp_enabled")
    private String mIsAfpEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/afp_advertise_bonjour")
    private String mIsAfpAdvertiseBonjourEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/afp_advertise_bonjour")
    private String mIsAfpAdvertiseBonjourEnabledOnUi;
    
    // Not on Ultra 6 UI?
//  @Element(name="value", required=false)
//  @Path("content/afp_advertise_atalkd")
//  private int isAfpAdvertiseAtalkdEnabled;
//  
    // Not on Ultra 6 UI?
//  @Element(name="enable", required=false)
//  @Path("content/afp_advertise_atalkd")
//  private String isAfpAdvertiseAtalkdEnabledOnUi;
    
    // ------------------------ FTP ------------------------ //
    
    @Element(name="value", required=false)
    @Path("content/ftp_enabled")
    private int mIsFtpEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/ftp_enabled")
    private String mIsFtpEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/ftp_passive_start")
    private String mFtpPassiveStart;
    
    @Element(name="enable", required=false)
    @Path("content/ftp_passive_start")
    private String mIsFtpPassiveStartEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/ftp_passive_end")
    private String mFtpPassiveEnd;
    
    @Element(name="enable", required=false)
    @Path("content/ftp_passive_end")
    private String mIsFtpPassiveEndEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/ftp_masquerade_address")
    private String mFtpMasqueradeAddressServer;
    
    @Element(name="enable", required=false)
    @Path("content/ftp_masquerade_address")
    private String mIsFtpMasqueradeAddressEnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/ftp_port")
    private String mFtpPort;
    
    @Element(name="enable", required=false)
    @Path("content/ftp_port")
    private String mIsFtpPortEnabledOnUi;
    
    @Element(name="selectedValue", required=false)
    @Path("content/ftp_mode/value")
    private String mFtpModeServer;
    
    @Element(name="enable", required=false)
    @Path("content/ftp_mode")
    private String mIsFtpModeEnabledOnUi;
    
    @Element(name="selectedValue", required=false)
    @Path("content/ftp_upload_resume/value")
    private String mFtpUploadResumeServer;
    
    @Element(name="enable", required=false)
    @Path("content/ftp_upload_resume")
    private String mIsFtpUploadResumeEnabledOnUi;

    // ------------------------ HTTP ------------------------ //

    @Element(name="value", required=false)
    @Path("content/http_enabled")
    private int mIsHttpEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/http_enabled")
    private String mIsHttpEnabledOnUi;
    
    @Element(name="selectedValue", required=false)
    @Path("content/http_webserver_share/value/value")
    private String mHttpWebServerShare;
    
    @Element(name="enable", required=false)
    @Path("content/http_webserver_share")
    private String mIsHttpWebServerShareEnabledOnUi;
    
    @Element(name="selectedValue", required=false)
    @Path("content/http_webserver_share_use_password/value/value")
    private String mIsHttpWebserverShareUsePassword;
    
    @Element(name="enable", required=false)
    @Path("content/http_webserver_share_use_password")
    private String mIsHttpWebserverShareUsePasswordEnabledOnUi;
    
    @ElementMap(entry="option", value="text", key="value", attribute=false, inline=true, required=false, empty=false)
    @Path("content/http_webserver_share/value")
    private Map<String, String> mWebServerShares;
    
    // ------------------------ HTTPS ------------------------ //
    
    @Element(name="value", required=false)
    @Path("content/https_enabled")
    private int mIsHttpsEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/https_enabled")
    private String mIsHttpsEnabledOnUi;

    @Element(name="value", required=false)
    @Path("content/https_port1")
    private String mHttpsPort1;
    
    @Element(name="enable", required=false)
    @Path("content/https_port1")
    private String mIsHttpsPort1EnabledOnUi;
    
    @Element(name="value", required=false)
    @Path("content/https_port")
    private String mHttpsPort2;
    
    @Element(name="enable", required=false)
    @Path("content/https_port")
    private String mIsHttpsPort2EnabledOnUi;

    @Element(name="value", required=false)
    @Path("content/https_ssl_key_host")
    private String mHttpsSslKeyHost;
    
    @Element(name="enable", required=false)
    @Path("content/https_ssl_key_host")
    private String mIsHttpsSslKeyHostEnabledOnUi;
    
    // ------------------------ RSYNC ------------------------ //

    @Element(name="value", required=false)
    @Path("content/rsync_enabled")
    private int mIsRsyncEnabled;
    
    @Element(name="enable", required=false)
    @Path("content/rsync_enabled")
    private String mIsRsyncEnabledOnUi;
        
    public ProtocolStandard() {     
        // Using a sorted map ensures that ("0", "Not Selected") is the first entry
        mWebServerShares = new TreeMap<>();
    }

    public int getIsCifsEnabled() {
        return mIsCifsEnabled;
    }

    public void setIsCifsEnabled(int isCifsEnabled) {
        this.mIsCifsEnabled = isCifsEnabled;
    }

    public String getIsCifsEnabledOnUi() {
        return mIsCifsEnabledOnUi;
    }

    public void setIsCifsEnabledOnUi(String isCifsEnabledOnUi) {
        this.mIsCifsEnabledOnUi = isCifsEnabledOnUi;
    }

    public int getIsNfsEnabled() {
        return mIsNfsEnabled;
    }

    public void setIsNfsEnabled(int isNfsEnabled) {
        this.mIsNfsEnabled = isNfsEnabled;
    }

    public String getIsNfsEnabledOnUi() {
        return mIsNfsEnabledOnUi;
    }

    public void setIsNfsEnabledOnUi(String isNfsEnabledOnUi) {
        this.mIsNfsEnabledOnUi = isNfsEnabledOnUi;
    }

    public String getNfsThreads() {
        return mNfsThreads;
    }

    public void setNfsThreads(String nfsThreads) {
        this.mNfsThreads = nfsThreads;
    }

    public String getIsNfsThreadsEnabledOnUi() {
        return mIsNfsThreadsEnabledOnUi;
    }

    public void setIsNfsThreadsEnabledOnUi(String isNfsThreadsEnabledOnUi) {
        this.mIsNfsThreadsEnabledOnUi = isNfsThreadsEnabledOnUi;
    }

    public int getIsAfpEnabled() {
        return mIsAfpEnabled;
    }

    public void setIsAfpEnabled(int isAfpEnabled) {
        this.mIsAfpEnabled = isAfpEnabled;
    }

    public String getIsAfpEnabledOnUi() {
        return mIsAfpEnabledOnUi;
    }

    public void setIsAfpEnabledOnUi(String isAfpEnabledOnUi) {
        this.mIsAfpEnabledOnUi = isAfpEnabledOnUi;
    }

    public String getIsAfpAdvertiseBonjourEnabled() {
        return mIsAfpAdvertiseBonjourEnabled;
    }

    public void setIsAfpAdvertiseBonjourEnabled(String isAfpAdvertiseBonjourEnabled) {
        this.mIsAfpAdvertiseBonjourEnabled = isAfpAdvertiseBonjourEnabled;
    }

    public String getIsAfpAdvertiseBonjourEnabledOnUi() {
        return mIsAfpAdvertiseBonjourEnabledOnUi;
    }

    public void setIsAfpAdvertiseBonjourEnabledOnUi(
            String isAfpAdvertiseBonjourEnabledOnUi) {
        this.mIsAfpAdvertiseBonjourEnabledOnUi = isAfpAdvertiseBonjourEnabledOnUi;
    }

    public int getIsFtpEnabled() {
        return mIsFtpEnabled;
    }

    public void setIsFtpEnabled(int isFtpEnabled) {
        this.mIsFtpEnabled = isFtpEnabled;
    }

    public String getIsFtpEnabledOnUi() {
        return mIsFtpEnabledOnUi;
    }

    public void setIsFtpEnabledOnUi(String isFtpEnabledOnUi) {
        this.mIsFtpEnabledOnUi = isFtpEnabledOnUi;
    }

    public String getFtpPassiveStart() {
        return mFtpPassiveStart;
    }

    public void setIsFtpPassiveStart(String ftpPassiveStart) {
        this.mFtpPassiveStart = ftpPassiveStart;
    }

    public String getIsFtpPassiveStartEnabledOnUi() {
        return mIsFtpPassiveStartEnabledOnUi;
    }

    public void setIsFtpPassiveStartEnabledOnUi(String isFtpPassiveStartEnabledOnUi) {
        this.mIsFtpPassiveStartEnabledOnUi = isFtpPassiveStartEnabledOnUi;
    }

    public String getFtpPassiveEnd() {
        return mFtpPassiveEnd;
    }

    public void setFtpPassiveEnd(String ftpPassiveEnd) {
        this.mFtpPassiveEnd = ftpPassiveEnd;
    }

    public String getIsFtpPassiveEndEnabledOnUi() {
        return mIsFtpPassiveEndEnabledOnUi;
    }

    public void setIsFtpPassiveEndEnabledOnUi(String isFtpPassiveEndEnabledOnUi) {
        this.mIsFtpPassiveEndEnabledOnUi = isFtpPassiveEndEnabledOnUi;
    }

    public String getFtpMasqueradeAddressServer() {
        return mFtpMasqueradeAddressServer;
    }

    public void setFtpMasqueradeAddressServer(String ftpMasqueradeAddressServer) {
        this.mFtpMasqueradeAddressServer = ftpMasqueradeAddressServer;
    }

    public String getIsFtpMasqueradeAddressEnabledOnUi() {
        return mIsFtpMasqueradeAddressEnabledOnUi;
    }

    public void setIsFtpMasqueradeAddressEnabledOnUi(
            String isFtpMasqueradeAddressEnabledOnUi) {
        this.mIsFtpMasqueradeAddressEnabledOnUi = isFtpMasqueradeAddressEnabledOnUi;
    }

    public String getFtpPort() {
        return mFtpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.mFtpPort = ftpPort;
    }

    public String getIsFtpPortEnabledOnUi() {
        return mIsFtpPortEnabledOnUi;
    }

    public void setIsFtpPortEnabledOnUi(String isFtpPortEnabledOnUi) {
        this.mIsFtpPortEnabledOnUi = isFtpPortEnabledOnUi;
    }

    public String getFtpModeServer() {
        return mFtpModeServer;
    }

    public void setFtpModeServer(String ftpModeServer) {
        this.mFtpModeServer = ftpModeServer;
    }

    public String getIsFtpModeEnabledOnUi() {
        return mIsFtpModeEnabledOnUi;
    }

    public void setIsFtpModeEnabledOnUi(String isFtpModeEnabledOnUi) {
        this.mIsFtpModeEnabledOnUi = isFtpModeEnabledOnUi;
    }

    public String getFtpUploadResumeServer() {
        return mFtpUploadResumeServer;
    }

    public void setFtpUploadResumeServer(String ftpUploadResumeServer) {
        this.mFtpUploadResumeServer = ftpUploadResumeServer;
    }

    public String getIsFtpUploadResumeEnabledOnUi() {
        return mIsFtpUploadResumeEnabledOnUi;
    }

    public void setIsFtpUploadResumeEnabledOnUi(String isFtpUploadResumeEnabledOnUi) {
        this.mIsFtpUploadResumeEnabledOnUi = isFtpUploadResumeEnabledOnUi;
    }

    public int getIsHttpEnabled() {
        return mIsHttpEnabled;
    }

    public void setIsHttpEnabled(int isHttpEnabled) {
        this.mIsHttpEnabled = isHttpEnabled;
    }

    public String getIsHttpEnabledOnUi() {
        return mIsHttpEnabledOnUi;
    }

    public void setIsHttpEnabledOnUi(String isHttpEnabledOnUi) {
        this.mIsHttpEnabledOnUi = isHttpEnabledOnUi;
    }

    public String getHttpWebServerShare() {
        return mHttpWebServerShare;
    }

    public void setHttpWebServerShare(String httpWebServerShare) {
        this.mHttpWebServerShare = httpWebServerShare;
    }

    public String getIsHttpWebServerShareEnabledOnUi() {
        return mIsHttpWebServerShareEnabledOnUi;
    }

    public void setIsHttpWebServerShareEnabledOnUi(String isHttpWebServerShareEnabledOnUi) {
        this.mIsHttpWebServerShareEnabledOnUi = isHttpWebServerShareEnabledOnUi;
    }

    public String getIsHttpWebserverShareUsePassword() {
        return mIsHttpWebserverShareUsePassword;
    }

    public void setIsHttpWebserverShareUsePassword(String isHttpWebserverShareUsePassword) {
        this.mIsHttpWebserverShareUsePassword = isHttpWebserverShareUsePassword;
    }

    public String getIsHttpWebserverShareUsePasswordEnabledOnUi() {
        return mIsHttpWebserverShareUsePasswordEnabledOnUi;
    }

    public void setIsHttpWebserverShareUsePasswordEnabledOnUi(
            String isHttpWebserverShareUsePasswordEnabledOnUi) {
        this.mIsHttpWebserverShareUsePasswordEnabledOnUi = isHttpWebserverShareUsePasswordEnabledOnUi;
    }

    public int getIsHttpsEnabled() {
        return mIsHttpsEnabled;
    }

    public void setIsHttpsEnabled(int isHttpsEnabled) {
        this.mIsHttpsEnabled = isHttpsEnabled;
    }

    public String getIsHttpsEnabledOnUi() {
        return mIsHttpsEnabledOnUi;
    }

    public void setIsHttpsEnabledOnUi(String isHttpsEnabledOnUi) {
        this.mIsHttpsEnabledOnUi = isHttpsEnabledOnUi;
    }

    public String getHttpsPort1() {
        return mHttpsPort1;
    }

    public void setHttpsPort1(String httpsPort1) {
        this.mHttpsPort1 = httpsPort1;
    }

    public String getIsHttpsPort1EnabledOnUi() {
        return mIsHttpsPort1EnabledOnUi;
    }

    public void setIsHttpsPort1EnabledOnUi(String isHttpsPort1EnabledOnUi) {
        this.mIsHttpsPort1EnabledOnUi = isHttpsPort1EnabledOnUi;
    }

    public String getHttpsPort2() {
        return mHttpsPort2;
    }

    public void setHttpsPort2(String httpsPort2) {
        this.mHttpsPort2 = httpsPort2;
    }

    public String getIsHttpsPort2EnabledOnUi() {
        return mIsHttpsPort2EnabledOnUi;
    }

    public void setIsHttpsPort2EnabledOnUi(String isHttpsPort2EnabledOnUi) {
        this.mIsHttpsPort2EnabledOnUi = isHttpsPort2EnabledOnUi;
    }

    public String getHttpsSslKeyHost() {
        return mHttpsSslKeyHost;
    }

    public void setHttpsSslKeyHost(String httpsSslKeyHost) {
        this.mHttpsSslKeyHost = httpsSslKeyHost;
    }

    public String getIsHttpsSslKeyHostEnabledOnUi() {
        return mIsHttpsSslKeyHostEnabledOnUi;
    }

    public void setIsHttpsSslKeyHostEnabledOnUi(String isHttpsSslKeyHostEnabledOnUi) {
        this.mIsHttpsSslKeyHostEnabledOnUi = isHttpsSslKeyHostEnabledOnUi;
    }

    public int getIsRsyncEnabled() {
        return mIsRsyncEnabled;
    }

    public void setIsRsyncEnabled(int isRsyncEnabled) {
        this.mIsRsyncEnabled = isRsyncEnabled;
    }

    public String getIsRsyncEnabledOnUi() {
        return mIsRsyncEnabledOnUi;
    }

    public void setIsRsyncEnabledOnUi(String isRsyncEnabledOnUi) {
        this.mIsRsyncEnabledOnUi = isRsyncEnabledOnUi;
    }

    public Map<String, String> getWebServerShares() {
        return mWebServerShares;
    }

    public void setWebServerShares(Map<String, String> webServerShares) {
        this.mWebServerShares = webServerShares;
    }
}
