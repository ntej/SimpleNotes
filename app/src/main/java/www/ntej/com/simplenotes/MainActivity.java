package www.ntej.com.simplenotes;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import model.CustomListViewAdapter;
import model.NotepadContent;

public class MainActivity extends AppCompatActivity implements
        CustomListViewAdapter.CustomAdapterOnClickListener,
        NotesListFragment.AddNoteButtonListener


{
    NewNoteFragment newNoteFragment;
    EditNoteFragment editNoteFragment;
    NotesListFragment notesListFragment;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    boolean isLargeOrXlargeScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newNoteFragment = new NewNoteFragment();
        editNoteFragment = new EditNoteFragment();
        notesListFragment = new NotesListFragment();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //for tablets(large/x-large screens) locking screen orientation to landscape
        if(isScreenLarge())
        {
            //handle double pan

            //locking the orientation to landscape
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isLargeOrXlargeScreen = true;

            fragmentTransaction.add(R.id.noteslist_container,notesListFragment);
            fragmentTransaction.commit();



        }
        else
        {
            //handle single pan
            fragmentTransaction.add(R.id.noteslist_container,notesListFragment);
            fragmentTransaction.commit();
        }
    }



    //for checking screen size
    public  boolean isScreenLarge()
    {
        final int screenSize =
                getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;

        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
                || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;

    }


    /***************/
    @Override
    public void ClickedOnNoteObject(NotepadContent notepadObject) {

        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();


            if (isLargeOrXlargeScreen)
            {
                //double pan

                if (getSupportFragmentManager().findFragmentById(R.id.new_edit_notes_container) == null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.new_edit_notes_container, editNoteFragment);
                    fragmentTransaction.commit();
                    editNoteFragment.setNoteContent(notepadObject);



                }
                else
                {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.new_edit_notes_container, editNoteFragment);
                    fragmentTransaction.commit();
                    editNoteFragment.setNoteContent(notepadObject);

                }

            }
            else
            {
                //single pan
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.noteslist_container, editNoteFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                editNoteFragment.setNoteContent(notepadObject);



            }



        }

    @Override
    public void addNote() {

        if(isLargeOrXlargeScreen)
        {
            //double pan

            if(getSupportFragmentManager().findFragmentById(R.id.new_edit_notes_container)==null)
            {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.new_edit_notes_container, newNoteFragment);
                fragmentTransaction.commit();
            }
            else
            {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.new_edit_notes_container, newNoteFragment);
                fragmentTransaction.commit();
            }


        }
        else
        {
            //single pan
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.noteslist_container,newNoteFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        /*****************/


    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0)
        {
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

//    public byte[] getSerilizedNoteObject(NotepadContent notepadContent) throws IOException
//    {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutput out =new ObjectOutputStream(bos); ;
//
//
//        out.writeObject(notepadContent);
//        byte[] noteBytes = bos.toByteArray();
//
//        out.close();
//        bos.close();
//
//
//        return noteBytes;
//
//    }

}
