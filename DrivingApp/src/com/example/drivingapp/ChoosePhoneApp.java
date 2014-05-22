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
import android.provider.ContactsContract;
import android.widget.ImageView;

public class ChoosePhoneApp extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PrefsFragment prefsFragment = new PrefsFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefsFragment).commit();
        prefsFragment.setActivity(this);
	}
	
	public void selectPhone() {
		// set preference to equal the phone
		String packageName = "com.android.phone";
		Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
    	if (intent != null)
    	{
			MainActivity.CurrentApps[SetAppsActivity.selectedApp] = packageName;
			Intent i = new Intent(this, SetAppsActivity.class);
	    	startActivity(i);
    	} else {
    		// alert that they don't have that app... everyone should have the phone contacts doe
    	}
	}
	
    public static class PrefsFragment extends PreferenceFragment {
    	SharedPreferences prefs;
    	ChoosePhoneApp choose;
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    	addPreferencesFromResource(R.xml.pref_choose_phone_app);
	    	choose.setContentView(R.layout.activity_phone);
	    	getPhoneIcons();
	    }
    	
    	
    	public void setActivity(ChoosePhoneApp choose) {
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
        	if(key.equals("app_phone")) {
        		choose.selectPhone();
        	} 
        	return super.onPreferenceTreeClick(preferenceScreen, preference);
    	}
    	
    	private void getPhoneIcons() {
    		ImageView firstImage = (ImageView)choose.findViewById(R.id.ivPhone);
    		String phoneKey = "com.android.phone";
    		Drawable firstIcon;
    		try {
				firstIcon = choose.getPackageManager().getApplicationIcon(phoneKey);
				firstImage.setImageDrawable(firstIcon);
    		} catch(NameNotFoundException e) {
    		
    		}
    	}
    }
}
