package com.example.drivingapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

public class OptionsActivity extends Activity {

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
	  		CharSequence text = "Speed Setting Is " + data.getIntExtra(SpeedAdjustActivity.SPEED, 0);
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show();  
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
  
}
