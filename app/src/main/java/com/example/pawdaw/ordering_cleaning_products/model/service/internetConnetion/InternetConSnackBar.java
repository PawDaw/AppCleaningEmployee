package com.example.pawdaw.ordering_cleaning_products.model.service.internetConnetion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.example.pawdaw.ordering_cleaning_products.R;

/**
 * Created by pawdaw on 02/04/17.
 */

public class InternetConSnackBar {

    private Activity activity;

    public InternetConSnackBar(Activity activity) {
        this.activity = activity;
    }

    public void isConnected(boolean isConnected) {

        String message;

        if (isConnected) {
            message = activity.getString(R.string.SnackBar_connected);

            Snackbar snackbar_con = Snackbar.make(activity.findViewById(R.id.myCoordinatorLayout), message, Snackbar.LENGTH_LONG);

            View snackbarView = snackbar_con.getView();
            snackbarView.setBackgroundColor(Color.GREEN);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar_con.show();


        } else {
            message = activity.getString(R.string.SnackBar_disconnected);

            Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.myCoordinatorLayout), message, Snackbar.LENGTH_LONG);

            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.RED);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }


    }
}
