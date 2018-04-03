package com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Manager;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pawdaw on 10/06/17.
 */

public class GetManagerAsyncTask extends AsyncTask<Void, Void, DatabaseReference> {


    private ProgressDialog pDialog;
    private OnCompleteTaskListener callback;
    private Manager manager;


    // ---------   Firebase  -------------
    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref,refWorkplaces;

    public GetManagerAsyncTask(OnCompleteTaskListener callback, ProgressDialog pDialog ) {
        this.pDialog = pDialog;
        this.callback = callback;
    }

    public GetManagerAsyncTask(ProgressDialog pDialog ) {
        this.pDialog = pDialog;
        this.callback = callback;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog.setMessage("");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    @Override
    protected DatabaseReference doInBackground(Void... args) {

        // Create main reference - parent
        ref = firebaseDatabase.getReference().child("manager");

        //    addValueEventListener(ValueEventListener listener)
        //    Add a listener for changes in the data at this location, ONLY when the children values changes, ALL values under "workplaces" - onDataChange is called

        // get more detail from manager tree by name of place
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Storage.getInstance().managers.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // GET data snapshot to Object Class
                    manager = ds.getValue(Manager.class);
                    // SAVE data to STORAGE
                    Storage.getInstance().addManager(manager);

                    //pDialog.dismiss();

                    // send data to another Activity
                    if (callback != null){
                        callback.onTaskCompleteAsyncTask();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("info Manager :","crash : " +databaseError );

            }
        });

        return ref;
    }

    @Override
    protected void onPostExecute(DatabaseReference ref) {


        pDialog.dismiss();



    }
}