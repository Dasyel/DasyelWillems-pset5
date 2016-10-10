package com.dasyel.dasyelwillems_pset5.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dasyel.dasyelwillems_pset5.NoteDb.Note;
import com.dasyel.dasyelwillems_pset5.R;

import java.util.List;

/*
A custom adapter to load all Notes in a listview
 */
public class NoteListAdapter extends ArrayAdapter<Note> {

    public NoteListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public NoteListAdapter(Context context, int resource, List<Note> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_row, null);
        }

        Note note = getItem(position);

        if (note != null) {
            TextView numberView = (TextView) v.findViewById(R.id.lrNumber);
            TextView titleView = (TextView) v.findViewById(R.id.lrTitle);
            TextView contentsView = (TextView) v.findViewById(R.id.lrContents);

            numberView.setText(String.valueOf(position));
            titleView.setText(note.title);
            contentsView.setText(note.contents);
            if(note.done){
                titleView.setPaintFlags(titleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                contentsView.setPaintFlags(contentsView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                titleView.setPaintFlags(titleView.getPaintFlags() &
                        (~Paint.STRIKE_THRU_TEXT_FLAG));
                contentsView.setPaintFlags(contentsView.getPaintFlags() &
                        (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        return v;
    }

}
