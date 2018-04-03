package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawdaw on 02/11/16.
 */
public class OrderList {

    private String date;
    private String placeName;
    private String product;

    private Map<String,Object> products = new HashMap<>();

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public OrderList() {

    }

//    constructor used in the service,
    public OrderList(String placeName, String product) {
        this.placeName = placeName;
        this.product = product;
    }

    //    constructor used in the Missing Product Activity,
    public OrderList(String placeName, String product,String date) {
        this.placeName = placeName;
        this.product = product;
        this.date = date;
    }


    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("placeName", placeName);
        result.put("products", products);
        return result;
    }

    //    ---- add new User to MAP for one PLACE ----
    @Exclude
    public Map<String, Object> toProductMap(String string , Boolean someBool) {

        products.put(string, someBool);
        return products;
    }

    public void setProduct(String string , Boolean someBool) {

        toProductMap(string, someBool);
    }
//    ----------------

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

    public Map<String, Object> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Object> products) {
        this.products = products;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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
        OrderList other = (OrderList) obj;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        return result;
    }

    // ------- end HashMAP ------


    @Override
    public String toString() {
        return placeName;
    }


}
