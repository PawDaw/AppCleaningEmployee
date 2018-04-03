package com.example.pawdaw.ordering_cleaning_products.model.storage;

import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.CheckInLog;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.OrderList;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Place;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Product;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Report;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Manager;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by pawdaw on 01/11/16.
 */
public class Storage {

    // Singleton instance

    private static Storage instance = new Storage();

    public static Storage getInstance() {

        return instance;
    }

    private Storage() {
        //
    }

    //avoid duplicate item
    public HashSet<OrderList> orderedProductHset = new HashSet<OrderList>();
    public HashSet<CheckInLog> myLogbooks = new HashSet<CheckInLog>();
    public HashSet<Place> places = new HashSet<Place>();


    public ArrayList<Manager> managers = new ArrayList<Manager>();
    public ArrayList<User> employees = new ArrayList<User>();
    public ArrayList<OrderList>  productsByDate = new ArrayList<OrderList>();
    public ArrayList<OrderList>  orderLists = new ArrayList<OrderList>();
    public ArrayList<Product>  products = new ArrayList<Product>();
    public ArrayList<Report>  reports = new ArrayList<Report>();


    public ArrayList<OrderList> getOrderLists() {
        return new ArrayList<OrderList>(orderLists);
    }

    public void addOrderLists(OrderList orderList) {
        orderLists.add(orderList);
    }

    public String Password;


    public String getPassword() {

        return Password;
    }

    public void setPassword(String password) {

        Password = password;
    }


    public ArrayList<User> getEmployees() {

        return new ArrayList<User>(employees);
    }

    public void addEmployee(User user)
    {

        employees.add(user);
    }


    public ArrayList<Manager> getManagers() {

        return new ArrayList<Manager>(managers);
    }

    public void addManager(Manager manager) {

        managers.add(manager);
    }



    public ArrayList<Place> getPlaces() {

        return new ArrayList<Place>(places);
    }

    public void addPlace(Place place) {

        places.add(place);
    }

    public void removePlace(Place place){

        places.remove(place);
    }


    public ArrayList<OrderList> getProductsByDate() {

        return new ArrayList<OrderList>(productsByDate);
    }

    public void setProductsByDate(OrderList product) {


        this.productsByDate.add(product);
    }

    public void removeProductByDate(OrderList product)
    {

        productsByDate.remove(product);
    }




    public HashSet<OrderList> getOrderedProductHset() {
        return orderedProductHset;

    }

    public void setOrderedProductHset(OrderList product) {

        this.orderedProductHset.add(product);
    }




    public ArrayList<Product> getProducts() {

        return new ArrayList<Product>(products);

    }

    public void addProduct(Product product) {

        this.products.add(product);
    }




    public ArrayList<CheckInLog> getMyLogbooks() {

        return new ArrayList<CheckInLog> (myLogbooks);
    }

    public void addLogbook(CheckInLog logbook)
    {

        myLogbooks.add(logbook);
    }

    public ArrayList<Report> getReports(){
        return new ArrayList<Report>(reports);
    }

    public void addReport(Report report){
        this.reports.add(report);
    }



}
