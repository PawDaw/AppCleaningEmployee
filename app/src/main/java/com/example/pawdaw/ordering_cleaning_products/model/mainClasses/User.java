package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawdaw on 01/11/16.
 */
public class User implements Serializable {

    private String username;
    private String email;
    private String mobileNumber;
    private Image image;


    private Map<String,Object> places = new HashMap<>();

//  Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User(){

    }

//  (SignUpActivity)  Constructor used when the process of creation/registration new employee is started
    public User(String email, String username, String mobileNumber, Map<String,Object> places) {
        this.email = email;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.places = places;
    }

    public User(String username, String email, String mobileNumber) {
        this.username = username;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public User(String username) {
        this.username = username;

    }


//    Create Tree structure for Firebase

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("email", email);
        result.put("mobileNumber", mobileNumber);
        result.put("places", places);
        result.put("image",image);

        return result;
    }


    //    ---- add new place to MAP for one USER ----
    @Exclude
    public Map<String, Object> placeToUser(String placeNameString, Boolean placeAssigned) {

        places.put(placeNameString, placeAssigned);
        return places;
    }

    // Assign place to user
    public void setPlaces(String placeNameString, Boolean placeAssigned) {

        placeToUser(placeNameString, placeAssigned);
    }
    //    -----------



    @Exclude
    public Map<String, Object> newMapOfPlaces(Map<String, Object> map) {

        this.places = map;
        return places;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public Map<String, Object> getPlaces() {

        return places;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // important to print the name of the User
    @Override
    public String toString() {
        return username;
    }

}