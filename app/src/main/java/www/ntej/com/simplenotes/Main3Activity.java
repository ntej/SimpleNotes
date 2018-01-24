package www.ntej.com.simplenotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import data.AWSDynamoDBHelper;
import data.dynamoDBhelperAsyncTaskInterfaces.DeleteNoteAsyncTaskCompleted;
import model.Util;
import ntej.time.UTCTimeGenerator;

public class Main3Activity extends AppCompatActivity implements DeleteNoteAsyncTaskCompleted {

    private String noteId;
    private String initialNoteText;
    private AWSDynamoDBHelper awsDynamoDBHelper;
    private EditText existingNoteEditText;
    private TextView datePan;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        awsDynamoDBHelper = AWSDynamoDBHelper.getAwsDynamoDBHelper();

        existingNoteEditText = (EditText) findViewById(R.id.notepad2);
        datePan = (TextView) findViewById(R.id.datePan);


        NotesDO notepadObject = (NotesDO) getIntent().getSerializableExtra("noteObj");


        initialNoteText = notepadObject.getContent();
        existingNoteEditText.setText(initialNoteText);

        noteId = notepadObject.getNoteId();

        UTCTimeGenerator utcTimeGenerator = new UTCTimeGenerator(notepadObject.getDate());


        datePan.setText("Last Edited on " + utcTimeGenerator.getTime()+" "+utcTimeGenerator.getMonthandDate());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    protected void onPause() {
        super.onPause();
        upDateTextToDB();
    }

    @Override
    public void onDeleteTaskCompleted() {
        Toast.makeText(this, "EmptyNotes Discarded", Toast.LENGTH_SHORT).show();
    }

    public void upDateTextToDB() {


        if (textChanged()) {
            if (!Util.editTextIsEmpty(existingNoteEditText.getText().toString())) {

                awsDynamoDBHelper.updateNote(noteId, existingNoteEditText.getText().toString());

            } else {
                awsDynamoDBHelper.new DeleteNoteAsyncTask(this).execute(noteId);
            }
        }

    }

    public boolean textChanged() {

        if (!(initialNoteText.contentEquals(existingNoteEditText.getText()))) {
            return true;
        } else {
            return false;
        }

    }


}

