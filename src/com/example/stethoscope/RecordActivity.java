package com.example.stethoscope;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.stethoscope.R;

public class RecordActivity extends ActionBarActivity implements OnClickListener {
	
	private Button btnStart, btnPlay;
	private Boolean recording=false, playing=false;
	
	private MediaPlayer mPlayer = null;
	
	private DatabaseHandler datasource;
	
	private List<SensorData> values;
	private static final String LOG_TAG = "AudioRecordTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		this.btnStart = (Button)findViewById(R.id.btnStart);
		this.btnStart.setOnClickListener(this);
		
		this.btnPlay = (Button)findViewById(R.id.btnPlay);
		this.btnPlay.setOnClickListener(this);
		
		datasource = new DatabaseHandler(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intentServiceAudioCapture = new Intent(getApplicationContext(), ServiceAudioRecord.class);
		if(v.getId()==R.id.btnStart){
			if(!recording){
		        getApplicationContext().startService(intentServiceAudioCapture);
		        btnStart.setText("stop recording");
		        recording=true;
			}
			else{
				stopService(intentServiceAudioCapture);
				btnStart.setText("start recording");
				recording=false;
			}
		}
		if(v.getId()==R.id.btnPlay){
			if(!playing){
				startPlaying();
				btnPlay.setText("stop playback");
				playing=true;
			}
			else{
				btnPlay.setText("start playback");
				playing=false;
			}
		}
	}
	
	private void startPlaying() {
		values=datasource.getAllContacts();
        mPlayer = new MediaPlayer();
        String filePath = Environment.getExternalStorageDirectory().getPath();
        String name = values.get(values.size()-1).getAudioFileName();
        //String name2 = name.substring(name.length()-13);
        String mFileName = "sdcard/AudioRecorder/" + name;
        Integer test = values.get(5).getID();
        Toast.makeText(getApplicationContext(), mFileName, Toast.LENGTH_SHORT).show();
        try {
            mPlayer.setDataSource(name);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

}
