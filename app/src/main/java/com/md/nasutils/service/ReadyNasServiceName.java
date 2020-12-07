/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service;

/** 
 * @author michaeldoyle
 */
public enum ReadyNasServiceName {

    GET_LOGS             (0x1, "get_logs"),
    GET_FTP_SERVICE      (0x2, "get_ftp_service"),
    GET_SERVICES         (0x3, "get_services"),
    GET_SMART_DISK_INFO  (0x4, "get_smart_disk"),
    GET_STATUS           (0x5, "get_status"),
    RECALIBRATE_FAN      (0x6, "recalibrate_fan"),
    SEND_LOCATE_DISK     (0x7, "locate_disk"),
    SEND_SHUTDOWN        (0x8, "shutdown"),
    SEND_RESTART         (0x9, "restart"),
    SEND_WOL             (0x10, "wake_on_lan"),
    SET_SERVICES         (0x11, "set_services"),
    GET_TELEMETRY        (0x12, "get_telemetry"),
    RESCAN_DLNA          (0x13, "rescan_dlna"),
    GET_BACKUPS          (0x14, "get_backups"),
    START_BACKUP         (0x15, "start_backup"),
    GET_ADDONS           (0x16, "get_addons"),
    TOGGLE_ADDONS        (0x17, "toggle_addons");
    
    private int mCode;
    private String mDisplayName;
    
    ReadyNasServiceName(int code, String displayName) {
        this.mCode = code;
        this.mDisplayName = displayName;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }
    
    public static ReadyNasServiceName fromCode(int code) {
        for (ReadyNasServiceName value : ReadyNasServiceName.values()) {
            if (code == value.mCode) {
                return value;
            }
        }
        throw new IllegalArgumentException("No Service Name for name " + code);
    }
}
