package com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Report;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pawdaw on 15/07/17.
 */

public class GetReportAsyncTask extends AsyncTask<Void, Void, DatabaseReference>{

    private ProgressDialog pDialog;
    private OnCompleteTaskListener callback;
    private Report report;

    // ---------   Firebase  -------------
    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // Create main reference - parent
    private DatabaseReference ref;

    public GetReportAsyncTask(ProgressDialog progressDialog, OnCompleteTaskListener callback) {
        this.pDialog = progressDialog;
        this.callback = callback;
    }

    public GetReportAsyncTask(ProgressDialog progressDialog) {
        this.pDialog = progressDialog;
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
        ref = firebaseDatabase.getReference().child("report");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Storage.getInstance().reports.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // GET data snapshot to Object Class
                    report = ds.getValue(Report.class);
                    // SAVE data to STORAGE
                    Storage.getInstance().addReport(report);


                    // send data to another Activity
                    if (callback != null){
                        callback.onTaskCompleteAsyncTask();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("info Report :","crash : " +databaseError );

            }
        });

        return ref;
    }

    @Override
    protected void onPostExecute(DatabaseReference ref) {

        pDialog.dismiss();



    }
}
