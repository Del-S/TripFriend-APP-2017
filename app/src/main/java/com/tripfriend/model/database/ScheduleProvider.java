package com.tripfriend.model.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tripfriend.model.Schedule;

public class ScheduleProvider extends ContentProvider {

    private static final String AUTHORITY = "com.tripfriend";
    private static final String BASE_PATH = "schedules";
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int SCHEDULES = 1;
    private static final int SCHEDULE_ID = 2;
    public static final String CONTENT_ITEM_TYPE = "Schedules";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, SCHEDULES);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", SCHEDULE_ID);
    }

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (URI_MATCHER.match(uri) == SCHEDULE_ID) {
            selection = Schedule.KEY_ID + "=" + uri.getLastPathSegment();
        }

        Cursor c = mDatabase.query(Schedule.TABLE, null, selection, null, null, null, null);

        // The data is filtered in the UI so the 'selection' argument is passed with it
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id = mDatabase.insert(Schedule.TABLE, null, values);
        //Create the URI to pass back that includes the new primary key value.
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return mDatabase.delete(Schedule.TABLE, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDatabase.update(Schedule.TABLE, values, selection, selectionArgs);
    }
}
