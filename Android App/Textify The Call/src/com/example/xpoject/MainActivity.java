package com.example.xpoject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// helpers
	
	static int recordnum = 0;
	boolean isRecording_up = false, isRecording_down = false;
	String number;
	public static Boolean callActive = false;
	static String address;
	//ArrayList<String> processingQueue = new ArrayList<String>();
	public static  TextView conversation;
	// File Paths
	// String CURRENT_PATH_NAME_UP = "/mnt/sdcard/V"+recordnum_up+".amr";
	// String CURRENT_PATH_NAME_DOWN = "/mnt/sdcard/V"+recordnum_down+".amr";

	// ResponseStrings
	public static ArrayList<String> voiceTextResponses_UPLINK = new ArrayList<String>();
	public static ArrayList<String> voiceTextResponses_DOWNLINK = new ArrayList<String>();

	// FUNCTIONS

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Getting the Number
		Bundle x = this.getIntent().getExtras();
		if(x!=null)
		{
		number = x.getString("NUMBER");
		address = x.getString("ADDRESS");
		}
		
		conversation = (TextView)findViewById(R.id.conversation);
		conversation.setText("Receiving  : "+"\n"+"\n");

		displayToast("Number : " + number);

		//address = "169.254.3.150";
		displayToast("Server Address : " + address);
		
		new callRecorders("U").execute();
		// new callRecorders("D").execute(); :C :C :C :c :c:c

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.Email:
			Toast.makeText(getApplicationContext(), "Enter destination email",
					Toast.LENGTH_LONG).show();
			Intent mail=new Intent(MainActivity.this, email.class);
			mail.putExtra("Data", conversation.getText().toString());
			startActivity(mail);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// ----------------------------------- TOAST DISPLAYER
	public void displayToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}


	public void postAndGet(String path, String mode) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			
			new wavPost(mode,MainActivity.this).execute(path, address);
			
		} else {
			displayToast("No Network Connection Available.Please Check Your Internet Settings.");
		}
	}
	
	public static void updateConversation(String string)
	{
		
		Log.d("Convrersation :"+string,"!!!!!!!");
		conversation.append(string);
	}
	
	public class callRecorders extends AsyncTask<Void, Void, Void> {

		MediaRecorder audioRecorder;
		String Mode;

		public callRecorders(String mode) {
			Mode = mode;
			Log.d("Object Initialising..", "!!!!!!!!");
		}
	
		
		
		public void startIntelligentRecording() {

			Log.d("In startIntelligentMethod", "!!!!!!!!!");

			String filepath = Environment.getExternalStorageDirectory()
					.getPath();
			File file = new File(filepath, "AudioRecorder");
			if (!file.exists())
				file.mkdirs();
			String fn = null;

			Log.d("Initailising AudioRecorder", "!!!!!!!!!");
			
			audioRecorder = new MediaRecorder();
			
			while (MainActivity.callActive == true) {

					audioRecorder
							.setAudioSource(MediaRecorder.AudioSource.MIC);
				

				audioRecorder
						.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
				audioRecorder.setAudioEncodingBitRate(16);
				audioRecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

				Log.d("Setting Path Directories", "!!!!!!!!!");
				if (Mode == "U") {
					fn = file.getAbsolutePath() + "//" + Mode
							+ MainActivity.recordnum + ".amr";
					MainActivity.recordnum++;
				} else if (Mode == "D") {
					fn = file.getAbsolutePath() + "//" + Mode
							+ MainActivity.recordnum + ".amr";
					MainActivity.recordnum++;
				}
				audioRecorder.setOutputFile(fn);

				Log.d("Preparing AudioRecorder", "!!!!!!!!!");
				try {
					audioRecorder.prepare();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.d("Starting Recording", "!!!!!!!!!");
				
				audioRecorder.start();

				try{
				Thread.sleep(8000);
				}
				 catch(Exception  e)
				 {
					 e.printStackTrace();
				 }
;				
				Log.d("Waiting For Record to be over", "!!!!!!!!!");
				

				audioRecorder.stop();
				audioRecorder.reset();
				
				Log.d("Record MAde", fn);

				MainActivity.voiceTextResponses_UPLINK.add(fn);
				postAndGet(fn, "U");
				
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Log.d("In Async Task !", "Starting REcording IN Async Task");
				startIntelligentRecording();
			} catch (Exception ex) {

				Log.d("In Recording Async Task Error", "Error !!!!!!!!");
				ex.printStackTrace();
			}
			return null;
		}

	}
}
