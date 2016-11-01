package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.NotepadContent;

/**
 * Created by navatejareddy on 10/29/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper  {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="SimpleNotes.db";

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

//    public String getNoteText(String id)
//    {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String[] projection = {
//                NotepadContract.NotepadEntry._ID,
//                NotepadContract.NotepadEntry.COLUMN_NAME_CONTENT,
//                NotepadContract.NotepadEntry.DATE
//        };
//
//        String selection = NotepadContract.NotepadEntry._ID + " = ? ";
//        String[] selectionArgs = {id};
//
//        String sortOrder = NotepadContract.NotepadEntry.DATE + " DESC";
//
//        Cursor c = db.query(
//                NotepadContract.NotepadEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder
//        );
//
//        c.moveToFirst();
//
//        String textAtId = c.getString(c.getColumnIndexOrThrow(NotepadContract.NotepadEntry.COLUMN_NAME_CONTENT));
//
//        return textAtId;
//
//    }


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

                DateFormat dateFormat =DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(c.getLong(c.getColumnIndexOrThrow(NotepadContract.NotepadEntry.DATE))).getTime());

                notepadContent.setDate(date);

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

}






























