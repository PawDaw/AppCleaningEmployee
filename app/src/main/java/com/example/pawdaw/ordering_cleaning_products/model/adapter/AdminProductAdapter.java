package com.example.pawdaw.ordering_cleaning_products.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;
import com.example.pawdaw.ordering_cleaning_products.R;

import java.util.ArrayList;

/**
 * Created by pawdaw on 15/11/16.
 */
public class AdminProductAdapter extends ArrayAdapter<Product> {

    Context context;
    private ArrayList<Product> products;

    public AdminProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
        this.products = products;
        this.context = context;
    }

    // method to reload ViewList
    public void refreshViewList() {

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;

        // view lookup cache stored in tag
        ViewHolder holder;

        // Get the data item for this position
        final Product product = getItem(position);

        if (row == null) {

            // Refresh List View
            refreshViewList();

            holder = new ViewHolder();

            row = LayoutInflater.from(getContext()).inflate(R.layout.row_product, parent, false);

            holder.name = (TextView) row.findViewById(R.id.textName);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        // Populate the data into the template view using the data object
        holder.name.setText(product.getName());

        // Return the completed view to render on screen
        return row;
    }


    public class ViewHolder {
        TextView name;
    }
}
