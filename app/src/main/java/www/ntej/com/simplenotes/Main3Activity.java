package www.ntej.com.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.CommonMethods;
import model.NotepadContent;

public class Main3Activity extends AppCompatActivity {

    private int noteId;
    private String noteDate;
    private String initialNoteText;
    private boolean notesDeletedFromToolBar = false;
    private DatabaseHandler dbh;
    private Toolbar toolbar;
    private EditText existingNoteEditText;
    private TextView datePan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbh = new DatabaseHandler(this);

        existingNoteEditText = (EditText) findViewById(R.id.notepad2);
        datePan = (TextView) findViewById(R.id.datePan);


        NotepadContent notepadObject = (NotepadContent) getIntent().getSerializableExtra("userObj");


        initialNoteText = notepadObject.getText();
        existingNoteEditText.setText(initialNoteText);

        noteId = notepadObject.getId();
        noteDate = notepadObject.getDateAndTime();


        datePan.setText("Last Edited on " + noteDate);

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (!notesDeletedFromToolBar)
            upDateTextToDB();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity3menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deletebutton:
                notesDeletedFromToolBar = true;
                dbh.deleteText(noteId);
                Toast.makeText(this, "Notes deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main3Activity.this, MainActivity.class));
                finish();
                MainActivity.h.sendEmptyMessage(0);
                return true;

            case R.id.erasebutton:
                if (existingNoteEditText.getText().toString().trim().length() != 0) {
                    existingNoteEditText.setText("");
                    Toast.makeText(this, "Notes cleared", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nothing to clear", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.sharebutton:
                if (!CommonMethods.editTextIsEmpty(existingNoteEditText.getText().toString())) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, existingNoteEditText.getText().toString());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } else {
                    Toast.makeText(this, "Are you sure! you want to share nothing?", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void upDateTextToDB() {


        if (textChanged()) {
            if (!CommonMethods.editTextIsEmpty(existingNoteEditText.getText().toString())) {

                dbh.upDateNoteText(noteId, existingNoteEditText.getText().toString());

            } else {
                dbh.deleteText(noteId);
                Toast.makeText(this, "Empty notes! not good, deleted to save you some space for" + CommonMethods.funPhrases[CommonMethods.ranNumGenerator(CommonMethods.funPhrases.length)], Toast.LENGTH_SHORT).show();
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

