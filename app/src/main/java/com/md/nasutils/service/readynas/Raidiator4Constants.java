/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides constant values commonly used when communicating
 * with ReadyNAS FrontView
 * 
 * @author michaeldoyle
 */
public final class Raidiator4Constants {

    // Services
    public static final String GET_HANDLER = "/get_handler/";
    public static final String GET_STATUS_ALL = "/get_handler/?OPERATION=get&PAGE=Nasstate&OUTER_TAB=NONE&INNER_TAB=NONE&SECTION=ALL";
    public static final String GET_STATUS_HEALTH = "/get_handler/?OPERATION=get&PAGE=Status&OUTER_TAB=tab_health&INNER_TAB=NONE";
    public static final String GET_STATUS_LOGS = "/get_handler/?OPERATION=get&PAGE=Status&OUTER_TAB=tab_log&INNER_TAB=NONE";
    public static final String GET_SMART_DISK_INFO = "/get_handler?OPERATION=get&PAGE=Status&INNER_TAB=NONE&OUTER_TAB=tab_smart";
    public static final String GET_ADDON = "/addons/";
    public static final String GET_TOGGLE_ADDON_ON = "/${SERVICE}/${SERVICE}_HANDLER.pl?command=ToggleService&OPERATION=set&CHECKBOX_${SERVICE}_ENABLED=checked";
    public static final String GET_TOGGLE_ADDON_OFF = "/${SERVICE}/${SERVICE}_HANDLER.pl?command=ToggleService&OPERATION=set&CHECKBOX_${SERVICE}_ENABLED=unchecked";
    public static final String GET_TOGGLE_RNP2_ON = "/READYNAS_PHOTOS_II/RNP2_HANDLER.pl?command=ModifyAddOnService&OPERATION=set&CHECKBOX_READYNAS_PHOTOS_II_ENABLED=checked";
    public static final String GET_TOGGLE_RNP2_OFF = "/READYNAS_PHOTOS_II/RNP2_HANDLER.pl?command=ModifyAddOnService&OPERATION=set&CHECKBOX_READYNAS_PHOTOS_II_ENABLED=unchecked";

    // Common Form Fields
    public static final String INNER_TAB = "INNER_TAB";
    public static final String OPERATION = "OPERATION";
    public static final String OUTER_TAB = "OUTER_TAB";
    public static final String PAGE = "PAGE";

    // Power Form Fields
    public static final String COMMAND = "command";
    public static final String FORCE_FILE_SYSTEM_CHECK = "force_fsck";
    public static final String FORCE_QUOTA_CHECK = "force_quotacheck";
    public static final String SHUTDOWN_OPTION_1 = "shutdown_option1";
    public static final String SHUTDOWN_OPTION_2 = "shutdown_option2";
    
    // Volume Fields
    public static final String BLINK = "Blink";
    public static final String DISK = "disk";
    public static final String TAB_RAID_SETTINGS = "tab_raid_settings";
    public static final String TAB_SMART = "tab_smart";
    public static final String TAB_VOLUME_C = "tab_volume_C";
    public static final String VOLUME = "Volume";
    
    // Recalibrate Fan
    public static final String RECALIBRATE_FAN = "RecalibrateFan";
    public static final String STATUS = "Status";

    // Service Fields
    public static final String SERVICES = "Services";
    public static final String TAB_STANDARD = "tab_standard";
    public static final String TAB_STREAMING = "tab_streaming";
    public static final String TAB_DISCOVERY = "tab_discovery";
    
    // Services/Protocols
    public static final String SERVICE_CIFS = "cifs";
    public static final String SERVICE_NFS = "nfs";
    public static final String SERVICE_AFP = "afp";
    public static final String SERVICE_FTP = "ftp";
    public static final String SERVICE_HTTP = "http";
    public static final String SERVICE_HTTPS = "https";
    public static final String SERVICE_RSYNC = "rsync";
    public static final String SERVICE_BONJOUR = "bonjour";
    public static final String SERVICE_UPNP = "upnpd";
    public static final String SERVICE_DLNA = "dlna";
    public static final String SERVICE_DAAP = "daap";
    public static final String SERVICE_READY_CLOUD = "readycloud";
    public static final String SERVICE_REPLICATE = "replicate";
    public static final String SERVICE_SNMP = "snmp";
    public static final String SERVICE_SMART_NETWORK = "smartnetwork";
    public static final String SERVICE_ANTIVIRUS = "anti-virus";
    public static final String SERVICE_SSH = "ssh";

