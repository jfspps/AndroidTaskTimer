package com.example.tasktimer;

import android.provider.BaseColumns;

public class TaskContract {

    static final String TABLE_NAME = "Tasks";

    public static class Columns {
        public static final String TASKS_NAME = "Name";
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORTORDER = "SortOrder";

        private Columns(){
            // private Constructor, hence Columns cannot be instantiated
        }
    }
}
