package com.example.drivingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.ContactsContract;
import android.widget.ImageView;

public class ChooseOtherApp extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PrefsFragment prefsFragment = new PrefsFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefsFragment).commit();
        prefsFragment.setActivity(this);
	}
	
	public void selectApp(String packageName) {
    	Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
    	if (intent != null)
    	{
			MainActivity.CurrentApps[SetAppsActivity.selectedApp] = packageName;
			Intent i = new Intent(this, SetAppsActivity.class);
	    	startActivity(i);
	    	finish();
    	} else {
    		Dialog dialog = new Dialog(this);
            dialog.setTitle("This app is not installed.");
            dialog.show();
    	}
	}
	   
	@Override
    public void onBackPressed()
    {
    	Intent i = new Intent(this, ChooseCategory.class);
    	startActivity(i);
    	finish();
    }
	
    public static class PrefsFragment extends PreferenceFragment {
    	SharedPreferences prefs;
    	ChooseOtherApp choose;
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    	addPreferencesFromResource(R.xml.pref_choose_other_app);
	    	choose.setContentView(R.layout.activity_other);
	    	getIcons();
	    }
    	
    	
    	public void setActivity(ChooseOtherApp choose) {
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
        	if(key.equals("app_yelp")) {
        		choose.selectApp("com.yelp.android");
        	} else if (key.equals("app_gas_buddy")) {
        		choose.selectApp("gbis.gbandroid");
        	}
        	return super.onPreferenceTreeClick(preferenceScreen, preference);
    	}
    	
    	private void getIcons() {
    		ImageView firstImage = (ImageView)choose.findViewById(R.id.ivYelp);
    		ImageView secondImage = (ImageView)choose.findViewById(R.id.ivGasBuddy);
    		
    		String yelpKey = "com.yelp.android";
    		String gasBuddyKey = "gbis.gbandroid";
    		
    		Drawable firstIcon;
    		Drawable secondIcon;
    		try {
				firstIcon = choose.getPackageManager().getApplicationIcon(yelpKey);
				firstImage.setImageDrawable(firstIcon);
    		} catch(NameNotFoundException e) {
    		
    		}
    		try {
				secondIcon = choose.getPackageManager().getApplicationIcon(gasBuddyKey);
				secondImage.setImageDrawable(secondIcon);
    		} catch(NameNotFoundException e) {
    		
    		}
    	}
    }
}
