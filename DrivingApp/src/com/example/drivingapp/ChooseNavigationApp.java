package com.example.drivingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.ImageView;

public class ChooseNavigationApp extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_categories);
		PrefsFragment prefsFragment = new PrefsFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefsFragment).commit();
        prefsFragment.setActivity(this);
	}
	
	public void selectMaps() {
		// TODO: store preferences
		String packageName = "com.google.android.apps.maps";
    	Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
    	if (intent != null)
    	{
			MainActivity.CurrentApps[SetAppsActivity.selectedApp] = packageName;
			Intent i = new Intent(this, SetAppsActivity.class);
	    	startActivity(i);
    	} else {
    		// alert that they don't have that app... 
    	}
	}
	
    public static class PrefsFragment extends PreferenceFragment {
    	SharedPreferences prefs;
    	ChooseNavigationApp choose;
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    	addPreferencesFromResource(R.xml.pref_choose_navigation_app);
	    	choose.setContentView(R.layout.activity_navigation);
	    	getNavigationIcons();
	    }
    	
    	public void setActivity(ChooseNavigationApp choose) {
    		this.choose = choose;
    		prefs = PreferenceManager.getDefaultSharedPreferences(this.choose.getBaseContext());
    	}
    	
    	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        	String key = preference.getKey();
        	if(choose == null) {
        		try {
					throw new Exception("NOOB SET TEH MAIN OBJECT!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	if(key.equals("app_googleMaps")) {
        		choose.selectMaps();
        	} 
        	return super.onPreferenceTreeClick(preferenceScreen, preference);
    	}
    	
    	private void getNavigationIcons() {
    		ImageView firstImage = (ImageView)choose.findViewById(R.id.ivMaps);
    		String googleMapKey = "com.google.android.apps.maps";
    		Drawable firstIcon;
    		try {
				firstIcon = choose.getPackageManager().getApplicationIcon(googleMapKey);
				firstImage.setImageDrawable(firstIcon);
    		} catch(NameNotFoundException e) {
    		
    		}
    	}
    }
}
