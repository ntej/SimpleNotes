package www.ntej.com.simplenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import data.DatabaseHandler;

public class Main2Activity extends AppCompatActivity {

    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        text = (EditText)findViewById(R.id.notepad);

    }

    @Override
    protected void onPause() {
        super.onPause();

        DatabaseHandler dba = new DatabaseHandler(getApplicationContext());

        String notepadtext =text.getText().toString();

        if(notepadtext!=null)
        {
            if(notepadtext=="")
            {
                dba.storeNoteText("Empty Note");
            }
            else
            {
                dba.storeNoteText(notepadtext);
            }
        }
    }
}
