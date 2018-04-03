package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawdaw on 02/11/16.
 */
public class Product {


    // I will use one instance of MAPHash in the product adapter class, to prevent override products when is more than one
    // --------- Singleton instance  ----------------

    private static Product instance = new Product();

    public static Product getInstance() {

        return instance;
    }
    //  ----------------------------------------


    private Map<String,Object> products = new HashMap<>();


    private String name;
    private String EAN;

    private String place;
    private String user;


    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Product() {

    }

    public Product(String name) {

        this.name = name;
    }

    public Product(String name,String EAN) {
        this.name = name;
        this.EAN = EAN;
    }

    public Product(String name, String nameOfPlace, String username) {
        this.name = name;
        this.place = nameOfPlace;
        this.user = username;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //  ---- toMAP used while new product is created  -----------
    @Exclude
    public Map<String, Object> toMapNameProduct() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("EAN", EAN);
        result.put("name", name);
        return result;
    }


    //  ---- toMAP used to pass data to orderList -----------
    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("products", products);
        return result;
    }


    //    ---- add new product to MAP ----
    @Exclude
    public Map<String, Object> toProductMap(String string , Boolean someBool) {

        products.put(string, someBool);
        return products;
    }

    public void setProduct(String string , Boolean someBool) {

        toProductMap(string, someBool);
    }

    public void removeProduct(String product){

        products.remove(product);
    }
//    ----------------


    public String getEAN() {
        return EAN;
    }

    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void clearHasmMap(){
        products.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}
