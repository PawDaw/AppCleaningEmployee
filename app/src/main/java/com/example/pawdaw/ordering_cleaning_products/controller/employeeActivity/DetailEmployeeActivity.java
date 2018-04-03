package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.adapter.PlaceAdapterCustom;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pawdaw on 12/06/17.
 */

public class DetailEmployeeActivity extends AppCompatActivity {

    private ArrayList<Place> places = new ArrayList<Place>();
    private PlaceAdapterCustom placeAdapter;
    private TextView nameTitle,email,textAddress,phoneValue,secretView,passTextView;
    private ListView employeeList;
    private ImageView image_circle;

    private Place place;
    private User userExtras;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_user_edit);

        nameTitle = (TextView) findViewById(R.id.nameTitle);
        email = (TextView) findViewById(R.id.whoValue);
        phoneValue = (TextView) findViewById(R.id.whereValue);
        image_circle = (ImageView) findViewById(R.id.image_circle);

        // set color for ImageView ICON
        //image_circle.setColorFilter(this.getResources().getColor(R.color.saveButtonColor));


        // Getting extras from the Admin WorkplaceActivityEmployee
        userExtras = (User) getIntent().getSerializableExtra("user");


        nameTitle.setText(userExtras.getUsername());
        email.setText(userExtras.getEmail());
        phoneValue.setText(userExtras.getMobileNumber());

        if(userExtras.getImage() != null ){
            Picasso.with(this).load(Uri.parse(userExtras.getImage().getUrl())).into(image_circle);

        }

        if(userExtras.getPlaces()!= null && userExtras.getPlaces().size()>0){
            for (String key :userExtras.getPlaces().keySet()){
                for(Place p : Storage.getInstance().getPlaces()){
                    if(p.getName().equals(key)){
                        places.add(p);

                    }
                }

            }
        }

        // list View + Adapter
        employeeList = (ListView) findViewById(R.id.placesList);
        placeAdapter = new PlaceAdapterCustom(this, R.layout.row, places);
        employeeList.setAdapter(placeAdapter);
        placeAdapter.notifyDataSetChanged();


        // 1/3 BACK BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    // 2/3 BACK BUTTON
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // --- On Click Image, position captured from Adapter ( workPlaceAdapter)

    public void key_On_Click_image(View view) {

        int itemPosition = (int) view.getTag();

        place = places.get(itemPosition);

        // Put values to extras
        Intent passwordDetail = new Intent(DetailEmployeeActivity.this, PasswordDetailWorkPlaceActivity.class);
        passwordDetail.putExtra("place",place);
        startActivity(passwordDetail);

    }

    public void house_On_Click_image(View view) {

        int itemPosition = (int) view.getTag();

        place = places.get(itemPosition);

        // Put values to extras
        Intent mapPlace = new Intent(DetailEmployeeActivity.this, MapPlaceDetailActivity.class);
        mapPlace.putExtra("place", place);
        startActivity(mapPlace);

    }

    public void On_Click_Edit_Button_Employee(View view) {

        Service.getInstance().edit_User(userExtras,this);
    }

    public void on_click_circle_image(View view) {

        Intent takePicture = new Intent(getApplicationContext(), TakePictureActivity.class);
        takePicture.putExtra("user",userExtras);
        startActivity(takePicture);

    }

}

