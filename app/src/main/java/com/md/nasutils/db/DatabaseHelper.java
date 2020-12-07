/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nasutils.db";
    private static final int DATABASE_VERSION = 5;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        DictionaryTable.onCreate(database);
        NasDeviceTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        DictionaryTable.onUpgrade(database, oldVersion, newVersion);
        NasDeviceTable.onUpgrade(database, oldVersion, newVersion);
    }
}
