package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawdaw on 02/11/16.
 */
public class Place implements Serializable {

    private String name;
    private String address;
    private String email;
    private String password;
    private String secretWord;
    private String latitude;
    private String longitude;
    private String cleaningTime;
    private String requiredCleaningUntil;
    private String recentlyCleaned;
    private String notificationSent;
    private Map<String,Object> users = new HashMap<>();
    private Map<String,Object> reports = new HashMap<>();

    private String userName;


    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Place() {

    }

    public Place(String name, String address,  String email, String password, String secretWord, String latitude, String longitude,String cleaningTime,String requiredCleaningUntil) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.password= password;
        this.secretWord = secretWord;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cleaningTime = cleaningTime;
        this.requiredCleaningUntil = requiredCleaningUntil;

    }

    //    Create Tree structure for Firebase
    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("address", address);
        result.put("email", email);
        result.put("password", password);
        result.put("secretWord", secretWord);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("cleaningTime", cleaningTime);
        result.put("requiredCleaningUntil", requiredCleaningUntil);
        result.put("recentlyCleaned", recentlyCleaned);
        result.put("notificationSent", notificationSent);
        result.put("users",users);
        result.put("reports",reports);

        return result;
    }

//    ---- add new User to MAP for one PLACE ----
    @Exclude
    public Map<String, Object> toUsersMap(String string , Boolean someBool) {

        users.put(string, someBool);
        return users;
    }

    public void setUsers(String string , Boolean someBool) {

        toUsersMap(string, someBool);
    }
//    ---------------


    //    ---- add new Report to MAP for one PLACE ----
    @Exclude
    public Map<String, Object> toReportMap(String string , Boolean someBool) {

        reports.put(string, someBool);
        return reports;
    }

    public void setReports(String string , Boolean someBool) {

        toReportMap(string, someBool);
    }
//    ---------------


    @Exclude
    public Map<String, Object> newMapOfUsers(Map<String, Object> map) {

        this.users = map;
        return users;
    }

    public Map<String, Object> getUsers() {

        return users;
    }

    public Map<String, Object> getReports() {

        return reports;
    }


    public Place (String name) {
        this.name = name;
    }


    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCleaningTime() {
        return cleaningTime;
    }

    public void setCleaningTime(String cleaningTime) {
        this.cleaningTime = cleaningTime;
    }

    public String getRequiredCleaningUntil() {
        return requiredCleaningUntil;
    }

    public void setRequiredCleaningUntil(String requiredCleaningUntil) {
        this.requiredCleaningUntil = requiredCleaningUntil;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecentlyCleaned() {
        return recentlyCleaned;
    }

    public void setRecentlyCleaned(String recentlyCleaned) {
        this.recentlyCleaned = recentlyCleaned;
    }

    public String getNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(String notificationSent) {
        this.notificationSent = notificationSent;
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
        Place other = (Place) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    // ------- end HashMAP ------

    //  important to print the name of the place
    @Override
    public String toString() {
        return name;
    }
}
