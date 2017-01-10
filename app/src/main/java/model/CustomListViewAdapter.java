package model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import data.DatabaseHandler;
import data.DatabaseHandlerEncrypted;
import www.ntej.com.simplenotes.Main3Activity;
import www.ntej.com.simplenotes.MainActivity;
import www.ntej.com.simplenotes.R;

/**
 * Created by navatejareddy on 10/29/16.
 */

public class CustomListViewAdapter extends ArrayAdapter<NotepadContent> {

    private Context context;
    private int layoutResource;

    private AlertDialog.Builder alertBuilder;
    private AlertDialog alertDialog;

    private DatabaseHandlerEncrypted dbh_e;

    public CustomListViewAdapter(Context context, int resource, ArrayList<NotepadContent> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResource = resource;

        alertBuilder = new AlertDialog.Builder(context);
        dbh_e = new DatabaseHandlerEncrypted(context);

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

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.notepadObject = getItem(position);


        holder.noteText.setText(holder.notepadObject.getText() + "...");
        holder.noteDate.setText(holder.notepadObject.getDateAndTime());

        final ViewHolder finalHolder = holder;

        row.setOnClickListener(
                new View.OnClickListener()  //Ananomous inner class
                {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, Main3Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("userObj", finalHolder.notepadObject);

                        i.putExtras(mBundle);
                        context.startActivity(i);

                    }
                });

        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                alertBuilder.setTitle("QuickView");
                alertBuilder.setMessage(finalHolder.notepadObject.getText());

                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbh_e.deleteText(finalHolder.notepadObject.getId());
                        MainActivity.h.sendEmptyMessage(1);
                    }
                });

                alertBuilder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog = alertBuilder.create();
                alertDialog.show();

                return true;
            }
        });

        return row;

    }


    public class ViewHolder {
        NotepadContent notepadObject;
        TextView noteText;
        TextView noteDate;
    }
}

