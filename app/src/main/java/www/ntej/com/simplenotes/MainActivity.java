package www.ntej.com.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    private FloatingActionButton fab;

    DatabaseHandler dbh;

    public static Handler h;

    ArrayList<NotepadContent> noteslistobjects = new ArrayList<>();




    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbh = new DatabaseHandler(getApplicationContext());

        noteslistview = (ListView) findViewById(R.id.noteslist);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Main2Activity.class));

            }
        });




        //to finish this activity from stack after performing 'delete action' in Main3Activity and 'discard action' in Main2Activity
        h = new Handler() {
            public void  handleMessage(Message msg)
            {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;

                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {

        noteslistobjects.clear(); //mandatory for notifyDataSetChanged() to work



        noteslistobjects = dbh.getNotesObjectsAsList();

        CustomListViewAdapter adapter = new CustomListViewAdapter(getApplicationContext(), R.layout.listrow, noteslistobjects);
        noteslistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
