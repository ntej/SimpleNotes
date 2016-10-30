package model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import www.ntej.com.simplenotes.R;

/**
 * Created by navatejareddy on 10/29/16.
 */

public class CustomListViewAdapter extends ArrayAdapter<NotepadContent> {

    private ArrayList<NotepadContent> noteslistObject;

    public CustomListViewAdapter(Context context, int textViewResourceId, ArrayList<NotepadContent> objects) {
        super(context, textViewResourceId, objects);

        this.noteslistObject = objects;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;

        if (mView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflater.inflate(R.layout.listrow, null);
        }

            NotepadContent noteslistObjectTemp = noteslistObject.get(position);

            if (noteslistObjectTemp != null) {
                TextView notetext = (TextView) mView.findViewById(R.id.notetext);
                TextView notedate = (TextView) mView.findViewById(R.id.date);

                notetext.setText(noteslistObjectTemp.getText());
                notedate.setText(noteslistObjectTemp.getDate());
            }
            return mView;

    }
}

