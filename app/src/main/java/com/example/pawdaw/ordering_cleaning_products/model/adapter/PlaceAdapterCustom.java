package com.example.pawdaw.ordering_cleaning_products.model.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;

import java.util.ArrayList;

/**
 * Created by pawdaw on 14/11/16.
 */
public class PlaceAdapterCustom extends ArrayAdapter<Place> {


    private Context context;
    private ArrayList<Place> places;
    private int resource;


    //  Constructor
    public PlaceAdapterCustom(Context c, int resource, ArrayList<Place> places) {
        super(c, resource, places);
        this.context = c;
        this.places = places;
        this.resource = resource;
    }

    // Method to reload ViewList
    public void refreshViewList() {

        this.notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;

        // view lookup cache stored in tag
        Holder holder = null;

        // Get the data item for this position
        final Place place = getItem(position);



        if (row == null) {

            // Refresh List View
            refreshViewList();

            row = LayoutInflater.from(getContext()).inflate(resource, parent, false);

            holder = new Holder();
            holder.name = (TextView) row.findViewById(R.id.placeName);
            holder.cleaningTime = (TextView) row.findViewById(R.id.cleaningTimeValue);
            holder.requiredCleaningUntil = (TextView) row.findViewById(R.id.requiredCleaningUntilValue);
            holder.recentlyCleaned = (TextView) row.findViewById(R.id.recentlyCleanedValue);


            // image KEY , helper send position to the View (WorkPlaceActivity) and on the key_On_Click_image method we capture the position
            ImageView keyImageview =(ImageView) row.findViewById(R.id.keyImage);
            keyImageview.setTag(new Integer(position));

            // image KEY , helper send position to the View (WorkPlaceActivity) and on the house_On_Click_image method we capture the position
            ImageView houseImageview =(ImageView) row.findViewById(R.id.locationImage);
            houseImageview.setTag(new Integer(position));




            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }


            holder.name.setText(place.getName());
            holder.cleaningTime.setText(place.getCleaningTime());
            holder.requiredCleaningUntil.setText(place.getRequiredCleaningUntil());


            if(Service.getInstance().ifCleanedToday(place.getRecentlyCleaned())){
                holder.recentlyCleaned.setTextColor(ContextCompat.getColor(context,R.color.row_today_color));
                holder.recentlyCleaned.setTextSize(12);
                holder.recentlyCleaned.setText("Today");
            }else{
                holder.recentlyCleaned.setTextColor(ContextCompat.getColor(context,R.color.row_date_color));
                holder.recentlyCleaned.setText(place.getRecentlyCleaned());

            }



        // Return the completed view to render on screen
        return row;

    }



    public class Holder {
        TextView name,cleaningTime,requiredCleaningUntil,recentlyCleaned;
    }
}
