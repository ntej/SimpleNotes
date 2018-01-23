package data;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import aws.AWSProvider;
import data.dynamoDBhelperAsyncTaskInterfaces.DeleteNoteAsyncTaskCompleted;
import data.dynamoDBhelperAsyncTaskInterfaces.GetAllNotesAsyncTaskCompleted;
import www.ntej.com.simplenotes.NotesDO;

import static android.content.ContentValues.TAG;

/**
 * Amazon Web services DynamoDB helper class - Singleton
 */

public class AWSDynamoDBHelper {

    private DynamoDBMapper dynamoDBMapper;

    private static AWSDynamoDBHelper awsDynamoDBHelper = null;

    private AWSDynamoDBHelper() {
        createDynamoDBMapperClient();
    }

    public static AWSDynamoDBHelper getAwsDynamoDBHelper() {
        // To ensure only one instance is created
        if (awsDynamoDBHelper == null) {
            awsDynamoDBHelper = new AWSDynamoDBHelper();
        }
        return awsDynamoDBHelper;
    }

    /**
     * Create new entry in to DynamoDB
     * @param note_id:unique id created for the new note
     * @param text:Note content
     */
    public void createNote(String note_id, String text) {
        final NotesDO notesDO = new NotesDO();

        notesDO.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
        notesDO.setNoteId(note_id);
        notesDO.setContent(text);
        notesDO.setDate((double)System.currentTimeMillis());

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(notesDO);
                // Item saved
            }
        }).start();

    }


    /**
     * Update the existing item in DynamoDB
     * @param note_id: id of edited note
     * @param updatedText: changed note content
     */
    public void updateNote(String note_id, String updatedText) {
        final NotesDO notesDO = new NotesDO();

        notesDO.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
        notesDO.setNoteId(note_id);
        notesDO.setContent(updatedText);
        notesDO.setDate((double)System.currentTimeMillis());

        new Thread(new Runnable() {
            @Override
            public void run() {

                dynamoDBMapper.save(notesDO);
                // Item updated
            }
        }).start();

    }

    /**
     * Async Task to delete item form DynamoDB
     */
    public class DeleteNoteAsyncTask extends AsyncTask<String, Void, Void> {

        /**
         * callback
         */
        private DeleteNoteAsyncTaskCompleted listener;

        public DeleteNoteAsyncTask(DeleteNoteAsyncTaskCompleted listener){

            this.listener = listener;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.onDeleteTaskCompleted();
        }

        @Override
        protected Void doInBackground(String... strings) {

            NotesDO notesDO = new NotesDO();

            notesDO.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());    //partition key

            notesDO.setNoteId(strings[0]);  //range (sort) key

            dynamoDBMapper.delete(notesDO);

            return null;
        }
    }

    /**
     * Asynctask to get allnotes from DynamoDB
     */
    public class GetAllNotesAsyncTask extends AsyncTask<String, Void, ArrayList<NotesDO>> {

        /**
         * callback
         */
        private GetAllNotesAsyncTaskCompleted listener;

        public GetAllNotesAsyncTask(GetAllNotesAsyncTaskCompleted listener){
            this.listener = listener;
        }

        @Override
        protected void onPostExecute(ArrayList<NotesDO> notesDOArrayList) {
            super.onPostExecute(notesDOArrayList);
            listener.onGetAllNotesTaskCompleted(notesDOArrayList);
        }

        @Override
        protected ArrayList<NotesDO> doInBackground(String... strings) {

            ArrayList<NotesDO> notesDOArrayList = new ArrayList<>();

            DynamoDBMapper dbMapper = dynamoDBMapper;
            NotesDO template = new NotesDO();
            template.setUserId(AWSProvider.getInstance().getIdentityManager().getCachedUserID());
            // Now create a query expression that is based on the template record.
            DynamoDBQueryExpression<NotesDO> queryExpression;
            queryExpression = new DynamoDBQueryExpression<NotesDO>()
                    .withHashKeyValues(template);
            // Finally, do the query with that query expression.
            List<NotesDO> result = dbMapper.query(NotesDO.class, queryExpression);
            Iterator<NotesDO> iterator = result.iterator();

            while (iterator.hasNext()) {
                NotesDO note = iterator.next();
                notesDOArrayList.add(note);
                Log.d(TAG, "doInBackground: "+note.getDate());
            }

            Collections.sort(notesDOArrayList);

            return notesDOArrayList;
        }
    }

    /**
     * creates the dynamoDB client
     */
    private void createDynamoDBMapperClient() {
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
    }

}
