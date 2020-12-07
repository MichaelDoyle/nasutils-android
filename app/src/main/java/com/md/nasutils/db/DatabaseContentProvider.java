/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.text.TextUtils;

public class DatabaseContentProvider extends ContentProvider {

    /** Uri Codes */
    private static final int NAS_DEVICE = 1;
    private static final int NAS_DEVICE_ID = 2;
    private static final int DICTIONARY = 3;
    private static final int DICTIONARY_ID = 4;

    /** Authority */
    private static final String AUTHORITY = "com.md.nasutils.provider";

    /** Content Uris */
    public static final Uri CONTENT_URI_NAS_DEVICE = Uri.parse("content://" + AUTHORITY + "/" + NasDeviceTable.TABLE);
    public static final Uri CONTENT_URI_DICTIONARY = Uri.parse("content://" + AUTHORITY + "/" + DictionaryTable.TABLE);

    /** MIME Types */
    public static final String TYPE_BASE = "/vnd.nasutils.";
    public static final String TYPE_DIR_NAS_DEVICE = ContentResolver.CURSOR_DIR_BASE_TYPE + TYPE_BASE + NasDeviceTable.TABLE;
    public static final String TYPE_ITEM_NAS_DEVICE = ContentResolver.CURSOR_ITEM_BASE_TYPE + TYPE_BASE + NasDeviceTable.TABLE;
    public static final String TYPE_DIR_DICTIONARY = ContentResolver.CURSOR_DIR_BASE_TYPE + TYPE_BASE + DictionaryTable.TABLE;
    public static final String TYPE_ITEM_DICTIONARY = ContentResolver.CURSOR_ITEM_BASE_TYPE + TYPE_BASE + DictionaryTable.TABLE;
    
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private DatabaseHelper mDatabase;

    static {
        sUriMatcher.addURI(AUTHORITY, NasDeviceTable.TABLE, NAS_DEVICE);
        sUriMatcher.addURI(AUTHORITY, NasDeviceTable.TABLE + "/#", NAS_DEVICE_ID);
        sUriMatcher.addURI(AUTHORITY, DictionaryTable.TABLE, DICTIONARY);
        sUriMatcher.addURI(AUTHORITY, DictionaryTable.TABLE + "/#", DICTIONARY_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabase = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sUriMatcher.match(uri);
        
        switch (uriType) {
        case NAS_DEVICE:
            queryBuilder.setTables(NasDeviceTable.TABLE);
            break;
        case NAS_DEVICE_ID:
            queryBuilder.setTables(NasDeviceTable.TABLE);
            queryBuilder.appendWhere(NasDeviceTable.COLUMN_ID + "=" + uri.getLastPathSegment());
            break;
        case DICTIONARY:
            queryBuilder.setTables(DictionaryTable.TABLE);
            break;
        case DICTIONARY_ID:
            queryBuilder.setTables(DictionaryTable.TABLE);
            queryBuilder.appendWhere(DictionaryTable.COLUMN_ID + "=" + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        
        String limit = uri.getQueryParameter("limit");

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder, limit);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        long id = 0;
        Uri retUri = null;
        
        switch (uriType) {
        case NAS_DEVICE:
            id = db.insert(NasDeviceTable.TABLE, null, values);
            retUri = Uri.parse(NasDeviceTable.TABLE + "/" + id);
            break;
        case DICTIONARY:
            id = db.insert(DictionaryTable.TABLE, null, values);
            retUri = Uri.parse(DictionaryTable.TABLE + "/" + id);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        
        return retUri;
    }
    
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        
        int rowsInserted = 0;
        
        int uriType = sUriMatcher.match(uri);
        
        switch (uriType) {
        case DICTIONARY:
            rowsInserted = insertDictionaryValues(values);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        
        return rowsInserted;
    };

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int rowsDeleted = 0;
        
        switch (uriType) {
        case NAS_DEVICE:
            rowsDeleted = db.delete(NasDeviceTable.TABLE, selection, selectionArgs);
            break;
        case NAS_DEVICE_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsDeleted = db.delete(NasDeviceTable.TABLE, NasDeviceTable.COLUMN_ID + "=" + id, null);
            } else {
                rowsDeleted = db.delete(NasDeviceTable.TABLE,
                        NasDeviceTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {

        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int rowsUpdated = 0;
        String id = null;
        
        switch (uriType) {
        case NAS_DEVICE:
            rowsUpdated = db.update(NasDeviceTable.TABLE, values, selection, selectionArgs);
            break;
        case NAS_DEVICE_ID:
            id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsUpdated = db.update(NasDeviceTable.TABLE, values,
                        NasDeviceTable.COLUMN_ID + "=" + id, null);
            } else {
                rowsUpdated = db.update(NasDeviceTable.TABLE, values,
                        NasDeviceTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
            }
            break;
        case DICTIONARY:
            rowsUpdated = db.update(DictionaryTable.TABLE, values, selection, selectionArgs);
            break;
        case DICTIONARY_ID:
            id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsUpdated = db.update(DictionaryTable.TABLE, values,
                        DictionaryTable.COLUMN_ID + "=" + id, null);
            } else {
                rowsUpdated = db.update(DictionaryTable.TABLE, values,
                        DictionaryTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        
        return rowsUpdated;
    }
    
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NAS_DEVICE:
                return TYPE_DIR_NAS_DEVICE;
            case NAS_DEVICE_ID:
                return TYPE_ITEM_NAS_DEVICE;
            case DICTIONARY:
                return TYPE_DIR_DICTIONARY;
            case DICTIONARY_ID:
                return TYPE_ITEM_DICTIONARY;
            default:
                return null;
        }
    }
    
    private int insertDictionaryValues(ContentValues[] values) {
        
        SQLiteDatabase db = mDatabase.getWritableDatabase();
        int rowsInserted = 0;
        
        db.beginTransaction();
        try {
            SQLiteStatement insert = 
                    db.compileStatement("INSERT INTO " + DictionaryTable.TABLE
                        + "(" + DictionaryTable.COLUMN_LANGUAGE_CODE
                        + "," + DictionaryTable.COLUMN_KEY
                        + "," + DictionaryTable.COLUMN_VALUE + ")"
                        +" values " + "(?,?,?)");

            for (ContentValues value : values){
                insert.bindString(1, value.getAsString(DictionaryTable.COLUMN_LANGUAGE_CODE));
                insert.bindString(2, value.getAsString(DictionaryTable.COLUMN_KEY));
                insert.bindString(3, value.getAsString(DictionaryTable.COLUMN_VALUE));
                insert.execute();
            }
            
            db.setTransactionSuccessful();
            rowsInserted = values.length;
        } finally {
            db.endTransaction();
        }
        
        return rowsInserted;
    }
}
