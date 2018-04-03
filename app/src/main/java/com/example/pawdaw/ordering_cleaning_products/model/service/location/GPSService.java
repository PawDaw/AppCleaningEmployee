package com.example.pawdaw.ordering_cleaning_products.model.service.location;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.model.service.location.OnLocationChangedListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by pawdaw on 27/03/17.
 */

public class GPSService implements LocationListener {

    public LocationManager locationManager;
    private final Context context;
    private Location location;
    public OnLocationChangedListener notify;


    public  GPSService(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

    }

    public GPSService(Context context, OnLocationChangedListener callback) {
        this.context = context;
        this.notify = callback;
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    // Network is fast
    public void requestLocationUpdatesNetworkProvider(long minTime, float minDistance) {

        //provider	    the name of the provider with which we would like to regiser.
        //minTime	    minimum time interval between location updates (in milliseconds).
        //minDistance	minimum distance between location updates (in meters).
        //listener	    a LocationListener whose onLocationChanged(Location) method will be called for each location update.

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);



        if (locationManager != null) {

            Location lastKnownLocation;

            // getLastKnownLocation
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(lastKnownLocation != null){

                long millisLocationLastUpdated = lastKnownLocation.getTime();
                long currentMillisTime = System.currentTimeMillis();

                long milliseconds = (currentMillisTime - millisLocationLastUpdated);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);

                Log.e("info :", "last update location NETWORK_PROVIDER , Minutes : " + minutes );

                // if last Known Location was less than 4 min pass to current location
                if(minutes < 4){

                    location = lastKnownLocation;
                    Log.e("info :", "last Know location passed to Location NETWORK_PROVIDER");

                }else {

                    // getting network status
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                }
            }


        }else {

            // getting network status
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
        }


    }

    //    GPS is very slow
    public void requestLocationUpdatesGPSProvider(long minTime, float minDistance) {

        //provider	    the name of the provider with which we would like to regiser.
        //minTime	    minimum time interval between location updates (in milliseconds).
        //minDistance	minimum distance between location updates (in meters).
        //listener	    a LocationListener whose onLocationChanged(Location) method will be called for each location update.

        // getting network status
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // getting GPS status
        //Convert minute to millisecond
        // 10 min - 600000 millisecond
        // 0.03 min - 2000 millisecond
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,0 , this);

        if (locationManager != null) {

            Location lastKnownLocation;

            // getLastKnownLocation
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastKnownLocation != null) {

                    long millisLocationLastUpdated = lastKnownLocation.getTime();
                    long currentMillisTime = System.currentTimeMillis();

                    long milliseconds = ((currentMillisTime - millisLocationLastUpdated));

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);

                    Log.e("info :", "last update location GPS_PROVIDER , Minutes : " + minutes);

                    // if last Known Location was less than 4 min pass to current location
                    if (minutes < 4) {

                        location = lastKnownLocation;
                        Log.e("info :", "last Know location passed to Location GPS_PROVIDER");

                    } else {

                        // getting network status
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
                    }
            }

        }else {

            // getting network status
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
        }

    }

    public boolean getLastKnownLocation() {

        Boolean exist = false;
        // getLastKnownLocation
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null){

            long millisLocation = location.getTime();
            //long minutes = (millis / 1000)  / 60;
            int seconds = (int) ((millisLocation / 1000) % 60);
            //long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

            long end = System.currentTimeMillis();
            long minutesLastLocation = ((end - millisLocation) / 1000);

            Log.e("info :", "get last location , Minutes :" + TimeUnit.MILLISECONDS.toMinutes(minutesLastLocation));
            if(minutesLastLocation < 1){

                exist = true;
                location = loc;
                Log.e("info :", "get last location" + location.getLatitude());

            }
        }

        return exist;
    }


    public void removeUpdates() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }




    //  ----- Location Listener --------

    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        setLocation(location);

        if (notify != null){
            notify.onLocationChangedGPS(location);
        }

        if (location != null){

            Log.e("info :"," distance in meters :"+ distance(location.getLatitude(), location.getLongitude(), 56.210557, 10.042586));

        }

        Log.e("info :"," Latitude :" + location.getLatitude());
        Log.e("info :"," Longitude :" + location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(context, "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //context.startActivity(intent);
        //Toast.makeText(context, "Gps is turned off!! ", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Dialog's title
        alertDialog.setTitle("GPS is disabled");

        // Dialog's message
        alertDialog.setMessage("Click 'Go to settings' button to change your location options, than try to log in again");

        // Navigate employee to the Settings window to turn on the location options
        alertDialog.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        // Cancel button - come back to the previous view - nothing happened
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show dialog
        alertDialog.show();


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



}
