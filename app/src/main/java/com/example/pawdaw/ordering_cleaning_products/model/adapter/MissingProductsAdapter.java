package com.example.pawdaw.ordering_cleaning_products.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.OrderList;
import com.example.pawdaw.ordering_cleaning_products.R;

import java.util.ArrayList;

/**
 * Created by pawdaw on 17/11/16.
 */
public class MissingProductsAdapter extends ArrayAdapter<OrderList> {

    private Context context;
    private ArrayList<OrderList> orderedProducts;


    //  Constructor
    public MissingProductsAdapter(Context c, int resource, ArrayList<OrderList> orderedProducts) {
        super(c, resource, orderedProducts);
        this.context = c;
        this.orderedProducts = orderedProducts;
    }

    // Method to reload ViewList
    public void refreshViewList() {

        this.notifyDataSetChanged();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        final OrderList ordereList = getItem(position);

        refreshViewList();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_date_product, parent, false);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.product = (TextView) convertView.findViewById(R.id.product);



            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(ordereList.getPlaceName());
        holder.date.setText(ordereList.getDate());
        holder.product.setText(ordereList.getProduct());

        // Return the completed view to render on screen
        return convertView;

    }

    public class Holder {
        TextView name;
        TextView date;
        TextView product;

    }
}