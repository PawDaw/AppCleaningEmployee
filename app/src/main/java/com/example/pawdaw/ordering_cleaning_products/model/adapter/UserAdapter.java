package com.example.pawdaw.ordering_cleaning_products.model.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by pawdaw on 14/11/16.
 */
public class UserAdapter extends ArrayAdapter<User> {


    private Context context;
    private ArrayList<User> users;
    private int resourceId;

    public UserAdapter(Context c, int resourceId, ArrayList<User> users) {
        super(c, resourceId,users);
        this.context = c;
        this.users = users;
        this.resourceId = resourceId;

    }

    // Method to reload ViewList
    public void refreshViewList() {

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // View of Holder lookup cache stored in tag
        Holder holder = null;

        // Get the data item for this position
        final User user = getItem(position);

        if (row == null) {

            refreshViewList();

            holder = new Holder();
            row = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            holder.userNameTitle = (TextView) row.findViewById(R.id.userNameTitle);
            holder.userEmailValue = (TextView) row.findViewById(R.id.userEmailValue);
            holder.userPhoneValue = (TextView) row.findViewById(R.id.userPhoneValue);
            holder.image_circle = (ImageView) row.findViewById(R.id.image_circle);

            // set color for ImageView ICON
            //holder.image_circle.setColorFilter(context.getResources().getColor(R.color.saveButtonColor));

            if(user.getImage() != null ){
                Picasso.with(context).load(Uri.parse(user.getImage().getUrl())).into(holder.image_circle);
            }

            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }

        // Populate the data into the template view using the data object
        holder.userNameTitle.setText(user.getUsername());
        holder.userEmailValue.setText(user.getEmail());
        holder.userPhoneValue.setText(user.getMobileNumber());
        // Return the completed view to render on screen
        return row;
    }

    public class Holder {
        TextView userNameTitle,userEmailValue,userPhoneValue;
        ImageView image_circle;
    }
}

