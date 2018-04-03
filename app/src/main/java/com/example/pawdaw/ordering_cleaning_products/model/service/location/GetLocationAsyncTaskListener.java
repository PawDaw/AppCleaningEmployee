package com.example.pawdaw.ordering_cleaning_products.model.service.location;

/**
 * Created by pawdaw on 20/05/17.
 */

public interface GetLocationAsyncTaskListener {

    void onTaskCompleteGetLocation(String addressName, String Latitude,String Longitude);

}
