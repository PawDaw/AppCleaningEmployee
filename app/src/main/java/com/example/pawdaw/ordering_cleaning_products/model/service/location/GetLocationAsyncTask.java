package com.example.pawdaw.ordering_cleaning_products.model.service.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by pawdaw on 20/05/17.
 */

public class GetLocationAsyncTask extends AsyncTask<Void, Void, Address> {

    private String address;
    private Context context;
    private GetLocationAsyncTaskListener callback;
    private String Latitude,Longitude,addressName;

    public GetLocationAsyncTask(String address, GetLocationAsyncTaskListener callBack, Context context) {
        this.address = address;
        this.context = context;
        this.callback = callBack;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Address doInBackground(Void ... none) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            Log.e("info :", context.getString(R.string.errorMessage), e);
            Toast.makeText(context, context.getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();

        }

        if(addresses != null && addresses.size() > 0)
            return addresses.get(0);

        return null;
    }

    protected void onPostExecute(Address address) {

        if(address == null) {
            Toast.makeText(context, context.getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
        }
        else {
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressName += " " + address.getAddressLine(i);
            }
            Latitude = String.valueOf(address.getLatitude());
            Longitude = String.valueOf(address.getLongitude());

            if (callback != null){
                callback.onTaskCompleteGetLocation(addressName,Latitude,Longitude);
            }

        }

    }

}
