package com.example.drivingapp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import receiver.LockScreenReceiver;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class LockScreenAppActivity extends Activity {

	//made static so that it can be referenced in order to prevent the lock screen from popping 
	//up when the user is in the allowed app
	public static String RUNNING_TASK = "";
	AppWidgetManager mAppWidgetManager;
	AppWidgetHost mAppWidgetHost;
	
    /** Called when the activity is first created. */
	KeyguardManager.KeyguardLock k1;
	boolean inDragMode;
 	int selectedImageViewX;
 	int selectedImageViewY;
 	int windowwidth;
 	int windowheight;
 	ImageView droid,phone,home;
 	//int phone_x,phone_y;
 	int home_x,home_y;
 	int[] droidpos;

 	private LayoutParams layoutParams;

//	  @Override
//	 public void onAttachedToWindow() {
//		// TODO Auto-generated method stub
//		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG|WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//          super.onAttachedToWindow();
//	 }
 	public void onCreate(Bundle savedInstanceState) {
 		MainActivity.LOCK_SCREEN_ACTIVE = true;
 		super.onCreate(savedInstanceState);
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setTheme(android.R.style.Theme_Holo);
		setContentView(R.layout.main);
		
//		droid =(ImageView)findViewById(R.id.droid);
//		System.out.println("measured width"+droid.getMeasuredWidth());
//		System.out.println(" width"+droid.getWidth());

		mAppWidgetManager = AppWidgetManager.getInstance(this);
		int APPWIDGET_HOST_ID = 0;
		mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
		AppWidgetProviderInfo newAppWidgetProviderInfo = new AppWidgetProviderInfo();
		
		int appWidgetId = mAppWidgetHost.allocateAppWidgetId();
		// Get the list of installed widgets
		List<AppWidgetProviderInfo> appWidgetInfos = new ArrayList<AppWidgetProviderInfo>();
		appWidgetInfos = mAppWidgetManager.getInstalledProviders();
		
		for(int j = 0; j < appWidgetInfos.size(); j++) {				
		    if ((appWidgetInfos.get(j).provider.getPackageName().equals("com.google.android.deskclock") || appWidgetInfos.get(j).provider.getPackageName().equals("com.android.deskclock")) && appWidgetInfos.get(j).provider.getClassName().equals("com.android.alarmclock.DigitalAppWidgetProvider")) {
		        Log.i("Widgets", "clock widget exists");
		    	// Get the full info of the required widget
		        Log.i("Widgets", "get app widget info");
		        newAppWidgetProviderInfo = appWidgetInfos.get(j);
		        addClockWidget(appWidgetId, newAppWidgetProviderInfo);
		        break;
		    }
//			Log.i("Widgets", appWidgetInfos.get(j).provider.getPackageName());
//			Log.i("Widgets", appWidgetInfos.get(j).provider.getClassName());
		 }

		

		if(getIntent()!=null&&getIntent().hasExtra("kill")&&getIntent().getExtras().getInt("kill")==1){
			// Toast.makeText(this, "" + "kill activityy", Toast.LENGTH_SHORT).show();
			finish();
		}
        try{
        	// initialize receiver
        	startService(new Intent(this,MyService.class));

  /*      KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        k1 = km.newKeyguardLock("IN");
        k1.disableKeyguard();*/
        StateListener phoneStateListener = new StateListener();
        TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

        windowwidth=getWindowManager().getDefaultDisplay().getWidth();
        System.out.println("windowwidth"+windowwidth);
        windowheight=getWindowManager().getDefaultDisplay().getHeight();
        System.out.println("windowheight"+windowheight);

//        MarginLayoutParams marginParams2 = new MarginLayoutParams(droid.getLayoutParams());

//        marginParams2.setMargins((windowwidth/24)*10,((windowheight/32)*8),0,0);

        //marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
//        RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(marginParams2);

//        droid.setLayoutParams(layoutdroid);

        /* phone =(ImageView)findViewById(R.id.phone);
        MarginLayoutParams marginParams = new MarginLayoutParams(phone.getLayoutParams());
         marginParams.setMargins(0,windowheight/32,windowwidth/24,0);
         LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(marginParams);
         phone.setLayoutParams(layoutParams1);
*/

//         LinearLayout homelinear = (LinearLayout)findViewById(R.id.homelinearlayout);
//         homelinear.setPadding(0,0,0,(windowheight/32)*3);
//         home =(ImageView)findViewById(R.id.home);

//         MarginLayoutParams marginParams1 = new MarginLayoutParams(home.getLayoutParams());

//         marginParams1.setMargins((windowwidth/24)*10,0,(windowheight/32)*8,0);
        // marginParams1.setMargins(((windowwidth-home.getWidth())/2),0,(windowheight/32)*10,0);
//         LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(marginParams1);

//         home.setLayoutParams(layout);
         
//         displayTime();
         getAppIcons();

         droid.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				   layoutParams = (LayoutParams) v.getLayoutParams();

				switch(event.getAction())
		         {

		         case MotionEvent.ACTION_DOWN:
		        	 int[] hompos=new int[2];
		        	// int[] phonepos=new int[2];
		        	 droidpos=new int[2];
			         //phone.getLocationOnScreen(phonepos);
			         home.getLocationOnScreen(hompos);
			         home_x=hompos[0];
			         home_y=hompos[1];
			       //  phone_x=phonepos[0];
			        // phone_y=phonepos[1];


		        	 break;
		         case MotionEvent.ACTION_MOVE:
		             int x_cord = (int)event.getRawX();
		             int y_cord = (int)event.getRawY();

		             if(x_cord>windowwidth-(windowwidth/24)){x_cord=windowwidth-(windowwidth/24)*2;}
		             if(y_cord>windowheight-(windowheight/32)){y_cord=windowheight-(windowheight/32)*2;}

		             layoutParams.leftMargin = x_cord ;
		             layoutParams.topMargin = y_cord;

		             droid.getLocationOnScreen(droidpos);
		             v.setLayoutParams(layoutParams);

		             if(((x_cord-home_x)<=(windowwidth/24)*5 && (home_x-x_cord)<=(windowwidth/24)*4)&&((home_y-y_cord)<=(windowheight/32)*5))
		              {
		                 System.out.println("home overlapps");
		                 System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+droidpos[0]);

		            	 System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+droidpos[1]);

		            	 v.setVisibility(View.GONE);
		            	 
		            	 MainActivity.LOCK_SCREEN_ACTIVE = false;
		            	// startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
		                finish();
		              }
		             else{
		            	 System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+droidpos[0]);

		            	 System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+droidpos[1]);


		            	 System.out.println("home notttt overlapps");
		             }
		             /* if(((x_cord-phone_x)>=128 && (x_cord-phone_x)<=171 )&&((phone_y-y_cord)<=10))
		              {
		            	  System.out.println("phone overlapps");
                       finish();
		              }
		              else{
		            	     System.out.println(phone_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+droidpos[0]);

			            	 System.out.println(phone_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+droidpos[1]);


			            	 System.out.println("phone not overlapps" +
			            	 		" overlapps");
		              }*/
		            // v.invalidate();
		             break;
		         case MotionEvent.ACTION_UP:
		        	    int x_cord1 = (int)event.getRawX();
			             int y_cord2 = (int)event.getRawY();

			             if(((x_cord1-home_x)<=(windowwidth/24)*5 && (home_x-x_cord1)<=(windowwidth/24)*4)&&((home_y-y_cord2)<=(windowheight/32)*5))
			              {
			                 System.out.println("home overlapps");
			                 System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord1+" "+droidpos[0]);

			            	 System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord2+" "+droidpos[1]);

			            	// startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
			               //finish();
			              }
			             else{

			            	 layoutParams.leftMargin = (windowwidth/24)*10;
				             layoutParams.topMargin = (windowheight/32)*8;
				             v.setLayoutParams(layoutParams);


			             }
		         }
				return true;
			}
		});
