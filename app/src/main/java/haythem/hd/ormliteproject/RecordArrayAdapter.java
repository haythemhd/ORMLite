package haythem.hd.ormliteproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

@SuppressWarnings("rawtypes")
public class RecordArrayAdapter extends ArrayAdapter<String> {

		private LayoutInflater inflater;
		
		// This would hold the database objects. It could be TeacherDetails or StudentDetails objects
		private List records;
		
		// Declaration of DAO to interact with corresponding table

		@SuppressWarnings("unchecked")
		public RecordArrayAdapter(Context context, int resource, List objects) {
			super(context, resource, objects);

			this.records = objects;

			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//Reuse the view to make the scroll effect smooth
			if(convertView == null)
				convertView = inflater.inflate(R.layout.list_item, parent, false);
			
			// If the ListView needs to display the records of StudentDetails objects
				final Note NoteDao = (Note) records.get(position);

				((TextView)convertView.findViewById(R.id.subject_tv)).setText(NoteDao.subject);
				((TextView)convertView.findViewById(R.id.text_tv)).setText(NoteDao.text);

			return convertView;
		}
	
}
