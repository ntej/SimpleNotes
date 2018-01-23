package data.dynamoDBhelperAsyncTaskInterfaces;

import java.util.ArrayList;

import www.ntej.com.simplenotes.NotesDO;

/**
 * Created by hz7d7v on 1/21/18.
 */

public interface GetAllNotesAsyncTaskCompleted {
    void onGetAllNotesTaskCompleted(ArrayList<NotesDO> notesDOS);
}
