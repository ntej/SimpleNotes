package www.ntej.com.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import data.DatabaseHandler;
import data.DatabaseHandlerEncrypted;
import model.CommonMethods;
import model.NotepadContent;

public class Main2Activity extends AppCompatActivity {

    private boolean notDiscardThroughToolBar = true;
    private boolean noteObjectAlreadyCreated = false;
    private NotepadContent latestNoteObject;
    private DatabaseHandlerEncrypted dbh_e;
    private Toolbar toolbar;
    private EditText newNoteEditText;
    private ArrayList<NotepadContent> noteslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newNoteEditText = (EditText) findViewById(R.id.notepad);

        dbh_e = new DatabaseHandlerEncrypted(this);

        newNoteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.toString().trim().length() == 0)) {

                    continuousUpdateTextToDB(newNoteEditText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        notDiscardThroughToolBar = true;


        if (!noteObjectAlreadyCreated) {
            //creating entry
            createDBEntry();
            //getting reference to the entry
            latestNoteObject = noteslist.get(0);
        }

        noteObjectAlreadyCreated = true;

    }

    @Override
    protected void onPause() {

        if (newNoteEditText.getText().toString().trim().length() == 0 && notDiscardThroughToolBar) {

            dbh_e.deleteText(latestNoteObject.getId());
            noteObjectAlreadyCreated = false;

        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity2menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.erasebutton:
                if (newNoteEditText.getText().toString().trim().length() != 0) {
                    newNoteEditText.setText("");
                    Toast.makeText(this, "Notes cleared", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nothing to clear", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.discardbutton:
                notDiscardThroughToolBar = false;
                dbh_e.deleteText(latestNoteObject.getId());
                Toast.makeText(this, "Notes discarded  ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
                finish();
                MainActivity.h.sendEmptyMessage(0);
                return true;

            case R.id.sharebutton:
                if (!CommonMethods.editTextIsEmpty(newNoteEditText.getText().toString())) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, newNoteEditText.getText().toString());
                    sendIntent.setType("newNoteEditText/plain");
                    startActivity(sendIntent);
                } else {
                    Toast.makeText(this, "Are you sure! you want to share nothing?", Toast.LENGTH_SHORT).show();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    public void continuousUpdateTextToDB(String content) {

        dbh_e.upDateNoteText(latestNoteObject.getId(), content);

    }

    public void createDBEntry() {
        dbh_e.storeNoteText("");
        noteslist = dbh_e.getNotesObjectsAsList();
    }


}
