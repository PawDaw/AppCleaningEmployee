package com.example.pawdaw.ordering_cleaning_products.model.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.EmployeeActivity;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.WorkplaceActivity;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.EditUserActivity;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.EditWorkplaceActivity;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.CheckInLog;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.CheckInTime;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.OrderList;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Report;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Manager;
import com.example.pawdaw.ordering_cleaning_products.model.service.alertDialog.AlertDialog;
import com.example.pawdaw.ordering_cleaning_products.model.service.alertDialog.AlertDialogCompleteListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.GetEmployeeAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.GetManagerAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.GetOrderListAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.GetProductAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.GetReportAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.OnChangeTaskListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.OnCompleteTaskListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.GetPlacesAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion.InternetConnectionListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion.InternetConnectionReceiver;
import com.example.pawdaw.ordering_cleaning_products.model.service.location.GetLocationAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.location.GetLocationAsyncTaskListener;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by pawdaw on 01/11/16.
 */
public class Service {


    // --------- Singleton instance  ----------------

    private static Service instance = new Service();

    public static Service getInstance() {

        return instance;
    }

    public Service() {
        //
    }


    // ---------   Firebase  -------------
    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // Create main reference - parent
    private DatabaseReference dbRefOrderList,dbRefProducts,dbRefPlace,dbRefManagers,dbRefPass,ref,refWorkplaces,dbRefEmployees;


    // Calendar functionality
    private TimeDateService timeDataService;


    // Objects and Strings
    private User user;
    private Place place;
    private Manager manager;
    private OrderList orderedProduct;
    private ProgressDialog dialog;
    private Log log;

    private String nameOfThePlace,product,employeeName,employeeEmail,productName,productEAN,placeName;



//    addChildEventListener(ChildEventListener listener)
//    Add a listener for child events occurring at this location.
//
//    addListenerForSingleValueEvent(ValueEventListener listener)
//    Add a listener for a single change in the data at this location. ONLY when the child("workplaces") changes, onDataChange is called
//
//    addValueEventListener(ValueEventListener listener)
//    Add a listener for changes in the data at this location. ONLY when the children values changes, ALL values under "workplaces" - onDataChange is called


