package com.example.drivingapp;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//toggled in initLocationManager in this class when speed conditions met
	//and in LockScreenAppActivity when the user kills the lock screen
	public static boolean LOCK_SCREEN_ACTIVE = false;
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Display the fragment as the main content.
        android.app.FragmentManager FragmentManager = getFragmentManager();
        FragmentTransaction FragmentTransaction = FragmentManager.beginTransaction();
        PrefsFragment PrefsFragment = new PrefsFragment();
        FragmentTransaction.replace(android.R.id.content, PrefsFragment);
        FragmentTransaction.commit();

    }
   
    // This is to see if the setting is preserved across exiting out of app and
    // then coming back to it
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Dialog dialog = new Dialog(this);
        dialog.setTitle(retrieveSwitchPreference());
        dialog.show();
        dialog = null;
    }
    
    // currently a test to see if it can return the current value of the switch
    private String retrieveSwitchPreference() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    	// acquire the set boolean for the preference with the key 'button_app_enabled_key'
    	boolean switchBox = prefs.getBoolean("button_app_enabled_key", false);
    	return String.valueOf(switchBox);
}

    public static class PrefsFragment extends PreferenceFragment {
    	 
        @Override
        public void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	// Load the preferences from an XML resource
        	addPreferencesFromResource(R.xml.pref_main);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void startAccelerometer(View view) {
    	/*
    	 * onSensorChanged still fires even when activity is stopped,
    	 * so as a result both ways below will still trigger a
    	 * popup to appear when x > 5, however only the service causes
    	 * the textview to appear while the regular activity doesn't.
    	 */
    	
    	//Intent intentAccel = new Intent(this, SensorActivity.class);
    	//startActivity(intentAccel);
    	TextView test = (TextView) findViewById(R.id.tvSpeed);
		test.setText("0 MPH");
		
		initLocationManager();
		
		Intent service = new Intent(this, VolumeCheckService.class);
		startService(service);
		
		
    	//Intent readAccel = new Intent(this, LockScreenAppActivity.class);
    	//startActivity(readAccel);

    }
    
    public void openOptions(View view) {
    	Intent options = new Intent(this, OptionsActivity.class);
    	startActivity(options);
    }
    
    public void displaySpeed(View view)
    {
    	TextView test = (TextView) findViewById(R.id.tvSpeed);
		test.setText("0 MPH");
		initLocationManager();
    }
    
    public void displayLockScreen(View view)
    {
    	TextView test = (TextView) findViewById(R.id.tvSpeed);
		test.setText("Display Lock Screen Clicked!");
    	Intent displayLockScreen = new Intent(this, LockScreenAppActivity.class);
    	startActivity(displayLockScreen);
		//Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.package.lockscreen");
		//startActivity(LaunchIntent);
    }
    
    private void initLocationManager() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				makeUseOfNewLocation(location);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			public void onProviderEnabled(String provider) {

			}

			public void onProviderDisabled(String provider) {
				// alert something saying we can't do anything if they don't give us permissions?
			}
		};
		String locationProvider = LocationManager.GPS_PROVIDER;

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

		// gets last known location
		//saving in case we want it later
		//Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
	}

	private void makeUseOfNewLocation(Location location) {
		// TODO Auto-generated method stub
		System.out.println("Latitude = " + location.getLatitude());
		System.out.println("Longitude = " + location.getLongitude());
		System.out.println("Speed = " + location.getSpeed());
		// might be able to use time for determining speed
		System.out.println("Time = " + location.getTime());
		float speed = location.getSpeed();
		System.out.println("Speed = " + speed);
		String speedAsString = Float.toString(speed);

		TextView test = (TextView) findViewById(R.id.tvSpeed);
		test.setText(speedAsString);
		checkIfDriving(location);
	}
	
	private void checkIfDriving(Location location)
	{
		float speed = location.getSpeed();
		long time = location.getTime();
		// speed is returned in meters per second
		// 10 mph ~ 4.4 meters per second
		// this is 
		if (speed > OptionsActivity.SPEED_LIMIT && !LOCK_SCREEN_ACTIVE) {
			LOCK_SCREEN_ACTIVE = true;
			
			//
			if(OptionsActivity.RINGER_MODE_SILENCED) {
				//for phone volumes (notifications, alarms, calls, music, etc)
		    	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		    	am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		    	
		    	int vol = am.getStreamVolume(AudioManager.STREAM_RING);
		    	
		    	Context context = getApplicationContext();
		  		CharSequence text = "Volume is " + vol;
		  		int duration = Toast.LENGTH_SHORT;
		  		
		      	Toast toast = Toast.makeText(context, text, duration);
		  		toast.show(); 
			} 
			initCustomLockScreen(time); 
		}
	}
	
	// 
	private void initCustomLockScreen(long time) {
		// initialize custom lock screen here:
		
		// disable location manager since we don't want to continue
		// to check location after home screen is locked
		//disableLocationManager();
		
		//wasScreenOn=false;
    	Intent intent11 = new Intent(this, LockScreenAppActivity.class);
    	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    	
    	
    	startActivity(intent11);

	}
	
	// we need a method that will delay the lock screen for X minutes until it checks again
	// for the phone moving more than 10 miles an hour (or whatever speed we choose)
	// how can we say... in X hours, restart location manager?
	/*private void disableLocationManager() {
		// do we want to keep track of time here so the app knows how long to disable location checking
		// and knows when to start checking for speed again
		locationManager.removeUpdates(locationListener);

	}*/
	
	
}
