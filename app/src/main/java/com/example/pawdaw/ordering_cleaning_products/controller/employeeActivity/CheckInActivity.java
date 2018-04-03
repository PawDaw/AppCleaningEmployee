package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import com.example.pawdaw.ordering_cleaning_products.controller.helper.BarcodeScannerActivity;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.OrderList;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.service.location.GPSIntentServiceThread;
import com.example.pawdaw.ordering_cleaning_products.model.service.location.GPSService;
import com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion.InternetConnectionListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.location.LocationIntentService;
import com.example.pawdaw.ordering_cleaning_products.model.service.location.OnLocationChangedListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion.InternetConSnackBar;
import com.example.pawdaw.ordering_cleaning_products.model.service.TimeDateService;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pawdaw on 15/11/16.
 */
public class CheckInActivity extends AppCompatActivity implements OnLocationChangedListener, InternetConnectionListener {

    public String employeeName;
    public String employeePlaceName;
    public String productName;

    private Product product;
    private GPSService gpsService;
    private TimeDateService timeDataService;
    private InternetConSnackBar internetConSnackBar;
    public  ProgressDialog dialogProgress;
    private BroadcastReceiver broadcastReceiver;
    private LocationIntentService locationIntentService;
    private boolean isRegisteredReceiver = false;

    private CheckBox checkBox;
    private final double DISTANCE_TO_END_TIME_METERS = 10;

    //public ScheduledExecutorService execService;

    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // Create main reference - parent
    private DatabaseReference dbRefProduct = firebaseDatabase.getReference("orderList");
    private DatabaseReference dbRefCheckInLog = firebaseDatabase.getReference("checkInLog");
    private DatabaseReference dbRefWorkplace = firebaseDatabase.getReference("workplaces");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        // 1 threads running
        //execService = Executors.newScheduledThreadPool(1);

        // Time Service
        timeDataService = new TimeDateService();

        // GPS Service
        gpsService = new GPSService(this, this);


        // Set Listener for this Activity to notify internet Connection
        Service.getInstance().setConnectivityListener(this);

        // Set this Activity to show SNACK BAR on the button
        internetConSnackBar = new InternetConSnackBar(this);

        checkBox = (CheckBox) findViewById(R.id.check_Box);

        // data from another activity
        employeeName = getIntent().getStringExtra("employeeName");
        employeePlaceName = getIntent().getStringExtra("employeePlaceName");


        // lable to top BAR
        setTitle(employeePlaceName);


        // load Location, required to button "Check IN "
        if (gpsService.getLocation() == null) {

                gpsService.requestLocationUpdatesNetworkProvider(2000, 0);

                dialogProgress = new ProgressDialog(CheckInActivity.this);
                dialogProgress.setMessage("Getting Location ... ");
                dialogProgress.setIndeterminate(false);
                dialogProgress.show();

                checkBox.setChecked(false);

            if(dialogProgress !=null && gpsService.getLocation() != null ){
                dialogProgress.dismiss();
                gpsService.removeUpdates();
            }

        }



        // Turn ON or OFF button
        if (Service.getInstance().ifExistStartTimeInCheckInLog(employeePlaceName,employeeName)) {

            // if End Time NOT Exist turn on button
            if(!Service.getInstance().ifExistEndTimeInCheckInLog(employeePlaceName,employeeName)){

                if(Service.getInstance().isNetworkAvailable(this)){

                    checkBox.setChecked(true);

                    gpsService.removeUpdates();
                    gpsService.requestLocationUpdatesNetworkProvider(2000,0);
                    gpsService.requestLocationUpdatesGPSProvider(2000,0);

                }else{
                    checkBox.setChecked(false);
                    internetConSnackBar.isConnected(Service.getInstance().isNetworkAvailable(this));
                }

            }


        }


        // 1/3 BACK BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 2/3 BACK BUTTON
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // ------  Location notify method --------

