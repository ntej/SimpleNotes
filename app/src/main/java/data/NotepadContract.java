package data;

import android.provider.BaseColumns;

/**
 * Created by navatejareddy on 10/29/16.
 */
@Deprecated
public final class NotepadContract {


    private NotepadContract() {
    }

    ;

    public static class NotepadEntry implements BaseColumns {

        //Values of the table
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_CONTENT = "notepadcontent";
        public static final String DATE = "dateAndTime";

        //tabel creation
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + NotepadContract.NotepadEntry.TABLE_NAME + " (" +
                        NotepadContract.NotepadEntry._ID + " INTEGER PRIMARY KEY," +
                        NotepadContract.NotepadEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                        NotepadContract.NotepadEntry.DATE + " LONG)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS" + NotepadContract.NotepadEntry.TABLE_NAME;

    }


}