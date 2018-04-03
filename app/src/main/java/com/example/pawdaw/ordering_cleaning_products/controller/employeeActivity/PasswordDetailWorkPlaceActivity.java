package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;

/**
 * Created by pawdaw on 07/05/17.
 */

public class PasswordDetailWorkPlaceActivity extends AppCompatActivity {

    private TextView placeName,password, secretWord;
    private Place placeExtras;
    private RelativeLayout relativeLayout;
    private Animation slideUpAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_detail);

        placeName = (TextView) findViewById(R.id.name);
        password = (TextView) findViewById(R.id.passTextView);
        secretWord = (TextView) findViewById(R.id.secretView);

        // Get the references of views, rquires for animation
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        // Animation slide
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down_animation);
        relativeLayout.startAnimation(slideUpAnimation);


        // Getting extras from the Admin WorkplaceActivityEmployee
        placeExtras = (Place) getIntent().getSerializableExtra("place");


        // data from another activity
        placeName.setText(placeExtras.getName());
        password.setText(placeExtras.getPassword());
        secretWord.setText(placeExtras.getSecretWord());

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
