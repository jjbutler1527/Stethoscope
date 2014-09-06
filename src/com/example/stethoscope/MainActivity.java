package com.example.stethoscope;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener, ContinueDialogFragment.ContinueDialogListener {
	
	ImageButton btnRecord;
	Button btnPrevious;
	
	private Boolean recording=false, playing=false, pluggedIn;
	
	private MediaPlayer mPlayer = null;
	
	private DatabaseHandler datasource;
	
	private List<SensorData> values;
	private static final String LOG_TAG = "AudioRecordTest";
	
	private static final String TAG = "MainActivity";
	private MusicIntentReceiver myReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.btnRecord = (ImageButton)findViewById(R.id.btnRecord);
		this.btnRecord.setOnClickListener(this);
		this.btnPrevious = (Button)findViewById(R.id.btnPrevious);
		this.btnPrevious.setOnClickListener(this);
		
		datasource = new DatabaseHandler(this);
		myReceiver = new MusicIntentReceiver();
	}
	
	@Override
	public void onClick(View v) {
		Intent intentServiceAudioCapture = new Intent(getApplicationContext(), ServiceAudioRecord.class);
		if(v.getId()==R.id.btnRecord){
			if(!recording){
				if(pluggedIn){
			        getApplicationContext().startService(intentServiceAudioCapture);
			        Toast.makeText(getApplicationContext(), "recording...", Toast.LENGTH_SHORT).show();
			        btnRecord.setImageResource(R.drawable.states2);
			        recording=true;
				}
				else{
					Toast.makeText(getApplicationContext(), "Stethoscope not connected!", Toast.LENGTH_SHORT).show();
				}

			}
			else{
				stopService(intentServiceAudioCapture);
				Toast.makeText(getApplicationContext(), "recording stopped", Toast.LENGTH_SHORT).show();
				btnRecord.setImageResource(R.drawable.states);
				recording=false;
				DialogFragment newFragment = new ContinueDialogFragment();
			    newFragment.show(getSupportFragmentManager(), "continue");
			}
		}
		if(v.getId()==R.id.btnPrevious){
			Intent intent = new Intent(this, PreviousRecordingsActivity.class);
			startActivity(intent);
		}
	}
	
	private class MusicIntentReceiver extends BroadcastReceiver {
	    @Override public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
	            int state = intent.getIntExtra("state", -1);
	            switch (state) {
	            case 0:
	                Log.d(TAG, "Headset is unplugged");
	                pluggedIn=false;
	                break;
	            case 1:
	                Log.d(TAG, "Headset is plugged");
	                pluggedIn=true;
	                break;
	            default:
	                Log.d(TAG, "I have no idea what the headset state is");
	                pluggedIn=false;
	            }
	        }
	    }
	}
	

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
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
	
	@Override public void onResume() {
	    IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
	    registerReceiver(myReceiver, filter);
	    super.onResume();
	}
	
	@Override public void onPause() {
	    unregisterReceiver(myReceiver);
	    super.onPause();
	}

}
