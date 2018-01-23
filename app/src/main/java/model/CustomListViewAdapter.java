package model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ntej.time.UTCTimeGenerator;
import www.ntej.com.simplenotes.Main3Activity;
import www.ntej.com.simplenotes.NotesDO;
import www.ntej.com.simplenotes.R;

/**
 * ListView Adapter
 */

public class CustomListViewAdapter extends ArrayAdapter<NotesDO> {

    private Context context;
    private int layoutResource;

    public CustomListViewAdapter(Context context, int resource, ArrayList<NotesDO> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null || row.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResource, null);

            holder = new ViewHolder();

            holder.noteText = (TextView) row.findViewById(R.id.noteTextView);
            holder.noteDate = (TextView) row.findViewById(R.id.dateTextView);
            holder.button = (ImageButton) row.findViewById(R.id.open_note_btn);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.notepadObject = getItem(position);

        String previewContent = holder.notepadObject.getContent().toString();

        if (previewContent.length() >= 50) {
            holder.noteText.setText(previewContent.substring(0, 50));
        } else {
            holder.noteText.setText(previewContent);
        }

        UTCTimeGenerator utcTimeGenerator = new UTCTimeGenerator(Double.doubleToLongBits(holder.notepadObject.getDate()));

        holder.noteDate.setText(utcTimeGenerator.getTime() + " " + utcTimeGenerator.getMonthandDate());

        final ViewHolder finalHolder = holder;

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Main3Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("noteObj", finalHolder.notepadObject);

                i.putExtras(mBundle);
                context.startActivity(i);
            }
        });

        return row;

    }


    public class ViewHolder {
        NotesDO notepadObject;
        TextView noteText;
        ImageButton button;
        TextView noteDate;
    }
}

