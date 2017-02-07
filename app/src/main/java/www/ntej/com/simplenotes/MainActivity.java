/*Modified by Balaji*/

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

import data.DatabaseHandlerEncrypted;
import model.CustomListViewAdapter;
import model.NotepadContent;

public class MainActivity extends AppCompatActivity {

    public static Handler h;
    private DatabaseHandlerEncrypted dbh_e;
    private CustomListViewAdapter adapter;
    private ArrayList<NotepadContent> noteslistobjects = new ArrayList<>();
    private Toolbar toolbar;
    private ListView noteslistview;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbh_e = new DatabaseHandlerEncrypted(this);

        noteslistobjects = dbh_e.getNotesObjectsAsList();

        adapter = new CustomListViewAdapter(this, R.layout.listrow, noteslistobjects);
        noteslistview = (ListView) findViewById(R.id.noteslist);
        noteslistview.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Main2Activity.class));

            }
        });


        //to finish this activity from stack after performing 'delete action'
        // in Main3Activity, 'discard action' in Main2Activity and
        // updating list view when delete action(dialog box) performed from list view longclick listener.
        h = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

                    case 0:
                        finish();
                        break;
                    case 1:
                        refresh();
                        break;

                }
            }
        };



    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    private void refresh() {

        noteslistobjects.clear(); //mandatory for notifyDataSetChanged() to work

        noteslistobjects = dbh_e.getNotesObjectsAsList();

        adapter.notifyDataSetChanged();

    }

}
