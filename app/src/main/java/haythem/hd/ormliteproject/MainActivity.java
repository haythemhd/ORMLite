package haythem.hd.ormliteproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Reference of DatabaseHelper class to access its DAOs and other components
    private DatabaseHelper databaseHelper = null;

    private EditText subject_et, text_et;
    private Button reset_btn, submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        subject_et = (EditText) findViewById(R.id.subject_et);
        text_et = (EditText) findViewById(R.id.text_et);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        submit_btn = (Button) findViewById(R.id.submit_btn);

        reset_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    // This is how DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this,DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onClick(View v) {

        if(v == submit_btn)
        {
            // All input fields are mandatory, so made a check
            if(subject_et.getText().toString().trim().length() > 0 &&
                    text_et.getText().toString().trim().length() > 0)
            {
                // Once click on "Submit", it's first creates the TeacherDetails object
                final Note note = new Note();

                // Then, set all the values from user input
                note.subject = subject_et.getText().toString();
                note.text = text_et.getText().toString();

                try {
                    // This is how, a reference of DAO object can be done
                    //Okey. What's the Integer?
                    final Dao<Note, Integer> noteDao = getHelper().getNotesDao();

                    //This is the way to insert data into a database table
                    noteDao.create(note);
                    reset();
                    showDialog();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // Show a dialog with appropriate message in case input fields are blank
            else
            {
                showMessageDialog("All fields are mandatory !!");
            }
        }
        else if(v == reset_btn)
        {
            reset();
        }
    }

    private void showMessageDialog(final String message)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Clear the entered text
    private void reset()
    {
        subject_et.setText("");
        text_et.setText("");
    }

    private void showDialog()
    {
        // After submission, Dialog opens up with "Success" message. So, build the AlartBox first
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the appropriate message into it.
        alertDialogBuilder.setMessage("Note record added successfully !!");

        // Add a positive button and it's action. In our case action would be, just hide the dialog box ,
        // so no need to write any code for that.
        alertDialogBuilder.setPositiveButton("Add More",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();
                    }
                });

        // Add a negative button and it's action. In our case, just open up the ViewTeacherRecordActivity screen
        // to display all the records
        alertDialogBuilder.setNegativeButton("View Records",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent negativeActivity = new Intent(getApplicationContext(),ViewNoteRecordActivity.class);
                        startActivity(negativeActivity);
                        finish();
                    }
                });

        // Now, create the Dialog and show it.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * You'll need this in your class to release the helper when done.
         */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}