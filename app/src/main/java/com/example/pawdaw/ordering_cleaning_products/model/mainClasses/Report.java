package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawdaw on 14/07/17.
 */

public class Report implements Serializable {

    public String message;
    public String where;
    public String timestamp;
    public String place;
    public String owner;
    public String rating;
    public Image image;


    //  Default constructor required for calls to DataSnapshot.getValue(Report.class)
    public Report(){

    }

    public Report( Image image, String message, String where, String timestamp,String place) {
        this.image = image;
        this.message = message;
        this.where = where;
        this.timestamp = timestamp;
        this.place = place;
    }

    //    Create Tree structure for Firebase

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("where", where);
        result.put("owner",owner);
        result.put("place",place);
        result.put("timestamp", timestamp);
        result.put("image", image);
        result.put("rating",rating);

        return result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Image getImage() {

        return image;
    }

    public void setImage(Image image) {

        this.image = image;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return timestamp;
    }
}
