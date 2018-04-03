package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

/**
 * Created by pawdaw on 26/03/17.
 */

public class CheckInLog extends CheckInTime{

    private String date;
    private String placeName;
    private String employeeName;


    public CheckInLog(){

    }

    public CheckInLog(String date, String placeName, String employeeName,String start_time,String end_time) {
        super(start_time,end_time);
        this.date = date;
        this.placeName = placeName;
        this.employeeName = employeeName;

    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public String getPlaceName() {

        return placeName;
    }

    public void setPlaceName(String placeName) {

        this.placeName = placeName;
    }

    public String getEmployeeName() {

        return employeeName;
    }

    public void setEmployeeName(String employeeName) {

        this.employeeName = employeeName;
    }





    // ------- HashMAP ------
    // required for hashmap, avoid duplicate item
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CheckInLog other = (CheckInLog) obj;
        if (placeName == null) {
            if (other.placeName != null)
                return false;
        } else if (!placeName.equals(other.placeName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((placeName == null) ? 0 : placeName.hashCode());
        return result;
    }

    // ------- end HashMAP ------


    @Override
    public String toString() {
        return placeName;
    }
}

