package www.ntej.com.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import data.DatabaseHandler;
import model.CustomListViewAdapter;
import model.NotepadContent;

public class MainActivity extends AppCompatActivity {

    private ListView noteslistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Main2Activity.class));

            }
        });

        noteslistview = (ListView) findViewById(R.id.noteslist);
        displayList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayList();
    }

    private void displayList() {

        DatabaseHandler dba = new DatabaseHandler(getApplicationContext());

        ArrayList<NotepadContent> noteslistobjects;

        noteslistobjects = dba.getNotesObjectsAsList();

        CustomListViewAdapter adapter = new CustomListViewAdapter(getApplicationContext(), R.layout.listrow, noteslistobjects);

        noteslistview.setAdapter(adapter);

    }


}