/*
        Button close =(Button)findViewById(R.id.lockk);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//k1.reenableKeyguard();
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
                finish();
            }
        });
*/
       //PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

      // PowerManager.WakeLock w1 =pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.FULL_WAKE_LOCK,"MyApp");
      // w1.acquire();
      // w1.release();
        }catch (Exception e) {
			// TODO: handle exception
		}
    }
    private void addClockWidget(int appWidgetId, AppWidgetProviderInfo newAppWidgetProviderInfo) {
    	/* To ensure that the app does not crash if the phone does not have a Google default
		 * clock widget, check to see if there's anything in AppWidgetProviderInfo
		 *  */
		
		// Create Widget
		AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, newAppWidgetProviderInfo);
		hostView.setAppWidget(appWidgetId, newAppWidgetProviderInfo);
		// Add it to your layout
		LinearLayout ll = (LinearLayout) findViewById(R.id.clockWidgetLocation);
		ll.addView(hostView);
	
}
	class StateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("call Activity off hook");
                	finish();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };
    
    public void onSlideTouch(View view, MotionEvent event) {
    	 switch(event.getAction())
         {
         case MotionEvent.ACTION_DOWN:
        	 break;
         case MotionEvent.ACTION_MOVE:
             int x_cord = (int)event.getRawX();
             int y_cord = (int)event.getRawY();

             if(x_cord>windowwidth){x_cord=windowwidth;}
             if(y_cord>windowheight){y_cord=windowheight;}

             layoutParams.leftMargin = x_cord -25;
             layoutParams.topMargin = y_cord - 75;

             view.setLayoutParams(layoutParams);
             break;
         default:
             break;
         }

        //When the user pushes down on an ImageView
      /*  if ( event.getAction() == MotionEvent.ACTION_DOWN )
        {
           inDragMode = true; //Set a variable so we know we started draggin the imageView
           //Set the selected ImageView X and Y exact position
          selectedImageViewX = Math.abs((int)event.getRawX()-((ImageView)view).getLeft());
          selectedImageViewY = Math.abs((int)event.getRawY()-((ImageView)view).getTop());
           //Bring the imageView in front
           ((ImageView)view).bringToFront();
        }

        //When the user let's the ImageView go (raises finger)
        if ( event.getAction() == MotionEvent.ACTION_UP )
        {
           inDragMode = false; //Reset the variable which let's us know we're not in drag mode anymore
        }

        //When the user keeps his finger on te screen and drags it (slides it)
        if ( event.getAction() == MotionEvent.ACTION_MOVE )
        {
            //If we've started draggin the imageView
            if ( inDragMode )
            {
                //Get the imageView object
               // ImageView slide = (ImageView)findViewById(R.id.slide);
                //Get a parameters object (THIS EXAMPLE IS FOR A RELATIVE LAYOUT)
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
                //Change the position of the imageview accordingly
                params.setMargins((int)event.getRawX()-selectedImageViewX, (int)event.getRawY()-selectedImageViewY, 0, 0);
                //Set the new params
                view.setLayoutParams(params);

                //If we hit a limit with our imageView position
                /*if((int)event.getRawX())
                {
                    //Open another activity
                    Intent it = new Intent(Slide.this,NextActivity.class);
                    startActivity(it);
                }
            }
        }*/
    }
    
    @Override
	protected void onStart() {
		super.onStart();
		mAppWidgetHost.startListening();
	}

    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss.
        return;
    }

    //only used in lockdown mode
    @Override
    protected void onPause() {
        super.onPause();
        // Don't hang around.
       // finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Don't hang around.
       // finish();
        mAppWidgetHost.stopListening();
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)||(keyCode == KeyEvent.KEYCODE_POWER)||(keyCode == KeyEvent.KEYCODE_VOLUME_UP)||(keyCode == KeyEvent.KEYCODE_CAMERA)) {
    		//this is where I can do my stuff
    		return true; //because I handled the event
    	}
    	if((keyCode == KeyEvent.KEYCODE_HOME)){
    	    return true;
        }
    	return false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
    	if (event.getKeyCode() == KeyEvent.KEYCODE_POWER ||(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)||(event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
    	    //Intent i = new Intent(this, NewActivity.class);
    	    //startActivity(i);
    	    return false;
    	}
    	if((event.getKeyCode() == KeyEvent.KEYCODE_HOME)){
//           System.out.println("alokkkkkkkkkkkkkkkkk");
      	   return true;
         }
    return false;
    }

    public void onDestroy(){
       // k1.reenableKeyguard();
        super.onDestroy();
    }
    
    public void exitLockScreen(View view) {
    	MainActivity.LOCK_SCREEN_ACTIVE = false;
    	finish();
    }
    
