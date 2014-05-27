package com.example.drivingapp;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class VolumeCheckService extends IntentService {
	
	public Handler handler;
	public int vol;
	public boolean handlerFlag = false;
	
	public VolumeCheckService() {
		super("VolumeCheckService");
		// TODO Auto-generated constructor stub
		
	}

	public void onCreate() {
		handler = new Handler();
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		SilenceCheck sc = new SilenceCheck();
		
		//infinite loop
		while(true) {
			//continuously set the volume to silent when true
			if(MainActivity.TERMINATE_APP) {
				stopSelf();
			}
			//continuously set the volume to silent when true
			if(!handlerFlag) {
				handler.postDelayed(sc, 7000);
				handlerFlag = true;
			}
			/*
			if(OptionsActivity.RINGER_MODE_SILENCED) {
				//for phone volumes (notifications, alarms, calls, music, etc)
		    	
						    	
		    	//toast's new thread is bound to the main UI thread
				handler.post(new Runnable() { 
		            @Override 
		            public void run() {     
			            	Context context = getApplicationContext();
	
							AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
					    	am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					    	
					    	vol = am.getStreamVolume(AudioManager.STREAM_RING);

			            	CharSequence text = "Volume is " + vol;
					  		int duration = Toast.LENGTH_SHORT;
					  		
					      	Toast toast = Toast.makeText(context, text, duration);
					  		toast.show();           
		        		} 
				});   
				
			} 
			*/
		}
	}
	
	private class SilenceCheck implements Runnable {
		public void run() {  
			handlerFlag = false;
			if(MainActivity.RINGER_MODE_SILENCED) {
				//for phone volumes (notifications, alarms, calls, music, etc)
		    	
				
				
		    	//toast's new thread is bound to the main UI thread
				handler.post(new Runnable() { 
		            @Override 
		            public void run() {     
			            	Context context = getApplicationContext();
	
							AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
					    	am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					    	
					    	vol = am.getStreamVolume(AudioManager.STREAM_RING);

			            	CharSequence text = "Volume is " + vol;
					  		int duration = Toast.LENGTH_SHORT;
					  		
					      	Toast toast = Toast.makeText(context, text, duration);
					  		toast.show();           
		        		} 
				}); 
			}
		} 
	}
}
