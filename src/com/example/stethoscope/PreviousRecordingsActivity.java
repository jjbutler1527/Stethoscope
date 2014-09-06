package com.example.stethoscope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PreviousRecordingsActivity extends ActionBarActivity {
	
	private DatabaseHandler datasource;
	List<SensorData> rawData;
	ArrayList<String> values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previous_recordings);
		datasource = new DatabaseHandler(this);
		final ListView listview = (ListView) findViewById(R.id.listview);
		rawData =datasource.getAllContacts();
		values = new ArrayList<String>();
		for(int i=0;i<rawData.size();i++){
			values.add(rawData.get(i).getDateAndTime());
		}

	    final ArrayList<String> list = new ArrayList<String>();
	    for (int i = 0; i < values.size(); ++i) {
	      list.add(values.get(i));
	    }
	    final StableArrayAdapter adapter = new StableArrayAdapter(this,
	        android.R.layout.simple_list_item_1, list);
	    listview.setAdapter(adapter);

	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	      @Override
	      public void onItemClick(AdapterView<?> parent, final View view,
	          int position, long id) {
	    	  
	      }

	    });
	  }

	  private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	  }
}
