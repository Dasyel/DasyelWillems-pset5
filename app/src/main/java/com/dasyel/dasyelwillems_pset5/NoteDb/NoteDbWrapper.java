package com.dasyel.dasyelwillems_pset5.NoteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.dasyel.dasyelwillems_pset5.NoteDb.NoteDbContract.*;

/*
A wrapper around the db for easy CRUD functionality from other classes
 */
public class NoteDbWrapper {
    private static NoteDbWrapper instance;
    private NoteDbHelper noteDbHelper;

    private NoteDbWrapper(Context context){
        this.noteDbHelper = new NoteDbHelper(context);
    }

    public static NoteDbWrapper getInstance(Context context){
        if(instance == null){
            instance = new NoteDbWrapper(context);
        }
        return instance;
    }

    public NoteList[] getLists(){
        String selectQuery = "SELECT  * FROM " + ListTable.TABLE_NAME;
        SQLiteDatabase db = noteDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<NoteList> noteListArrayList = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                int list_id = c.getInt(c.getColumnIndexOrThrow(ListTable._ID));
                String list_name = c.getString(c.getColumnIndexOrThrow(ListTable.COLUMN_NAME_NAME));
                noteListArrayList.add(new NoteList(list_id, list_name));
            } while(c.moveToNext());
        }
        c.close();
        return noteListArrayList.toArray(new NoteList[noteListArrayList.size()]);
    }

    // Get a list by id
    public NoteList getList(long id){
        SQLiteDatabase db = noteDbHelper.getReadableDatabase();

        String[] projection = {
                ListTable._ID,
                ListTable.COLUMN_NAME_NAME
        };

        String selection = ListTable._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                ListTable.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if(c.getCount() == 0){
            return null;
        }

        c.moveToFirst();
        long list_id = c.getLong(c.getColumnIndexOrThrow(NoteTable._ID));
        String list_name = c.getString(c.getColumnIndexOrThrow(ListTable.COLUMN_NAME_NAME));
        c.close();
        return new NoteList(list_id, list_name);
    }

    public NoteList createList(String name){
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListTable.COLUMN_NAME_NAME, name);
        long newId = db.insert(ListTable.TABLE_NAME, null, values);
        return new NoteList(newId, name);
    }

    // Create an empty note and return the Note with the ID
    public Note createNote(NoteList noteList){
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_NAME_TITLE, "");
        values.put(NoteTable.COLUMN_NAME_CONTENTS, "");
        values.put(NoteTable.COLUMN_NAME_LIST, noteList.id);
        long newId = db.insert(NoteTable.TABLE_NAME, null, values);
        return new Note(newId, "", "", noteList.id, false);
    }

    // Get all notes from the database
    public Note[] getNotes(NoteList noteList){
        String selectQuery = "SELECT * FROM " + NoteTable.TABLE_NAME + " WHERE " +
                NoteTable.COLUMN_NAME_LIST + " LIKE " + noteList.id;
        SQLiteDatabase db = noteDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<Note> noteArrayList = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                long note_id = c.getLong(c.getColumnIndexOrThrow(NoteTable._ID));
                String note_title = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_TITLE));
                String note_contents = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_CONTENTS));
                long note_list = c.getLong(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_LIST));
                int note_done = c.getInt(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_DONE));
                noteArrayList.add(new Note(note_id, note_title, note_contents, note_list,
                        (note_done == 1)));
            } while(c.moveToNext());
        }
        c.close();
        return noteArrayList.toArray(new Note[noteArrayList.size()]);
    }

    // Get a note by id
    public Note getNote(long id){
        SQLiteDatabase db = noteDbHelper.getReadableDatabase();

        String[] projection = {
                NoteTable._ID,
                NoteTable.COLUMN_NAME_TITLE,
                NoteTable.COLUMN_NAME_CONTENTS,
                NoteTable.COLUMN_NAME_LIST,
                NoteTable.COLUMN_NAME_DONE
        };

        String selection = NoteTable._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                NoteTable.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if(c.getCount() == 0){
            return null;
        }

        c.moveToFirst();
        long note_id = c.getLong(c.getColumnIndexOrThrow(NoteTable._ID));
        long note_list = c.getLong(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_LIST));
        boolean note_done = (c.getInt(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_DONE)) == 1);
        String note_title = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_TITLE));
        String note_contents = c.getString(c.getColumnIndexOrThrow(NoteTable.COLUMN_NAME_CONTENTS));
        c.close();
        return new Note(note_id, note_title, note_contents, note_list, note_done);
    }

    // Update a note
    public void updateNote(Note note){
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_NAME_TITLE, note.title);
        values.put(NoteTable.COLUMN_NAME_CONTENTS, note.contents);
        values.put(NoteTable.COLUMN_NAME_DONE, note.done);

        String selection = NoteTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(note.id)};
        db.update(
                NoteTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    // Delete a note
    public void deleteNote(Note note){
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();
        String selection = NoteTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(note.id)};
        db.delete(NoteTable.TABLE_NAME, selection, selectionArgs);
    }

    // Delete a list
    public void deleteList(NoteList noteList){
        Note[] notes = this.getNotes(noteList);
        for(Note note: notes){
            deleteNote(note);
        }
        SQLiteDatabase db = noteDbHelper.getWritableDatabase();
        String selection = ListTable._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(noteList.id)};
        db.delete(ListTable.TABLE_NAME, selection, selectionArgs);
    }
}
