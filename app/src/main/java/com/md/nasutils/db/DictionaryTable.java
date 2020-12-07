/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DictionaryTable {

    private static final String TAG = DictionaryTable.class.getSimpleName();

    public static final String TABLE = "dictionary";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LANGUAGE_CODE = "language_code";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";

    private static final String CREATE_TABLE_V1 = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LANGUAGE_CODE + " text not null, "
            + COLUMN_KEY + " text not null," 
            + COLUMN_VALUE + " text not null" + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_V1);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading table " + TABLE + " from version " + oldVersion + " to " + newVersion);
        // Do nothing - We are currently on the initial version. Once we reach 
        // the next iteration, we will have a better idea of what we might want to do
    }
}
