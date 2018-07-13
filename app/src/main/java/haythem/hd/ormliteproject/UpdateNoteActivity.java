package haythem.hd.ormliteproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

public class UpdateNoteActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;

    EditText text,subject;
    Button update,toRecordActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("id",0);

        text = (EditText)findViewById(R.id.et_text);
        subject = (EditText)findViewById(R.id.et_subject);
        update = (Button) findViewById(R.id.update_bt);
        toRecordActivity = (Button) findViewById(R.id.list_bt);

        try {

            QueryBuilder<Note, Integer> qb;
            qb = getHelper().getNotesDao().queryBuilder();
            qb.where().eq("id", String.valueOf(id));

            PreparedQuery<Note> pq = qb.prepare();
            Note n = getHelper().getNotesDao().queryForFirst(pq);

            text.setText(n.text);
            subject.setText(n.subject);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        toRecordActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UpdateNoteActivity.this,ViewNoteRecordActivity.class);
                startActivity(in);
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBuilder<Note, Integer> updateBuilder = null;
                try {

                    updateBuilder = getHelper().getNotesDao().updateBuilder();
                    // set the criteria like you would a QueryBuilder
                    updateBuilder.where().eq("id", id);
                    // update the value of your field(s)
                    updateBuilder.updateColumnValue("subject",subject.getText().toString());
                    updateBuilder.updateColumnValue("text",text.getText().toString());
                    updateBuilder.update();
                    Toast.makeText(v.getContext(),"Note updated", Toast.LENGTH_LONG).show();

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

    }
    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}