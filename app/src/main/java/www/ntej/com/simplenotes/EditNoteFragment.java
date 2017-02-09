package www.ntej.com.simplenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandlerEncrypted;
import model.CommonMethods;
import model.NotepadContent;

public class EditNoteFragment extends Fragment {

    private int noteId;
    private String noteDate;
    private String initialNoteText = "";
    private boolean notesDeletedFromToolBar = false;
    private DatabaseHandlerEncrypted dbh_e;
    private Toolbar toolbar;
    private EditText existingNoteEditText;
    private TextView datePan;

    //interface implemented by hosting activity
    DeleteNoteListener deleteNoteMethodListener;

    interface DeleteNoteListener
    {
        public void deleteNote();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.edit_note_fragment);

        setHasOptionsMenu(true);

        dbh_e = new DatabaseHandlerEncrypted(getActivity());

        // NotepadContent notepadObject = (NotepadContent) getIntent().getSerializableExtra("userObj");

    }

    //called from Hosting Activity
    public void setNoteContent(NotepadContent notepadObject)
    {
            initialNoteText = notepadObject.getText();

            existingNoteEditText.setText(initialNoteText);

            noteId = notepadObject.getId();
            noteDate = notepadObject.getDateAndTime();


            datePan.setText("Last Edited on " + noteDate);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_note_fragment,container,false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        existingNoteEditText = (EditText) view.findViewById(R.id.notepad2);
        datePan = (TextView) view.findViewById(R.id.datePan);



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!notesDeletedFromToolBar)
            upDateTextToDB();


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.edit_note_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deletebutton:
                notesDeletedFromToolBar = true;
                dbh_e.deleteText(noteId);
                Toast.makeText(getActivity(), "Notes deleted", Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(EditNoteFragment.this, NotesListFragment.class));
               // finish();
               // NotesListFragment.h.sendEmptyMessage(0);
                deleteNoteMethodListener.deleteNote();
                return true;

            case R.id.erasebutton:
                if (existingNoteEditText.getText().toString().trim().length() != 0) {
                    existingNoteEditText.setText("");
                    Toast.makeText(getActivity(), "Notes cleared", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Nothing to clear", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.sharebutton:
                if (!CommonMethods.editTextIsEmpty(existingNoteEditText.getText().toString())) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, existingNoteEditText.getText().toString());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } else {
                    Toast.makeText(getActivity(), "Are you sure! you want to share nothing?", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void upDateTextToDB() {


        if (textChanged()) {
            if (!CommonMethods.editTextIsEmpty(existingNoteEditText.getText().toString())) {

                dbh_e.upDateNoteText(noteId, existingNoteEditText.getText().toString());

            } else {
                dbh_e.deleteText(noteId);
                Toast.makeText(getActivity(), "Empty notes! not good, deleted to save you some space for" + CommonMethods.funPhrases[CommonMethods.ranNumGenerator(CommonMethods.funPhrases.length)], Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean textChanged() {

        if (!(initialNoteText.contentEquals(existingNoteEditText.getText()))) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            deleteNoteMethodListener = (DeleteNoteListener) activity;
        }
        catch (ClassCastException e)
        {

        }
    }
}

