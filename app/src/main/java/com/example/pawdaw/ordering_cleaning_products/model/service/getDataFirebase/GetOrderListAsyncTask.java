package com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.MissingProductsActivity;
import com.example.pawdaw.ordering_cleaning_products.model.adapter.MissingProductsAdapter;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.OrderList;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pawdaw on 10/06/17.
 */

public class GetOrderListAsyncTask  extends AsyncTask<Void, Void, DatabaseReference> {

    private ProgressDialog pDialog;
    private OnCompleteTaskListener callback;
    private String date,placeName,productName;
    private OrderList orderedProduct;


    // ---------   Firebase  -------------
    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    public GetOrderListAsyncTask(OnCompleteTaskListener callback, ProgressDialog pDialog ) {
        this.pDialog = pDialog;
        this.callback = callback;
    }

    public GetOrderListAsyncTask( ProgressDialog pDialog ) {
        this.pDialog = pDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected DatabaseReference doInBackground(Void... args) {

        // Create main reference - parent
        ref = firebaseDatabase.getReference().child("orderList");

        // get more detail from workplace tree by name of place
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Storage.getInstance().orderLists.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    // GET data snapshot to Object Class Place
                    date = (String) d.getKey();

                    for (DataSnapshot placeSnapshot : d.getChildren()) {  //  loop   /orderList/Sabro ... -  go through in all items

                        placeName = (String) placeSnapshot.getKey();

                        for (DataSnapshot productSnapshot : placeSnapshot.child("products").getChildren()) { // loop  /orderList/Sabro/products ... -  go through in all items products

                            productName = (String) productSnapshot.getKey();

                            // Add new product to ARRAY
                            orderedProduct = new OrderList(placeName, productName, date);


                            // SAVE data to STORAGE
                            Storage.getInstance().addOrderLists(orderedProduct);

                            // send data to another Activity
                            if (callback != null){
                                callback.onTaskCompleteAsyncTask();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("info Order :","crash : " +databaseError );

            }
        });

        return ref;
    }

    @Override
    protected void onPostExecute(DatabaseReference ref) {


        pDialog.dismiss();




    }
}