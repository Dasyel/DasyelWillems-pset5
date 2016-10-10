package com.dasyel.dasyelwillems_pset5.NoteDb;

import android.provider.BaseColumns;

/*
A contract to have app-wide consistency in database structure
 */
public final class NoteDbContract {
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";

    private NoteDbContract(){}

    public static class NoteTable implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENTS = "contents";
        public static final String COLUMN_NAME_LIST = "list_id";
        public static final String COLUMN_NAME_DONE = "done";
    }

    public static class ListTable implements BaseColumns {
        public static final String TABLE_NAME = "list";
        public static final String COLUMN_NAME_NAME = "name";
    }
}
