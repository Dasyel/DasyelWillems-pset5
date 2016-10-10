package com.dasyel.dasyelwillems_pset5;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dasyel.dasyelwillems_pset5.Adapter.ListsAdapter;
import com.dasyel.dasyelwillems_pset5.Fragments.ToDoItemFragment;
import com.dasyel.dasyelwillems_pset5.Fragments.ToDoListFragment;
import com.dasyel.dasyelwillems_pset5.NoteDb.InitialNotes;
import com.dasyel.dasyelwillems_pset5.NoteDb.Note;
import com.dasyel.dasyelwillems_pset5.NoteDb.NoteDbWrapper;
import com.dasyel.dasyelwillems_pset5.NoteDb.NoteList;

import java.util.ArrayList;
import java.util.Arrays;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private NoteDbWrapper noteDbWrapper;
    private ListsAdapter listsAdapter;
    private ArrayList<NoteList> noteListArrayList;
    static FragmentManager fragmentManager;
    private SharedPreferences sp;
    private static boolean viewingNote;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteDbWrapper = NoteDbWrapper.getInstance(this);
        fragmentManager = getSupportFragmentManager();
        sp = getDefaultSharedPreferences(MainActivity.this);
        handleInitialStart(); // Handle the population of database on first start

        // Set the burger button for the side drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Get all lists from database for side drawer
        NoteList[] noteListArray = noteDbWrapper.getLists();
        noteListArrayList = new ArrayList<>(Arrays.asList(noteListArray));

        // Create the drawer
        addDrawerItems();
        setupDrawer();

        // Load the last open fragment
        if(sp.getLong(getString(R.string.NOTE_ID_KEY), -1) == -1) {
            showToDoList();
        } else {
            showToDoItem();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // Handles the backButton to either go back to list view from item view or to leave the app
    @Override
    public void onBackPressed() {
        if(viewingNote){
            showToDoList();
        } else {
            super.onBackPressed();
        }
    }

    // Adds a list with the specified name when the add button is pressed in side drawer
    public void addList(View view){
        EditText editText = (EditText) findViewById(R.id.list_name_et);
        String name = editText.getText().toString();

        if(name.isEmpty()){
            Toast toast = Toast.makeText(this, R.string.empty_list_name, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        editText.setText(""); // Clear the contents of the editText
        NoteList noteList = noteDbWrapper.createList(name);
        noteListArrayList.add(noteList);
        listsAdapter.notifyDataSetChanged();
    }

    // Loads the todoList fragment
    public static void showToDoList(){
        viewingNote = false;
        ToDoListFragment toDoListFragment = new ToDoListFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentHolder, toDoListFragment)
                .commit();
    }

    // Loads the todoItem fragment
    public static void showToDoItem(){
        viewingNote = true;
        ToDoItemFragment toDoItemFragment = new ToDoItemFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentHolder, toDoItemFragment)
                .commit();
    }

    // Sets the side drawer contents and onClickListeners
    private void addDrawerItems(){
        ListView listView = (ListView) findViewById(R.id.navList);
        listsAdapter = new ListsAdapter(this, R.layout.drawer_list_row, noteListArrayList);
        listView.setAdapter(listsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteList noteList = (NoteList) parent.getItemAtPosition(position);

                sp.edit().putLong(getString(R.string.LIST_ID_KEY), noteList.id).apply();
                mDrawerLayout.closeDrawers(); // Close the side drawer
                MainActivity.showToDoList();
            }
        });

        // Set onClickListener for deletion of lists
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                NoteList noteList = (NoteList) parent.getItemAtPosition(position);
                long defaultListId = sp.getLong(getString(R.string.DEFAULT_LIST_ID_KEY), -1);

                // Don't allow the defaultList to be deleted
                if(noteList.id == defaultListId){
                    return true;
                }

                noteDbWrapper.deleteList(noteList);
                noteListArrayList.remove(noteList);
                listsAdapter.notifyDataSetChanged();

                // If the deleted list was open, jump to default list
                long openListId = sp.getLong(getString(R.string.LIST_ID_KEY), -1);
                if(noteList.id == openListId) {
                    sp.edit().putLong(getString(R.string.LIST_ID_KEY), defaultListId).apply();
                    mDrawerLayout.closeDrawers(); // Close the side drawer
                    MainActivity.showToDoList();
                }
                return true;
            }
        });
    }

    // Lets the fragments change the title of the actionBar when needed
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    // Sets some values for the burger menu button
    private void setupDrawer(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    // Handles initial database population
    private void handleInitialStart(){
        boolean initialStart = sp.getBoolean("init", true);

        //If app is started for first time: add all initMessages as notes to a default list
        if(initialStart){
            String[][] initMessages = InitialNotes.initMessages;
            NoteList defaultList = noteDbWrapper.createList("Default");

            for(String[] msg: initMessages) {
                Note note = noteDbWrapper.createNote(defaultList);
                Note newNote = new Note(note.id, msg[0], msg[1], defaultList.id, false);
                noteDbWrapper.updateNote(newNote);
            }

            // save the defaultList id for future reference
            sp.edit().putLong(getString(R.string.DEFAULT_LIST_ID_KEY), defaultList.id).apply();
            sp.edit().putBoolean("init", false).apply(); //from now on it's not first start
        }
    }

    public void deleteNote(View v){
        long noteId = sp.getLong(getString(R.string.NOTE_ID_KEY), -1);
        Note note = noteDbWrapper.getNote(noteId);
        noteDbWrapper.deleteNote(note);
        showToDoList();
    }
}
