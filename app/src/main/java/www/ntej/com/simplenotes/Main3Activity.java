package www.ntej.com.simplenotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import data.AWSDynamoDBHelper;
import data.dynamoDBhelperAsyncTaskInterfaces.DeleteNoteAsyncTaskCompleted;
import model.CommonMethods;
import ntej.time.UTCTimeGenerator;

public class Main3Activity extends AppCompatActivity implements DeleteNoteAsyncTaskCompleted {

    private String noteId;
    private String noteDate;
    private String initialNoteText;
    //private boolean notesDeletedFromToolBar = false;
    //private DatabaseHandler dbh;
    private AWSDynamoDBHelper awsDynamoDBHelper;
    private EditText existingNoteEditText;
    private TextView datePan;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //dbh = new DatabaseHandler(this);
        awsDynamoDBHelper = AWSDynamoDBHelper.getAwsDynamoDBHelper();

        existingNoteEditText = (EditText) findViewById(R.id.notepad2);
        datePan = (TextView) findViewById(R.id.datePan);


        NotesDO notepadObject = (NotesDO) getIntent().getSerializableExtra("noteObj");


        initialNoteText = notepadObject.getContent();
        existingNoteEditText.setText(initialNoteText);

        UTCTimeGenerator utcTimeGenerator = new UTCTimeGenerator(Double.doubleToLongBits(notepadObject.getDate()));

        noteId = notepadObject.getNoteId();
        noteDate = utcTimeGenerator.getTime()+" "+utcTimeGenerator.getMonthandDate();


        datePan.setText("Last Edited on " + noteDate);

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
            if (!CommonMethods.editTextIsEmpty(existingNoteEditText.getText().toString())) {

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