//    public void displayTime() {
//			    	String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//			    	
//			    	TextView textView = (TextView) findViewById(R.id.tvTime);
//			    	textView.setText("EasyDrive -" + currentDateTimeString);
//    }
    
    public void getAppIcons() {
		// May want to maintain a list of apps on the screen, then do a foreach loop to assign
		// drawable icons to each app
		String[] currentApps = MainActivity.CurrentApps;
		Map<String, String> customAppsList = MainActivity.CustomAppsList;
		
		Drawable firstIcon = null;
		Drawable secondIcon = null;
		Drawable thirdIcon = null;
		Drawable fourthIcon = null;
		try {
			firstIcon = getPackageManager().getApplicationIcon("com.android.phone");
		} catch (Exception e) {
		}
		try {
			secondIcon = getPackageManager().getApplicationIcon(currentApps[0]);
		} catch (Exception e) {
		}
		try {
			thirdIcon = getPackageManager().getApplicationIcon(currentApps[1]);
		} catch (Exception e) {
		}
		try {
			fourthIcon = getPackageManager().getApplicationIcon(currentApps[2]);
		} catch (Exception e) {
		}
		
		ImageView firstIconView =(ImageView)findViewById(R.id.firstIcon);
		ImageView secondIconView =(ImageView)findViewById(R.id.secondIcon);
		ImageView thirdIconView =(ImageView)findViewById(R.id.thirdIcon);
		ImageView fourthIconView =(ImageView)findViewById(R.id.fourthIcon);
		
		//Button firstButton =(Button)findViewById(R.id.firstButton);
		Button secondButton =(Button)findViewById(R.id.secondButton);
		Button thirdButton =(Button)findViewById(R.id.thirdButton);
		Button fourthButton =(Button)findViewById(R.id.fourthButton);
		
		secondButton.setText(customAppsList.get(currentApps[0]));
		thirdButton.setText(customAppsList.get(currentApps[1]));
		fourthButton.setText(customAppsList.get(currentApps[2]));

		if (firstIcon != null) {
			firstIconView.setImageDrawable(firstIcon);
		}
		if (secondIcon != null) {
			secondIconView.setImageDrawable(secondIcon);
		}
		if (thirdIcon != null) {
			thirdIconView.setImageDrawable(thirdIcon);
		}
		if (fourthIcon != null) {
			fourthIconView.setImageDrawable(fourthIcon);
		}
    }	
    
    public void openApp(View view) {
    	MainActivity.LOCK_SCREEN_ACTIVE = false;
    	String packageName = "";
    	switch(view.getId()){
	        case R.id.firstButton:
	            packageName = "com.android.phone";
	        	break;
	        case R.id.secondButton:
	        	packageName = MainActivity.CurrentApps[0];
	        	break;
	        case R.id.thirdButton:
	        	packageName = MainActivity.CurrentApps[1];
	        	break;
	        case R.id.fourthButton:
	        	packageName = MainActivity.CurrentApps[2];
	        	break;
    	}
    	launchExternalApp(packageName);
    }
    
    private void launchExternalApp(String packageName) {
    	Context context = getApplicationContext();
    	Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
    	if (packageName.equals("com.android.phone")) {
    		intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
    	}
    	if (intent != null)
    	{
    		startActivity(intent);
    		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
    		// get the info from the currently running task
		    List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 

		    Log.d("topActivity", "CURRENT Activity ::"
		             + taskInfo.get(0).topActivity.getClassName());

		    ComponentName componentInfo = taskInfo.get(0).topActivity;
		    String pk = componentInfo.getPackageName();
            
            RUNNING_TASK = pk;
    	} else {
    		Dialog dialog = new Dialog(this);
            dialog.setTitle("This app is not installed.");
            dialog.show();
            //dialog = null;
    	}
    }
    //public void openPhoneScreen(View view) {
    	//open phone yo
   // }
}