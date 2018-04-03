package com.example.pawdaw.ordering_cleaning_products.model.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pawdaw on 05/06/17.
 */

public class SpinnerAdapterUser extends ArrayAdapter<User> {
    private Context context;
    private ArrayList<User> users;
    private int resourceId;



    //  Constructor
    public SpinnerAdapterUser(Context c, int resourceId, int textViewName, ArrayList<User> users) {
        super(c, resourceId,textViewName, users);
        this.context = c;
        this.users = users;
        this.resourceId = resourceId;
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
        final User user = getItem(position);



        if (row == null) {

            // Refresh List View
            refreshViewList();

            row = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            holder = new Holder();
            holder.name = (TextView) row.findViewById(R.id.userNameTitle);
            holder.image_circle = (ImageView) row.findViewById(R.id.image_circle);




            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }

        holder.name.setText(user.getUsername());

        if(user.getImage() != null ){
            Picasso.with(context).load(Uri.parse(user.getImage().getUrl())).into(holder.image_circle);

        }

        // Return the completed view to render on screen
        return row;

    }



    public class Holder {
        TextView name;
        ImageView image_circle;

    }
}