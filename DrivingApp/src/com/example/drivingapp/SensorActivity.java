package com.example.drivingapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.*;

public class SensorActivity extends Activity implements SensorEventListener {
	  private SensorManager mSensorManager;
	  private Sensor mAccel;
	  String data = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		
		//get service
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    //could be multiple accelerometers on phone or none of that type at all, so check and get default
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		
		 //activities don't destroy themselves, and currently this doesn't!
		
		//comment out since we don't need it. readAccel should be the only one listening
	    mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
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
    	data += "x: " + x + " y: " + y + " z: " + z + "\n\n";

    	if(x > 5)
    	{
    		/*
	    	//feedback popup test
			Context context = getApplicationContext();
			CharSequence text = "Hello toast!";
			int duration = Toast.LENGTH_SHORT;
			
	    	Toast toast = Toast.makeText(context, text, duration);
			toast.show();   
			
			// Create the text view
		    TextView textView = new TextView(this);
		    textView.setTextSize(20);
		    textView.setText(data);

		    // Set the text view as the activity layout
		    setContentView(textView);
		    */
    		/*
    		KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
    		km.newKeyguardLock(tag)*/
    		
    		/*
    		 * WORKAROUND ID: 111
    		 * <category android:name="android.intent.category.DEFAULT" />
				<category android:name="android.intent.category.HOME" />
				
				Adding these to manifest would be an option, but it could potential 
				override user's default home setting
    		 * */
    		 
    	}
    	/*
		// Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(8);
	    textView.setText(data);

	    // Set the text view as the activity layout
	    setContentView(textView);
	    */
    	/*
    	if(y > 5) {
				this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
				super.onAttachedToWindow();
		} else if (y < -5) {
				this.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
				super.onAttachedToWindow();
		}
		*/
    }
	/*
    public void onAttachedToWindow() {
    
			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
			super.onAttachedToWindow();

    }*/
	  @Override
	protected void onResume() {
	    super.onResume();
	   
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//unregister to save energy and battery, just leaving this here for now
	    //mSensorManager.unregisterListener(this);
	}
	
	protected void onStop() {
		
		super.onStop();
		//while(true);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) { //doesn't :(
            Toast.makeText(this, "You pressed the home button!", Toast.LENGTH_LONG).show();                     
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK)) { //works!!!! 
            Toast.makeText(this, "You pressed the back button!", Toast.LENGTH_LONG).show();                     
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	/*
	 //Breaks the app
	@Override
	public void onAttachedToWindow()
	{  
	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);     
	    super.onAttachedToWindow();  
	}
	*/
}
