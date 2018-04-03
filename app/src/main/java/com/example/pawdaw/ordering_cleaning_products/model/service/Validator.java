package com.example.pawdaw.ordering_cleaning_products.model.service;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.controller.MainActivity;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.AdminActivity;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Manager;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.AuthenticationAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.AuthenticationListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.GetOrderListAsyncTask;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.OnCompleteTaskListener;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;


import java.util.ArrayList;
import java.util.concurrent.Executor;


/**
 * Created by pawdaw on 15/03/17.
 */

public class Validator {


    // --------- Singleton instance  ----------------

    private static Validator instance = new Validator();

    public static Validator getInstance() {

        return instance;
    }

    private Validator() {
        //
    }

    private Log log;
    private ProgressDialog dialog;

    // check if String. True = String, False = numbers or string with numbers
    public boolean emailValidator(String string) {

        return string.matches("^[A-Za-z0-9+_.-]+@(.+)$");

    }

    // check if Email exist in firebase
    public boolean checkAccountEmailExistInFirebase(String email) {
        boolean emailExists = false;

        ArrayList<Manager> managerToCheck = Storage.getInstance().getManagers();

        for (Manager user : managerToCheck) {
            log.e("info :","name: " + user);
            if (user.getEmail() != null && user.getEmail().equals(email)) {
                emailExists = true;
            }
        }

        ArrayList<User> usersToCheck = Storage.getInstance().getEmployees();

        for (User user : usersToCheck) {
            log.e("info :","name: " + user);
            if (user.getEmail() != null && user.getEmail().equals(email)) {
                emailExists = true;
            }
        }


        return emailExists;
    }

    //  check if Manger with given email already exist, If exist return TRUE
    public boolean check_Whether_Manager(String email) {

        boolean emailExists = false;
        Log log = null;


        ArrayList<Manager> usersToCheck = Storage.getInstance().getManagers();

        for (Manager user : usersToCheck) {
            log.e("info :","name: " + user);
            if (user.getEmail() != null && user.getEmail().equals(email)) {
                emailExists = true;
            }
        }
        return emailExists;
    }

    // check if Employee with given email already exist, If exist return TRUE
    public boolean ifEmployeeExist(String email) {

        boolean emailExists = false;
        Log log = null;


        ArrayList<User> usersToCheck = Storage.getInstance().getEmployees();

        for (User user : usersToCheck) {
            log.e("info :","name: " + user);
            if (user.getEmail() != null && user.getEmail().equals(email)) {
                emailExists = true;
            }
        }
        return emailExists;
    }


    public boolean checkAccountEmailExistInFirebaseShort(String email) {
        final boolean[] exist = {false};
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                exist[0] = !task.getResult().getProviders().isEmpty();
                log.e("info :","exist : " + exist[0]);

            }
        });
        log.e("info :","exist 2 : " + exist[0]);
        return exist[0];
    }

    public boolean registerPasswordCheck(String secretPassword) {

        boolean correct = false;


        // get password form firebase, Manager PASSWORD
                if (secretPassword.equals(Storage.getInstance().getPassword())) {

                    correct = true;
                }

        return correct;
    }



    public void loginPasswordCheck(AuthenticationListener callBack, final Context context, String email, String password) {

        dialog = new ProgressDialog(context);
        new AuthenticationAsyncTask(callBack,dialog,email,password).execute();

    }

}
