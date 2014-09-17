package com.example.stethoscope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.stethoscope.TestRecordActivity.PlayButton;
import com.example.stethoscope.TestRecordActivity.RecordButton;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PreviousRecordingsActivity extends ActionBarActivity {
	
	private DatabaseHandler datasource;
	List<SensorData> rawData;
	ArrayList<String> values, names;
	
	private static final String LOG_TAG = "AudioRecordTest";
	
	private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;
    
    private String tempName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previous_recordings);
		datasource = new DatabaseHandler(this);
		final ListView listview = (ListView) findViewById(R.id.listview);
		rawData =datasource.getAllContacts();
		values = new ArrayList<String>();
		names = new ArrayList<String>();
		for(int i=0;i<rawData.size();i++){
			values.add(rawData.get(i).getDateAndTime());
			names.add(rawData.get(i).getAudioFileName());
		}
		
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

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
	    	  mFileName = names.get(position);
	    	  startPlaying();
	      }

	    });
	  }
	
	private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
	
	public void stop() {
        if (mPlayer != null) {
        	mPlayer.release();
        	mPlayer = null;
        }
    }

    public void play(String path) {
    	MediaPlayer.create(this, Uri.parse(path)).start();
    }
	
	private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
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
	  
    @Override
	public void onPause() {
	    super.onPause();
	
	    if (mPlayer != null) {
	        mPlayer.release();
	        mPlayer = null;
	    }
	}
}
