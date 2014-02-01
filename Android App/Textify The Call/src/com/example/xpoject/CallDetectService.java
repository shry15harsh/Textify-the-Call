package com.example.xpoject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Call detect service. 
 * This service is needed, because MainActivity can lost it's focus,
 * and calls will not be detected.
 * 
 *
 */
 
public class CallDetectService extends Service {
	
	private CallHelper callHelper;
	
 
    public CallDetectService() {
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		callHelper = new CallHelper(this);
		
		int res = super.onStartCommand(intent, flags, startId);
		
		callHelper.start();
		
		
		return res;
	}
	
	// Starting the Application Interface
	public void startMyApplication()
	{
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
	}
	
    @Override
	public void onDestroy() {
		super.onDestroy();
		
		callHelper.stop();
	}

	@Override
    public IBinder onBind(Intent intent) {
		// not supporting binding
    	return null;
    }
}
