package com.cs201.g1t1.array;

import java.util.*;
import com.cs201.g1t1.model.Business;
import com.cs201.g1t1.model.Category;


public class ArrayTest {
    


    public static void main(String[] args) {

        Category mexican = new Category ("Mexican");
        Category pub = new Category ("Pub");
        Category takeaway = new Category ("Takeaway");
        Category lunch = new Category ("Lunch");
        Category halal = new Category ("Halal");
        Category german = new Category ("German");


        Set <Category> categories = new HashSet<Category>();
        categories.add(mexican);
        categories.add(pub);
        categories.add(takeaway);

        Set <Category> categories1 = new HashSet<Category>();
        categories.add(german);
        categories.add(halal);
        categories.add(lunch);


       
        Business restaurant = new Business("0101", "Fred's Restaurant", "address", "city", "state", "postalCode", 1, 1, 2, categories);
        Business restaurant1 = new Business("0201", "Sam's Restaurant", "address1", "city1", "state1", "postalCode1", 1, 1, 2, categories1);


        List <Business> businesses = new ArrayList <Business> ();
        businesses.add(restaurant);
        businesses.add(restaurant1);

        LinearSearch linearSearch = new LinearSearch ();

        Category popular =  linearSearch.findMostPopularBusiness(businesses,5);

        System.out.print(popular);
    }
}
