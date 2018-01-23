package www.ntej.com.simplenotes;

import android.support.annotation.NonNull;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName = "simplenotes-mobilehub-1104285407-Notes")

public class NotesDO implements Serializable, Comparable<NotesDO>{
    private static final long serialVersionUID = 10L;
    private String _userId;
    private String _noteId;
    private String _content;
    private Double _date;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBIndexHashKey(attributeName = "userId", globalSecondaryIndexName = "DateSorted")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

    @DynamoDBRangeKey(attributeName = "noteId")
    @DynamoDBAttribute(attributeName = "noteId")
    public String getNoteId() {
        return _noteId;
    }

    public void setNoteId(final String _noteId) {
        this._noteId = _noteId;
    }

    @DynamoDBAttribute(attributeName = "content")
    public String getContent() {
        return _content;
    }

    public void setContent(final String _content) {
        this._content = _content;
    }

    @DynamoDBAttribute(attributeName = "date")
    public Double getDate() {
        return _date;
    }

    public void setDate(final Double _date) {
        this._date = _date;
    }

    @Override
    public int compareTo(@NonNull NotesDO o) {
        return o.getDate().compareTo(new Double(_date) );
    }
}
