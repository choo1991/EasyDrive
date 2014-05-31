/* Temporary testing class to attempt to send accelerometer data
 * back to the main thread via a service. Delete if doesn't work
 * later.
 * */

package com.example.drivingapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;

public class AccelerometerDataService extends Service implements SensorEventListener {

	public void onCreate() {
	    super.onCreate();
	//register your sensor manager listener here
	}

	@Override
	public void onDestroy() {
	//unregister your listener here
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
	    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	        //detect the shake and do your work here
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
