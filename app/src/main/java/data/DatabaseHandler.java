package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

import model.NotepadContent;


/**
 * Created by navatejareddy on 10/29/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper  {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="SimpleNotes.db";
    private long timeInMilliseconds;
    String TAG = " dbh";

    private final ArrayList<NotepadContent> noteslist = new ArrayList<>();



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NotepadContract.NotepadEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(NotepadContract.NotepadEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void storeNoteText(String text)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotepadContract.NotepadEntry.COLUMN_NAME_CONTENT,text);
        values.put(NotepadContract.NotepadEntry.DATE,System.currentTimeMillis());

        db.insert(NotepadContract.NotepadEntry.TABLE_NAME,null,values);

        db.close();

    }




    public ArrayList<NotepadContent> getNotesObjectsAsList()
    {
        noteslist.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                NotepadContract.NotepadEntry._ID,
                NotepadContract.NotepadEntry.COLUMN_NAME_CONTENT,
                NotepadContract.NotepadEntry.DATE
        };

        String sortOrder = NotepadContract.NotepadEntry.DATE + " DESC";
        Cursor c = db.query(
               NotepadContract.NotepadEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if(c.moveToFirst())
        {
            do {
                NotepadContent notepadContent = new NotepadContent();

                notepadContent.setText(c.getString(c.getColumnIndexOrThrow(NotepadContract.NotepadEntry.COLUMN_NAME_CONTENT)));

                notepadContent.setId(c.getInt(c.getColumnIndexOrThrow(NotepadContract.NotepadEntry._ID)));

                timeInMilliseconds = c.getLong(c.getColumnIndexOrThrow(NotepadContract.NotepadEntry.DATE));

                notepadContent.setDateAndTime(dateAndTimeGenerator(timeInMilliseconds));

                noteslist.add(notepadContent);

            }while(c.moveToNext());
        }

        c.close();
        db.close();

        return noteslist;


    }

    public void upDateNoteText(int _id, String updatedtext)
    {
        String id = Integer.toString(_id);
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotepadContract.NotepadEntry.COLUMN_NAME_CONTENT,updatedtext);
        values.put(NotepadContract.NotepadEntry.DATE,System.currentTimeMillis());

        String selection = NotepadContract.NotepadEntry._ID + " LIKE ?";
        String[] selectionArgs = { id };

        db.update(NotepadContract.NotepadEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

    }

    public void deleteText(int _id)
    {

        SQLiteDatabase dba = this.getWritableDatabase();

        String selection = NotepadContract.NotepadEntry._ID + " LIKE ?";

        String[] selectionArgs = {Integer.toString(_id)};

        dba.delete(NotepadContract.NotepadEntry.TABLE_NAME, selection,selectionArgs);
    }


    private String dateAndTimeGenerator(long millis)
    {

        /*Date and Month variables*/
        int dateofTheMonthSaved;
        int monthNumberSaved;
        String monthName="";

        /*Time Variables*/
        String timeofTheDaysaved;
        String hour;
        String minutes;
        int AM_orPM_number;
        String AM_orPM;



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        /*Getting Month and Date*/
        dateofTheMonthSaved = calendar.get(Calendar.DATE);
        monthNumberSaved = calendar.get(Calendar.MONTH);
        switch (monthNumberSaved) {
            case 0:
                monthName = "Jan";
                break;

            case 1:
                monthName = "Feb";
                break;

            case 2:
                monthName = "Mar";
                break;

            case 3:
                monthName = "Apr";
                break;

            case 4:
                monthName = "May";
                break;

            case 5:
                monthName = "Jun";
                break;

            case 6:
                monthName = "Jul";
                break;

            case 7:
                monthName = "Aug";
                break;

            case 8:
                monthName = "Sep";
                break;

            case 9:
                monthName = "Oct";
                break;

            case 10:
                monthName = "Nov";
                break;

            case 11:
                monthName = "Dec";
                break;
        }


        /*Getting Time*/
        hour = Integer.toString(calendar.get(Calendar.HOUR));
        minutes =Integer.toString(calendar.get(Calendar.MINUTE));
        if(minutes.trim().length()==1) //adding zero infront of single digit minutes
        {
            minutes = 0+minutes;
        }


        timeofTheDaysaved = hour + ":" + minutes;

        AM_orPM_number = calendar.get(Calendar.AM_PM);
        if(AM_orPM_number == 0)
        {
            AM_orPM = "AM";
        }
        else
        {
            AM_orPM = "PM";
        }


        return dateofTheMonthSaved+" "+monthName + " at " + timeofTheDaysaved+AM_orPM;
    }

}






























