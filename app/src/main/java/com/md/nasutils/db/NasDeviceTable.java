/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NasDeviceTable {

    private static final String TAG = NasDeviceTable.class.getSimpleName();

    public static final String TABLE = "nas_device";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DNS_NAME = "dns_name";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_OS_VERSION = "os_version";
    public static final String COLUMN_PORT = "port";
    public static final String COLUMN_MAC_ADDRESS = "mac_address";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_SSH_USERNAME = "ssh_user_name";
    public static final String COLUMN_SSH_PORT = "ssh_port";
    public static final String COLUMN_ISTAT_PASSCODE = "istat_passcode";
    public static final String COLUMN_ISTAT_PORT = "istat_port";
    public static final String COLUMN_WOL_PORT = "wol_port";
    public static final String COLUMN_WOL_PACKETS = "wol_packets";
    public static final String COLUMN_WOL_SEND_AS_BCAST = "wol_send_as_bcast";

    private static final String CREATE_TABLE_V3 = "create table " + TABLE + "(" 
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null,"
            + COLUMN_MODEL + " text not null,"
            + COLUMN_OS_VERSION + " integer not null,"
            + COLUMN_DNS_NAME + " text not null, "
            + COLUMN_PORT + " integer not null,"
            + COLUMN_MAC_ADDRESS + " text,"
            + COLUMN_USERNAME + " text not null,"
            + COLUMN_PASSWORD + " text not null,"
            + COLUMN_SSH_USERNAME + " text,"
            + COLUMN_SSH_PORT + " integer,"
            + COLUMN_ISTAT_PASSCODE + " text,"
            + COLUMN_ISTAT_PORT + " integer,"
            + COLUMN_WOL_PORT + " integer,"
            + COLUMN_WOL_PACKETS + " integer,"
            + COLUMN_WOL_SEND_AS_BCAST + " integer" + ");";
    
    private static final String ALTER_TABLE_V2_1 = "ALTER TABLE " + TABLE
            + " ADD COLUMN " + COLUMN_ISTAT_PASSCODE + " text;";
            
    private static final String ALTER_TABLE_V2_2 = "ALTER TABLE " + TABLE           
            + " ADD COLUMN " + COLUMN_ISTAT_PORT + " integer;";
    
    private static final String ALTER_TABLE_V3_1 = "ALTER TABLE " + TABLE           
            + " ADD COLUMN " + COLUMN_WOL_PORT + " integer;";
    
    private static final String ALTER_TABLE_V3_2 = "ALTER TABLE " + TABLE           
            + " ADD COLUMN " + COLUMN_WOL_PACKETS + " integer;";
    
    private static final String ALTER_TABLE_V3_3 = "ALTER TABLE " + TABLE           
            + " ADD COLUMN " + COLUMN_WOL_SEND_AS_BCAST + " integer;";
    
    private static final String UPDATE_OS_VERSION_V4 = "UPDATE " + TABLE + 
            " set " + COLUMN_OS_VERSION + " = (CASE WHEN " + COLUMN_OS_VERSION + 
            " >= 600 THEN 6 WHEN " + COLUMN_OS_VERSION + " >= 500 THEN 5 ELSE 4 END);";

    private static final String UPDATE_LEGACY_MODELS_V5 = "UPDATE " + TABLE +
            " set " + COLUMN_MODEL + " = 'Generic ReadyNAS OS 6'" +
            " WHERE " + COLUMN_MODEL + " IN ('ReadyNAS 100 Series', 'ReadyNAS 300 Series', 'ReadyNAS 500 Series', 'ReadyNAS 700 Series');";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_V3);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading table " + TABLE + " from version " + oldVersion + " to " + newVersion);
        
        // Allow the execution to "fall through" so 
        // that all relevant upgrades are applied
        switch(oldVersion) {
        case 1:
            database.execSQL(ALTER_TABLE_V2_1);
            database.execSQL(ALTER_TABLE_V2_2);
        case 2:
            database.execSQL(ALTER_TABLE_V3_1);
            database.execSQL(ALTER_TABLE_V3_2);
            database.execSQL(ALTER_TABLE_V3_3);
        case 3:
            database.execSQL(UPDATE_OS_VERSION_V4);
        case 4:
            database.execSQL(UPDATE_LEGACY_MODELS_V5);
        }
    }
}
