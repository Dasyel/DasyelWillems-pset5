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
import android.widget.EditText;

import com.dasyel.dasyelwillems_pset5.MainActivity;
import com.dasyel.dasyelwillems_pset5.NoteDb.Note;
import com.dasyel.dasyelwillems_pset5.NoteDb.NoteDbWrapper;
import com.dasyel.dasyelwillems_pset5.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/*
This fragment shows an editable toDoItem
 */
public class ToDoItemFragment extends Fragment {
    private NoteDbWrapper noteDbWrapper;
    private Note note;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the noteId of the note to be loaded
        SharedPreferences sp = getDefaultSharedPreferences(getContext());
        long noteId = sp.getLong(getString(R.string.NOTE_ID_KEY), -1);

        // Load the note from the database
        noteDbWrapper = NoteDbWrapper.getInstance(getContext());
        note = noteDbWrapper.getNote(noteId);

        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle(note.title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_to_do_item, container, false);

        // Set the title and contents according to the note object
        EditText titleView = (EditText) view.findViewById(R.id.title);
        EditText contentsView = (EditText) view.findViewById(R.id.contents);
        titleView.setText(note.title);
        contentsView.setText(note.contents);

        return view;
    }

    // Add the save button to the menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_button, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    // Handle the plus sign button pressed to createNote a note and go to its ToDoItem activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity.showToDoList();
        return true;
    }

    // Save the note if the activity is paused in any way
    @Override
    public void onPause() {
        super.onPause();
        EditText titleView = (EditText) view.findViewById(R.id.title);
        EditText contentsView = (EditText) view.findViewById(R.id.contents);
        Note newNote = new Note(note.id, titleView.getText().toString(),
                contentsView.getText().toString(), note.noteListId, note.done);
        noteDbWrapper.updateNote(newNote);
    }
}
