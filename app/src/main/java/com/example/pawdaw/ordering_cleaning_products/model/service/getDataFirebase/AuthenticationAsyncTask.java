package com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pawdaw on 17/06/17.
 */

public class AuthenticationAsyncTask extends AsyncTask<Void, Void, FirebaseAuth> {


    private ProgressDialog pDialog;
    private AuthenticationListener callback;
    private String email,password;


    // ---------   Firebase  -------------
    // Firebase properties
    public FirebaseAuth authentication;


    public AuthenticationAsyncTask(AuthenticationListener callback, ProgressDialog pDialog,String email, String password ) {
        this.pDialog = pDialog;
        this.callback = callback;
        this.email = email;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected FirebaseAuth doInBackground(Void... args) {

        //Get Firebase authentication instance
        authentication = FirebaseAuth.getInstance();

        return authentication;
    }

    @Override
    protected void onPostExecute(FirebaseAuth authentication) {

        //authenticate user
        authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    // there was an error
                    pDialog.dismiss();

                        task.getException().getMessage();
                        Log.e("info Authentication :", task.getException().getMessage());


                    // send data to another Activity
                    if (callback != null){
                        callback.loginFail();
                    }
                }else{
                    // OK !!!
                    pDialog.dismiss();

                    // send data to another Activity
                    if (callback != null) {
                        callback.loginSuccessful();
                    }
                }

                }



        });



    }
}