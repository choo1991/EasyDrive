package com.example.drivingapp;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class GPSLocationService extends Service {
	
	/* Retrieved from http://stackoverflow.com/questions/20839627/how-to-get-the-most-accurate-speed-from-location
	 * to create a service that returns MPH to store in database
	 */
	
    private static final String TAG = "GPSLocationService";
    private LocationManager locationManager;
    private LocationListener gpsLocationListener;
    private long lastGPStime;
    private double lastLatitude;
    private double lastLongitude;
    private float lastSpeed;
    private Address lastAddress;
    private Location mLastLocation;
    private float maxSpeed;
    private final float metersSec_in_MPH = 2.2369f;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    public static final String BROADCAST_ACTION = "com.example.MainActivity.speeddata";
    Intent intent;

    // other modules will call these public methods
    public String getTime() {
        return timeFormat.format(new Date(lastGPStime));
    }

    public String getLocation(){
        if(mLastLocation != null)
            return mLastLocation.toString();
        else
            return "0";
    }

    public String getAddress(){
        if(lastAddress != null)
            return lastAddress.getAddressLine(0) + " " + lastAddress.getAddressLine(1) + " " + lastAddress.getAddressLine(2) + " " + lastAddress.getAddressLine(3);
        else
            return "0";
    }

    public float getSpeedFloat(){
        if (lastSpeed < 1.0f) { return 0; }
        float mph = lastSpeed * metersSec_in_MPH;
        return mph;
    } 

    public Float getGpsStatus(){
        if(mLastLocation != null)
            return mLastLocation.getAccuracy();
        else
            return 0.0f;
    }

    // latitude ranges from 0.0 to 90.0
    // In the US, latitude is always double-digits: 44.xxyyzz
    // We'll keep six digits after the decimal point
    public String getLat() {
        String lValue = Double.toString(lastLatitude);
        if (lValue.length() < 9)
            return lValue;
        return lValue.substring(0, 9);
    } // latitude has max 2 digits before

    // in the US, Longitude is always three digits: 123.xxyyzz
    // We'll keep six digits after the decimal point (ITIS)
    public String getLong() {
        String lValue = Double.toString(lastLongitude);
        if (lValue.length() < 10)
            return lValue;
        return lValue.substring(0, 10);
    } // longitude has up to 3 digits

    // speed is reported in meters/second
    // speed needs three digits, and maybe three more past the decimal point:
    // 145.608
    public String getSpeed() {
        if (lastSpeed < 1.0f) { return "000"; }
        float mph = lastSpeed * metersSec_in_MPH;
        String lValue = Integer.toString((int) mph);
        return lValue;
    }

    public String getMaxSpeed() {
        if (maxSpeed < 1.0f) { return "0.0"; }
        String lValue = Float.toString(maxSpeed * metersSec_in_MPH);
        if (lValue.length() < 7) {
            return lValue;
        } else
            return lValue.substring(0, 7);
    }

    // setup this service to allow binding for access to public methods above.
    // http://developer.android.com/guide/components/bound-services.html
    private final IBinder mBinder = new GPSBinder();

    public class GPSBinder extends Binder {
        GPSLocationService getService() {
            return GPSLocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // the usual 'Service' methods below
    @Override
    public void onCreate() {
    	intent = new Intent(BROADCAST_ACTION); 
    	super.onCreate();
        // instantiate the inner class
        gpsLocationListener = new GPSLocationListener();
        // get the system manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // and demand Speed values
        Criteria criteria = new Criteria();
        criteria.setSpeedRequired(true);
        locationManager.requestLocationUpdates(
                locationManager.getBestProvider(criteria, false), 250, 
                5, gpsLocationListener);
//        Toast toast = Toast.makeText(GPSLocationService.this, "GPS updates requested.", Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(gpsLocationListener);
    }

    private class GPSLocationListener implements LocationListener,
            GpsStatus.Listener {
        boolean isGPSFix;

        public void onGpsStatusChanged(int event) {
            switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                if (mLastLocation != null)
                    isGPSFix = (SystemClock.elapsedRealtime() - lastGPStime) < 3000;
                if (isGPSFix) { // A fix has been acquired.
                    Toast toast = Toast.makeText(GPSLocationService.this, "GPS has a fix.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else { // The fix has been lost.
                    Toast toast = Toast.makeText(GPSLocationService.this, "GPS DOES NOT have a fix.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Toast toast = Toast.makeText(GPSLocationService.this, "GPS got first fix.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                isGPSFix = true;
                break;
            }
        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            try {
//                lastAddress = getAddressForLocation(Main.context, location);
                lastAddress = getAddressForLocation(getBaseContext(), location);
            } catch (IOException e) {
                Log.i("EXCEPTION", "Exception on Address");
                e.printStackTrace();
            }
            lastGPStime = location.getTime();
            lastLatitude = location.getLatitude();
            lastLongitude = location.getLongitude();
            lastSpeed = location.getSpeed();
            if (lastSpeed > maxSpeed) {
                maxSpeed = lastSpeed;
            }
            Log.i(TAG, "GPS update received.");
            intent.putExtra("speed", lastSpeed);
            sendBroadcast(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String statusDescription = "unknown";
            switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                statusDescription = "OUT_OF_SERVICE";
                break;
            case LocationProvider.AVAILABLE:
                statusDescription = "AVAILABLE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                statusDescription = "TEMPORARILY_UNAVAILABLE";
                break;
            }

            Toast toast = Toast.makeText(GPSLocationService.this, TAG + " GPS provider status changed to "
                    + statusDescription + "and the last speed was: " + getSpeed()  , Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast toast = Toast.makeText(GPSLocationService.this, "GPS provider enabled.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast toast = Toast.makeText(GPSLocationService.this, "GPS provider disabled?", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public void zeroMaxSpeed() {
        maxSpeed = 0.0f;
    }

    public Address getAddressForLocation(Context context, Location location) throws IOException {

        if (location == null) {
            return null;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        int maxResults = 1;

        Geocoder gc = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gc.getFromLocation(latitude, longitude, maxResults);

        return addresses.get(0);
    }

}