    public void getOrderedProductsCurrentDate(){


        // Time Service
        timeDataService = new TimeDateService();

        // Create instance of Firebase
        dbRefOrderList  = firebaseDatabase.getReference("orderList");

        dbRefOrderList.child(timeDataService.getDate()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/DataSnapshot.html
//                Gives access to all of the immediate children of this snapshot. Can be used in native for loops:
//                for (DataSnapshot child : parent.getChildren()) {

                nameOfThePlace = (String) dataSnapshot.getKey();  // /orderList/Sabro
                DataSnapshot proSnapshot = dataSnapshot.child("products");  // /orderList/Sabro/products


                    for(DataSnapshot productSnapshot : proSnapshot.getChildren()){  // /orderList/Sabro/products ... -  go through in all items in the "products"

                        product = (String) productSnapshot.getKey();

                        orderedProduct = new OrderList(nameOfThePlace,product);


                        //  save every single product to storage, possible duplicates data, data are saved into ArrayList
                      Storage.getInstance().setProductsByDate(orderedProduct);
//                        Storage.getInstance().setOrderedProductHset(orderedProduct);

                    }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                nameOfThePlace = (String) dataSnapshot.getKey();  // /orderList/Sabro
                DataSnapshot proSnapshot = dataSnapshot.child("products");  // /orderList/Sabro/products


                for(DataSnapshot productSnapshot : proSnapshot.getChildren()){  // /orderList/Sabro/products ... -  go through in all items in the "products"

                    product = (String) productSnapshot.getKey();

                    orderedProduct = new OrderList(nameOfThePlace,product);
                    //  save every single product to storage
                  Storage.getInstance().setProductsByDate(orderedProduct);
//                    Storage.getInstance().setOrderedProductHset(orderedProduct);


                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                nameOfThePlace = (String) dataSnapshot.getKey();  // /orderList/Sabro
                DataSnapshot proSnapshot = dataSnapshot.child("products");  // /orderList/Sabro/products


                for(DataSnapshot productSnapshot : proSnapshot.getChildren()){  // /orderList/Sabro/products ... -  go through in all items in the "products"

                    product = (String) productSnapshot.getKey();

                    orderedProduct = new OrderList(nameOfThePlace,product);
                    //  save every single product to storage
                   Storage.getInstance().setProductsByDate(orderedProduct);
//                    Storage.getInstance().setOrderedProductHset(orderedProduct);

                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                nameOfThePlace = (String) dataSnapshot.getKey();  // /orderList/Sabro
                DataSnapshot proSnapshot = dataSnapshot.child("products");  // /orderList/Sabro/products


                for(DataSnapshot productSnapshot : proSnapshot.getChildren()){  // /orderList/Sabro/products ... -  go through in all items in the "products"

                    product = (String) productSnapshot.getKey();

                    orderedProduct = new OrderList(nameOfThePlace,product);
                    //  save every single product to storage
                    Storage.getInstance().setProductsByDate(orderedProduct);
//                   Storage.getInstance().setOrderedProductHset(orderedProduct);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    public void getOrderedProductsCurrentDate_Place(final String placeName){


        // Time Service
        timeDataService = new TimeDateService();

        dbRefOrderList  = firebaseDatabase.getReference("orderList");

//        Add a listener for changes in the data at this location.
        dbRefOrderList.child(timeDataService.getDate()).child(placeName).child("products").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Add a listener for changes in the data at this location (//products) .(add or remove triggered onDataChange)
                Storage.getInstance().orderedProductHset.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    product = (String) ds.getKey();
                    log.e("info :","Triggered onDataChange PRODUCT name......: " + product);
                    orderedProduct = new OrderList(placeName,product);

                    Storage.getInstance().setOrderedProductHset(orderedProduct);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //  ----- PLACE  EDIT -----


    public void edit_Place(Place place,Context context){

        // Map of the users in current place - needed for send by extras to EditWorkplaceActivity
        Map<String, Object> usersMap = new HashMap<>();

        usersMap = place.getUsers();


        // Put values to extras
        Intent editWorkplace = new Intent(context, EditWorkplaceActivity.class);
        editWorkplace.putExtra("place", place);
        editWorkplace.putExtra("map", (Serializable) usersMap);
        editWorkplace.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(editWorkplace);
    }

    //  ----- PLACE  REMOVE -----

    public void remove_Place(final String placeName, final Context context ){

        // Create main reference - parent
        ref = firebaseDatabase.getReference();

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setTitle("Confirm Delete...");
        alert.setMessage("Are you sure you want delete Place: " + placeName + " ? ");

        // Button positive "YES" - solution deleted
        alert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                            // Delete place and all users belong to this place.
                            ref.child("workplaces").child(placeName).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    dataSnapshot.getRef().setValue(null);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                            // Delete linked place from employee
                            ref.child("employees").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ds.child("places").child(placeName).getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            // Confirmation
                            Toast.makeText(context, "Place deleted", Toast.LENGTH_SHORT).show();

                        // Put values to extras
                        Intent listOfWorkplaces = new Intent(context, WorkplaceActivity.class);
                        context.startActivity(listOfWorkplaces);

                    }
                });

        // Button negative "NO" - cancel; coming back to the list
        alert.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();

    }

    //  ----- USER EDIT -----


    public void edit_User(User user,Context context){

        // Map of the users in current place - needed for send by extras to EditWorkplaceActivity
        Map<String, Object> placeMap = new HashMap<>();

        placeMap = user.getPlaces();

        // Put values to extras
        Intent editUser = new Intent(context, EditUserActivity.class);
        editUser.putExtra("user", user);
        editUser.putExtra("map", (Serializable) placeMap);
        editUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(editUser);
    }


    //  ----- USER REMOVE -----

    public void remove_User(final String userName, final Context context ) {


        // Create main reference - parent
        ref = firebaseDatabase.getReference();

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setTitle("Confirm Delete...");
        alert.setMessage("Are you sure you want delete User: " + userName + " ? ");

        // Button positive "YES" - solution deleted
        alert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        // Delete clicked employee from workplaces he is assigned to
                        ref.child("workplaces").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ds.child("users").child(userName).getRef().removeValue();
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        // Delete employee and all places he is assigned to
                        ref.child("employees").child(userName).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                dataSnapshot.getRef().setValue(null);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        // Confirmation
                        Toast.makeText(context, "Employee deleted", Toast.LENGTH_SHORT).show();

                        // Put values to extras
                        Intent listOfUser = new Intent(context, EmployeeActivity.class);
                        context.startActivity(listOfUser);


                    }
                });

        // Button negative "NO" - cancel; coming back to the list
        alert.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();

    }