    //Called when a new location is found by the network location provider.
    @Override
    public void onLocationChangedGPS(Location location) {

        if(dialogProgress !=null){
            dialogProgress.dismiss();
            gpsService.removeUpdates();
        }
        // If the distance between 2 compared locations is bigger than (140 m), Employee is on different location than Cleaning place
        double distance  = gpsService.distance(location.getLatitude(), location.getLongitude(), Double.parseDouble(Service.getInstance().getPlace(employeePlaceName).getLatitude()), Double.parseDouble(Service.getInstance().getPlace(employeePlaceName).getLongitude()));

        if (Service.getInstance().ifExistStartTimeInCheckInLog(employeePlaceName, employeeName)) {

            if ( distance > DISTANCE_TO_END_TIME_METERS) {

                        gpsService.removeUpdates();
                        checkBox.setChecked(false);

                        Log.i("info CheckInActivity :", "end_time automatic");

                        dbRefCheckInLog.child(timeDataService.getDate()).child(employeePlaceName).child(employeeName).child("end_time").setValue(timeDataService.getEndTime());
                        gpsService.removeUpdates();



                        //NOTIFICATION
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

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
                        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(1, builder.build());


            }

        }

    }


    // ---------  Buttons -------------


    //    BUTTON to Check IN button
    public void checkInButton(final View view) {



        if(Service.getInstance().isNetworkAvailable(this)){


                    if (((CheckBox) view).isChecked()) {
                        //    not checked BUTTON

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("START");
                        alertDialog.setMessage("Click 'Check IN' to START");

                        // Navigate employee to the Settings window to turn on the location options
                        alertDialog.setPositiveButton("Check IN", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                gpsService.requestLocationUpdatesNetworkProvider(2000, 0);
                                gpsService.requestLocationUpdatesGPSProvider(2000, 0);

                                // Check if employee is already checked in the current place in current date
                                // If employee is already checked in on current date, show TOAST
                                if (Service.getInstance().ifExistStartTimeInCheckInLog(employeePlaceName, employeeName)) {

                                    Toast.makeText(view.getContext(), R.string.cant_clean, Toast.LENGTH_LONG).show();
                                    checkBox.setChecked(false);
                                    gpsService.removeUpdates();

                                } else {
                                    // If not, create new logbook value

                                    // // If the distance between 2 compared locations is bigger than (140 m), Employee is on different location than Cleaning place, don't SAVE
                                    if (gpsService.distance(gpsService.getLocation().getLatitude(), gpsService.getLocation().getLongitude(), Double.parseDouble(Service.getInstance().getPlace(employeePlaceName).getLatitude()), Double.parseDouble(Service.getInstance().getPlace(employeePlaceName).getLongitude())) < DISTANCE_TO_END_TIME_METERS) {
                                        // SAVE

                                        // Create Start Time
                                        dbRefCheckInLog.child(timeDataService.getDate()).child(employeePlaceName).child(employeeName).child("start_time").setValue(timeDataService.getStartTime());


                                        // update value in the Workplace "recentlyCleaned" -> date
                                        HashMap<String,Object> newDate = new HashMap<String, Object>();
                                        newDate.put("recentlyCleaned",timeDataService.getDate());
                                        dbRefWorkplace.child(employeePlaceName).updateChildren(newDate);

                                        HashMap<String,Object> notificationUpdate = new HashMap<String, Object>();
                                        notificationUpdate.put("notificationSent","false");
                                        dbRefWorkplace.child(employeePlaceName).updateChildren(notificationUpdate);



                                        Toast.makeText(view.getContext(), getString(R.string.start_work) + timeDataService.getStartTime(), Toast.LENGTH_LONG).show();


                                    } else {
                                        //  don't save distance, is to big
                                        Toast.makeText(view.getContext(), R.string.not_on_location, Toast.LENGTH_LONG).show();
                                        checkBox.setChecked(false);
                                        gpsService.removeUpdates();
                                    }

                                }


                            }
                        });

                        // Cancel button - come back to the previous view - nothing happened
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                checkBox.setChecked(false);

                            }
                        });

                        // Show dialog
                        alertDialog.show();


                    }else{
                        //    checked BUTTON


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("STOP");
                        alertDialog.setMessage("Click 'Check OUT' when you finish work");

                        // Navigate employee to the Settings window to turn on the location options
                        alertDialog.setPositiveButton("Check OUT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                dbRefCheckInLog.child(timeDataService.getDate()).child(employeePlaceName).child(employeeName).child("end_time").setValue(timeDataService.getEndTime()).isSuccessful();
                                gpsService.removeUpdates();

                                Toast.makeText(view.getContext(), "Checked out. " + timeDataService.getEndTime(), Toast.LENGTH_SHORT).show();



                            }
                        });

                        // Cancel button - come back to the previous view - nothing happened
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                checkBox.setChecked(true);

                            }
                        });

                        // Show dialog
                        alertDialog.show();

                    }

        }else{
            checkBox.setChecked(false);
            internetConSnackBar.isConnected(Service.getInstance().isNetworkAvailable(this));

        }

    }

    //    BUTTON to scanner,BARCODE SCANNER
    public void scanBarcodeButton(View view) {

        //IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //scanIntegrator.initiateScan();

        Intent intent = new Intent(this,BarcodeScannerActivity.class);
        intent.putExtra("employeeName", employeeName);
        intent.putExtra("employeePlaceName", employeePlaceName);
        //method is below onActivityResult
        startActivityForResult(intent,0);

    }


    //    BUTTON to Cleaning products LIST
    public void cleaningProductListButton(View view) {

        // Extras to send data to CheckInActivity
        Intent toDetails = new Intent(CheckInActivity.this, ProductActivityList.class);
        toDetails.putExtra("employeeName", employeeName);
        toDetails.putExtra("employeePlaceName", employeePlaceName);
        toDetails.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toDetails);
    }


    //    BUTTON to Ordered products
    public void OrderedProductsButton(View view) {

        // Extras to send data to CheckInActivity
        Intent toDetails = new Intent(CheckInActivity.this, ProductOrderedlist.class);
        toDetails.putExtra("employeeName", employeeName);
        toDetails.putExtra("employeePlaceName", employeePlaceName);
        toDetails.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toDetails);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode !=0 || resultCode == CommonStatusCodes.SUCCESS ){

            if  (data != null) {

                Barcode barcodeContent = data.getParcelableExtra("barcode");
                String scanContent = barcodeContent.displayValue;
                Log.e("info CheckInActivity :", "Scanned VALUE : "+ scanContent);


                if (Service.getInstance().barcodeExist(scanContent)) {

                    productName = Service.getInstance().getProductName(scanContent);
                    Log.e("info CheckInActivity :", "product NAME : "+ productName);

                    product.getInstance().setProduct(productName, true);


                    for (OrderList o : Storage.getInstance().getOrderedProductHset()) {

                        product.getInstance().setProduct(o.getProduct(), true);
                        product.getInstance().toMap();

                    }

                    Map<String, Object> userValues = product.getInstance().toMap();

                    dbRefProduct.child(timeDataService.getDate()).child(employeePlaceName).setValue(userValues);


                    Toast.makeText(getApplicationContext(), "Product: " + Service.getInstance().getProductName(scanContent) + " added", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Product unknown", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
            }

        }else{

            Toast.makeText(getApplicationContext(), "Barcode Error", Toast.LENGTH_SHORT).show();
        }




    }


    @Override
    protected void onResume() {
        super.onResume();

        ////kill thread for GPS
        //execService.shutdown();


        //if(broadcastReceiver == null){
        //    broadcastReceiver = new BroadcastReceiver() {
        //        @Override
        //        public void onReceive(Context context, Intent intent) {
        //
        //            // get value Location from the Intent Service.
        //            Bundle b = intent.getExtras();
        //            Location location = (Location)b.get(android.location.LocationManager.KEY_LOCATION_CHANGED);
        //
        //            // call the function to set end time and sent notification
        //            onLocationChangedGPS(location);
        //
        //        }
        //    };
        //    registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        //
        //}

        if(isRegisteredReceiver) {
            //locationIntentService.onDestroy();
            unregisterReceiver(broadcastReceiver);
        }


        gpsService.removeUpdates();
        gpsService.requestLocationUpdatesNetworkProvider(2000,0);

        // Turn ON or OFF button
        if (Service.getInstance().ifExistStartTimeInCheckInLog(employeePlaceName,employeeName)) {

            // if End Time NOT Exist turn on button
            if(!Service.getInstance().ifExistEndTimeInCheckInLog(employeePlaceName,employeeName)){

                checkBox.setChecked(true);
                gpsService.requestLocationUpdatesGPSProvider(2000,0);
                Log.i("info :", "onResume");

            }
        }
    }


//  -----  BACK Button Pressed ----

    @Override
    public void onBackPressed() {

        // Turn ON or OFF button
        if (Service.getInstance().ifExistStartTimeInCheckInLog(employeePlaceName,employeeName)) {

            // if End Time NOT Exist in LOG, launch GPS every 5 minutes
            if (!Service.getInstance().ifExistEndTimeInCheckInLog(employeePlaceName, employeeName)) {
                //Convert minute to millisecond
                // 10 min - 600000 millisecond
                // 5 min - 300000 millisecond
                // 5 meters
                gpsService.removeUpdates();
                //gpsService.requestLocationUpdatesNetworkProvider(20000,5);
                //gpsService.requestLocationUpdatesGPSProvider(300000, 5);

                if(broadcastReceiver == null){
                    broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {

                            // get value Location from the Intent Service.
                            Bundle b = intent.getExtras();
                            Location location = (Location)b.get(android.location.LocationManager.KEY_LOCATION_CHANGED);

                            // call the function to set end time and sent notification
                            onLocationChangedGPS(location);

                        }
                    };

                    if(!isRegisteredReceiver) {
                        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
                        isRegisteredReceiver = true;
                    }

                }

                Log.i("info :", "onBackPressed");

            }else {

                if(isRegisteredReceiver) {
                    //locationIntentService.onDestroy();
                    unregisterReceiver(broadcastReceiver);
                }
            }
        }

        Service.getInstance().getOrderedProductsCurrentDate();

        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        gpsService.removeUpdates();

        // Create intent for Location Thread
        Intent intent = new Intent(CheckInActivity.this, LocationIntentService.class);
        //intent.putExtra("start",true);
        intent.putExtra("employeePlaceName",employeePlaceName);
        intent.putExtra("employeeName",employeeName);
        //intent.putExtra("employeePlaceLatitude",Double.parseDouble(Service.getInstance().getPlace(employeePlaceName).getLatitude()));
        //intent.putExtra("employeePlaceLongitude",Double.parseDouble(Service.getInstance().getPlace(employeePlaceName).getLongitude()));

        //intent.putExtra("context", (Serializable) this);


        // Turn ON or OFF button
        if (Service.getInstance().ifExistStartTimeInCheckInLog(employeePlaceName,employeeName)) {

            // if End Time NOT Exist in LOG, launch GPS every 5 minutes
            if (!Service.getInstance().ifExistEndTimeInCheckInLog(employeePlaceName, employeeName)) {


                //---  Call Particular Method after regular interval of time ---
                // initialDelay, PERIOD, TimeUnit.MILLISECONDS

                startService(intent);
                //execService.scheduleAtFixedRate(periodTask ,0, 2 , TimeUnit.MINUTES);

                Log.i("info :", "onPause");
            }
        }
    }

    //// THREAD task
    //Runnable periodTask = new Runnable(){
    //    @Override
    //    public void run() {
    //
    //        try{
    //            //Convert minute to millisecond
    //            // 10 min - 600000 millisecond
    //            // 5 min - 300000 millisecond
    //            // 5 meters
    //            gpsService.requestLocationUpdatesNetworkProvider(1,5);
    //            gpsService.requestLocationUpdatesGPSProvider(1, 5);
    //            Log.e("info GPS CheckAct :", "Auto running ");
    //
    //        }catch(Exception e){
    //            Log.e("info GPS CheckAct :", "EXEPTION = " + e.getMessage());
    //        }
    //    }
    //};


    // when the network connection changed
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        internetConSnackBar.isConnected(isConnected);

    }


    @Override
    protected void onStop() {
        if(isRegisteredReceiver) {
            //locationIntentService.onDestroy();
            unregisterReceiver(broadcastReceiver);
        }
        super.onStop();

        Log.i("info :", "onStop");
    }
}
