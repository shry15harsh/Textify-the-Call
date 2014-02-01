package com.example.xpoject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main activity, with button to toggle phone calls detection on and off.
 *
 */
public class callDetection_MainActivity extends Activity {

	private boolean detectEnabled;
	
	private TextView textViewDetectState;
	private Button buttonToggleDetect;
	private Button buttonExit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_detection_service);
        
        textViewDetectState = (TextView) findViewById(R.id.textViewDetectState);
        
        buttonToggleDetect = (Button) findViewById(R.id.buttonDetectToggle);
        buttonToggleDetect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDetectEnabled(!detectEnabled);
			}
		});
        
        buttonExit = (Button) findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDetectEnabled(false);
				callDetection_MainActivity.this.finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setDetectEnabled(boolean enable) {
    	detectEnabled = enable;
    	
        Intent intent = new Intent(this, CallDetectService.class);
    	if (enable) {
    		 // start detect service 
            startService(intent);
           
            buttonToggleDetect.setText("Turn off");
    		textViewDetectState.setText("Detecting");
    	}
    	else {
    		// stop detect service
    		stopService(intent);
    		
    		buttonToggleDetect.setText("Turn on");
    		textViewDetectState.setText("Not detecting");
    	}
    }

}
