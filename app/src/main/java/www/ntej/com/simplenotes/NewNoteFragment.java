package www.ntej.com.simplenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import data.DatabaseHandlerEncrypted;
import model.CommonMethods;
import model.NotepadContent;

public class NewNoteFragment extends Fragment {

    private boolean notDiscardThroughToolBar = true;
    private boolean noteObjectAlreadyCreated = false;
    private NotepadContent latestNoteObject;
    private DatabaseHandlerEncrypted dbh_e,dbh_e_listener;
    private Toolbar toolbar;
    private EditText newNoteEditText;
    private ArrayList<NotepadContent> noteslist = new ArrayList<>();

    //Interface to be implemented by host activity
    public DiscardListener discardMethodListener;

    interface DiscardListener
    {
        public void discardNotes();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.new_note_fragment);

        //this fragment uses appbar menu
        setHasOptionsMenu(true);


        dbh_e = new DatabaseHandlerEncrypted(getActivity());
        dbh_e_listener = new DatabaseHandlerEncrypted(getActivity());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_note_fragment,container,false);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        newNoteEditText = (EditText) view.findViewById(R.id.notepad);

        newNoteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.toString().trim().length() == 0)) {

                    continuousUpdateTextToDB(newNoteEditText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        notDiscardThroughToolBar = true;


        if (!noteObjectAlreadyCreated) {
            //creating entry
            createDBEntry();
            //getting reference to the entry
            latestNoteObject = noteslist.get(0);
        }

        noteObjectAlreadyCreated = true;

    }

    @Override
    public void onPause() {

        if (newNoteEditText.getText().toString().trim().length() == 0 && notDiscardThroughToolBar) {

            dbh_e.deleteText(latestNoteObject.getId());
            noteObjectAlreadyCreated = false;

        }
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.new_note_fragment_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);

        //getMenuInflater().inflate(R.menu.new_note_fragment_menu, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.erasebutton:
                if (newNoteEditText.getText().toString().trim().length() != 0) {
                    newNoteEditText.setText("");
                    Toast.makeText(getActivity(), "Notes cleared", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Nothing to clear", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.discardbutton:
                notDiscardThroughToolBar = false;
                dbh_e.deleteText(latestNoteObject.getId());
                Toast.makeText(getActivity(), "Notes discarded  ", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(NewNoteFragment.this, NotesListFragment.class));
                discardMethodListener.discardNotes();
                //finish();
                //NotesListFragment.h.sendEmptyMessage(0);
                return true;

            case R.id.sharebutton:
                if (!CommonMethods.editTextIsEmpty(newNoteEditText.getText().toString())) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, newNoteEditText.getText().toString());
                    sendIntent.setType("newNoteEditText/plain");
                    startActivity(sendIntent);
                } else {
                    Toast.makeText(getActivity(), "Are you sure! you want to share nothing?", Toast.LENGTH_SHORT).show();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    public void continuousUpdateTextToDB(String content) {

        dbh_e_listener.upDateNoteTextListener(latestNoteObject.getId(), content);

    }

    public void createDBEntry() {
        dbh_e.storeNoteText("");
        noteslist = dbh_e.getNotesObjectsAsList();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
                discardMethodListener = (DiscardListener)activity;
        }
        catch (ClassCastException e)
        {

        }

    }
}
