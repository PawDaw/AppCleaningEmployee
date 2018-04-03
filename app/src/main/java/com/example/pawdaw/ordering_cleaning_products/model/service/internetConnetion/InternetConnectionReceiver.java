package com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by pawdaw on 31/03/17.
 */

public class InternetConnectionReceiver extends BroadcastReceiver  {


    public static InternetConnectionListener internetConnectionListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (internetConnectionListener != null) {
            internetConnectionListener.onNetworkConnectionChanged(isConnected);
        }
    }



}
