package com.example.drivingapp;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SetAppsActivity extends Activity {
	public static int selectedApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_apps);
		// maybe turn into a forloop? only problem is how do you dynamically get the other app ids?
		TextView firstApp = (TextView) findViewById(R.id.customApp1);
		TextView secondApp = (TextView) findViewById(R.id.customApp2);
		TextView thirdApp = (TextView) findViewById(R.id.customApp3);
		ImageView firstImage = (ImageView) findViewById(R.id.firstImage);
		ImageView secondImage = (ImageView) findViewById(R.id.secondImage);
		ImageView thirdImage = (ImageView) findViewById(R.id.thirdImage);
		
		
		String firstKey = MainActivity.CurrentApps[0];
		String secondKey = MainActivity.CurrentApps[1];
		String thirdKey = MainActivity.CurrentApps[2];
		
		try {
			Drawable firstIcon = getPackageManager().getApplicationIcon(firstKey);
			firstImage.setImageDrawable(firstIcon);
			Drawable secondIcon = getPackageManager().getApplicationIcon(secondKey);
			secondImage.setImageDrawable(secondIcon);
			Drawable thirdIcon = getPackageManager().getApplicationIcon(thirdKey);
			thirdImage.setImageDrawable(thirdIcon);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			
		}

		String firstName = MainActivity.CustomAppsList.get(firstKey);
		String secondName = MainActivity.CustomAppsList.get(secondKey);
		String thirdName = MainActivity.CustomAppsList.get(thirdKey);

		firstApp.setText(firstName);
		secondApp.setText(secondName);
		thirdApp.setText(thirdName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_apps, menu);
		return true;
	}
	
	public void changeApp(View view) {
		switch(view.getId()){
	        case R.id.app1:
	        	selectedApp = 0;
	        	Intent i = new Intent(this, ChooseCategory.class);
	        	startActivity(i);
	        	break;
	        case R.id.app2:
	        	selectedApp = 1;
	        	Intent i2 = new Intent(this, ChooseCategory.class);
	        	startActivity(i2);
	        	break;
	        case R.id.app3:
	        	selectedApp = 2;
	        	Intent i3 = new Intent(this, ChooseCategory.class);
	        	startActivity(i3);
	        	break;
		}
	}

}
