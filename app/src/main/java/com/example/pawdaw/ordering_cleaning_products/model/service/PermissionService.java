package com.example.pawdaw.ordering_cleaning_products.model.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity.TakePictureActivity;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by pawdaw on 22/07/17.
 */

public class PermissionService {

    // --------- Singleton instance  ----------------

    private static PermissionService instance = new PermissionService();

    public static PermissionService getInstance() {

        return instance;
    }

    private PermissionService() {
        //
    }


    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 3;


    public boolean askForCameraWriteReadPermission(Context context) {

        boolean permission = false;
        boolean cancel = false;

        // Required PERMISSION FOR API greater than 23
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have read/write permission
            int cameraPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            int readPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                cancel = true;
                requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            } else if (readPermission != PackageManager.PERMISSION_GRANTED) {
                cancel = true;
                requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            } else if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                cancel = true;
                requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 3);
            }

            if (cancel) {

                // There was an ERROR
                Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
            } else {
                // OK
                permission = true;
            }

            //    API lees than 23, withOUT Permission
        } else {

            permission = true;

        }

        return permission;
    }


    public boolean askGeoLocationPermission(Context context) {

        boolean permission = false;
        boolean cancel = false;


        // Required PERMISSION FOR API greater than 23
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have read/write permission
            int finePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            int coarsePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            int networkPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);


            if (finePermission != PackageManager.PERMISSION_GRANTED) {
                cancel = true;
                requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);

            } else if (coarsePermission != PackageManager.PERMISSION_GRANTED) {
                cancel = true;
                requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            } else if (networkPermission != PackageManager.PERMISSION_GRANTED) {
                cancel = true;
                requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 6);
            }

                if (cancel) {

                    // There was an ERROR
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show();
                } else {
                    // OK
                    permission = true;
                }


                //    API lees than 23, withOUT Permission
            } else {

            permission = true;

        }

        return permission;
    }
}