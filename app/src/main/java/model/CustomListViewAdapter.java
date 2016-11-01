package model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import www.ntej.com.simplenotes.Main3Activity;
import www.ntej.com.simplenotes.R;

/**
 * Created by navatejareddy on 10/29/16.
 */

public class CustomListViewAdapter extends ArrayAdapter<NotepadContent> {

    private Context context;
    private int layoutResource;

    public CustomListViewAdapter(Context context, int resource, ArrayList<NotepadContent> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null || row.getTag()==null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResource, null);

            holder = new ViewHolder();

            holder.noteText = (TextView) row.findViewById(R.id.noteTextView);
            holder.noteDate = (TextView) row.findViewById(R.id.dateTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

//            NotepadContent noteslistObjectsTemp = noteslistObjects.get(position);
//
////            if (noteslistObjectsTemp != null) {
////                TextView notetext = (TextView) row.findViewById(R.id.noteTextView);
////                TextView notedate = (TextView) row.findViewById(R.id.dateTextView);
////
////                notetext.setText(noteslistObjectsTemp.getText());
////                notedate.setText(noteslistObjectsTemp.getDate());
////            }

        holder.notepadObject = getItem(position);

        holder.noteText.setText(holder.notepadObject.getText());
        holder.noteDate.setText(holder.notepadObject.getDate());

       final ViewHolder finalHolder = holder;

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, Main3Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("userObj",finalHolder.notepadObject);

                i.putExtras(mBundle);
                context.startActivity(i);

            }
        });
        return row;

    }


    public class ViewHolder{
        NotepadContent notepadObject;
        TextView noteText;
        TextView noteDate;
    }
}

