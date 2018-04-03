package com.example.pawdaw.ordering_cleaning_products.model.service.location;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity.CheckInActivity;
import com.example.pawdaw.ordering_cleaning_products.model.service.TimeDateService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by pawdaw on 25/01/18.
 */

public class LocationIntentService extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    private String employeeName,employeePlaceName;
    private final double DISTANCE_TO_END_TIME_METERS = 200;
    public com.example.pawdaw.ordering_cleaning_products.model.service.Service service;

    private TimeDateService timeDataService;


    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // Create main reference - parent
    private DatabaseReference dbRefCheckInLog = firebaseDatabase.getReference("checkInLog");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        employeeName = intent.getStringExtra("employeeName");
        employeePlaceName = intent.getStringExtra("employeePlaceName");
    }

    @Override
    public void onCreate() {

        // Time Service
        timeDataService = new TimeDateService();

        service = new com.example.pawdaw.ordering_cleaning_products.model.service.Service();

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {


                Log.i("info Location SERVICE :", "change location");
                Intent i = new Intent("location_update");
                i.putExtra("location",location);
                sendBroadcast(i);

                Log.i("info Location SERVICE :", "location: " +location);

                //Log.i("info SERVICE :", "Listener SERVICE , location : " + location );
                Log.i("info Location SERVICE :", "service: " +service.getInstance().getPlace(employeePlaceName).getLatitude());
                Log.i("info Location SERVICE :", "palace name: " +employeePlaceName);



                // Create intent for Location Thread
                //Intent intent2 = new Intent(LocationIntentService.this, CheckInActivity.class);
                //startActivity(intent2);



                 //If the distance between 2 compared locations is bigger than (140 m), Employee is on different location than Cleaning place
                double distance  = distance(location.getLatitude(), location.getLongitude(), Double.parseDouble(service.getInstance().getPlace(employeePlaceName).getLatitude()), Double.parseDouble(service.getInstance().getPlace(employeePlaceName).getLongitude()));

                if (com.example.pawdaw.ordering_cleaning_products.model.service.Service.getInstance().ifExistStartTimeInCheckInLog(employeePlaceName, employeeName)) {

                    if ( distance > DISTANCE_TO_END_TIME_METERS) {


                        Log.i("info CheckInActivity :", "end_time automatic");

                        dbRefCheckInLog.child(timeDataService.getDate()).child(employeePlaceName).child(employeeName).child("end_time").setValue(timeDataService.getEndTime());

                        //NOTIFICATION
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                        // Set the notification to auto-cancel. This means that the notification will disappear
                        // after the user taps it, rather than remaining until it's explicitly dismissed.
                        builder.setAutoCancel(true);
                        builder.setSmallIcon(R.drawable.stop_icone);
                        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                        builder.setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        builder.setLights(Color.RED, 0, 1);
                        builder.setContentTitle("Auto set end time to : "+ employeePlaceName);
                        builder.setContentText("You crossed the distance of : " + Math.round(distance) +" meters.");
                        builder.setSubText("End Time "+timeDataService.getEndTime());

                        //Send the notification. This will immediately display the notification icon in the notification bar.
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(1, builder.build());


                    }

                }





            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, listener);

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
    public void onDestroy() {
        super.onDestroy();

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(listener);
        }
    }
}
