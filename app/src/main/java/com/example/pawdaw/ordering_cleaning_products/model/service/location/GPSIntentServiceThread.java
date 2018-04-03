package com.example.pawdaw.ordering_cleaning_products.model.service.location;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import android.app.Service;

import com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity.CheckInActivity;
import com.google.android.gms.location.LocationRequest;

import java.util.concurrent.TimeUnit;

/**
 * Created by pawdaw on 26/01/18.
 */

public class GPSIntentServiceThread extends Service implements OnLocationChangedListener {

    private boolean start,data;
    private String employeeName,employeePlaceName;
    private double employeePlaceLatitude,employeePlaceLongitude;
    private Context context;
    private GPSService locationProvider;
    private LocationRequest locationRequest;
    public static final String TAG = GPSIntentServiceThread.class.getSimpleName();

    private final double DISTANCE_TO_END_TIME_METERS = 200;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationProvider = new GPSService(this);


        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(500);

        // Set Listener for this Activity to notify internet Connection
        //Service.getInstance().setConnectivityListener(this);

        Log.d(TAG, "onCreate Called");
        Toast.makeText(getApplicationContext(), "On Destroy called " + locationProvider.getLocation().getLatitude(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





    //  ----- Distance--------

    public double distance(double lat1, double lng1, double lat2, double lng2) {

        // distanceBetween two locations, return meters
        float[] result = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, result);
        float meters = result[0];  // result in meters,    0.001 - km
        Log.e("info :"," meters "+ meters);   //Here distance in meters

        if(meters<140){
            Log.e("info :"," true ");   //Here distance in meters
            //Latitude 56.20990167
            //Longitude 10.04077835
            //meters 133.8161

        }

        return meters;
    }


    @Override
    public void onLocationChangedGPS(Location location) {

    }
}
