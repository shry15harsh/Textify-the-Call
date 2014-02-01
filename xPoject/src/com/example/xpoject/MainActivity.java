package com.example.xpoject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	// recorders
	AudioRecord recorder_up,recorder_down;
	// helpers
	int  recordnum_up = 0;
	int recordnum_down=0;
	int processedRecordNum=0;
	boolean isRecording_up = false,isRecording_down=false;
	String number;
	public static  Boolean callActive=false;
	
	// File Paths
	//String CURRENT_PATH_NAME_UP = "/mnt/sdcard/V"+recordnum_up+".amr";
	//String CURRENT_PATH_NAME_DOWN = "/mnt/sdcard/V"+recordnum_down+".amr";
	
	//ResponseStrings
	public static ArrayList<String> voiceTextResponses_UPLINK = new ArrayList<String>();
	public static ArrayList<String> voiceTextResponses_DOWNLINK = new ArrayList<String>();

	// FUNCTIONS
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
	    number = savedInstanceState.getString("NUMBER");
		}
		catch(Exception e)
		{
			displayToast("NUmber passing...This was an error !");
		}
		//Initailise Refrences to Views
		
		
		
		
		
		//Starting up the Recording Module
	    //-------------------------------------Change Context to ctx if force Close ISsues exist
	//	new callRecorders(recorder_up, this, "U").execute(1);
	//	new callRecorders(recorder_down, this, "D").execute(1);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// ----------------------------------- TOAST DISPLAYER	
	public void displayToast(String msg)
	{
		Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
	}
	
	/*
	// --------------------------------- STARTING RECORDING
	// __________________________________ FOR UPSTREAM
	public void startRecordingUp()
	{
		if (!isRecording_up) {
			
			recorder_up = new MediaRecorder();
			recordnum_up++;
			recorder_up.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);
			recorder_up.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			recorder_up.setAudioEncodingBitRate(16);
			recorder_up.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder_up.setOutputFile(CURRENT_PATH_NAME_UP);
			isRecording_up = true;
			try {
				recorder_up.prepare();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			recorder_up.start();
			displayToast("Starting Recording Up....");
			Log.d("F1", "Recording Started !");
		} else {
			displayToast("Already Recording....");
		}
	}
	//_______________________________________ FOR DOWNSTREAM
	public void startRecordingDown()
	{
           if (!isRecording_down) {
			
			recorder_down = new MediaRecorder();
			recordnum_down++;
			recorder_down.setAudioSource(MediaRecorder.AudioSource.VOICE_DOWNLINK);
			recorder_down.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			recorder_down.setAudioEncodingBitRate(16);
			recorder_down.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder_down.setOutputFile(CURRENT_PATH_NAME_DOWN);
			isRecording_down = true;
			try {
				recorder_down.prepare();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			recorder_down.start();
			displayToast("Starting Recording....");
			Log.d("F1", "Recording Started !");
		} else {
			displayToast("Already Recording....");
		}
		
	}
	
	
	// ----------------------------------- STOPPING RECORDING
	// ____________________________________ FOR UPSTREAM
	public void stopRecordingUp()
	{
		if (isRecording_up) {
			recorder_up.stop();
			recorder_up.reset();
			//recorder.release();
			isRecording_up = false;
			displayToast("Stopping Recording Up...");
			Log.d("F3", "Saving File");
		} else {
			displayToast("No Present Recording Avaliable To Stop");
		}
	}
	
	// ____________________________________ FOR DOWNSTREAM
	public void stopRecordingDown()
	{
		if (isRecording_down) {
			recorder_down.stop();
			recorder_down.reset();
			//recorder.release();
			isRecording_down = false;
			displayToast("Stopping Recording Down...");
			Log.d("F3", "Saving File");
		} else {
			displayToast("No Present Recording Avaliable To Stop");
		}
	}
	
	*/
	// ------------------------------------ POSTING THE RECORD 
	
	
	// x=> U for upstream requests
	// x=> D for downstream requests
	public void postAndGet(String path,String x)
	{
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new wavPost(x).execute(path);
        } else {
            displayToast("No Network Connection Available.Please Check Your Internet Settings.");
        }
	}
	
	public class callRecorders extends AsyncTask<Integer, Integer, Integer>{
		

		// VARIABLES
		private  final int RECORDER_BPP = 16;
		private  int RECORDER_SAMPLERATE = 8000; 
		private  int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
		private  int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
		
		private AudioRecord recorder;
		private Context c;
		private String Mode;
		public callRecorders(AudioRecord ar,Context ctx,String mode)
		{
			recorder=ar;
			c=ctx;
			Mode=mode;
		}
		
		
		public void startIntelligentRecording(AudioRecord audioRecorder,String Mode,Context c)
	{
		Toast.makeText(c,"Recording Starting...",Toast.LENGTH_SHORT).show();
		  Log.d("Initialising..","!!!!!!!!");
		    // Get the minimum buffer size required for the successful creation of an AudioRecord object. 
		    int bufferSizeInBytes = AudioRecord.getMinBufferSize( RECORDER_SAMPLERATE,
		                                                          RECORDER_CHANNELS,
		                                                          RECORDER_AUDIO_ENCODING
		                                                         ); 
		    
		    Log.d("Initialising Recorder","!!!!!!!!");
		    // Initialize Audio Recorder.
		    if(Mode=="U")
		    {
		    audioRecorder = new AudioRecord( MediaRecorder.AudioSource.VOICE_UPLINK,
		                                                 RECORDER_SAMPLERATE,
		                                                 RECORDER_CHANNELS,
		                                                 RECORDER_AUDIO_ENCODING,
		                                                 bufferSizeInBytes
		                                                );
		    }
		    else if(Mode=="D")
		    {
		    	  audioRecorder = new AudioRecord( MediaRecorder.AudioSource.VOICE_DOWNLINK,
                          RECORDER_SAMPLERATE,
                          RECORDER_CHANNELS,
                          RECORDER_AUDIO_ENCODING,
                          bufferSizeInBytes
                         );
		    }
		
		    Log.d("Starting Recording","!!!!!!!!");
		    // Start Recording.
		    audioRecorder.startRecording();

		    int numberOfReadBytes   = 0; 
		    byte audioBuffer[]      = new  byte[bufferSizeInBytes];
		    boolean recording       = false;
		    float tempFloatBuffer[] = new float[3];
		    int tempIndex           = 0;
		    int totalReadBytes      = 0;
		    byte totalByteBuffer[]  = new byte[60 * 44100 * 2];

		    Log.d("Getting Data From MIC","!!!!!!!!");
		    
		    // While data come from microphone. 
		    while( callActive )
		    {
		        float totalAbsValue = 0.0f;
		        short sample        = 0; 

		        numberOfReadBytes = audioRecorder.read( audioBuffer, 0, bufferSizeInBytes );

		        // Analyze Sound.
		        for( int i=0; i<bufferSizeInBytes; i+=2 ) 
		        {
		            sample = (short)( (audioBuffer[i]) | audioBuffer[i + 1] << 8 );
		            totalAbsValue += Math.abs( sample ) / (numberOfReadBytes/2);
		        }

		        // Analyze temp buffer.
		        tempFloatBuffer[tempIndex%3] = totalAbsValue;
		        float temp                   = 0.0f;
		        for( int i=0; i<3; ++i )
		            temp += tempFloatBuffer[i];

		        if( (temp >=0 && temp <= 350) && recording == false )
		        {
		            Log.i("TAG", "1");
		            tempIndex++;
		            continue;
		        }

		        if( temp > 350 && recording == false )
		        {
		            Log.i("TAG", "2");
		            recording = true;
		        }

		        if( (temp >= 0 && temp <= 350) && recording == true )
		        {
		            Log.i("TAG", "Save audio to file.");

		            // Save audio to file.
		            String filepath = Environment.getExternalStorageDirectory().getPath();
		            File file = new File(filepath,"AudioRecorder");
		            if( !file.exists() )
		                file.mkdirs();
		            
		            //FILE PATHS
		            String fn;
		            if(Mode=="U")
		            {
		               fn = file.getAbsolutePath() + "/" +recordnum_up+ ".wav";
		               recordnum_up++;
		            }
		            else
		            {
		            	fn = file.getAbsolutePath() + "/" +recordnum_down+ ".wav";
		            	recordnum_down++;
		            }

		            long totalAudioLen  = 0;
		            long totalDataLen   = totalAudioLen + 36;
		            long longSampleRate = RECORDER_SAMPLERATE;
		            int channels        = 1;
		            long byteRate       = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;
		            totalAudioLen       = totalReadBytes;
		            totalDataLen        = totalAudioLen + 36;
		            byte finalBuffer[]  = new byte[totalReadBytes + 44];

		            finalBuffer[0] = 'R';  // RIFF/WAVE header
		            finalBuffer[1] = 'I';
		            finalBuffer[2] = 'F';
		            finalBuffer[3] = 'F';
		            finalBuffer[4] = (byte) (totalDataLen & 0xff);
		            finalBuffer[5] = (byte) ((totalDataLen >> 8) & 0xff);
		            finalBuffer[6] = (byte) ((totalDataLen >> 16) & 0xff);
		            finalBuffer[7] = (byte) ((totalDataLen >> 24) & 0xff);
		            finalBuffer[8] = 'W';
		            finalBuffer[9] = 'A';
		            finalBuffer[10] = 'V';
		            finalBuffer[11] = 'E';
		            finalBuffer[12] = 'f';  // 'fmt ' chunk
		            finalBuffer[13] = 'm';
		            finalBuffer[14] = 't';
		            finalBuffer[15] = ' ';
		            finalBuffer[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		            finalBuffer[17] = 0;
		            finalBuffer[18] = 0;
		            finalBuffer[19] = 0;
		            finalBuffer[20] = 1;  // format = 1
		            finalBuffer[21] = 0;
		            finalBuffer[22] = (byte) channels;
		            finalBuffer[23] = 0;
		            finalBuffer[24] = (byte) (longSampleRate & 0xff);
		            finalBuffer[25] = (byte) ((longSampleRate >> 8) & 0xff);
		            finalBuffer[26] = (byte) ((longSampleRate >> 16) & 0xff);
		            finalBuffer[27] = (byte) ((longSampleRate >> 24) & 0xff);
		            finalBuffer[28] = (byte) (byteRate & 0xff);
		            finalBuffer[29] = (byte) ((byteRate >> 8) & 0xff);
		            finalBuffer[30] = (byte) ((byteRate >> 16) & 0xff);
		            finalBuffer[31] = (byte) ((byteRate >> 24) & 0xff);
		            finalBuffer[32] = (byte) (2 * 16 / 8);  // block align
		            finalBuffer[33] = 0;
		            finalBuffer[34] = RECORDER_BPP;  // bits per sample
		            finalBuffer[35] = 0;
		            finalBuffer[36] = 'd';
		            finalBuffer[37] = 'a';
		            finalBuffer[38] = 't';
		            finalBuffer[39] = 'a';
		            finalBuffer[40] = (byte) (totalAudioLen & 0xff);
		            finalBuffer[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		            finalBuffer[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		            finalBuffer[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		            for( int i=0; i<totalReadBytes; ++i )
		                finalBuffer[44+i] = totalByteBuffer[i];

		            FileOutputStream out;
		            try {
		                out = new FileOutputStream(fn);
		                 try {
		                        out.write(finalBuffer);
		                        out.close();
		                    } catch (IOException e) {
		                        // TODO Auto-generated catch block
		                        e.printStackTrace();
		                    }

		            } catch (FileNotFoundException e1) {
		                // TODO Auto-generated catch block
		                e1.printStackTrace();
		            }

		            //*/
		            tempIndex++;
		           // break;
		            //Voice Recorded now at path fn
		            //POsting the request !!
		            //postAndGet(fn, Mode);
		            
		        }

		        // -> Recording sound here.
		        Log.i( "TAG", "Recording Sound." );
		        for( int i=0; i<numberOfReadBytes; i++ )
		            totalByteBuffer[totalReadBytes + i] = audioBuffer[i];
		        totalReadBytes += numberOfReadBytes;
		        //*/

		        tempIndex++;

		    }
		    
		    audioRecorder.stop();
		    audioRecorder.release();
		    Toast.makeText(c,"Closing Recorder...",Toast.LENGTH_SHORT).show();
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		//Starting Recording
		try{
			startIntelligentRecording(recorder, Mode, c);
		}
		catch(Exception ex)
		{
			Log.d("In Recording Async TAsk Error","Error !!!!!!!!");
			ex.printStackTrace();
		}
		return null;
	}
	
	}
}
