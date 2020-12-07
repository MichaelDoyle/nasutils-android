/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.http;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.md.nasutils.NasUtilsApplication;
import com.md.nasutils.R;

/**
 * Provides configuration information for making
 * HTTP requests.
 * 
 * @author michaeldoyle
 */
public class NasConfiguration implements Parcelable {

    @SuppressWarnings("unused")
    private static final String TAG = NasConfiguration.class.getSimpleName();

    private String mHostname;
    private int mPort;
    private String mUsername;
    private String mPassword;
    private String mProtocol;
    private String mMacAddress;
    private int mOsVersion;
    private String mModel;
    
    private String mSshUsername;
    private int mSshPort;
    
    private int mIstatPort;
    private String mIstatPasscode;
    
    private int mWolPort;
    private int mWolPackets;
    private int mWolSendAsBroadcast;
    
    public NasConfiguration(String protocol, String hostname, int port) {
        super();
        
        this.mHostname = hostname;
        this.mPort = port;
        this.mProtocol = protocol;
    }
    
    public NasConfiguration(String protocol, String hostname, int port,
            String username, String password, String macAddress, int osVersion) {
        super();
        
        this.mPort = port;
        this.mHostname = hostname;
        this.mUsername = username;
        this.mPassword = password;
        this.mProtocol = protocol;
        this.mMacAddress = macAddress;
        this.mOsVersion = osVersion;
    }
    
    public NasConfiguration(String protocol, String hostname, int port,
            String username, String password, String macAddress, int osVersion,
            String sshUsername, int sshPort, String istatPasscode, int istatPort,
            int wolPort, int wolPackets, int wolSendAsBroadcast, String model) {
        super();
        
        this.mPort = port;
        this.mHostname = hostname;
        this.mUsername = username;
        this.mPassword = password;
        this.mProtocol = protocol;
        this.mMacAddress = macAddress;
        this.mOsVersion = osVersion;
        this.mSshUsername = sshUsername;
        this.mSshPort = sshPort;
        this.mIstatPort = istatPort;
        this.mIstatPasscode = istatPasscode;
        this.mWolPort = wolPort;
        this.mWolPackets = wolPackets;
        this.mWolSendAsBroadcast = wolSendAsBroadcast;
        this.mModel = model;
    }
    
    public NasConfiguration(Parcel in) {
        mProtocol = in.readString();
        mHostname = in.readString();
        mPort = in.readInt();
        mUsername = in.readString();
        mPassword = in.readString();
        mMacAddress = in.readString();
        mOsVersion = in.readInt();
        mSshUsername = in.readString();
        mSshPort = in.readInt();
        mIstatPort = in.readInt();
        mIstatPasscode = in.readString();
        mWolPort = in.readInt();
        mWolPackets = in.readInt();
        mWolSendAsBroadcast = in.readInt();
        mModel = in.readString();
    }

    public String getHostname() {
        return mHostname;
    }
    
    public void setHostname(String hostname) {
        this.mHostname = hostname;
    }
    
    public int getPort() {
        return mPort;
    }
    
    public void setPort(int port) {
        this.mPort = port;
    }
    
    public String getUsername() {
        return mUsername;
    }
    
    public void setUsername(String username) {
        this.mUsername = username;
    }
    
    public String getPassword() {
        return mPassword;
    }
    
    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getProtocol() {
        return mProtocol;
    }

