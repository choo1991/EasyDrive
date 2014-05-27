package receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import android.util.Log;
import android.widget.Toast;

import com.example.drivingapp.LockScreenAppActivity;
import com.example.drivingapp.MainActivity;
import com.example.drivingapp.OptionsActivity;
import com.example.drivingapp.SpeedAdjustActivity;

import android.app.Activity;

public class LockScreenReceiver extends BroadcastReceiver  {
	 public static boolean wasScreenOn = true;
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("LOG_TAG", Boolean.toString(MainActivity.ABOVE_SPEED_LIMIT));
		//needs testing
		if(MainActivity.POSTPONE_APP > 0) {
			
			try {
				Thread.sleep(MainActivity.POSTPONE_APP);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MainActivity.POSTPONE_APP = 0;
		}
		
		//onReceive is called when screen is off and turns back based on testing
		if(OptionsActivity.RINGER_MODE_SILENCED) {
			//for phone volumes (notifications, alarms, calls, music, etc)
	    	AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	    	am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	    	
	    	int vol = am.getStreamVolume(AudioManager.STREAM_RING);
	    	
	  		CharSequence text = "Volume is " + vol;
	  		int duration = Toast.LENGTH_SHORT;
	  		
	      	Toast toast = Toast.makeText(context, text, duration);
	  		toast.show(); 
		} 
		//Toast.makeText(context, "" + "enterrrrrr", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && MainActivity.ABOVE_SPEED_LIMIT) {
        	//Toast.makeText(context, "" + "screeen off", Toast.LENGTH_SHORT).show();

        	wasScreenOn=false;
        	Intent intent11 = new Intent(context,LockScreenAppActivity.class);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        	context.startActivity(intent11);

            // do whatever you need to do here
            //wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

        	wasScreenOn=true;
        	Intent intent11 = new Intent(context,LockScreenAppActivity.class);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        	//context.startActivity(intent11);
        	//Toast.makeText(context, "" + "start activity", Toast.LENGTH_SHORT).show();
            // and do whatever you need to do here
           // wasScreenOn = true;
        }
       else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) && MainActivity.ABOVE_SPEED_LIMIT)
        {
      /*  	KeyguardManager.KeyguardLock k1;
        	KeyguardManager km =(KeyguardManager)context.getSystemService(context.KEYGUARD_SERVICE);
            k1 = km.newKeyguardLock("IN");
            k1.disableKeyguard();
*/
        	Intent intent11 = new Intent(context, LockScreenAppActivity.class);

        	intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
           context.startActivity(intent11);

        	//  Intent intent = new Intent(context, LockPage.class);
	        //  context.startActivity(intent);
	        //  Intent serviceLauncher = new Intent(context, UpdateService.class);
	        //  context.startService(serviceLauncher);
	        //  Log.v("TEST", "Service loaded at start");
       }

    }
}
