package receiver;

import com.example.drivingapp.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class accelerometerReceiver extends BroadcastReceiver {

	float mph = 0.0f;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.example.MainActivity.acceldata")) {
			if (MainActivity.LOCK_SCREEN_ACTIVE) {
				float x = intent.getFloatExtra("x", 0.0f); 
				float y = intent.getFloatExtra("y", 0.0f);
				float z = intent.getFloatExtra("z", 0.0f);
				Log.i("AccelReceiver", "x: " + x + " y: " + y + " z: " + z + " mph: " + mph);
			}
		}
		if (intent.getAction().equals("com.example.MainActivity.speeddata")) {
			float speed = intent.getFloatExtra("speed", 0.0f);
			mph = speed;
//			Log.i("AccelReceiver", speed + "MPH");
		}
	}

}
