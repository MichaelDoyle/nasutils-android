/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.fragment;

/**
 * Provides constant values related to user preferences
 * 
 * @author michaeldoyle
 *
 */
public final class PreferenceConstants {
    
    // preference
    public static final String TEMPERATURE_UNIT = "list_temperature_unit";
    public static final String ANALYTICS = "checkbox_analytics";
    public static final String CONNECTION_TIMEOUT = "edittext_conn_timeout";
    public static final String SOCKET_TIMEOUT = "edittext_sock_timeout";
    public static final String SHOW_ISTAT_CHECKBOX = "show_istat_checkbox";
    
    // legacy
    public static final String SERVER_HOSTNAME = "edittext_server_hostname";
    public static final String SERVER_PORT = "edittext_server_port";
    public static final String SERVER_MAC = "edittext_server_mac";
    public static final String SERVER_USERNAME = "edittext_server_username";
    public static final String SERVER_PASSWORD = "edittext_server_password";
    public static final String READYNAS_MODEL = "list_model_name";

    // regex
    public static final String REGEX_MAC_ADDRESS = "^([0-9A-F]{2}[:]){5}([0-9A-F]{2})$";
    public static final String REGEX_MAC_ADDRESS_PARTIAL = "^([0-9A-F]{2}[:]){0,5}([0-9A-F]{0,2})$";
    
    private PreferenceConstants() {
        throw new AssertionError();     
    }
}
