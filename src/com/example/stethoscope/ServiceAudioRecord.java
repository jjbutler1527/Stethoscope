package com.example.stethoscope;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceAudioRecord extends Service{
	private static final int RECORDER_BPP = 16;
	private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
	private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
	private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
	private static final int RECORDER_SAMPLERATE = 8000;//44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	
	private AudioRecord recorder = null;
	private int bufferSize = 0;
	private Thread recordingThread = null;
	private boolean isRecording = false;
	String lat,lon,alt,bea,pr,sp,t;
	String defaultValue="novalue";
	String currentYMD_HMS;
	String afname;
	
	private long audioLengthSecond = 10;// 10 seconds
	private Handler rHandler = new Handler();
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);
        //getting the location info
		lat=(String) intent.getExtras().get("Lat");
		lon=(String) intent.getExtras().get("Lon");
		alt=(String) intent.getExtras().get("Alt");
		bea=(String) intent.getExtras().get("Bea");
		pr=(String) intent.getExtras().get("Pr");
		sp=(String) intent.getExtras().get("Sp");
		t=(String) intent.getExtras().get("T");// did not use it yet.
		//getting the current time
        currentYMD_HMS = ""+System.currentTimeMillis();
		
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);
		rHandler.removeCallbacks(recordAudio);
		rHandler.postDelayed(recordAudio,audioLengthSecond*1000);// register again to start after 10 seconds...
		startRecording();
		return super.onStartCommand(intent, flags, startId);
	}

	public Runnable recordAudio = new Runnable(){
		public void run(){
			stopRecording();
			//put all the information in the SQLite database
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			SensorData sd= new SensorData();
			sd.setIsManual("false");
			sd.setDateAndTime(currentYMD_HMS);
			sd.setGPSData(lat, lon, alt, pr, sp, bea);
			sd.setAudioFileName(afname);
			long lastRow = db.addContact(sd);
			db.close();
			Toast.makeText(getApplicationContext(), "Getting Row of last entry "+lastRow, Toast.LENGTH_LONG).show();
			Log.i("MainActivity", "Getting Row of last entry "+lastRow);
	        
			stopSelf();
		}
	};

	private String getFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		//String filepath = Environment.getDataDirectory().getPath();
		//Toast.makeText(getBaseContext(), filepath, Toast.LENGTH_LONG).show();
		Log.i("filepath", filepath);
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);
		
		if(!file.exists()){
			file.mkdirs();
		}

		return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
	}

	private String getTempFilename(){
		String filepath = Environment.getExternalStorageDirectory().getPath();
		//String filepath = Environment.getDataDirectory().getPath();
		File file = new File(filepath,AUDIO_RECORDER_FOLDER);

		if(!file.exists()){
			file.mkdirs();
		}

		File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);

		if(tempFile.exists())
			tempFile.delete();

		return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
	}

	private void startRecording(){
		Log.i("MainActivity", "StartAudioCapture");
		recorder.startRecording();

		isRecording = true;

		Toast.makeText(getBaseContext(), "Start AudioCapture", Toast.LENGTH_LONG).show();
		recordingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				writeAudioDataToFile();
			}
		},"AudioRecorder Thread");

		recordingThread.start();
	}

	private void writeAudioDataToFile(){
		byte data[] = new byte[bufferSize];
		String filename = getTempFilename();
		FileOutputStream os = null;

		try {
			os = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int read = 0;

		if(null != os){
			while(isRecording){
				read = recorder.read(data, 0, bufferSize);

				if(AudioRecord.ERROR_INVALID_OPERATION != read){
					try {
						os.write(data);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopRecording(){
		Log.i("MainActivity", "StopAudioCapture");
		Toast.makeText(getBaseContext(), "Stop AudioCapture", Toast.LENGTH_LONG).show();
		if(null != recorder){
			isRecording = false;

			recorder.stop();
			recorder.release();

			recorder = null;
			recordingThread = null;
		}
		afname= getFilename();
		copyWaveFile(getTempFilename(),afname);
		deleteTempFile();
	}

	private void deleteTempFile() {
		File file = new File(getTempFilename());

		file.delete();
	}

	private void copyWaveFile(String inFilename,String outFilename){
		FileInputStream in = null;
		FileOutputStream out = null;
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen + 36;
		long longSampleRate = RECORDER_SAMPLERATE;
		int channels = 2;
		long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

		byte[] data = new byte[bufferSize];

		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			totalAudioLen = in.getChannel().size();
			totalDataLen = totalAudioLen + 36;


			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
					longSampleRate, channels, byteRate);

			while(in.read(data) != -1){
				out.write(data);
			}

			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void WriteWaveFileHeader(
			FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels,
			long byteRate) throws IOException {

		byte[] header = new byte[44];

		header[0] = 'R';  // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f';  // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1;  // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8);  // block align
		header[33] = 0;
		header[34] = RECORDER_BPP;  // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		out.write(header, 0, 44);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}


