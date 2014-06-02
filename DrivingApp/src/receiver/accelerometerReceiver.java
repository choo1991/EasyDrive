package receiver;

import java.util.Calendar;
import java.util.List;

import com.example.drivingapp.DataORM;
import com.example.drivingapp.DataStorageClass;
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
				Calendar rightNow = Calendar.getInstance();
				long rightNowMilli = rightNow.getTimeInMillis();
				Log.i("AccelReceiver", "x: " + x + " y: " + y + " z: " + z + " mph: " + mph);
				DataStorageClass temp = new DataStorageClass(rightNowMilli, x, y, z, mph);
				Context appContext = context.getApplicationContext();
				DataORM.insertData(appContext, temp);
//				List<DataStorageClass> dsc = DataORM.getData(context);
//	     		int length = dsc.size();
//	     		Log.i("AccelReceiver", "The size of the database is: " + length);
//	     		for (int i = 0; i < length; i++) {
//	     			DataStorageClass tempData = dsc.get(i);
//	     			Log.i("AccelReceiver", tempData.returnTime() + ": " + tempData.returnX() + ", " + 
//	     			tempData.returnY() + ", " + tempData.returnZ() + ", " + tempData.returnSpeed());
//	     		}
			}
			
		}
		if (intent.getAction().equals("com.example.MainActivity.speeddata")) {
			float speed = intent.getFloatExtra("speed", 0.0f);
			mph = speed;
//			Log.i("AccelReceiver", speed + "MPH");
		}
	}

}
