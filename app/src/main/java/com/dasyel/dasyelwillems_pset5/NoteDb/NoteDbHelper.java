package com.dasyel.dasyelwillems_pset5.NoteDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dasyel.dasyelwillems_pset5.NoteDb.NoteDbContract.*;

/*
A custom SQLiteOpenHelper
 */
class NoteDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Notes.db";

    NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_LIST_TABLE =
                "CREATE TABLE " + ListTable.TABLE_NAME + " (" +
                        ListTable._ID + " INTEGER PRIMARY KEY," +
                        ListTable.COLUMN_NAME_NAME + TEXT_TYPE + ")";
        String SQL_CREATE_NOTE_TABLE =
                "CREATE TABLE " + NoteTable.TABLE_NAME + " (" +
                        NoteTable._ID + " INTEGER PRIMARY KEY," +
                        NoteTable.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        NoteTable.COLUMN_NAME_CONTENTS + TEXT_TYPE + COMMA_SEP +
                        NoteTable.COLUMN_NAME_LIST + " INTEGER" + COMMA_SEP +
                        NoteTable.COLUMN_NAME_DONE + " INTEGER DEFAULT 0)";
        db.execSQL(SQL_CREATE_LIST_TABLE);
        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_NOTE_TABLE = "DROP TABLE IF EXISTS " + NoteTable.TABLE_NAME;
        String SQL_DELETE_LIST_TABLE = "DROP TABLE IF EXISTS " + ListTable.TABLE_NAME;
        db.execSQL(SQL_DELETE_NOTE_TABLE);
        db.execSQL(SQL_DELETE_LIST_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
