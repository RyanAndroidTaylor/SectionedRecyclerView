package com.simple.sectionedrecyclerview.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class Provider extends ContentProvider {

    private static String AUTHORITY = "com.test.sectionadapter.database.Provider";
    private static Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri PERSON_URI = Uri.withAppendedPath(AUTHORITY_URI, PersonTable.TABLE_NAME);

    private static final int PERSON_DIR = 1;
    private static final int PERSON_ID = 2;

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PersonTable.TABLE_NAME, PERSON_DIR);
        uriMatcher.addURI(AUTHORITY, PersonTable.TABLE_NAME + "/#", PERSON_ID);
    }

    private static Database database;

    @Override
    public boolean onCreate() {
        database = new Database(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase dbConnection = database.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case PERSON_ID:
                queryBuilder.appendWhere("_id" + "=" + uri.getPathSegments().get(1));
            case PERSON_DIR:
                queryBuilder.setTables(PersonTable.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(dbConnection, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PERSON_DIR:
                return PersonTable.CONTENT_TYPE;
            case PERSON_ID:
                return PersonTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase dbConnection = database.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (uriMatcher.match(uri)) {
                case PERSON_DIR:
                case PERSON_ID:
                    return insert(dbConnection, PERSON_URI, PersonTable.TABLE_NAME, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return null;
    }

    private Uri insert(SQLiteDatabase dbConnection, Uri uri, String tableName, ContentValues values) throws SQLiteException {
        long id = dbConnection.insertOrThrow(tableName, null, values);
        Uri fullUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(fullUri, null);
        dbConnection.setTransactionSuccessful();
        return fullUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase dbConnection = database.getWritableDatabase();
        int updateCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (uriMatcher.match(uri)) {
                case PERSON_DIR:
                    updateCount = updateDir(dbConnection, PersonTable.TABLE_NAME, values, selection, selectionArgs);
                    break;
                case PERSON_ID:
                    updateCount = updateId(dbConnection, uri, PersonTable.TABLE_NAME, values, selection, selectionArgs);
                    break;
            }

        } finally {
            dbConnection.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return updateCount;
    }

    private int updateDir(SQLiteDatabase dbConnection, String tableName, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount = dbConnection.update(tableName, values, selection, selectionArgs);
        dbConnection.setTransactionSuccessful();
        return updateCount;
    }

    private int updateId(SQLiteDatabase dbConnection, Uri uri, String tableName, ContentValues values, String selection, String[] selectionArgs) {
        long id = ContentUris.parseId(uri);
        int updateCount = dbConnection.update(tableName, values, "_id" + "=" + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection
                + ")"), selectionArgs);
        dbConnection.setTransactionSuccessful();
        return updateCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase dbConnection = database.getWritableDatabase();
        int deleteCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (uriMatcher.match(uri)) {
                case PERSON_DIR:
                    deleteCount = deleteDir(dbConnection, PersonTable.TABLE_NAME, selection, selectionArgs);
                    break;
                case PERSON_ID:
                    deleteCount = deleteId(dbConnection, PersonTable.TABLE_NAME, uri);
                    break;
            }

        } finally {
            dbConnection.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;
    }

    private int deleteDir(SQLiteDatabase dbConnection, String tableName, String selection, String[] selectionArgs) {
        int deleteCount = dbConnection.delete(tableName, selection, selectionArgs);
        dbConnection.setTransactionSuccessful();
        return deleteCount;
    }

    private int deleteId(SQLiteDatabase dbConnection, String tableName, Uri uri) {
        int deleteCount = dbConnection.delete(tableName, "_id", new String[]{uri.getPathSegments().get(1)});
        dbConnection.setTransactionSuccessful();
        return deleteCount;
    }
}
