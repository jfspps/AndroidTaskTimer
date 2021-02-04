package com.example.tasktimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Processes the TaskTimer database. This is only accessible from {@link AppProvider}.
 * */
class AppDatabase extends SQLiteOpenHelper {

    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "TaskTimer.db";
    public static final int DATABASE_VERSION = 1;
    private static AppDatabase instance = null;

    private AppDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor called");
    }

    /**
     * Get instance of the singleton DB
     *
     * @param context the content providers context
     * @return a SQLite database helper object
     */
    static AppDatabase getInstance(Context context){
        if (instance == null){
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: started");

        String sSQL = "CREATE TABLE " + TasksContract.TABLE_NAME + " ("
                + TasksContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TasksContract.Columns.TASKS_NAME + " TEXT NOT NULL, "
                + TasksContract.Columns.TASKS_DESCRIPTION + " TEXT, "
                + TasksContract.Columns.TASKS_SORTORDER + " INTEGER);";

        Log.d(TAG, "SQL query: " + sSQL);
        db.execSQL(sSQL);

        Log.d(TAG, "onCreate: ended");
    }

    // called when a database is upgraded (edit the schema here, if required)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: upgrade commencing");

        // will return to this later...
        switch (oldVersion){
            case 1: // current version 1, do nothing
                break;
            // do something when version no. is not 1
            default:
                throw new IllegalStateException("onUpgrade() with unknown new version: " + newVersion);
        }

        Log.d(TAG, "onUpgrade: upgrade ended");
    }
}
