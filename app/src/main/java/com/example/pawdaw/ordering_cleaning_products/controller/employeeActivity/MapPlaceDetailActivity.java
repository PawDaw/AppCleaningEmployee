package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

/**
 * Created by pawdaw on 20/05/17.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPlaceDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView name, address, email;
    private double  latitude, longitude;
    private String placeAddress;

    public GoogleMap mMap;
    public LatLng placeLocation;
    private Place placeExtras;
    private RelativeLayout relativeLayout;
    private Animation slideUpAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_detail_activity);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        //Set up TEXT VIEW
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.textAddress);
        email = (TextView) findViewById(R.id.whoValue);


        // Get the references of views, rquires for animation
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        // Animation slide
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down_animation);
        relativeLayout.startAnimation(slideUpAnimation);

        // capture data from another activity
        placeExtras = (Place) getIntent().getSerializableExtra("place");

        name.setText(placeExtras.getName());
        address.setText(placeExtras.getAddress());
        email.setText(placeExtras.getEmail());

        placeAddress = placeExtras.getAddress();
        latitude = Double.parseDouble(placeExtras.getLatitude());
        longitude = Double.parseDouble(placeExtras.getLongitude());

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        placeLocation = new LatLng(latitude,longitude);

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(placeAddress));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));

        marker.showInfoWindow();
    }

    public void normal_on_click_button(View view) {

        // Other supported types include:
        // MAP_TYPE_NORMAL,MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));

    }

    public void hybrid_on_click_button(View view) {

        // Other supported types include:
        // MAP_TYPE_NORMAL,MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,18));
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void on_click_Phone_Text_View(View view) {

        Service.getInstance().dialContactPhone("31433558",this);
    }
}