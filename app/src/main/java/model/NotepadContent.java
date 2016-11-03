package model;

import java.io.Serializable;

/**
 * Created by navatejareddy on 10/29/16.
 */

public class NotepadContent implements Serializable {

    private static final long serialVersionUID = 10L;
    public String text;
    public String dateAndTime;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

}
