package com.example.pawdaw.ordering_cleaning_products.model.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Report;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pawdaw on 15/07/17.
 */

public class ReportAdapterCustom extends ArrayAdapter<Report> {

    private Context context;
    private ArrayList<Report> reports;
    private int resourceId;

    public ReportAdapterCustom(Context c, int resourceId, ArrayList<Report> reports) {
        super(c, resourceId,reports);
        this.context = c;
        this.reports = reports;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // View of Holder lookup cache stored in tag
        Holder holder = null;

        // Get the data item for this position
        final Report report = getItem(position);

        if (row == null) {

            refreshViewList();

            holder = new Holder();
            row = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            holder.nameTitle = (TextView) row.findViewById(R.id.nameTitle);
            holder.whoValue = (TextView) row.findViewById(R.id.whoValue);
            holder.dateValue = (TextView) row.findViewById(R.id.dateValue);
            holder.image_circle = (ImageView) row.findViewById(R.id.image_circle);
            holder.ratingBar = (RatingBar) row.findViewById(R.id.ratingBar);

            // set color for ImageView ICON
            //holder.image_circle.setColorFilter(context.getResources().getColor(R.color.saveButtonColor));

            if(report.getImage() != null ){
                Picasso.with(context).load(Uri.parse(report.getImage().getUrl())).into(holder.image_circle);
            }

            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }

        // Populate the data into the template view using the data object
        holder.nameTitle.setText(report.getPlace());
        holder.whoValue.setText(report.getOwner());
        holder.dateValue.setText(report.getTimestamp());
        holder.ratingBar.setRating(Float.parseFloat(report.getRating()));

        if(Service.getInstance().ifCleanedToday(report.getTimestamp())){
            holder.dateValue.setTextColor(ContextCompat.getColor(context,R.color.row_today_color));
            holder.dateValue.setTextSize(12);
            holder.dateValue.setText("Today");
        }


        // Return the completed view to render on screen
        return row;
    }

    // Method to reload ViewList
    public void refreshViewList() {

        this.notifyDataSetChanged();
    }



    public class Holder {
        TextView nameTitle,whoValue,dateValue;
        RatingBar ratingBar;
        ImageView image_circle;
    }
}
