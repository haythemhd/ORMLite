package haythem.hd.ormliteproject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Haythem on 24/10/2017.
 */


@DatabaseTable(tableName = "note")
public class Note {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String subject;

    @DatabaseField
    String text;

    @DatabaseField
    Date date;


    public Note() {
    }

    public Note(String subject, String text) {
        this.subject = subject;
        this.text = text;
        this.date = new Date(System.currentTimeMillis());
    }


    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