    // Services/Protocols "Enabled"
    public static final String SERVICE_CIFS_ENABLED = "cifs_enabled";
    public static final String SERVICE_NFS_ENABLED = "nfs_enabled";
    public static final String SERVICE_AFP_ENABLED = "afp_enabled";
    public static final String SERVICE_FTP_ENABLED = "ftp_enabled";
    public static final String SERVICE_HTTP_ENABLED = "http_enabled";
    public static final String SERVICE_HTTPS_ENABLED = "https_enabled";
    public static final String SERVICE_RSYNC_ENABLED = "rsync_enabled";
    public static final String SERVICE_BONJOUR_ENABLED = "bonjour_enabled";
    public static final String SERVICE_UPNP_ENABLED = "upnp_enabled";
    public static final String SERVICE_DLNA_ENABLED = "upnpav_enabled";
    public static final String SERVICE_DAAP_ENABLED = "daapd_enabled";
    
    // RAIDiator 4 Services/Protocols "Modified"
    public static final String SERVICE_BONJOUR_MODIFIED = "bonjour_modified";
    public static final String SERVICE_DLNA_MODIFIED = "upnpav_modified";

    // RAIDiator 4 Services/Protocols "Attributes"
    public static final String SERVICE_AFP_ADVERTISE_ATALKD = "afp_advertise_atalkd";
    public static final String SERVICE_AFP_ADVERTISE_BONJOUR = "afp_advertise_bonjour";
    public static final String SERVICE_FTP_PORT = "ftp_port";
    public static final String SERVICE_FTP_PASSIVE_START = "ftp_passive_start";
    public static final String SERVICE_FTP_PASSIVE_END = "ftp_passive_end";
    public static final String SERVICE_FTP_MASQUERADE_ADDRESS = "ftp_masquerade_address";
    public static final String SERVICE_HTTPS_PORT1 = "https_port1";
    public static final String SERVICE_HTTPS_PORT = "https_port";
    public static final String SERVICE_HTTPS_SSL_KEY_HOST = "https_ssl_key_host";
    public static final String SERVICE_NFS_THREADS = "nfs_threads";
    public static final String SERVICE_FTP_MODE = "ftp_mode";
    public static final String SERVICE_FTP_UPLOAD_RESUME = "ftp_upload_resume";
    public static final String SERVICE_HTTP_WEBSERVER_SHARE = "http_webserver_share";
    public static final String SERVICE_HTTP_WEBSERVER_SHARE_USE_PASSWORD = "http_webserver_share_use_password";
    public static final String SERVICE_DLNA_DATABASE_UPDATE_ENABLED = "upnpav_database_update_enabled";
    public static final String SERVICE_DLNA_TIVO_SUPPORT_ENABLED = "upnpav_tivo_support_enabled";
    public static final String SERVICE_BONJOUR_ADVERTISE_FRONTVIEW = "bonjour_advertise_frontview";
    public static final String SERVICE_BONJOUR_ADVERTISE_PRINTER = "bonjour_advertise_printer";
    public static final String SERVICE_BONJOUR_ADVERTISE_AFP = "bonjour_advertise_afp";
    
