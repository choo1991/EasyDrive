package com.example.drivingapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Hello World");
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
    	
    	Intent readAccel = new Intent(this, readAccelerometer.class);
    	startService(readAccel);

    }
}
