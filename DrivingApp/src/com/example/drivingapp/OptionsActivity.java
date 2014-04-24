package com.example.drivingapp;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class OptionsActivity extends Activity {
	
	//set 10 as the default speed limit when the lock screen won't be active
	public static int SPEED_LIMIT = 5;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		TableLayout layout = (TableLayout)findViewById(R.id.optionscreen_layout);
		layout.setBackgroundColor(Color.BLUE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}
    
    public void speedAdjust(View view) {
    	Intent i = new Intent(this, SpeedAdjustActivity.class);
    	startActivityForResult(i, 1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras().containsKey(SpeedAdjustActivity.SPEED)){
            //width.setText(data.getStringExtra("widthInfo"));
        	Context context = getApplicationContext();
        	SPEED_LIMIT = data.getIntExtra(SpeedAdjustActivity.SPEED, 0);
	  		CharSequence text = "Speed Setting Is " + SPEED_LIMIT;
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  
        }
    }
    
    public void switchSilenced(View view) {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();
        
       //for phone volumes (notifications, alarms, calls, music, etc)
    	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    	
        if (on) {
        	Context context = getApplicationContext();
	  		CharSequence text = "ON";
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  
	  		 		
	  		am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
        	Context context = getApplicationContext();
	  		CharSequence text = "OFF";
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  

	  		am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        
    }
    public void optionAbout(View view) {
    	Intent i = new Intent(this, AboutActivity.class);
    	startActivity(i);
    }
    
    public void optionHelp(View view) {
    	Intent i = new Intent(this, HelpActivity.class);
    	startActivity(i);
    }
    
    public void optionStats(View view) {
    	Intent i = new Intent(this, StatsActivity.class);
    	startActivity(i);
    }
    
    public void optionSetApps(View view) {
    	Intent i = new Intent(this, SetAppsActivity.class);
    	startActivity(i);
    }
  
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//apparently intercepting either keycode volume button prevents
    	//the user from changing the volume up or down
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
        	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        	int vol = am.getStreamVolume(AudioManager.STREAM_RING);
        	
        	Context context = getApplicationContext();
	  		CharSequence text = "Volume: " + vol;
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  
        }
        super.onKeyDown(keyCode, event);
        return true;
    }
}
