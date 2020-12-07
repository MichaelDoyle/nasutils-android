/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.os6;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by ReadyNAS OS 6
 * using the Simple XML Framework.
 *
 * @author michaeldoyle
 */
@Root(name="Option", strict=false)
public class BackupOption {

    @Element(name="Full_Backup_Frequency", required=false)
    private String mFullBackupFrequency;

    @Element(name="Remove_Previous_Backup", required=false)
    private int mRemovePreviousBackup;

    @Element(name="Chown_To_Share_Owner", required=false)
    private int mChownToShareOwner;

    @Element(name="Rsync_Option", required=false)
    private int mRsyncOption;

    @Element(name="Rsync_Exclude", required=false)
    private String mRsyncExclude; // String? Or object?

    @Element(name="Email_This_To_Alert_Email", required=false)
    private int mEmailThisToAlertEmail;

    @Element(name="Misc_Option", required=false)
    private int mMiscOption;

    @Element(name="Wol_Address", required=false)
    private String mWolAddress;

    public BackupOption() {
        // necessary to keep xml deserialization happy
    }

    public String getFullBackupFrequency() {
        return mFullBackupFrequency;
    }

    public void setFullBackupFrequency(String fullBackupFrequency) {
        this.mFullBackupFrequency = fullBackupFrequency;
    }

    public int getRemovePreviousBackup() {
        return mRemovePreviousBackup;
    }

    public void setRemovePreviousBackup(int removePreviousBackup) {
        this.mRemovePreviousBackup = removePreviousBackup;
    }

    public int getChownToShareOwner() {
        return mChownToShareOwner;
    }

    public void setChownToShareOwner(int chownToShareOwner) {
        this.mChownToShareOwner = chownToShareOwner;
    }

    public int getRsyncOption() {
        return mRsyncOption;
    }

    public void setRsyncOption(int rsyncOption) {
        this.mRsyncOption = rsyncOption;
    }

    public String getRsyncExclude() {
        return mRsyncExclude;
    }

    public void setRsyncExclude(String rsyncExclude) {
        this.mRsyncExclude = rsyncExclude;
    }

    public int getEmailThisToAlertEmail() {
        return mEmailThisToAlertEmail;
    }

    public void setEmailThisToAlertEmail(int emailThisToAlertEmail) {
        this.mEmailThisToAlertEmail = emailThisToAlertEmail;
    }

    public int getMiscOption() {
        return mMiscOption;
    }

    public void setMiscOption(int miscOption) {
        this.mMiscOption = miscOption;
    }

    public String getWolAddress() {
        return mWolAddress;
    }

    public void setWolAddress(String wolAddress) {
        this.mWolAddress = wolAddress;
    }
}
