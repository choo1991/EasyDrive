package com.example.drivingapp;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class readAccelerometer extends IntentService implements SensorEventListener {
	
	private SensorManager mSensorManager;
	private Sensor mAccel;
    String data = "";
	private Handler handler;
	Intent sensorIntent;
	
	public readAccelerometer() {
		super("readAccelerometer");
	}

	public void onCreate() {
		//onCreate is still run in the main UI thread before this service
		//branches off into its own thread, so we have to create a handler in here
		//so that the methods that are posted to it are run in a thread that's
		//bound to the main UI thread instead of this service's new thread
		handler = new Handler();
		
		sensorIntent = new Intent(this, SensorActivity.class);
		//this flag is required when a new activity is started outside of an activity,
		//such as a service like this
		sensorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		super.onCreate();
	}
	
	protected void onHandleIntent(Intent intent) {
		//get sensor service
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    //could be multiple accelerometers on phone or none of that type at all, so check and get default
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		//sensor listeners are repeatedly fired in the background regardless
		//of whether or not the activity is active
	    mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);  
		
	    //toast's new thread is bound to the main UI thread
		handler.post(new Runnable() { 
            @Override 
            public void run() {     
	            	//feedback popup test
	        		Context context = getApplicationContext();
	        		CharSequence text = "Hello toast!";
	        		int duration = Toast.LENGTH_SHORT;
	        		
	            	Toast toast = Toast.makeText(context, text, duration);
	        		toast.show();            
        		} 
		});   
		
		//sustain thy self
		while(true) {}
	}
	
	@Override
  	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	}
	
    @Override
    public final void onSensorChanged(SensorEvent event) {
	    // The accelerometer sensor returns a single value.
		// Many sensors return 3 values, one for each axis (x,y, z in order) 
    	float x = event.values[0];
    	float y = event.values[1];
    	float z = event.values[2];
    	//data += "x: " + x + " y: " + y + " z: " + z + "\n\n";
    	
    	if(x > 5) {
    		startActivity(sensorIntent);
    		//call in order to stop this service since we don't need it anymore
    		//the other activity will continue on as expected
    		stopSelf(); 
    	}
    }
	
	
}
