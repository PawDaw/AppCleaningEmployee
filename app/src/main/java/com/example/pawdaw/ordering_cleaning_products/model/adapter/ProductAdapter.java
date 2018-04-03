package com.example.pawdaw.ordering_cleaning_products.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.OrderList;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;
import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.pawdaw.ordering_cleaning_products.model.service.TimeDateService;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by pawdaw on 15/11/16.
 */
public class ProductAdapter extends ArrayAdapter<Product> {


    private Context context;
    private ArrayList<Product> products;
    private TimeDateService timeDataService;


    // Firebase
    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // Create main reference - parent
    private DatabaseReference dbRefProduct = firebaseDatabase.getReference("orderList");



    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context,0, products);
        this.products = products;
        this.context = context;
    }

    // method to reload ViewList
    public void refreshViewList() {

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Time Service
        timeDataService = new TimeDateService();

        View row = convertView;

        // view lookup cache stored in tag
        ViewHolder holder;

        // Get the data item for this position
        final Product product = getItem(position);

        if (row == null) {

//           Refresh List View
            refreshViewList();

            holder = new ViewHolder();


            row = LayoutInflater.from(getContext()).inflate(R.layout.row_product_check_box, parent, false);

            holder.name = (TextView) row.findViewById(R.id.name);
            holder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        // Populate the data into the template view using the data object
        holder.name.setText(product.getName());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {


                    //  I will use one instance of MAPHash in the product class, to prevent override products when is more than one
                    product.getInstance().setProduct(products.get(position).getName(), true);

                    Map<String, Object> userValues = product.getInstance().toMap();

                    dbRefProduct.child(timeDataService.getDate()).child(product.getPlace()).setValue(userValues);

                    // toast when a user goes back from the list of products
                    Toast.makeText(getContext(), "Item saved"  , Toast.LENGTH_SHORT).show();

                    //     Toast.makeText(getContext(), "Selected: true", Toast.LENGTH_SHORT).show();
                    //     System.out.println("Selected Are == "+ products.get(i).getName());


                } else {


                    product.getInstance().removeProduct(products.get(position).getName());

                    Map<String, Object> userValues = product.getInstance().toMap();

                    dbRefProduct.child(timeDataService.getDate()).child(product.getPlace()).setValue(userValues);

                    // toast when a user goes back from the list of products
                    Toast.makeText(getContext(), "Item removed"  , Toast.LENGTH_SHORT).show();

                }
            }
        });

        // this part of the code turn on or off the checkbox based on the saved items in database
        if (checkIfExist(product.getName(),product)) {

            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }


        // Return the completed view to render on screen
        return row;
    }


    // Method used above, go through in all items retrieved from database and compare based on Product and Place from current adapter
    public Boolean checkIfExist(String productName,Product product){

        boolean exist = false;


        for(OrderList o : Storage.getInstance().getOrderedProductHset()){

            if(productName.equals(o.getProduct())){

                exist = true;
                product.getInstance().setProduct(productName, true);
                product.getInstance().toMap();

            }
        }

        return exist;
    }



    public class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }
}

