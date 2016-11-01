package www.ntej.com.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import data.DatabaseHandler;
import model.CustomListViewAdapter;
import model.NotepadContent;

public class MainActivity extends AppCompatActivity {

    private ListView noteslistview;

    public static Handler h;

    ArrayList<NotepadContent> noteslistobjects = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Main2Activity.class));

            }
        });

        noteslistview = (ListView) findViewById(R.id.noteslist);
        refresh();

        //to finish this activity from stack after performing delete in Main3Activity and discard in Main2Activity
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

        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

        noteslistobjects = dbh.getNotesObjectsAsList();

//        ArrayList<NotepadContent> noteslistobjectsTemp = dbh.getNotesObjectsAsList();

//        for(int i =0; i<noteslistobjectsTemp.size();i++)
//        {
//            String text = noteslistobjectsTemp.get(i).getText();
//            String date = noteslistobjectsTemp.get(i).getDate();
//            int id = noteslistobjectsTemp.get(i).getId();
//
//            NotepadContent notepadObject = new NotepadContent();
//
//            notepadObject.setText(text);
//            notepadObject.setDate(date);
//            notepadObject.setId(id);
//
//            noteslistobjects.add(notepadObject);
//        }

        CustomListViewAdapter adapter = new CustomListViewAdapter(getApplicationContext(), R.layout.listrow, noteslistobjects);
        noteslistview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
