package com.example.drivingapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SpeedAdjustActivity extends Activity implements OnSeekBarChangeListener {

	public static String SPEED;
	SeekBar bar;
	int speedValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speed_adjust);
		
		bar = (SeekBar)findViewById(R.id.speedBar); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.speed_adjust, menu);
		return true;
	}
	
    public void onProgressChanged(SeekBar seekBar, int progress,
    		boolean fromUser) {
    	// TODO Auto-generated method stub
    	
    	// change progress text label with current seekbar value
    	TextView view = (TextView)findViewById(R.id.speedDisplay);
    	view.setText("The value is: " + progress);
    	speedValue = progress;
    	// change action text label to changing
    	//textAction.setText("changing");
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    	// TODO Auto-generated method stub
    	//textAction.setText("starting to track touch");
    	
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	// TODO Auto-generated method stub
    	seekBar.setSecondaryProgress(seekBar.getProgress());
    	//textAction.setText("ended tracking touch");    	
    }
    
	public void setSpeed(View view) {
		Intent i = getIntent();
		i.putExtra(SPEED, speedValue);
		setResult(RESULT_OK, i);
		finish();
	}

}
