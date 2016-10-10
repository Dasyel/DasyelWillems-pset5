package com.dasyel.dasyelwillems_pset5.NoteDb;

/*
A simple immutable container to serve as note object
 */
public class Note {
    final public long id;
    final public String title;
    final public String contents;
    final public long noteListId;
    final public boolean done;

    public Note(long id, String title, String contents, long noteListId, boolean done){
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.noteListId = noteListId;
        this.done = done;
    }
}
