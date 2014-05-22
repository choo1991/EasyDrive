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

public class ChooseMusicApp extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_categories);
		PrefsFragment prefsFragment = new PrefsFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, prefsFragment).commit();
        prefsFragment.setActivity(this);
		
	}
	
	public void selectApp(String packageName) {
		// set preference to equal the phone

    	Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
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
    	ChooseMusicApp choose;
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    	addPreferencesFromResource(R.xml.pref_choose_music_app);
	    	choose.setContentView(R.layout.activity_music);
	    	getMusicIcons();
	    }
    	
    	public void setActivity(ChooseMusicApp choose) {
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
        	if(key.equals("app_pandora")) {
        		choose.selectApp("com.pandora.android");
        	} else if (key.equals("app_shazam")) {
        		choose.selectApp("com.shazam.android");
        	} else if (key.equals("app_spotify")) {
        		choose.selectApp("com.spotify.mobile.android.ui");
        	} else if (key.equals("app_amazonMp3")) {
        		choose.selectApp("com.amazon.mp3");
        	} else if (key.equals("app_googlePlayMusic")) {
        		choose.selectApp("com.google.android.music");
        	}
        	return super.onPreferenceTreeClick(preferenceScreen, preference);
    	}
    	    	
    	private void getMusicIcons() {
    		ImageView firstImage = (ImageView)choose.findViewById(R.id.ivPandora);
    		ImageView secondImage = (ImageView)choose.findViewById(R.id.ivShazam);
    		ImageView thirdImage = (ImageView)choose.findViewById(R.id.ivSpotify);
    		ImageView fourthImage = (ImageView)choose.findViewById(R.id.ivAmazonMp3);
    		ImageView fifthImage = (ImageView)choose.findViewById(R.id.ivGooglePlayMusic);

			String pandoraKey = "com.pandora.android";
			String shazamKey = "com.shazam.android";
			String spotifyKey = "com.spotify.mobile.android.ui";
			String amazonMp3Key = "com.amazon.mp3";
			String googlePlayMusicKey = "com.google.android.music";

			Drawable firstIcon;
			Drawable secondIcon;
			Drawable thirdIcon;
			Drawable fourthIcon;
			Drawable fifthIcon;
			try {
				firstIcon = choose.getPackageManager().getApplicationIcon(pandoraKey);
				firstImage.setImageDrawable(firstIcon);
				secondIcon = choose.getPackageManager().getApplicationIcon(shazamKey);
				secondImage.setImageDrawable(secondIcon);
				thirdIcon = choose.getPackageManager().getApplicationIcon(spotifyKey);
				thirdImage.setImageDrawable(thirdIcon);
				fourthIcon	= choose.getPackageManager().getApplicationIcon(amazonMp3Key);
				fourthImage.setImageDrawable(fourthIcon);
				fifthIcon = choose.getPackageManager().getApplicationIcon(googlePlayMusicKey);
				fifthImage.setImageDrawable(fifthIcon);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
			}
    	}
    }
}
