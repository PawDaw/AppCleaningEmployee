package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import com.example.pawdaw.ordering_cleaning_products.model.adapter.Product_Ordered_list_Adapter;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.example.pawdaw.ordering_cleaning_products.model.storage.Storage;

import java.util.ArrayList;

/**
 * Created by pawdaw on 20/02/17.
 */

public class ProductOrderedlist extends AppCompatActivity {

    private ListView lv;
    private Product_Ordered_list_Adapter productAdapter;
    private ArrayList<Product> products = new ArrayList<Product>();
    private String employeeName, employeePlaceName,mobileNumber = "+4531433558";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        // data from another activity
        employeeName = getIntent().getStringExtra("employeeName");
        employeePlaceName = getIntent().getStringExtra("employeePlaceName");

        products = Service.getInstance().fetchOrderdProductsFromStorage(this,employeePlaceName,employeeName);

        lv = (ListView) findViewById(R.id.productList);
        productAdapter = new Product_Ordered_list_Adapter(this, products);
        lv.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.solutions_menu, menu);
        return true;
    }


    //    -------  MENU BUTTON ----------
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // required for checboxs in the list view in adapter
        Product.getInstance().clearHasmMap();
        Storage.getInstance().productsByDate.clear();
        Service.getInstance().getOrderedProductsCurrentDate();
        productAdapter.notifyDataSetChanged();


        // ---- ALERT ---- SMS ,  -----
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("SMS notification");
        alert.setMessage("Do you want a receipt with ordered products by SMS ?");

        // Button positive "YES" - solution deleted
        alert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        Service.getInstance().sendSMSMessage(employeeName,mobileNumber,getApplicationContext());

                        Intent toListOfWorkplaces = new Intent(ProductOrderedlist.this, CheckInActivity.class);
                        toListOfWorkplaces.putExtra("employeeName", employeeName);
                        toListOfWorkplaces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(toListOfWorkplaces);

                        Toast.makeText(ProductOrderedlist.this, "Items are saved"  , Toast.LENGTH_SHORT).show();
                        Toast.makeText(ProductOrderedlist.this, "An SMS is sent to your mobile phone"  , Toast.LENGTH_SHORT).show();
                        productAdapter.notifyDataSetChanged();

                    }

                });

        // Button negative "NO" - cancel; coming back to the list
        alert.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        alert.show();


        return super.onOptionsItemSelected(item);
    }


//  -----  BACK Button Pressed ----

    @Override
    public void onBackPressed() {


        // required for checkboxes in the list view in adapter
        Product.getInstance().clearHasmMap();
        productAdapter.notifyDataSetChanged();

        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
