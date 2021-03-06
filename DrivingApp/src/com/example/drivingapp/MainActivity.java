package com.example.drivingapp;

import java.util.Calendar;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.drivingapp.GPSLocationService.GPSBinder;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//toggled in initLocationManager in this class when speed conditions met
	//and in LockScreenAppActivity when the user kills the lock screen
	public static boolean LOCK_SCREEN_ACTIVE = false;
	//if speed tracking/app enabled started
	public static boolean APP_ENABLED = false;
	//OptionsActivity has its own copy, but for this class we'll just use this...
	public static boolean RINGER_MODE_SILENCED = false;
	private static final String LOG_TAG = "GPSDebugging";
	
	public static boolean TERMINATE_APP = false;
	
	public static boolean ABOVE_SPEED_LIMIT = false;
	
	public static boolean INITIAL_CREATION = false;
	
	public static int POSTPONE_APP = 0;
	public static float deviceSpeed;
	
	private static final String TAG = "AccelerometerDataService";
	Intent intent;
	
	public static Map<String, String> CustomAppsList;
	static {
		Map<String, String> tempMap = new HashMap<String, String>();
		tempMap.put("com.pandora.android", "Pandora");
		tempMap.put("com.google.android.apps.maps", "Google Maps");
		tempMap.put("com.shazam.android", "Shazam");
		tempMap.put("com.android.phone", "Phone");
		tempMap.put("com.amazon.mp3", "Amazon MP3");
		tempMap.put("com.spotify.mobile.android.ui", "Spotify");
		tempMap.put("com.google.android.music", "Google Play Music");
		tempMap.put("com.clearchannel.iheartradio.controller", "iHeartRadio");
		tempMap.put("com.waze", "Waze");
		tempMap.put("com.yelp.android", "Yelp");
		tempMap.put("gbis.gbandroid", "GasBuddy");
		tempMap.put("com.soundcloud.android", "SoundCloud");
		CustomAppsList = Collections.unmodifiableMap(tempMap);
	}
	public static String[] CurrentApps;
	static {
		//change this to add the values stored in preferences
		CurrentApps = new String[3];
		CurrentApps[0] = "com.pandora.android";
		CurrentApps[1] = "com.google.android.apps.maps";
		CurrentApps[2] = "com.spotify.mobile.android.ui";

	}
	//moved up here from initCustomLockScreen so that the intent can be reordered
	//from other methods
	Intent intent11;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        android.app.FragmentManager FragmentManager = getFragmentManager();
        FragmentTransaction FragmentTransaction = FragmentManager.beginTransaction();
        PrefsFragment PrefsFragment = new PrefsFragment();
        PrefsFragment.setActivity(this);
        setTheme(android.R.style.Theme_Holo);
        FragmentTransaction.replace(android.R.id.content, PrefsFragment);
        FragmentTransaction.commit();   
        Log.v(LOG_TAG, "Main Activity Created");
//        Intent showAccelData = new Intent(this, readAccelerometer.class);
//    	this.startService(showAccelData);
        intent = new Intent(this, AccelerometerDataService.class);
        startService(intent);  
