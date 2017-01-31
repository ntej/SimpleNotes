package www.ntej.com.simplenotes;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import data.DatabaseHandlerEncrypted;
import model.CustomListViewAdapter;
import model.NotepadContent;

public class NotesListFragment extends Fragment implements CustomListViewAdapter.CustomAdapterOnClickListener {

    public static Handler h;
    private DatabaseHandlerEncrypted dbh_e;
    private CustomListViewAdapter adapter;
    private ArrayList<NotepadContent> noteslistobjects = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    public void ClickedOnNoteObject(NotepadContent notepadObject) {

        getNoteMethodListener.getNoteObject(notepadObject);
    }

    private ListView noteslistview;
    private FloatingActionButton fab;

    //Interfaced to be implemented by hosted activities
    AddNoteButtonListener addNoteMethodListener;
    AdapterOnClickListener getNoteMethodListener;

    interface AddNoteButtonListener
    {
        public void addNote();
    }

    interface AdapterOnClickListener
    {
        public void getNoteObject(NotepadContent noteObject);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.notes_list_fragment);

       //this fragment uses appbar menu
        setHasOptionsMenu(true);

        //creating instance of the database
        dbh_e = new DatabaseHandlerEncrypted(getActivity());
        //getting data from the database
        noteslistobjects = dbh_e.getNotesObjectsAsList();

        //instantiating the adapter
        adapter = new CustomListViewAdapter(getActivity(), R.layout.listrow, noteslistobjects);


        //to finish this activity from stack after performing 'delete action'
        // in EditNoteFragment, 'discard action' in NewNoteFragment and
        // updating list view when delete action(dialog box) performed from list view longclick listener.
        h = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

//                    case 0:
//                        finish();
//                        break;
                    case 1:
                        refresh();
                        break;

                }
            }
        };



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notes_list_fragment,container,false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        noteslistview = (ListView) view.findViewById(R.id.noteslist);
        noteslistview.setAdapter(adapter);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(NotesListFragment.this, NewNoteFragment.class));
                addNoteMethodListener.addNote();

            }
        });

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


    private void refresh() {

        noteslistobjects.clear(); //mandatory for notifyDataSetChanged() to work

        noteslistobjects = dbh_e.getNotesObjectsAsList();

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try
        {
            addNoteMethodListener = (AddNoteButtonListener)activity;
            getNoteMethodListener = (AdapterOnClickListener)activity;

        }
        catch(ClassCastException e)
        {

        }

    }
}
