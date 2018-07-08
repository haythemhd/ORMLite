package haythem.hd.ormliteproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class ViewNoteRecordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    // Reference of DatabaseHelper class to access its DAOs and other components
    private DatabaseHelper databaseHelper = null;

    // This ListView displays the list of Student Details data fetched from database.
    private ListView listview;

    // This holds the value of the row number, which user has selected for further action
    private int selectedRecordPosition = -1;

    // Declaration of DAOs to interact with corresponding table
    private Dao<Note, Integer> noteDao;

    // It holds the list of StudentDetails objects fetched from Database
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note_record);
        listview = (ListView) findViewById(R.id.listview);

        try {
            // This is how, a reference of DAO object can be done
            noteDao =  getHelper().getNotesDao();

            // Query the database. We need all the records so, used queryForAll()
            noteList = noteDao.queryForAll();

            // Set the header of the ListView
            final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.list_item, listview, false);
            listview.addHeaderView(rowView);

            //Now, link the  RecordArrayAdapter with the ListView
            listview.setAdapter(new RecordArrayAdapter(this, R.layout.list_item, noteList));

            // Attach OnItemLongClickListener and OnItemClickListener to track user action and perform accordingly
            listview.setOnItemLongClickListener((AdapterView.OnItemLongClickListener) this);
            listview.setOnItemClickListener((AdapterView.OnItemClickListener) this);

            // If, no record found in the database, appropriate message needs to be displayed.
            populateNoRecordMsg();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateNoRecordMsg()
    {
        // If, no record found in the database, appropriate message needs to be displayed.
        if(noteList.size() == 0)
        {
            final TextView tv = new TextView(this);
            tv.setPadding(5, 5, 5, 5);
            tv.setTextSize(15);
            tv.setText("No Record Found !!");
            listview.addFooterView(tv);
        }
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0)
        {
            selectedRecordPosition = position - 1;
            showDialogToUpdate();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,long id) {

        // If the pressed row is not a header, update selectedRecordPosition and show dialog for further selection
        if(position > 0)
        {
            selectedRecordPosition = position - 1;
            showDialogToRemove();
        }
        return true;
    }

    private void showDialogToRemove()
    {
        // Before deletion of the long pressed record, need to confirm with the user. So, build the AlartBox first
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the appropriate message into it.
        alertDialogBuilder.setMessage("Are you Really want to delete the selected record ?");

        // Add a positive button and it's action. In our case action would be deletion of the data
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            // This is how, data from the database can be deleted
                            noteDao.delete(noteList.get(selectedRecordPosition));

                            // Removing the same from the List to remove from display as well
                            noteList.remove(selectedRecordPosition);
                            listview.invalidateViews();

                            // Reset the value of selectedRecordPosition
                            selectedRecordPosition = -1;
                            populateNoRecordMsg();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        // Add a negative button and it's action. In our case, just hide the dialog box
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        // Now, create the Dialog and show it.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void showDialogToUpdate()
    {
        // Before deletion of the long pressed record, need to confirm with the user. So, build the AlartBox first
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the appropriate message into it.
        alertDialogBuilder.setMessage("Are you Really want to update the selected record ?");

        // Add a positive button and it's action. In our case action would be deletion of the data
        alertDialogBuilder.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            // This is how, data from the database can be deleted
                            Intent in = new Intent(ViewNoteRecordActivity.this,UpdateNoteActivity.class);
                            in.putExtra("id",noteList.get(selectedRecordPosition).id);
                            startActivity(in);

                            populateNoRecordMsg();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        // Add a negative button and it's action. In our case, just hide the dialog box
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        // Now, create the Dialog and show it.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
