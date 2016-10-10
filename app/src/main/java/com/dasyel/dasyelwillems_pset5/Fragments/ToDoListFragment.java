package com.dasyel.dasyelwillems_pset5.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dasyel.dasyelwillems_pset5.Adapter.NoteListAdapter;
import com.dasyel.dasyelwillems_pset5.MainActivity;
import com.dasyel.dasyelwillems_pset5.NoteDb.Note;
import com.dasyel.dasyelwillems_pset5.NoteDb.NoteDbWrapper;
import com.dasyel.dasyelwillems_pset5.NoteDb.NoteList;
import com.dasyel.dasyelwillems_pset5.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/*
This fragment shows a listView of toDoItems
 */
public class ToDoListFragment extends Fragment{
    private NoteDbWrapper noteDbWrapper;
    private NoteListAdapter noteListAdapter;
    private ArrayList<Note> noteArrayList;
    private NoteList list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteDbWrapper = NoteDbWrapper.getInstance(getContext());

        // Remove the note id from sharedPreferences such that the MainActivity starts
        // this fragment on next startup
        SharedPreferences sp = getDefaultSharedPreferences(getContext());
        sp.edit().remove(getString(R.string.NOTE_ID_KEY)).apply();
        long defaultListId = sp.getLong(getString(R.string.DEFAULT_LIST_ID_KEY), -1);
        long listId = sp.getLong(getString(R.string.LIST_ID_KEY), defaultListId);
        this.list = noteDbWrapper.getList(listId);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle(list.name);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        // Get all notes from the database and add them to the listView
        Note[] noteArray = noteDbWrapper.getNotes(this.list);
        noteArrayList = new ArrayList<>(Arrays.asList(noteArray));
        ListView listView = (ListView) v.findViewById(R.id.noteListView);
        noteListAdapter = new NoteListAdapter(getContext(), R.layout.list_row, noteArrayList);
        listView.setAdapter(noteListAdapter);

        // Set onClickListener for transition to ToDoItem fragment
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Note note = (Note) parent.getItemAtPosition(position);

                SharedPreferences sp = getDefaultSharedPreferences(getContext());
                sp.edit().putLong(getString(R.string.NOTE_ID_KEY), note.id).apply();

                MainActivity.showToDoItem();
            }
        });

        // Set onClickListener for setting item done
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getItemAtPosition(position);
                Note newNote = new Note(note.id, note.title, note.contents, note.noteListId, !note.done);
                noteDbWrapper.updateNote(newNote);
                int index = noteArrayList.indexOf(note);
                noteArrayList.remove(index);
                noteArrayList.add(index, newNote);
                noteListAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return v;
    }

    // Add the plus sign button to the menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Handle the plus sign button pressed to createNote a note and go to its ToDoItem fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Note note = noteDbWrapper.createNote(this.list);
        SharedPreferences sp = getDefaultSharedPreferences(getContext());
        sp.edit().putLong(getString(R.string.NOTE_ID_KEY), note.id).apply();
        MainActivity.showToDoItem();
        return true;
    }


}