package model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

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
    private TextToSpeech textToSpeech;


    public CustomListViewAdapter(Context context, int resource, ArrayList<NotesDO> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResource = resource;

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        }, "com.gm.server.speech.tts");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null || row.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResource, null);

            holder = new ViewHolder();

            holder.noteText = (TextView) row.findViewById(R.id.noteTextView);
            holder.noteDate = (TextView) row.findViewById(R.id.dateTextView);
            holder.editButton = (ImageButton) row.findViewById(R.id.open_note_btn);
            holder.textToSpeechBtn = (ImageButton) row.findViewById(R.id.text_to_speech);

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

        UTCTimeGenerator utcTimeGenerator = new UTCTimeGenerator(holder.notepadObject.getDate());

        holder.noteDate.setText(utcTimeGenerator.getMonthandDate() + " at " + utcTimeGenerator.getTime());

        final ViewHolder finalHolder = holder;

        holder.editButton.setOnClickListener(new View.OnClickListener() {
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

        holder.textToSpeechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToSpeech != null) {
                    textToSpeech.stop();
                    textToSpeech.speak(holder.notepadObject.getContent(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        return row;

    }


    public class ViewHolder {
        NotesDO notepadObject;
        TextView noteText;
        ImageButton editButton;
        TextView noteDate;
        ImageButton textToSpeechBtn;
    }
}

