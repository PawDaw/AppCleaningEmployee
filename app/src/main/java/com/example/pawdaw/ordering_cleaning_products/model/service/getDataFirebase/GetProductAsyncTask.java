package com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity.ProductActivityList;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pawdaw on 21/05/17.
 */

public class GetProductAsyncTask extends AsyncTask<Void, Void, DatabaseReference> {

    private ProgressDialog pDialog;
    private OnCompleteTaskListener callback;
    private String employeePlaceName;
    public Product product;


    // ---------   Firebase  -------------
    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // Create main reference - parent
    private DatabaseReference ref;



    public GetProductAsyncTask(String employeePlaceName, OnCompleteTaskListener callback, ProgressDialog pDialog) {
        this.pDialog = pDialog;
        this.callback = callback;
        this.employeePlaceName = employeePlaceName;
    }

    public GetProductAsyncTask(String employeePlaceName, ProgressDialog pDialog) {
        this.pDialog = pDialog;
        this.employeePlaceName = employeePlaceName;

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
        ref = firebaseDatabase.getReference().child("products");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Storage.getInstance().products.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // GET data snapshot to Object Class
                    product = ds.getValue(Product.class);
                    // SAVE data to STORAGE
                    product.setPlace(employeePlaceName);
                    Storage.getInstance().addProduct(product);


                    // send data to another Activity
                    if (callback != null){
                        callback.onTaskCompleteAsyncTask();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return ref;
    }

    @Override
    protected void onPostExecute(DatabaseReference ref) {

        pDialog.dismiss();



    }
}
