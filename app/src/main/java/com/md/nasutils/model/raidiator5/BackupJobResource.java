/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by RAIDiator 5
 * using the Simple XML Framework.
 *
 * @author michaeldoyle
 */
@Root(name="BackupJob_Resource", strict=false)
public class BackupJobResource {

    @Attribute(name="item", required=false)
    private String mItem;

    @Element(name="Type", required=false)
    private String mType;

    @Element(name="Share_Name_Or_Protocol", required=false)
    private String mShareNameOrProtocol;

    @Element(name="Path", required=false)
    private String mPath;

    @Element(name="Login", required=false)
    private String mLogin;

    @Element(name="Password", required=false)
    private String mPassword;

    @Element(name="Port", required=false)
    private int mPort;

    public BackupJobResource() {
        // necessary to keep xml deserialization happy
    }

    public String getItem() {
        return mItem;
    }

    public void setItem(String item) {
        this.mItem = item;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getShareNameOrProtocol() {
        return mShareNameOrProtocol;
    }

    public void setShareNameOrProtocol(String shareNameOrProtocol) {
        this.mShareNameOrProtocol = shareNameOrProtocol;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        this.mPort = port;
    }
}
