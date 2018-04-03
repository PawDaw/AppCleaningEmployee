package com.example.pawdaw.ordering_cleaning_products.model.service.alertDialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by pawdaw on 01/06/17.
 */

public class AlertDialog {


    private Context context;
    private AlertDialogCompleteListener callback;


    public AlertDialog(Context context, AlertDialogCompleteListener callback) {
        this.context = context;
        this.callback = callback;
    }

    public void logOutAlert(){

        // ---- ALERT ---
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setTitle("LOGOUT");
        alert.setMessage("Do you want Logout?");

        // Button positive "YES" - solution deleted
        alert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (callback != null){
                            callback.onYesButton();
                        }

                    }

                });
        alert.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alert.show();


    }
}
