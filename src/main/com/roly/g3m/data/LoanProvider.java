package com.roly.g3m.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class LoanProvider extends ContentProvider {
    private static final String TAG = LoanProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.roly.g3m.provider";
    private static final String TYPE_SINGLE_LOAN = "vnd.android.cursor.item/loan";
    private static final String TYPE_ALL_LOAN = "vnd.android.cursor.dir/loan";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/loans");

    private Database database;

    private static final int URI_ALL_LOANS = 1;
    private static final int URI_SINGLE_LOAN = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "loans", URI_ALL_LOANS);
        uriMatcher.addURI(AUTHORITY, "loans/#", URI_SINGLE_LOAN);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_ALL_LOANS:
                return TYPE_ALL_LOAN;
            case URI_SINGLE_LOAN:
                return TYPE_SINGLE_LOAN;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        database = new Database(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert()");

        if (values == null || values.size() == 0)
            throw new IllegalArgumentException("Nothing to insert");

        SQLiteDatabase db = database.getWritableDatabase();
        long rowId = db.insertWithOnConflict(Database.TABLE_LOAN, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
        if (rowId >= 0) {
            Uri rowUri = ContentUris.withAppendedId(uri, rowId);
            if (!db.inTransaction())
                getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.v(TAG, "query()");

        SQLiteDatabase db = database.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Database.TABLE_LOAN);
        if (uriMatcher.match(uri) == URI_SINGLE_LOAN)
            queryBuilder.appendWhereEscapeString(Database.KEY_PRET_ID +
                    "=" + ContentUris.parseId(uri));
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.v(TAG, "update()");

        if (values == null || values.size() == 0)
            throw new IllegalArgumentException("Nothing to insert");

        SQLiteDatabase db = database.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case URI_ALL_LOANS:
                count = db.update(Database.TABLE_LOAN, values, selection, selectionArgs);
                break;
            case URI_SINGLE_LOAN:
                count = db.update(Database.TABLE_LOAN, values, Database.KEY_PRET_ID +
                        "=" + ContentUris.parseId(uri),
                        null);
        }
        if (count > 0 && !db.inTransaction())
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete()");

        SQLiteDatabase db = database.getWritableDatabase();
        int count=0;
        switch (uriMatcher.match(uri)) {
            case URI_ALL_LOANS:
                count = db.delete(Database.TABLE_LOAN, selection, selectionArgs);
                break;
            case URI_SINGLE_LOAN:
                count = db.delete(Database.TABLE_LOAN, Database.KEY_PRET_ID +
                        "=" + ContentUris.parseId(uri),
                        null);
        }
        if (count > 0 && !db.inTransaction())
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
