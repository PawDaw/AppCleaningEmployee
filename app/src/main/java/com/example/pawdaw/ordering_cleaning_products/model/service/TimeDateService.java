package com.example.pawdaw.ordering_cleaning_products.model.service;

import android.provider.ContactsContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pawdaw on 02/04/17.
 */

public class TimeDateService {


    private Calendar cal;
    private String date;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private String startTime;
    private String endTime;
    private java.util.Date currentTime;

    public TimeDateService() {

        // Calendar
        cal = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
    }

    public String getDate() {

        date = dateFormat.format(cal.getTime());
        return date;
    }


    public String getStartTime() {

        startTime = timeFormat.format(cal.getTime());
        return startTime;
    }


    public String getEndTime() {

        endTime = timeFormat.format(cal.getTime());
        return endTime;
    }

    public String getCurrentDateWithTime(){

        String outputPattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern.trim());
        Calendar c = Calendar.getInstance();
        String dateCurrent = outputFormat.format(c.getTime());
        return dateCurrent;
    }
}