//        IntentFilter intentFilter = new IntentFilter();      
//        intentFilter.addAction(AccelerometerDataService.BROADCAST_ACTION);
//        registerReceiver(a, intentFilter);
    }
    
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showAccelData(intent);       
        }
    };
    
    private void showAccelData(Intent intent) {
        float x = intent.getFloatExtra("x", 0.0f); 
        float y = intent.getFloatExtra("y", 0.0f);
        float z = intent.getFloatExtra("z", 0.0f);
        Log.i("AccelService", "x: " + x + " y: " + y + " z: " + z);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
//        accelReceiver = new Receiver();
//        IntentFilter intentFilter = new IntentFilter();      
//        intentFilter.addAction(SimpleService.MY_ACTION);
//        startService(intent);  
//        registerReceiver(accelReceiver, intentFilter);
//        startService(intent);
//        registerReceiver(broadcastReceiver, new IntentFilter(AccelerometerDataService.BROADCAST_ACTION));
        
    }
    
    // currently a test to see if it can return the current value of the switch
    private String retrieveSwitchPreference() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    	// acquire the set boolean for the preference with the key 'button_app_enabled_key'
    	boolean switchBox = prefs.getBoolean("button_app_enabled_key", false);
    	return String.valueOf(switchBox);
    }

    public static class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    	 
    	MainActivity main;
		SharedPreferences prefs;

    	public void setActivity(MainActivity main) {
    		this.main = main;
    		prefs = PreferenceManager.getDefaultSharedPreferences(this.main.getBaseContext());
    		prefs.registerOnSharedPreferenceChangeListener(this);
    	}
    	
        @Override
        public void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	// Load the preferences from an XML resource
        	addPreferencesFromResource(R.xml.pref_main);
        	
        }
        
      //accepts toggling from switch preference objects
        public void onSharedPreferenceChanged(SharedPreferences preferenceScreen, String key) {
        	//String key = preference.getKey();
        	if(main == null) {
        		try {
					throw new Exception("NOOB SET TEH MAIN OBJECT!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(key.equals("button_app_enabled_key")) {
        		//only enable it once while app is active
        		if(main.retrieveSwitchPreference().equals("true") && !APP_ENABLED) {
        			main.enableApp();
        			APP_ENABLED = true;
        		}
        	} else if(key.equals("button_enable_silencing")) {
        		boolean silenced = prefs.getBoolean("button_enable_silencing", false);
        		main.switchSilenced(silenced);
        	} else if(key.equals("button_app_speed_display_key")) {
        		if (prefs.getBoolean("button_app_speed_display_key", false)) {
        			Calendar rightNow = Calendar.getInstance();
        			long rightNowMilli = rightNow.getTimeInMillis();
        			
            		DataStorageClass testAddition = new DataStorageClass(rightNowMilli, 0.1f, 0.2f, 0.3f, 0.4f);
            		Context tempContext = main;
            		DataORM.insertData(tempContext, testAddition);
        		} else {
            		List<DataStorageClass> dsc = DataORM.getData(main);
            		int length = dsc.size();
            		Toast toast = Toast.makeText(main, Integer.toString(length), Toast.LENGTH_SHORT);
            		toast.show();
        		}
        	} else if(key.equals("button_app_accel_display_key")) {
        		boolean accel = prefs.getBoolean("button_app_accel_display_key", false);
//        		Dialog dialog = new Dialog(main);
//                dialog.setTitle("TODO - Show current XYZ");
//        		dialog.setTitle(Boolean.toString(accel));
//                dialog.show();
//                dialog = null;
                Toast toast3 = Toast.makeText(main.getBaseContext(), "accel", Toast.LENGTH_SHORT);
        		toast3.show();
//        		InitializeChart chart = new InitializeChart();

        	} else if(key.equals("button_speed_change_key")) { 
          		//change speed limit that activates lock screen
            	String speed = prefs.getString("button_speed_change_key", Integer.toString(OptionsActivity.SPEED_LIMIT));
            	//int speed = preferenceScreen.getInt("button_speed_change_key", 0);
            	Log.v("LOG_TAG", "Before: " + Integer.toString(OptionsActivity.SPEED_LIMIT));
            	OptionsActivity.SPEED_LIMIT = Integer.parseInt(speed);
            	Log.v("LOG_TAG", "After: " + Integer.toString(OptionsActivity.SPEED_LIMIT));
           		
            	
        	} else if(key.equals("button_temp_lock_off")) {
        		String time = prefs.getString("button_temp_lock_off", "0");
        		POSTPONE_APP = Integer.parseInt(time);
        		Log.i("LOG_TAG", Integer.toString(POSTPONE_APP));
        	} 
        }
        
        public boolean onPreferenceChange(Preference preference, Object newVal) {
        	//change speed limit that activates lock screen
        	//String speed = prefs.getString("button_speed_change_key", Integer.toString(OptionsActivity.SPEED_LIMIT));
        	Log.v("LOG_TAG", "Before X: " + Integer.toString(OptionsActivity.SPEED_LIMIT));
        	OptionsActivity.SPEED_LIMIT = Integer.parseInt((String)newVal);
        	Log.v("LOG_TAG", "After X: " + Integer.toString(OptionsActivity.SPEED_LIMIT));
        		
        	return false;
    	}
        
        //accepts clicks from preference screen objects
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        	String key = preference.getKey();
        	if(main == null) {
        		try {
					throw new Exception("NOOB SET TEH MAIN OBJECT!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(key.equals("button_change_applications")) {
        		main.optionSetApps();
        		
        	} else if(key.equals("button_about_app")) {
        		//Intent options = new Intent(getActivity(), OptionsActivity.class);
            	//startActivity(options);
        		main.openOptions();
        		
        		//change to about later!
        		
        	} else if(key.equals("button_help_app")) {
        		/*
        		ActivityManager am = (ActivityManager) main.getSystemService(ACTIVITY_SERVICE);
        		// get the info from the currently running task
        	    List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 

        	    Log.d("topActivity", "CURRENT Activity ::"
        	             + taskInfo.get(0).topActivity.getClassName());

        	    ComponentName componentInfo = taskInfo.get(0).topActivity;
        	    String pk = componentInfo.getPackageName();
        		 
        	    
        		Dialog dialog = new Dialog(main);
                dialog.setTitle(pk);
                dialog.show();
                dialog = null;
                */
        		main.optionHelp();
        		
        	} else if(key.equals("button_launch_lockscreen")) {
        		main.displayLockScreen();
        	} else if(key.equals("button_show_chart")) {
        		// take the user to the chart view and display stuff
        		Log.i("LATEST_DEBUGGING", "Show chart button clicked");
        		main.displayStats();
        	}
        	else if(key.equals("button_terminate_app")) {
        		TERMINATE_APP = true;
        		//need to end services too
        		main.finish();
        	} 
        	
        	return super.onPreferenceTreeClick(preferenceScreen, preference);
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
//    	TextView test = (TextView) findViewById(R.id.tvSpeed);
//		test.setText("0 MPH");
		
		initLocationManager();
		
		Intent service = new Intent(this, VolumeCheckService.class);
		startService(service);
//    	Intent readAccel = new Intent(this, LockScreenAppActivity.class);
//    	startActivity(readAccel);

    }
    
    public void switchSilenced(boolean on) {
    	//for phone volumes (notifications, alarms, calls, music, etc)
    	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    	if (on) {
			Context context = getBaseContext();
			CharSequence text = "The phone will be silenced";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();  	
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			RINGER_MODE_SILENCED = true;
        } else {
        	Context context = getBaseContext();
	  		CharSequence text = "The phone will not be silenced";
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  

	  		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	  		RINGER_MODE_SILENCED = false;
        }       
    }
    
    public void displayStats() {
//    	Log.i("LATEST_DEBUGGING", "Entered display stats method");
    	Intent i = new Intent(this, XYChartBuilder.class);
//    	Log.i("LATEST_DEBUGGING", "Created new intent to go to the class");
    	startActivity(i);
    }
    
    public void enableApp() {
		initLocationManager();
		Intent service = new Intent(this, VolumeCheckService.class);
		startService(service);
    }
  
    public void openOptions(View view) {
    	Intent options = new Intent(this, OptionsActivity.class);
    	startActivity(options);
    }
    
    public void openOptions() {
    	Intent options = new Intent(this, OptionsActivity.class);
    	startActivity(options);
    }
    
    public void optionAbout() {
    	Intent i = new Intent(this, AboutActivity.class);
    	startActivity(i);
    }
    
    public void optionHelp() {
    	Intent i = new Intent(this, HelpActivity.class);
    	startActivity(i);
    }
    
    public void optionStats() {
    	Intent i = new Intent(this, StatsActivity.class);
    	startActivity(i);
    }
    
    public void optionSetApps() {
    	Intent i = new Intent(this, SetAppsActivity.class);
    	startActivity(i);
    }
    
    public void displaySpeed(View view) {
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
    
    
    public void displayLockScreen()
    {
    	Intent displayLockScreen = new Intent(this, LockScreenAppActivity.class);
    	startActivity(displayLockScreen);
		//Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.package.lockscreen");
		//startActivity(LaunchIntent);
    }
    
    public void speedAdjust() {
    	Intent i = new Intent(this, SpeedAdjustActivity.class);
    	startActivityForResult(i, 1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras().containsKey(SpeedAdjustActivity.SPEED)){
            //width.setText(data.getStringExtra("widthInfo"));
        	Context context = getBaseContext();
        	OptionsActivity.SPEED_LIMIT = data.getIntExtra(SpeedAdjustActivity.SPEED, 0);
	  		CharSequence text = "Speed Setting Is " + OptionsActivity.SPEED_LIMIT;
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  
        }
    }
    
    // called everytime something is changed -- location manager starts for GPS/MPH
    public void initLocationManager() {
    	Toast display = Toast.makeText(this, "Location Manager Initiated", Toast.LENGTH_SHORT);
		display.show();
		display = null;
		
		Log.v("LOG_TAG", "initLocationManager");
        
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
		
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				Log.v("LOG_TAG", "pre-onLocationChanged");

				// Called when a new location is found by the network location provider.
				makeUseOfNewLocation(location);
				Log.v("LOG_TAG", "onLocationChanged");
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
		Log.v(LOG_TAG, ("Latitude = " + location.getLatitude()));
		Log.v(LOG_TAG, ("Longitude = " + location.getLongitude()));
		Log.v(LOG_TAG, ("Speed = " + location.getSpeed()));
	
//		System.out.println("Latitude = " + location.getLatitude());
//		System.out.println("Longitude = " + location.getLongitude());
//		System.out.println("Speed = " + location.getSpeed());
		
		// might be able to use time for determining speed
		Log.v(LOG_TAG, ("Time = " + location.getTime()));
//		System.out.println("Time = " + location.getTime());
		float speed = location.getSpeed();
		System.out.println("Speed = " + speed);
		String speedAsString = Float.toString(speed);

		/*
		TextView test = (TextView) findViewById(R.id.tvSpeed);
		test.setText(speedAsString);
		*/
		checkIfDriving(location);
	}
	
	private void checkIfDriving(Location location) {
		float speed = location.getSpeed();
		
		// presumably, this method is being called repeatedly as long as the
		// app is active. Therefore, if there's a static variable, set it
		// as the speed.
		deviceSpeed = speed;
		
		long time = location.getTime();
		// speed is returned in meters per second
		// 10 mph ~ 4.4 meters per second
		// this is 
		
		ActivityManager aManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		// get the info from the currently running task
	    List< ActivityManager.RunningTaskInfo > taskInfo = aManager.getRunningTasks(1); 

	    Log.v("topActivity", "CURRENT Activity ::"
	             + taskInfo.get(0).topActivity.getClassName());

	    ComponentName componentInfo = taskInfo.get(0).topActivity;
	    String pk = componentInfo.getPackageName();
		 
	    /*
		Dialog dialog = new Dialog(main);
        dialog.setTitle(pk);
        dialog.show();
        dialog = null;*/
	    
	    Log.v(LOG_TAG, ("LOCK_SCREEN_ACTIVE = " + LOCK_SCREEN_ACTIVE));
	    Log.v(LOG_TAG, ("RUNNING_TASK = " + LockScreenAppActivity.RUNNING_TASK));
	    //only activate lock screen if the user is over the speed limit and
	    //it isn't already active and the currently running task isn't
	    //either this app itself or one of the selected apps present from the lock screen
		
	    /*
	    ///condition works, just needs to reordered
	    if(!pk.equals(LockScreenAppActivity.RUNNING_TASK) &&
				!pk.equals("com.example.drivingapp")) {
	    	intent11
	    	Log.d(LOG_TAG, ("REORDER?"));
	    	//startActivity(intent11);
	    }
	    */
	    /*
	    if (speed >= OptionsActivity.SPEED_LIMIT && !LOCK_SCREEN_ACTIVE
	    		&& !pk.equals(LockScreenAppActivity.RUNNING_TASK) &&
				!pk.equals("com.example.drivingapp")) {
			*/	
	    if(speed >= OptionsActivity.SPEED_LIMIT) {
	    	ABOVE_SPEED_LIMIT = true;
	    } else {
	    	ABOVE_SPEED_LIMIT = false;
	    }
	    
	    if (ABOVE_SPEED_LIMIT && !LOCK_SCREEN_ACTIVE) {
	    	
			LOCK_SCREEN_ACTIVE = true;
			Log.v(LOG_TAG, ("SCREEN CREATED"));
			//
			if(OptionsActivity.RINGER_MODE_SILENCED) {
				//for phone volumes (notifications, alarms, calls, music, etc)
		    	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		    	am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		    	
		    	int vol = am.getStreamVolume(AudioManager.STREAM_RING);
		    	
		    	Context context = getBaseContext();
		  		CharSequence text = "Volume is " + vol;
		  		int duration = Toast.LENGTH_SHORT;
		  		
		      	Toast toast = Toast.makeText(context, text, duration);
		  		toast.show();
			}
			if(!INITIAL_CREATION) {
				initCustomLockScreen(time);
				INITIAL_CREATION = true;
			}
		}
	}
	
	// 
	private void initCustomLockScreen(long time) {
		// initialize custom lock screen here:
		
		// disable location manager since we don't want to continue
		// to check location after home screen is locked
		//disableLocationManager();
		
		//wasScreenOn=false;
    	intent11 = new Intent(this, LockScreenAppActivity.class);
    	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	//intent11.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
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
