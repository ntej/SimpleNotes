package model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.DatabaseHandler;
import ntej.time.UTCTimeGenerator;
import www.ntej.com.simplenotes.Main3Activity;
import www.ntej.com.simplenotes.NotesDO;
import www.ntej.com.simplenotes.R;

/**
 * Created by navatejareddy on 10/29/16.
 */

public class CustomListViewAdapter extends ArrayAdapter<NotesDO> {

    private Context context;
    private int layoutResource;

    private AlertDialog.Builder alertBuilder;
    private AlertDialog alertDialog;

    private DatabaseHandler dbh;

    public CustomListViewAdapter(Context context, int resource, ArrayList<NotesDO> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResource = resource;

        alertBuilder = new AlertDialog.Builder(context);
        dbh = new DatabaseHandler(context);

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

        holder.noteDate.setText(utcTimeGenerator.getTime()+" "+utcTimeGenerator.getMonthandDate());

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
//        row.setOnClickListener(
//                new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent i = new Intent(context, Main3Activity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        Bundle mBundle = new Bundle();
//                        mBundle.putSerializable("noteObj", finalHolder.notepadObject);
//
//                        i.putExtras(mBundle);
//                        context.startActivity(i);
//
//                    }
//                });

//        row.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                alertBuilder.setTitle("QuickView");
//                alertBuilder.setMessage(finalHolder.notepadObject.getText());
//
//                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        //Notify AWS the note deletion
//                        // Send Custom Event to Amazon Pinpoint
//                        final AnalyticsClient mgr = AWSProvider.getInstance()
//                                .getPinpointManager()
//                                .getAnalyticsClient();
//                        final AnalyticsEvent evt = mgr.createEvent("DeleteNote")
//                                .withAttribute("noteId", String.valueOf(finalHolder.notepadObject.getId()));
//                        mgr.recordEvent(evt);
//                        mgr.submitEvents();
//
//                        //deleting the entry form the DB
//                        dbh.deleteText(finalHolder.notepadObject.getId());
//                        MainActivity.h.sendEmptyMessage(1);
//                    }
//                });
//
//                alertBuilder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                alertDialog = alertBuilder.create();
//                alertDialog.show();
//
//                return true;
//            }
//        });

        return row;

    }


    public class ViewHolder {
        NotesDO notepadObject;
        TextView noteText;
        ImageButton button;
        TextView noteDate;
    }
}

