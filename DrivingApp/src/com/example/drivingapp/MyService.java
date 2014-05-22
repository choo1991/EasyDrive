package com.example.drivingapp;

//import com.example.drivingapp.VolumeCheckService.SilenceCheck;

import receiver.LockScreenReceiver;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

public class MyService extends Service{
	 BroadcastReceiver mReceiver;
	// Intent myIntent;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


@Override
public void onCreate() {
	 KeyguardManager.KeyguardLock k1;

	 //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

	 KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
     k1= km.newKeyguardLock("IN");
     k1.disableKeyguard();




     /*try{
     StateListener phoneStateListener = new StateListener();
     TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
     telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
     }catch(Exception e){
    	 System.out.println(e);
     }*/

    /* myIntent = new Intent(MyService.this,LockScreenAppActivity.class);
     myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
     Bundle myKillerBundle = new Bundle();
     myKillerBundle.putInt("kill",1);
     myIntent.putExtras(myKillerBundle);*/

     IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
     filter.addAction(Intent.ACTION_SCREEN_OFF);

     mReceiver = new LockScreenReceiver();
     registerReceiver(mReceiver, filter);


    super.onCreate();


}
@Override
public void onStart(Intent intent, int startId) {
	// TODO Auto-generated method stub

	super.onStart(intent, startId);
}

/*
protected void onHandleIntent(Intent intent) {
	
	//infinite loop
	while(true) {
		//continuously set the volume to silent when true
		if(MainActivity.TERMINATE_APP) {
			stopSelf();
		}
	}
}
*/
/*
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
*/
/*class StateListener extends PhoneStateListener{
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        super.onCallStateChanged(state, incomingNumber);
        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                System.out.println("call Activity off hook");
            	getApplication().startActivity(myIntent);



                break;
            case TelephonyManager.CALL_STATE_IDLE:
                break;
        }
    }
};*/


@Override
public void onDestroy() {
	unregisterReceiver(mReceiver);
	super.onDestroy();
}
}
