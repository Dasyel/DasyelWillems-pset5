package com.dasyel.dasyelwillems_pset5.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dasyel.dasyelwillems_pset5.NoteDb.Note;
import com.dasyel.dasyelwillems_pset5.NoteDb.NoteList;
import com.dasyel.dasyelwillems_pset5.R;

import java.util.List;

/*
A custom adapter to load all Notes in a listview
 */
public class ListsAdapter extends ArrayAdapter<NoteList> {

    public ListsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListsAdapter(Context context, int resource, List<NoteList> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.drawer_list_row, null);
        }

        NoteList noteList = getItem(position);

        if (noteList != null) {
            TextView nameView = (TextView) v.findViewById(R.id.list_name);
            TextView numberView = (TextView) v.findViewById(R.id.list_number);
            nameView.setText(noteList.name);
            numberView.setText(String.valueOf(position));
        }

        return v;
    }

}