    // Common Services/Protocols "Attributes"
    public static final String SERVICE_ATTR_AFP_ADVERTISE_ATALKD = "afp-advertise-atalkd";
    public static final String SERVICE_ATTR_AFP_ADVERTISE_BONJOUR = "afp-advertise-bonjour";
    public static final String SERVICE_ATTR_FTP_PORT = "port";
    public static final String SERVICE_ATTR_FTP_PASSIVE_START = "passive-range-min";
    public static final String SERVICE_ATTR_FTP_PASSIVE_END = "passive-range-max";
    public static final String SERVICE_ATTR_FTP_MASQUERADE_ADDRESS = "masquerade-address";
    public static final String SERVICE_ATTR_HTTPS_PORT = "port";
    public static final String SERVICE_ATTR_HTTPS_PORT_1 = "port-1";
    public static final String SERVICE_ATTR_HTTPS_PORT_2 = "port-2";
    public static final String SERVICE_ATTR_HTTPS_SSL_KEY_HOST = "ssl-key-host";
    public static final String SERVICE_ATTR_NFS_THREADS = "threads";
    public static final String SERVICE_ATTR_NFS_NFSV4_ENABLED = "nfsv4-enable";
    public static final String SERVICE_ATTR_NFS_NFSV4_DOMAIN = "nfsv4-domain";
    public static final String SERVICE_ATTR_FTP_MODE = "auth-mode";
    public static final String SERVICE_ATTR_FTP_UPLOAD_RESUME = "allow-upload-resume";
    public static final String SERVICE_ATTR_FTP_MAX_UPLOAD_RATE = "max-upload-rate";
    public static final String SERVICE_ATTR_FTP_MAX_DOWNLOAD_RATE = "max-download-rate";
    public static final String SERVICE_ATTR_FTP_FTPS = "ftps";
    public static final String SERVICE_ATTR_FTP_FORCE_FTPS = "force-ftps";
    public static final String SERVICE_ATTR_FTP_SERVER_TRANSFER_LOG = "server-transfer-log";
    public static final String SERVICE_ATTR_HTTP_SHARE_NAMES = "share-names";
    public static final String SERVICE_ATTR_HTTP_WEBSERVER_SHARE = "share-name";
    public static final String SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD = "login-enabled";
    public static final String SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED = "auto-scan";
    public static final String SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED = "enable-tivo-server";
    public static final String SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW = "bonjour-advertise-frontview";
    public static final String SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER = "bonjour-advertise-printer";
    public static final String SERVICE_ATTR_BONJOUR_ADVERTISE_AFP = "bonjour-advertise-afp";
    public static final String SERVICE_ATTR_DAAP_SHARE_NAME = "sharename";
    public static final String SERVICE_ATTR_DAAP_PASSWORD = "password";
    public static final String SERVICE_ATTR_DAAP_DIRECTORY = "directory";
    public static final String SERVICE_ATTR_SNMP_ALLOW_HOST = "allow-host";
    public static final String SERVICE_ATTR_SNMP_COMMUNITY = "community";
    public static final String SERVICE_ATTR_SNMP_TRAP_DESTINATION = "trap-destination";
    public static final String SERVICE_ATTR_SNMP_LOCATION = "location";
    public static final String SERVICE_ATTR_SNMP_CONTACT = "contact";

    // Common Services/Protocols "Attributes UI Enabled"
    public static final String SERVICE_ATTR_AFP_ADVERTISE_ATALKD_ENABLED_ON_UI = "afp-advertise-atalkd-enabled-on-ui";
    public static final String SERVICE_ATTR_AFP_ADVERTISE_BONJOUR_ENABLED_ON_UI = "afp-advertise-bonjour-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_PORT_ENABLED_ON_UI = "port-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_PASSIVE_START_ENABLED_ON_UI = "passive-range-min-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_PASSIVE_END_ENABLED_ON_UI = "passive-range-max-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_MASQUERADE_ADDRESS_ENABLED_ON_UI = "masquerade-address-enabled-on-ui";
    public static final String SERVICE_ATTR_HTTPS_PORT_ENABLED_ON_UI = "port-enabled-on-ui";
    public static final String SERVICE_ATTR_HTTPS_PORT_1_ENABLED_ON_UI = "port-1-enabled-on-ui";
    public static final String SERVICE_ATTR_HTTPS_PORT_2_ENABLED_ON_UI = "port-2-enabled-on-ui";
    public static final String SERVICE_ATTR_HTTPS_SSL_KEY_HOST_ENABLED_ON_UI = "ssl-key-host-enabled-on-ui";
    public static final String SERVICE_ATTR_NFS_NFSV4_ENABLED_ENABLED_ON_UI = "nfsv4-enabled-enabled-on-ui";
    public static final String SERVICE_ATTR_NFS_NFSV4_DOMAIN_ENABLED_ON_UI = "nfsv4-domain-enabled-on-ui";
    public static final String SERVICE_ATTR_NFS_THREADS_ENABLED_ON_UI = "threads-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_MODE_ENABLED_ON_UI = "auth-mode-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_UPLOAD_RESUME_ENABLED_ON_UI = "allow-upload-resume-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_MAX_UPLOAD_RATE_ENABLED_ON_UI = "max-upload-rate-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_MAX_DOWNLOAD_RATE_ENABLED_ON_UI = "max-download-rate-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_FTPS_ENABLED_ON_UI = "ftps-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_FORCE_FTPS_ENABLED_ON_UI = "force-ftps-enabled-on-ui";
    public static final String SERVICE_ATTR_FTP_SERVER_TRANSFER_LOG_ENABLED_ON_UI = "server-transfer-log-enabled-on-ui";
    public static final String SERVICE_ATTR_HTTP_WEBSERVER_SHARE_ENABLED_ON_UI = "share-name-enabled-on-ui";
    public static final String SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD_ENABLED_ON_UI = "login-enabled-enabled-on-ui";
    public static final String SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED_ENABLED_ON_UI = "auto-scan-enabled-on-ui";
    public static final String SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED_ENABLED_ON_UI = "enable-tivo-server-enabled-on-ui";
    public static final String SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW_ENABLED_ON_UI = "bonjour-advertise-frontview-enabled-on-ui";
    public static final String SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER_ENABLED_ON_UI = "bonjour-advertise-printer-enabled-on-ui";
    public static final String SERVICE_ATTR_BONJOUR_ADVERTISE_AFP_ENABLED_ON_UI = "bonjour-advertise-afp-enabled-on-ui";
    public static final String SERVICE_ATTR_DAAP_SHARE_NAME_ENABLED_ON_UI = "sharename-enabled-on-ui";
    public static final String SERVICE_ATTR_DAAP_PASSWORD_ENABLED_ON_UI = "password-enabled-on-ui";
    public static final String SERVICE_ATTR_DAAP_DIRECTORY_ENABLED_ON_UI = "directory-enabled-on-ui";
    public static final String SERVICE_ATTR_SNMP_ALLOW_HOST_ENABLED_ON_UI = "allow-host-enabled-on-ui";
    public static final String SERVICE_ATTR_SNMP_COMMUNITY_ENABLED_ON_UI = "community-enabled-on-ui";
    public static final String SERVICE_ATTR_SNMP_TRAP_DESTINATION_ENABLED_ON_UI = "trap-destination-enabled-on-ui";
    public static final String SERVICE_ATTR_SNMP_LOCATION_ENABLED_ON_UI = "location-enabled-on-ui";
    public static final String SERVICE_ATTR_SNMP_CONTACT_ENABLED_ON_UI = "contact-enabled-on-ui";

