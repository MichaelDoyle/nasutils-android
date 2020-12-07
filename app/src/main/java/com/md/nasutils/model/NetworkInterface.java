/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkInterface implements Parcelable {

    private String mInterfaceName;
    private String mInterfaceType;
    private String mMacAddress;
    private String mIpAddress;
    private String mIpAddressType;
    private String mIpv6Address;
    private String mIpv6AddressType;
    private String mStatus;
    
    /** Specific to bonded interfaces */
    private String mMode;
    private String mOver;
    
    public NetworkInterface() {
    }
    
    public NetworkInterface(Parcel in) {
        mInterfaceName = in.readString();
        mInterfaceType = in.readString();
        mMacAddress = in.readString();
        mIpAddress = in.readString();
        mIpv6Address = in.readString();
        mIpAddressType = in.readString();
        mIpv6AddressType = in.readString();
        mMode = in.readString();
        mOver = in.readString();
    }
    
    public String getInterfaceName() {
        return mInterfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.mInterfaceName = interfaceName;
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
    
    public String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        this.mMode = mode;
    }

    public String getOver() {
        return mOver;
    }

    public void setOver(String over) {
        this.mOver = over;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mInterfaceName);
        out.writeString(mInterfaceType);
        out.writeString(mMacAddress);
        out.writeString(mIpAddress);
        out.writeString(mIpv6Address);
        out.writeString(mIpAddressType);
        out.writeString(mIpv6AddressType);
        out.writeString(mMode);
        out.writeString(mOver);
    }

    public static final Parcelable.Creator<NetworkInterface> CREATOR = 
            new Parcelable.Creator<NetworkInterface>() {
                public NetworkInterface createFromParcel(Parcel in) {
                    return new NetworkInterface(in);
                }
        
                public NetworkInterface[] newArray(int size) {
                    return new NetworkInterface[size];
                }
            };
            
}
