package www.ntej.com.simplenotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import aws.AWSProvider;
import customListeners.SwipeDismissListViewTouchListener;
import data.AWSDynamoDBHelper;
import data.dynamoDBhelperAsyncTaskInterfaces.DeleteNoteAsyncTaskCompleted;
import data.dynamoDBhelperAsyncTaskInterfaces.GetAllNotesAsyncTaskCompleted;
import model.CustomListViewAdapter;


public class MainActivity extends AppCompatActivity implements GetAllNotesAsyncTaskCompleted, DeleteNoteAsyncTaskCompleted {

    public static Handler h;
    private AWSDynamoDBHelper dynamoDBHelper;
    private CustomListViewAdapter adapter;
    private ArrayList<NotesDO> noteslistobjects = new ArrayList<>();
    private Toolbar toolbar;
    private ListView noteslistview;
    //private FloatingActionButton fab;
    private ImageButton newNoteButton;
    private DeleteNoteAsyncTaskCompleted listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialSetup();
    }

    public void initialSetup(){

        dynamoDBHelper = AWSDynamoDBHelper.getAwsDynamoDBHelper();

        noteslistview = (ListView) findViewById(R.id.noteslist);
        adapter = new CustomListViewAdapter(this, R.layout.listrow, noteslistobjects);
        noteslistview.setAdapter(adapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        newNoteButton = (ImageButton) findViewById(R.id.new_button);
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        listener = this;

        setupSlideToSwipeListView();
    }

    @Override
    public void onGetAllNotesTaskCompleted(ArrayList<NotesDO> notesDOArrayList) {

        noteslistobjects.clear();
        for(int i =0; i<notesDOArrayList.size();i++){
            noteslistobjects.add(notesDOArrayList.get(i));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dynamoDBHelper.new GetAllNotesAsyncTask(this).execute(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
    }

    public void setupSlideToSwipeListView() {
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        noteslistview,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            boolean isDismissInProgress;

                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    isDismissInProgress = true;
                                    dynamoDBHelper.new DeleteNoteAsyncTask(listener).execute(noteslistobjects.get(position).getNoteId());
                                    noteslistobjects.remove(position);
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        });

        noteslistview.setOnTouchListener(touchListener);
    }

    @Override
    public void onDeleteTaskCompleted() {
        Toast.makeText(this, "Notes Deleted", Toast.LENGTH_SHORT).show();
    }
}