    public void setProtocol(String protocol) {
        this.mProtocol = protocol;
    }
    
    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }
    
    public int getOsVersion() {
        return mOsVersion;
    }

    public void setOsVersion(int osVersion) {
        this.mOsVersion = osVersion;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public int getSshPort() {
        return mSshPort;
    }

    public void setSshPort(int sshPort) {
        this.mSshPort = sshPort;
    }

    public String getSshUsername() {
        return mSshUsername;
    }

    public void setSshUsername(String sshUsername) {
        this.mSshUsername = sshUsername;
    }

    public int getIstatPort() {
        return mIstatPort;
    }

    public void setIstatPort(int istatPort) {
        this.mIstatPort = istatPort;
    }

    public String getIstatPasscode() {
        return mIstatPasscode;
    }

    public void setIstatPasscode(String istatPasscode) {
        this.mIstatPasscode = istatPasscode;
    }

    public int getWolPort() {
        return mWolPort;
    }

    public void setWolPort(int wolPort) {
        this.mWolPort = wolPort;
    }

    public int getWolPackets() {
        return mWolPackets;
    }

    public void setWolPackets(int wolPackets) {
        this.mWolPackets = wolPackets;
    }

    public int getWolSendAsBroadcast() {
        return mWolSendAsBroadcast;
    }

    public void setWolSendAsBroadcast(int wolSendAsBroadcast) {
        this.mWolSendAsBroadcast = wolSendAsBroadcast;
    }

    public String validateHttp() {      
        Resources resources = NasUtilsApplication.getContext().getResources();
        
        if (TextUtils.isEmpty(mHostname)) {
            return resources.getString(R.string.error_nas_config_hostname);
        } else if (TextUtils.isEmpty(mUsername)) {
            return resources.getString(R.string.error_nas_config_username);
        } else if (TextUtils.isEmpty(mPassword)) {
            return resources.getString(R.string.error_nas_config_password);
        } else if (mOsVersion <= 0) {
            return resources.getString(R.string.error_nas_config_model);
        }
        
        return null;
    }
    
    public String validateWol() {       
        Resources resources = NasUtilsApplication.getContext().getResources();
        
        if (TextUtils.isEmpty(mMacAddress)) {
            return resources.getString(R.string.error_nas_config_mac);
        }
        
        return null;
    }
    
    public String validateSsh() {       
        Resources resources = NasUtilsApplication.getContext().getResources();
        
        if (TextUtils.isEmpty(mHostname)) {
            return resources.getString(R.string.error_nas_config_hostname);
        } else if (TextUtils.isEmpty(mSshUsername)) {
            return resources.getString(R.string.error_nas_config_ssh_user);
        } else if (mSshPort <= 0) {
            return resources.getString(R.string.error_nas_config_ssh_port);
        }
        
        return null;
    }
    
    public String validateIstat() {     
        Resources resources = NasUtilsApplication.getContext().getResources();
        
        if (TextUtils.isEmpty(mHostname)) {
            return resources.getString(R.string.error_nas_config_hostname);
        } else if (TextUtils.isEmpty(mIstatPasscode)) {
            return resources.getString(R.string.error_nas_config_istat_passcode);
        } else if (mIstatPort <= 0) {
            return resources.getString(R.string.error_nas_config_istat_port);
        }
        
        return null;
    }
    
    public Uri getSshUri() {
        //ConnectBot: ssh://user@host:port/#nickname
        String uri = new StringBuilder("ssh://").append(mSshUsername).append("@")
                .append(mHostname).append(":").append(mSshPort).append("/#")
                .append(mSshUsername).append("@").append(mHostname).toString();

        return Uri.parse(uri);
    }
    
    public Uri getFtpUri(boolean ftpsEnabled, String port) {
        //AndFTP: ftp://host:port
        String protocol = ftpsEnabled ? "ftps" : "ftp";
        String uri = new StringBuilder(protocol).append("://")
                .append(mHostname).append(":").append(port).toString();

        return Uri.parse(uri);
    }
    
    public String validateFtp() {       
        Resources resources = NasUtilsApplication.getContext().getResources();
        
        if (TextUtils.isEmpty(mHostname)) {
            return resources.getString(R.string.error_nas_config_hostname);
        }

        return null;
    }
    
    public String getFtpUserName() {
        String username = null;
        
        if (mSshUsername != null) {
            username = mSshUsername;
        } else {
            username = mUsername;
        }
        
        return username;
    }

    public boolean isReadyData() {
        return mModel.contains("ReadyDATA");
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mProtocol);
        out.writeString(mHostname);
        out.writeInt(mPort);
        out.writeString(mUsername);
        out.writeString(mPassword);
        out.writeString(mMacAddress);
        out.writeInt(mOsVersion);
        out.writeString(mSshUsername);
        out.writeInt(mSshPort);
        out.writeInt(mIstatPort);
        out.writeString(mIstatPasscode);
        out.writeInt(mWolPort);
        out.writeInt(mWolPackets);
        out.writeInt(mWolSendAsBroadcast);
        out.writeString(mModel);
    }

    public static final Parcelable.Creator<NasConfiguration> CREATOR = 
            new Parcelable.Creator<NasConfiguration>() {
                public NasConfiguration createFromParcel(Parcel in) {
                    return new NasConfiguration(in);
                }
        
                public NasConfiguration[] newArray(int size) {
                    return new NasConfiguration[size];
                }
            };
}
