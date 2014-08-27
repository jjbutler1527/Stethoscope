package com.example.stethoscope;

import java.util.List;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	
	ImageButton btnRecord;
	
	private Boolean recording=false, playing=false;
	
	private MediaPlayer mPlayer = null;
	
	private DatabaseHandler datasource;
	
	private List<SensorData> values;
	private static final String LOG_TAG = "AudioRecordTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.btnRecord = (ImageButton)findViewById(R.id.btnRecord);
		this.btnRecord.setOnClickListener(this);
		
		datasource = new DatabaseHandler(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intentServiceAudioCapture = new Intent(getApplicationContext(), ServiceAudioRecord.class);
		if(v.getId()==R.id.btnRecord){
			if(!recording){
		        getApplicationContext().startService(intentServiceAudioCapture);
		        Toast.makeText(getApplicationContext(), "recording...", Toast.LENGTH_SHORT).show();
		        btnRecord.setImageResource(R.drawable.states2);
		        recording=true;
			}
			else{
				stopService(intentServiceAudioCapture);
				Toast.makeText(getApplicationContext(), "recording stopped", Toast.LENGTH_SHORT).show();
				btnRecord.setImageResource(R.drawable.states);
				recording=false;
			}
		}
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
}
