package com.example.pawdaw.ordering_cleaning_products.model.mainClasses;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawdaw on 16/06/17.
 */

public class Image implements Serializable {

    private String id;
    private String url;


    //  Default constructor required for calls to DataSnapshot.getValue(Image.class)
    public Image(){

    }

    public Image(String url, String id) {
        this.url = url;
        this.id = id;
    }


    //    Create Tree structure for Firebase

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("url", url);

        return result;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
