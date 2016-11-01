package www.ntej.com.simplenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import data.DatabaseHandler;
import model.CommonMethods;

public class Main2Activity extends AppCompatActivity {

    private EditText text;
    boolean discard;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity2menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.clearbutton:
                text.setText("");
                Toast.makeText(this, "Notes cleared", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.discardbutton:
                discard = true;
                Toast.makeText(this, "Notes discarded  ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main2Activity.this,MainActivity.class));
                finish();
                MainActivity.h.sendEmptyMessage(0);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void saveTextToDB()
    {
        if(!discard) {

            DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

            if(!CommonMethods.editTextIsEmpty(text.getText().toString()))
            {
                dbh.storeNoteText(text.getText().toString());
            }
            else
            {
                Toast.makeText(this, "Empty notes discarded to save you some space for"+ CommonMethods.funPhrases[CommonMethods.ranNumGenerator(CommonMethods.funPhrases.length)], Toast.LENGTH_LONG).show();
            }
        }
    }





}
