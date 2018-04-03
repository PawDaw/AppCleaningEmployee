package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawdaw on 01/11/16.
 */
public class Manager extends User{

    private String username;
    private String email;
    private String mobileNumber;
    private Image image;


    //  Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Manager(){

    }

    public Manager(String username, String email, String mobileNumber) {
        this.username = username;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    //    Create Tree structure for Firebase

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("email", email);
        result.put("mobileNumber", mobileNumber);
        result.put("image",image);

        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {return  username; }
}
