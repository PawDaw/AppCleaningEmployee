package com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pawdaw on 28/05/17.
 */

public class GetPlacesAsyncTask extends AsyncTask<Void, Void, DatabaseReference> {

    private ProgressDialog pDialog;
    private OnCompleteTaskListener callback;
    private OnChangeTaskListener changeCallback;
    private Place place;


    // ---------   Firebase  -------------
    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref,refWorkplaces;

    public GetPlacesAsyncTask(OnCompleteTaskListener callback, ProgressDialog pDialog ) {
        this.pDialog = pDialog;
        this.callback = callback;
    }
    //public GetPlacesAsyncTask(OnChangeTaskListener changeCallback, ProgressDialog pDialog ) {
    //    this.pDialog = pDialog;
    //    this.changeCallback = changeCallback;
    //}

    public GetPlacesAsyncTask( ProgressDialog pDialog ) {
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
        ref = firebaseDatabase.getReference().child("workplaces");

        //    addValueEventListener(ValueEventListener listener)
        //    Add a listener for changes in the data at this location, ONLY when the children values changes, ALL values under "workplaces" - onDataChange is called

        // get more detail from workplace tree by name of place
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Storage.getInstance().places.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // GET data snapshot to Object Class Place
                    place = ds.getValue(Place.class);

                    Log.e("info GetWorkPlace :", " firebase Auto update Workplace Under ");

                    // SAVE data to STORAGE
                    Storage.getInstance().addPlace(place);


                    //// notify another Activity,the data changed
                    //if(changeCallback != null){
                    //
                    //    changeCallback.OnChangeTaskListener();
                    //}

                    // send data to another Activity
                    if (callback != null){

                        callback.onTaskCompleteAsyncTask();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("info Place :","crash : " +databaseError );

            }
        });

        return ref;
    }

    @Override
    protected void onPostExecute(DatabaseReference ref) {


        pDialog.dismiss();


                     //    addListenerForSingleValueEvent(ValueEventListener listener)
                    //    Add a listener for a single change in the data at this location. ONLY when the child("workplaces") changes, onDataChange is called


                    // get more detail from workplace tree by name of place
                    //refWorkplaces.child("workplaces").addListenerForSingleValueEvent(new ValueEventListener() {
                    //    @Override
                    //    public void onDataChange(DataSnapshot dataSnapshot) {
                    //
                    //        Storage.getInstance().places.clear();
                    //
                    //        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //
                    //            // GET data snapshot to Object Class Place
                    //            place = ds.getValue(Place.class);
                    //
                    //            //if(!Storage.getInstance().places.isEmpty()){
                    //            //
                    //            //    Storage.getInstance().removePlace(place);
                    //            //}
                    //
                    //            Log.e("info GetWorkPlace :", " firebase Auto update Workplace NAME ");
                    //            pDialog.dismiss();
                    //
                    //            // SAVE data to STORAGE
                    //            Storage.getInstance().addPlace(place);
                    //
                    //            // send data to another Activity
                    //            if (callback != null){
                    //                callback.onTaskCompleteAsyncTask(place);
                    //            }
                    //        }
                    //
                    //
                    //    }
                    //
                    //    @Override
                    //    public void onCancelled(DatabaseError databaseError) {
                    //
                    //    }
                    //});


    }
}