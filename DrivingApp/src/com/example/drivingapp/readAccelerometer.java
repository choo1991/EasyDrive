package com.example.drivingapp;

import java.util.List;

import com.example.drivingapp.SensorActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
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
	static Boolean flag = false;
    CheckTopActivity checkTopAct;
    
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
		
		//runnable tasks sent to handler
		checkTopAct = new CheckTopActivity();
		
		super.onCreate();
	}
	
	//the "main" for a service that isn't bound
	//it destroys itself after this method finishes executing
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
		
	  //feedback popup test
	  		Context context = getApplicationContext();
	  		CharSequence text = "Hello toast X!";
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  
	  		
	  		
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
		
		//check top activity
		//handler.postDelayed(checkTopAct, 500);
		
		ActivityManager manager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
		
		//sustain thy self
		while(true) {
			List<RunningTaskInfo> runningTasks = manager.getRunningTasks(20);
	        if (runningTasks != null && runningTasks.size() > 0) {
	            ComponentName topActivity = runningTasks.get(0).topActivity;
	            // Here you can get the TopActivity for every 500ms
	            if(!topActivity.getPackageName().equals(getPackageName())){
	            	//feedback popup test
	            	/*
	    			Context context = getApplicationContext();
	    			CharSequence text = getPackageName().toString(); //name of our app's package
	    			int duration = Toast.LENGTH_SHORT;
	    			
	    	    	Toast toast = Toast.makeText(context, text, duration);
	    			toast.show();
	    			*/
	            	
	    			for(RunningTaskInfo taskInfo : runningTasks) {
	    				if(taskInfo.topActivity.getPackageName().equals(getPackageName())) {
	    					
	    					/*runningTasks.set(0, taskInfo);
	    					
	    					Context context2 = getApplicationContext();
	    	    			CharSequence text2 = "XX".toString(); //name of our app's package
	    	    			int duration2 = Toast.LENGTH_SHORT;
	    	    			
	    	    	    	Toast toast2 = Toast.makeText(context2, text2, duration2);
	    	    			toast2.show();
	    	    			*/
	    					flag = true;
	    					startActivity(sensorIntent);
	    					break; //just to be safe
	    				}
	    			}
	    			
	            }
	        }
		}
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
    	
    	if(x > 5 && !flag) {
    		//multiple sensorIntents are started unless we control it somehow,
    		//such as with a flag or something
    		flag = true;
    		//sensorIntent.addCategory(Intent.CATEGORY_HOME);
    		startActivity(sensorIntent); //are we generating many instances?
    		//call in order to stop this service since we don't need it anymore
    		//the other activity will continue on as expected
    		//don't need
    		//stopSelf(); 
    		
    		//feedback popup test
			Context context = getApplicationContext();
			CharSequence text = "Hello toast toast!" + x;
			int duration = Toast.LENGTH_SHORT;
			
	    	Toast toast = Toast.makeText(context, text, duration);
			toast.show();   
		
    	}
    }

    //detects when the app this service belongs to isn't the one that is currently
    //active on the user's screen (index 0 of running tasks), and by active this doesn't
    //necessarily mean that it's visible on screen, just the last recent active app that
    //is still being persisted. Either way we want our app on top.
    //and this code actually works o_O
    //http://stackoverflow.com/questions/20740440/how-to-check-the-top-activity-from-android-app-in-background-service
    //http://stackoverflow.com/questions/4278535/disable-home-button-in-android-toddler-app
	private class CheckTopActivity implements Runnable {
	    @Override
	    public void run() {
	        ActivityManager manager = (ActivityManager) getApplicationContext()
	                .getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> runningTasks = manager.getRunningTasks(20);
	        if (runningTasks != null && runningTasks.size() > 0) {
	            ComponentName topActivity = runningTasks.get(0).topActivity;
	            // Here you can get the TopActivity for every 500ms
	            if(!topActivity.getPackageName().equals(getPackageName())){
	            	//feedback popup test
	    			Context context = getApplicationContext();
	    			CharSequence text = getPackageName().toString(); //name of our app's package
	    			int duration = Toast.LENGTH_SHORT;
	    			
	    	    	Toast toast = Toast.makeText(context, text, duration);
	    			toast.show();
	    			
	    			for(RunningTaskInfo taskInfo : runningTasks) {
	    				if(taskInfo.topActivity.getPackageName().equals(getPackageName())) {
	    					
	    					/*runningTasks.set(0, taskInfo);
	    					
	    					Context context2 = getApplicationContext();
	    	    			CharSequence text2 = "XX".toString(); //name of our app's package
	    	    			int duration2 = Toast.LENGTH_SHORT;
	    	    			
	    	    	    	Toast toast2 = Toast.makeText(context2, text2, duration2);
	    	    			toast2.show();
	    	    			*/
	    					flag = true;
	    					startActivity(sensorIntent);
	    					break; //just to be safe
	    				}
	    			}
	    			
	            }
	            handler.postDelayed(this, 5500); //repeat
	        }
	    }
	}
}