    public void getCheckInLogCurrentDate( ) {

        // Time Service
        timeDataService = new TimeDateService();

        dbRefOrderList  = firebaseDatabase.getReference("checkInLog");

        dbRefOrderList.child(timeDataService.getDate()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // clear the array because when the placeName is not changed the value is not overridden even when check in time was changed
                Storage.getInstance().myLogbooks.clear();

                for (DataSnapshot placeNameSnapshot : dataSnapshot.getChildren()) {

                    nameOfThePlace = (String) placeNameSnapshot.getKey();

                    for (DataSnapshot employeeNameSnapshot : placeNameSnapshot.getChildren()) {

                        employeeName = (String) employeeNameSnapshot.getKey();

                        CheckInTime checkInTime = employeeNameSnapshot.getValue(CheckInTime.class);
                        CheckInLog checkInLog = new CheckInLog(timeDataService.getDate(),nameOfThePlace,employeeName,checkInTime.getStart_time(),checkInTime.getEnd_time());
                        Storage.getInstance().addLogbook(checkInLog);

                    }

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    /**
     *  Get the password from Firebase for Manager User Create
     */
    public void getPass() {

        Storage.getInstance().setPassword("");

        dbRefPass = firebaseDatabase.getReference("password");
        //ref.addListenerForSingleValueEvent(new ValueEventListener()

        dbRefPass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pass = dataSnapshot.getValue(String.class);
                Storage.getInstance().setPassword(pass);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    /**
     *
     */
    public void notification_place() {


        Storage.getInstance().products.clear();

        dbRefPlace = firebaseDatabase.getReference("employees");

            dbRefPlace.child("Pawel_Dawid").child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    placeName = (String) ds.getKey();
                    log.e("info :","Triggered Notification...: " + placeName);


                    Storage.getInstance().setOrderedProductHset(orderedProduct);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }







    public boolean ifExistStartTimeInCheckInLog(String placeName, String employeeName) {

        boolean employeeExistsInThisDate = false;

        ArrayList<CheckInLog> logbooksToCheck = Storage.getInstance().getMyLogbooks();

        for (CheckInLog logbook : logbooksToCheck) {

            if (logbook.getPlaceName() != null && logbook.getPlaceName().equals(placeName)) {
                if (logbook.getEmployeeName().equals(employeeName)) {
                    if(logbook.getStart_time() != null)
                    {
                        employeeExistsInThisDate = true;
                        log.e("info :","employeeExistsInThisDate start_time ......: " + logbook.getStart_time() );

                    }
                }
            }
        }
        return employeeExistsInThisDate;
    }

    public boolean ifExistEndTimeInCheckInLog(String placeName, String employeeName) {

        boolean employeeExistsInThisDate = false;

        ArrayList<CheckInLog> logbooksToCheck = Storage.getInstance().getMyLogbooks();

        for (CheckInLog logbook : logbooksToCheck) {
            if (logbook.getPlaceName() != null && logbook.getPlaceName().equals(placeName)) {
                if (logbook.getEmployeeName().equals(employeeName)) {
                    if(logbook.getEnd_time() != null){
                        employeeExistsInThisDate = true;
                        log.e("info :","employeeExistsInThisDate end_time ......: " + logbook.getEnd_time() );

                    }
                }
            }
        }
        return employeeExistsInThisDate;
    }




    // ---------- Listener for Internet Connection --------
    public void setConnectivityListener(InternetConnectionListener listener) {
        InternetConnectionReceiver.internetConnectionListener = listener;
    }



    // ------------ Internet Connection ---------

    // return TRUE if Connected
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    // get place name with given placeName
    public Place getPlace(String placeName){

        Place place = null;

        for(Place  p:Storage.getInstance().getPlaces()){
            if(p.getName().equals(placeName)){
                place = p;
            }
        }
        Log.i("info :", "place ..... " + place );
        Log.i("info :", "place langtitude..... " + place.getLatitude() );

        return place;
    }

    public ArrayList<Place> getPlaceForEmployee(String employeeName){


        ArrayList<Place> userPlacesTemp = new ArrayList<Place>();

        for(Place  p:Storage.getInstance().getPlaces()){
            for (String key : p.getUsers().keySet()){
                if(key.equals(employeeName)){
                    userPlacesTemp.add(p);
                }
            }
        }

        return userPlacesTemp;
    }


    // take product name by given EAN number
    public String getProductName(String ean) {


        log.e("info Service :", "products: " + Storage.getInstance().getProducts());

        for (Product p : Storage.getInstance().getProducts()) {

            if (ean.equals(p.getEAN())) {

                productName = p.getName();

            }
        }

        return productName;
    }

    // take product name by given EAN number
    public boolean barcodeExist(String barcode) {

        boolean exist = false;

        for (Product p : Storage.getInstance().getProducts()) {

            if (barcode.equals(p.getEAN())) {

                exist = true;

            }
        }

        return exist;
    }


    // --------- Product ActivityList ----------------


    //  go through in all items retrieved from the database and compare based on Place name, and return Map with all products pre particular Place
    public Map<String,Object> selectedItems(String employeePlaceName) {

        //  HashMap to prevent duplicate products, Storage.getInstance().getProductsByDate() - It contains duplicate items
        Map<String,Object> result = new HashMap<>();

        for(OrderList o : Storage.getInstance().getProductsByDate()){

            if(employeePlaceName.equals(o.getPlaceName())){

                result.put(o.getProduct(),employeePlaceName);

            }
        }

        return result;
    }




    /**
     * Method for sending SMS to an employee
     */
    public void sendSMSMessage(String employeePlaceName,String phoneNumber,Context context) {

//        Toast.makeText(CheckInActivity.this, "Selected items" + selectedItems() , Toast.LENGTH_SHORT).show();
//        Toast.makeText(CheckInActivity.this, "Number" + mobileNumber , Toast.LENGTH_SHORT).show();

        String sendTo = phoneNumber;
        String myMessage = "Hi " + employeeName + " you have ordered some Products for " + employeePlaceName + ": " + Service.getInstance().selectedItems(employeePlaceName) ;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sendTo, null, myMessage, null, null);
            Toast.makeText(context, "SMS sent.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "SMS failed, please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    // --------- Product ActivityOrderedList ----------------


    public ArrayList<Product> fetchOrderdProductsFromStorage(Context context,String employeePlaceName,String employeeName){

        ArrayList<Product> products = new ArrayList<Product>();

        if(Storage.getInstance().getOrderedProductHset().isEmpty()){
            Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
        }

        for(OrderList o : Storage.getInstance().getOrderedProductHset()){

            Product product;
            product = new Product(o.getProduct(),employeePlaceName,employeeName);
            products.add(product);

        }

        return products;
    }


    // ------ ASYNC TASK EMPLOYEE Activity LIST -----



    public void getDataFromFirebaseEmployeeCallBack(OnCompleteTaskListener callBack, Context context){

        dialog = new ProgressDialog(context);
        new GetEmployeeAsyncTask(callBack,dialog).execute();

    }

    public void getDataFromFirebaseEmployee(Context context){

        dialog = new ProgressDialog(context);
        new GetEmployeeAsyncTask(dialog).execute();

    }

    // ------ ASYNC TASK MANAGER Activity LIST -----



    public void getDataFromFirebaseManagerCallBack(OnCompleteTaskListener callBack, Context context){

        dialog = new ProgressDialog(context);
        new GetManagerAsyncTask(callBack,dialog).execute();

    }

    public void getDataFromFirebaseManager(Context context){

        dialog = new ProgressDialog(context);
        new GetManagerAsyncTask(dialog).execute();

    }



    // ------ ASYNC TASK PRODUCT Activity LIST -----



    public void getDataFromFirebaseProductCallBack(String employeePlaceName, OnCompleteTaskListener callBack, Context context){

        dialog = new ProgressDialog(context);
        new GetProductAsyncTask(employeePlaceName,callBack,dialog).execute();

    }

    public void getDataFromFirebaseProduct(String employeePlaceName, Context context){

        dialog = new ProgressDialog(context);
        new GetProductAsyncTask(employeePlaceName,dialog).execute();

    }

    // ------ ASYNC TASK REPORT Activity LIST -----



    public void getDataFromFirebaseReportCallBack(OnCompleteTaskListener callBack, Context context){

        dialog = new ProgressDialog(context);
        new GetReportAsyncTask(dialog,callBack).execute();

    }

    public void getDataFromFirebaseReport(Context context){

        dialog = new ProgressDialog(context);
        new GetReportAsyncTask(dialog).execute();

    }



    // ------ ASYNC TASK LOCATION   , AddPlaceActivity & EditWorkPlace -----

    public void getLocationAsyncTask(String address, GetLocationAsyncTaskListener callBack, Context context){

        new GetLocationAsyncTask(address, callBack,context).execute();

    }



    // ------ ASYNC TASK PLACE-----

    public void getPlacesAsyncTaskCallBack( OnCompleteTaskListener callBack, Context context){

        dialog = new ProgressDialog(context);
        new GetPlacesAsyncTask(callBack,dialog).execute();

    }

    //public void getPlacesAsyncTaskOnChangeCallBack( OnChangeTaskListener changeCallback, Context context){
    //
    //    dialog = new ProgressDialog(context);
    //    new GetPlacesAsyncTask(changeCallback,dialog).execute();
    //
    //}

    public void getPlacesAsyncTask(Context context){

        dialog = new ProgressDialog(context);
        new GetPlacesAsyncTask(dialog).execute();

    }

    // ------ ASYNC TASK PLACE-----

    public void getorderListAsyncTaskCallBack(OnCompleteTaskListener callBack, Context context){

        dialog = new ProgressDialog(context);
        new GetOrderListAsyncTask(callBack,dialog).execute();

    }


    // ----- ALERT DIALOG LOGOUT -----
    public void alertDialogLogOut(Context context, AlertDialogCompleteListener callback){

        AlertDialog alert =new AlertDialog(context,callback);
        alert.logOutAlert();
    }


    // --- Call method -----
    public void dialContactPhone(final String phoneNumber, Context context) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }




    // Return true if clean TODAY
    public boolean ifCleanedToday(String date){

        Boolean clean = false;
        String date2 = date.substring(0, date.length() - 6);
        // Time Service
        timeDataService = new TimeDateService();


        if(date != null){

            if(date.equals(timeDataService.getDate()))
                clean = true;

            if(date2.equals(timeDataService.getDate()))
                clean = true;

        }


        return clean;
    }

    public void notifyPlaceNotCleaned(Context context){


        // Time Service
        timeDataService = new TimeDateService();
        int i = 1;
        long day, diff = 0;
        int hour;
        String outputPattern = "HH:mm";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern.trim());
        Calendar c = Calendar.getInstance();
        String dateCurrent = outputFormat.format(c.getTime());

        log.e("info Service :", "notyfi this places: " + Storage.getInstance().getPlaces());

        for(Place p:Storage.getInstance().getPlaces()){

            if(!p.getRecentlyCleaned().equals(timeDataService.getDate())){

                try {
                    Date date1 = outputFormat.parse(p.getRequiredCleaningUntil());
                    Date date2 = outputFormat.parse(dateCurrent);
                    diff = date1.getTime() - date2.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                    day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    hour = (int) TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);

                    // notification if sent few hours until the place must be cleaned
                    if (hour < Integer.parseInt(p.getCleaningTime())){
                        log.e("info Service :", "notyfi hour: " +    hour);
                        log.e("info Service :", "place: " + p.getName() );
                        log.e("info Service :", "STATE: " + p.getNotificationSent() );

                        if( p.getNotificationSent() == null || p.getNotificationSent().equals("false")){

                            refWorkplaces = firebaseDatabase.getReference("workplaces");

                            //NOTIFICATION
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                            // Set the notification to auto-cancel. This means that the notification will disappear
                            // after the user taps it, rather than remaining until it's explicitly dismissed.
                            builder.setAutoCancel(true);
                            builder.setSmallIcon(R.drawable.message);
                            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                            builder.setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                            builder.setLights(Color.RED, 0, 1);
                            builder.setContentTitle("The "+p.getName()+" not cleaned");
                            builder.setContentText("Nobody clean this place yet.");
                            builder.setSubText("The place must be cleaned until " + p.getRequiredCleaningUntil());

                            //Send the notification. This will immediately display the notification icon in the notification bar.
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                            notificationManager.notify(i++, builder.build());

                            HashMap<String,Object> notificationUpdate = new HashMap<String, Object>();
                            notificationUpdate.put("notificationSent","true");
                            refWorkplaces.child(p.getName()).updateChildren(notificationUpdate);
                        }

                    }

            }

        }
    }

    public void ifCleanedPlaces(){


        String cleanBefore;

        for(Place p:Storage.getInstance().getPlaces()){

            cleanBefore = timeDataService.getDate() +" "+p.getRequiredCleaningUntil();
            log.e("info Service :", "cleanBefore: " +    getDateAsTime(cleanBefore));



        }
    }


    public String existEmployeeProfileImage(String userName, String email){

         String imageUUID = null;

        if (Validator.getInstance().check_Whether_Manager(email)){

            for(Manager u:Storage.getInstance().getManagers()){
                if(u.getUsername().equals(userName)){
                    if(u.getImage() != null){
                        imageUUID = u.getImage().getId();
                        //log.e("info Service :", "image Value: " + u.getImage().getId() );
                    }
                }
            }


        }else{

            for(User u:Storage.getInstance().getEmployees()){
                if(u.getUsername().equals(userName)){
                    if(u.getImage() != null){
                        imageUUID = u.getImage().getId();
                        //log.e("info Service :", "image Value: " + u.getImage().getId() );
                    }
                }
            }

        }


           return imageUUID;
    }



    private String getHours(String datePrev,String placeHour) {


// Format for input
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
// Parsing the date
        DateTime firebaseDate = dtf.parseDateTime(datePrev);
        DateTime currentDate = dtf.parseDateTime(timeDataService.getCurrentDateWithTime());

        Period p = new Period(currentDate, firebaseDate);
        int hours = p.getHours();
        int minutes = p.getMinutes();

        log.e("info Service :", "hours: " + hours);
        log.e("info Service :", "minutes: " + minutes);



        String daysAsTime = "";

        return daysAsTime;
    }


    private String getDateAsTime(String datePrev) {


        String daysAsTime = "";
        long day = 0, diff = 0;
        String outputPattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Calendar c = Calendar.getInstance();
        String dateCurrent = outputFormat.format(c.getTime());
        try {
            Date date1 = outputFormat.parse(datePrev);
            Date date2 = outputFormat.parse(dateCurrent);
            diff = date1.getTime() - date2.getTime();
            day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (day == 0) {
            long hour = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
            if (hour == 0)
                daysAsTime = String.valueOf(TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)).concat(" minutes ago");
            else
                daysAsTime = String.valueOf(hour).concat(" hours ago");
        } else {
            daysAsTime = String.valueOf(day).concat(" days ago");
        }
        return daysAsTime;
    }


    public byte[] compressImage(Bitmap bitmap){

        byte[] compressedImage = new byte[0];

        ByteArrayOutputStream outputStrem = new ByteArrayOutputStream();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
        resized.compress(Bitmap.CompressFormat.JPEG, 80, outputStrem);
        compressedImage = outputStrem.toByteArray();

        return compressedImage;
    }


    public ArrayList<Report> getReportByPlace(Map<String, Object> places){
        ArrayList<Report> reports = new ArrayList<Report>();

        for(Report r:Storage.getInstance().getReports()){
            for(String key:places.keySet()){
                if(r.getPlace().equals(key)){
                    reports.add(r);
                }

            }
        }
        return reports;
    }



}