    public static final String SUFFIX_ENABLED_ON_UI = "-enabled-on-ui";

    // Backups
    public static final String GO = "Go";
    public static final String CURRENT_BACKUP_JOB = "CURRENT_BACKUP_JOB";
    public static final String PAGE_BACKUP = "Backup";
    public static final String PAGE_BACKUP_STATUS = "BackupStatus";
    public static final String TAB_BACKUP_JOBS = "tab_backup_jobs";

    // Addons
    public static final String GET_ADDON_LIST = "GetAddOnList";
    public static final String GET_COMMAND = "get_command";
    public static final String PAGE_NASSTATE = "Nasstate";

    // General Form Values
    public static final String NONE = "NONE";
    public static final String POWEROFF = "poweroff";
    public static final String REBOOT = "reboot";
    public static final String SET = "set";
    public static final String SYSTEM = "System";
    public static final String TAB_SHUTDOWN = "tab_shutdown";
    public static final String ENABLED = "enabled";
    public static final String DISABLED = "disabled";
    public static final String GET = "get";
    public static final String RESCAN_DLNA = "UPnPAVRescan";
    
    // Boolean
    public static final String FALSE = "0";
    public static final String TRUE = "1";
    
    // Format
    public static final String DATE_FORMAT = "EE MMM dd HH:mm:ss z yyyy";

    // As it turns out, using the LED images passed along
    // in the XML document provides a good enumeration
    // of errors, warnings and informational statuses
    public static final List<String> LED_ERROR = Collections
            .unmodifiableList(Arrays.asList(
                    "/images/LED/dead.gif"));

    public static final List<String> LED_WARNING = Collections
            .unmodifiableList(Arrays.asList(
                    "/images/LED/warn.gif",
                    "/images/LED/awaiting_recovery.gif",
                    "/images/LED/spare_inactive.gif",
                    "/images/LED/life_support.gif"));

    public static final List<String> LED_INFO = Collections
            .unmodifiableList(Arrays.asList(
                    "/images/LED/OK.gif",
                    "/images/LED/update.gif",
                    "/images/LED/not_present.gif",
                    "/images/LED/resync.gif"));

    private Raidiator4Constants() {
        throw new AssertionError();
    }
}
