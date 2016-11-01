package www.ntej.com.simplenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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

       saveTextToDB();
    }

    public void saveTextToDB()
    {
        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

        String notepadtext= text.getText().toString();

        if(notepadtext!=null)
        {
            if(notepadtext=="")
            {
                dbh.storeNoteText("Empty Note");
            }
            else
            {
                dbh.storeNoteText(notepadtext);
            }
        }
    }
}
