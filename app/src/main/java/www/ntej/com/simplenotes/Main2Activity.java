package www.ntej.com.simplenotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsClient;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;

import java.util.UUID;

import aws.AWSProvider;
import data.AWSDynamoDBHelper;

public class Main2Activity extends AppCompatActivity {

    private AWSDynamoDBHelper dynamoDBHelper;
    private EditText newNoteEditText;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        newNoteEditText = (EditText) findViewById(R.id.notepad);

        dynamoDBHelper = AWSDynamoDBHelper.getAwsDynamoDBHelper();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onPause() {

        if (!(newNoteEditText.getText().toString().trim().length() == 0)) {

            final String note_id = UUID.randomUUID().toString();

            //Saving notes to Dynamo DB
            dynamoDBHelper.createNote(note_id, newNoteEditText.getText().toString());

            //Notes is not empty, report the AWS that the notes is created
            //Send Custom Event to Amazon Pinpoint
            final AnalyticsClient mgr = AWSProvider.getInstance()
                    .getPinpointManager()
                    .getAnalyticsClient();
            final AnalyticsEvent evt = mgr.createEvent("AddNote")
                    .withAttribute("noteId", note_id);
            mgr.recordEvent(evt);
            mgr.submitEvents();

        }
        finish();
        super.onPause();
    }
}
