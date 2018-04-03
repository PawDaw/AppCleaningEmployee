package com.example.pawdaw.ordering_cleaning_products.model.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by pawdaw on 26/02/17.
 */

public class DescriptionFragment extends Fragment implements OnMapReadyCallback {


    // Name of the place
    private TextView name, address, email;

    private double  latitude, longitude;
    private String placeAddress;
    public GoogleMap mMap;
    public LatLng placeLocation;


    private static int index = 0;

    public static void setIndex(int index) {

        DescriptionFragment.index = index;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(container == null)
            return  null;

        View view = inflater.inflate(R.layout.map_detail_activity,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set up TEXT VIEW
        name = (TextView) getActivity().findViewById(R.id.name);
        address = (TextView) getActivity().findViewById(R.id.textAddress);
        email = (TextView) getActivity().findViewById(R.id.whoValue);


        if(!Storage.getInstance().getPlaces().isEmpty()){

            if (name != null || address != null || email != null){


                name.setText(Storage.getInstance().getPlaces().get(index).getName());
                address.setText(Storage.getInstance().getPlaces().get(index).getAddress());
                email.setText(Storage.getInstance().getPlaces().get(index).getEmail());

                // Get data form STORAGE
                placeAddress = Storage.getInstance().getPlaces().get(index).getAddress();
                latitude = Double.parseDouble(Storage.getInstance().getPlaces().get(index).getLatitude());
                longitude = Double.parseDouble(Storage.getInstance().getPlaces().get(index).getLongitude());


            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        placeLocation = new LatLng(latitude,longitude);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
}
