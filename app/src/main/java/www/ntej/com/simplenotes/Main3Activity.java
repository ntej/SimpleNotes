package www.ntej.com.simplenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import model.NotepadContent;

public class Main3Activity extends AppCompatActivity {

    private EditText text2;
    int noteId;
    String noteDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        text2 = (EditText)findViewById(R.id.notepad2);

        NotepadContent notepadObject = (NotepadContent) getIntent().getSerializableExtra("userObj");

        text2.setText(notepadObject.getText());
        noteId = notepadObject.getId();
        noteDate = notepadObject.getDate();

        Toast.makeText(this, "ID:"+noteId+"Date:"+noteDate , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
