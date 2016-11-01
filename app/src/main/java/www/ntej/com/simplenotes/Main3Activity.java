package www.ntej.com.simplenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import data.DatabaseHandler;
import model.NotepadContent;

public class Main3Activity extends AppCompatActivity {

    private EditText text2;
    int noteId;
    String noteDate;
    String noteText;
    String updatedNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        text2 = (EditText)findViewById(R.id.notepad2);

        NotepadContent notepadObject = (NotepadContent) getIntent().getSerializableExtra("userObj");

        noteText = notepadObject.getText();
        noteId = notepadObject.getId();
        noteDate = notepadObject.getDate();


        text2.setText(noteText);



    }

    @Override
    protected void onPause() {
        super.onPause();
        upDateTextToDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity3menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.deletebutton:

                DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

                dbh.deleteText(noteId);

                startActivity(new Intent(Main3Activity.this,MainActivity.class));

                finish();
                MainActivity.h.sendEmptyMessage(0);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void upDateTextToDB()
    {
        updatedNoteText = text2.getText().toString();

        if(updatedNoteText==noteText){
           // Toast.makeText(this, "Nothing to upadate to DB ", Toast.LENGTH_SHORT).show();
        }

        else
        {
            DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
            dbh.upDateNoteText(noteId,updatedNoteText);
           // Toast.makeText(this, "Updated to DB", Toast.LENGTH_SHORT).show();
        }

    }
}

