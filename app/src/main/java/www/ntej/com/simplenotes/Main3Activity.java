package www.ntej.com.simplenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
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


        text2.setText(noteText); //



    }

    @Override
    protected void onStop() {
        super.onStop();

        upDateTextToDB();

    }

    public void upDateTextToDB()
    {
        updatedNoteText = text2.getText().toString();

        if(updatedNoteText==noteText){
           // Toast.makeText(this, "Nothing to upadate to DB ", Toast.LENGTH_SHORT).show();
        }

        else
        {
            DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
            dba.upDateNoteText(noteId,updatedNoteText);
           // Toast.makeText(this, "Updated to DB", Toast.LENGTH_SHORT).show();
        }

    }
}
