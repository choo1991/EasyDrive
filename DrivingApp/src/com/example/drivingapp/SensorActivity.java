package com.example.drivingapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;
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
    	
		// Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(8);
	    textView.setText(data);

	    // Set the text view as the activity layout
	    setContentView(textView);
    }
	
	  @Override
	protected void onResume() {
	    super.onResume();
	    //different types of sampling rates
	    mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//unregister to save energy and battery, just leaving this here for now
	    //mSensorManager.unregisterListener(this);
	}
}
