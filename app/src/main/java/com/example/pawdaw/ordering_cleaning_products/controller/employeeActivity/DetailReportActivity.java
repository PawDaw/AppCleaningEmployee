package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Report;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pawdaw on 16/07/17.
 */

public class DetailReportActivity extends AppCompatActivity{


    private RatingBar ratingBar;
    private TextView placeName,who,pictureAreaTextView,messageTextView,ratingValue;
    private ImageView imageView;
    private Report reportExtras;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_report_employee);

        imageView = (ImageView) findViewById(R.id.imageView);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingValue = (TextView) findViewById(R.id.ratingValue);
        placeName = (TextView) findViewById(R.id.nameTitle);
        who = (TextView) findViewById(R.id.whoValue);
        pictureAreaTextView = (TextView) findViewById(R.id.whereValue);
        messageTextView = (TextView) findViewById(R.id.messageValue);



        // set color for ImageView ICON
        //image_circle.setColorFilter(this.getResources().getColor(R.color.saveButtonColor));


        // Getting extras from the Admin WorkplaceActivityEmployee
        reportExtras = (Report) getIntent().getSerializableExtra("report");

        ratingBar.setRating(Float.parseFloat(reportExtras.getRating()));
        ratingValue.setText(reportExtras.getRating());
        placeName.setText(reportExtras.getPlace());
        who.setText(reportExtras.getOwner());
        pictureAreaTextView.setText(reportExtras.getWhere());
        messageTextView.setText(reportExtras.getMessage());

        if (reportExtras.getImage() != null) {
            Picasso.with(this).load(reportExtras.getImage().getUrl()).into(imageView);
        }
    }

    public void On_Click_Remove_Button_Employee(View view) {


    }

    public void On_CLick_Image_Full_Screen(View view) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reportExtras.getImage().getUrl())));

    }
}
