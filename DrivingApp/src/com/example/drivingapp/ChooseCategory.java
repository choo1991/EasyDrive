package com.example.drivingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;

public class ChooseCategory extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_categories);
        PrefsFragment prefsFragment = new PrefsFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefsFragment).commit();
        prefsFragment.setActivity(this);

	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.categories, menu);
	    return true;
	}
	
	public void chooseMusicApp() {
		Intent i = new Intent(this, ChooseMusicApp.class);
    	startActivity(i);
    	finish();
	}
	
    public void chooseNavigationApp() {
		Intent i = new Intent(this, ChooseNavigationApp.class);
    	startActivity(i);    
    	finish();
    }
     
    public void choosePhoneApp() {
		Intent i = new Intent(this, ChooseOtherApp.class);
    	startActivity(i);
    	finish();
    }
	
    public static class PrefsFragment extends PreferenceFragment {
    	
    	SharedPreferences prefs;
    	ChooseCategory choose;
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    	addPreferencesFromResource(R.xml.pref_categories);
	    }
    	
    	public void setActivity(ChooseCategory choose) {
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
        	if(key.equals("category_music")) {
        		choose.chooseMusicApp();
        	} else if (key.equals("category_navigation")) {
        		choose.chooseNavigationApp();
        	} else if (key.equals("category_other")) {
        		choose.choosePhoneApp();
        	}
        	return super.onPreferenceTreeClick(preferenceScreen, preference);
    	}
    }
    
}
