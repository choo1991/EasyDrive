package com.example.drivingapp;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class AccelerometerDataService extends Service implements SensorEventListener {

	SensorManager mSensorManager;
	Sensor mAccel;
	private static final String TAG = "AccelerometerDataService";
    public static final String BROADCAST_ACTION = "com.example.MainActivity.acceldata";
    Intent intent;
    Intent sensorIntent;
    private List<Sensor> sensorList;
	
	public void onCreate() {
	    intent = new Intent(BROADCAST_ACTION); 
		//get service
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		for (Sensor sensor : sensorList) {
			mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		super.onCreate();
	}
	
	protected void onHandleIntent() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    //could be multiple accelerometers on phone or none of that type at all, so check and get default
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		//sensor listeners are repeatedly fired in the background regardless
		//of whether or not the activity is active
	    mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);  
	}

	@Override
	public void onStart(Intent intent, int startId) {
		
	}
	
	@Override
	public void onDestroy() {
		//unregister your listener here
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
//		Log.i("AccelService", "AccelerometerDataService --> onSensorChanged");
	    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	    	// The accelerometer sensor returns a single value.
			// Many sensors return 3 values, one for each axis (x,y, z in order) 
	    	float x = event.values[0];
	    	float y = event.values[1];
	    	float z = event.values[2];
	    	
	    	intent.putExtra("x", x);
	    	intent.putExtra("y", y);
	    	intent.putExtra("z", z);
	    	sendBroadcast(intent);
	    }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
