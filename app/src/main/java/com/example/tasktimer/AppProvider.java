package com.example.tasktimer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Content Provider (manages access to a central repository of data)
 * class for TaskTimer (operates on {@link AppDatabase})
 */

public class AppProvider extends ContentProvider {

    private static final String TAG = "AppProvider";
    private AppDatabase mOpenHelper;

    // build URI to access table and row data
    // see https://developer.android.com/guide/topics/providers/content-provider-creating
    // UriMatcher matches URIs with content providers (more efficient that checking URIs with switch blocks)
    // sUriMatcher effectively stores the legal URIs
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // unique string
    static final String CONTENT_AUTHORITY = "com.example.tasktimer.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int TASKS = 100;
    private static final int TASKS_ID = 101;

    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;

//    private static final int TIMINGS = 300;
//    private static final int TIMINGS_ID = 301;

    private static final int TASK_DURATIONS = 400;
    private static final int TASK_DURATIONS_ID = 401;

    // one could also write a static block instead of buildUriMatcher
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // this would store "com.example.tasktimer.provider/Tasks"
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME, TASKS);

        // this would store "com.example.tasktimer.provider/Tasks/8", if Id = 8
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME + "/#", TASKS_ID);
//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS);
//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME + "/#", TIMINGS_ID);
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS);
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME + "/#", TASK_DURATIONS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        // establish a reference to the database for AppProvider
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: URI passed: " + uri);

        // get an integer ID from the passed uri
        final int matcher = sUriMatcher.match(uri);
        Log.d(TAG, "query: matcher is :" + matcher);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // with the integer ID, build the SQL query, queryBuilder
        switch(matcher){
            case TASKS:
                // return the entire table
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                break;
            case TASKS_ID:
                // get a particular row based on a parameter
                queryBuilder.setTables(TasksContract.TABLE_NAME);

                // get the task's primary key _ID and append it to the SQL query
                long taskId = TasksContract.getTaskId(uri);
                queryBuilder.appendWhere(TasksContract.Columns._ID + " = " + taskId);
                break;
//            case TIMINGS:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                break;
//            case TIMINGS_ID:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                long timingId = TimingsContract.getTimingId(uri);
//                queryBuilder.appendWhere(TimingsContract.Columns._ID + " = " + timingId);
//                break;
//            case TASK_DURATIONS:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                break;
//            case TASK_DURATIONS_ID:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                long durationId = DurationsContract.getDurationId(uri);
//                queryBuilder.appendWhere(DurationsContract.Columns._ID + " = " + durationId);
//                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        // get read-only access to the database, run the SQL query and store return values in a Cursor
        SQLiteDatabase database = mOpenHelper.getReadableDatabase();

        // projection are columns; selection are rows
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
