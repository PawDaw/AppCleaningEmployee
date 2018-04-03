package com.example.pawdaw.ordering_cleaning_products.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.AdminActivity;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.ForgetPasswordActivity;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.controller.managerClasses.RegisterValidation;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.AuthenticationListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.getDataFirebase.OnCompleteTaskListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion.InternetConnectionListener;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion.InternetConSnackBar;
import com.example.pawdaw.ordering_cleaning_products.model.service.Validator;
import com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity.WorkplaceActivityEmployee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by pawdaw on 25/10/16.
 **/

public class MainActivity extends AppCompatActivity implements InternetConnectionListener, AuthenticationListener, OnCompleteTaskListener {

    private String email,password;
    public TextView forgotPassword;
    private EditText nameEditText,passwordEditText;
    private InternetConSnackBar internetConSnackBar;

    private LinearLayout linearLayout;
    private Animation slideUpAnimation,shake;

    boolean cancel;
    View focusView;


    // Firebase properties
    private FirebaseAuth authentication;

    // Create instance of Firebase
    private  static FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // IMPORTANT  set Persistence Enabled when Internet connection lost, save all changes and send to the FIREBASE when connection return.
        if(database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }

        // Set Listener for this Activity to notify internet Connection
        Service.getInstance().setConnectivityListener(this);

        // Set this Activity to show SNACK BAR on the button
        internetConSnackBar = new InternetConSnackBar(this);

        Service.getInstance().getPass();

        // Get the references of views
        nameEditText = (EditText) findViewById(R.id.editTextEmailToLogin);
        passwordEditText = (EditText) findViewById(R.id.editTextPasswordToLogin);
        forgotPassword = (TextView) findViewById(R.id.forgotPasswordText);

        // Get the references of views, rquires for animation
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayout);


        // Animation slide
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up_animation);
        linearLayout.startAnimation(slideUpAnimation);


        //Get Firebase authentication instance
        authentication = FirebaseAuth.getInstance();




    }


    // --- LOGIN BUTTON -----
    public void onClick_button_Login(View view) {

        if(Service.getInstance().isNetworkAvailable(this)){
            attemptLogin();
        }else{
            internetConSnackBar.isConnected(false);
        }

    }


    // --- BUTTON Register ---
    public void onClick_button_Register(View view) {

        if(Service.getInstance().isNetworkAvailable(this)){

            Intent toRegisterValidation = new Intent(MainActivity.this, RegisterValidation.class);
            toRegisterValidation.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toRegisterValidation);


        }else{

            internetConSnackBar.isConnected(false);
        }

    }


    // --- BUTTON to Retrieve the PASSWORD ----
    public void onClick_button_ForgotPassword(View view) {

        Intent toForgetPassword = new Intent(MainActivity.this, ForgetPasswordActivity.class);
        toForgetPassword.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toForgetPassword);
    }


    private void attemptLogin() {

        // Reset errors.
        nameEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        email = nameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        cancel = false;
        focusView = null;

        // Check for a valid email.
        if (TextUtils.isEmpty(email)) {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        }else if (!Validator.getInstance().emailValidator(email)) {
            nameEditText.setError(getString(R.string.error_Email_Invalid));
            focusView = nameEditText;
            cancel = true;

        }
        // Check for a valid Password number.
        if(TextUtils.isEmpty(password)){
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        }else if(password.length() < 6){
            passwordEditText.setError(getString(R.string.error_Password_to_short));
            focusView = passwordEditText;
            cancel = true;
        }
        //else if (password.length() > 6){
        //    passwordEditText.setError(getString(R.string.error_Password_to_long));
        //    focusView = passwordEditText;
        //    cancel = true;
        //}

            if (cancel) {

                // Animation shake
                shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                nameEditText.startAnimation(shake);
                passwordEditText.startAnimation(shake);

                // There was an ERROR
                focusView.requestFocus();

            } else {

                // OK , run Authentication Async Task and check the Password
                Validator.getInstance().loginPasswordCheck(this,this,email,password);

            }
    }



    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        internetConSnackBar.isConnected(isConnected);

    }


    @Override
    public void loginSuccessful() {

        // Retrieve all users and managers from the database
        Service.getInstance().getDataFromFirebaseEmployeeCallBack(this,this);
        Service.getInstance().getDataFromFirebaseManagerCallBack(this,this);

    }

    @Override
    public void loginFail() {

        // Animation shake
        shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        nameEditText.startAnimation(shake);
        passwordEditText.startAnimation(shake);
        passwordEditText.setError(getString(R.string.authentication_failed));
        focusView = passwordEditText;


    }

    @Override
    public void onTaskCompleteAsyncTask() {


        //  -------  ADMIN PANEL -------
        // If email from EditText is equal to administrator's email - let him to log in to admin panel
        if (Validator.getInstance().check_Whether_Manager(email)) {

            // no errors, OPEN AdminActivity
            Intent toAdministrator = new Intent(MainActivity.this, AdminActivity.class);
            toAdministrator.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toAdministrator);

        }

        //  -------  EMPLOYEE PANEL -------
        if (Validator.getInstance().ifEmployeeExist(email)) {

            // no errors, OPEN WorkplaceActivityEmployee
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String employeeName = user.getDisplayName();

            Intent toCustomUser = new Intent(MainActivity.this, WorkplaceActivityEmployee.class);
            toCustomUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toCustomUser);

        }
    }
}






