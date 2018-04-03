package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

/**
 * Created by pawdaw on 30/03/17.
 */

public class CheckInTime {

    private String start_time;
    private String end_time;

    public CheckInTime(String start_time, String end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public CheckInTime() {

    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return start_time;
    }
}
