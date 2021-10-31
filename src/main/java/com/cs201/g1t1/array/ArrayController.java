package com.cs201.g1t1.array;

import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.repository.CategoryRepository;
import com.cs201.g1t1.map.*;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cs201.g1t1.model.Business;
import com.cs201.g1t1.model.Category;
import java.util.Iterator;
import java.util.ArrayList;



@RestController
public class ArrayController {

    @Autowired
    private BusinessRepository businesses;

    @Autowired
    private LinearSearch linearSearch;

    @Autowired 
    private Hashmap hashmap;


    // @GetMapping("/city")
    // public List<String> getCities(){
    //     List<String> allCity = businesses.getAllCity();
    //     return allCity;

    // }

    // @GetMapping("/postalCode")
    // public List<String> getPostalCode(){
    //     List<String> postalCode = businesses.getAllPostalCode("Austin");
    //     return postalCode;
    // }

    // @GetMapping("/businesses")
    // public List<Business> getBusiness(){
    //     List<Business> b = businesses.findByCity("Austin");
    //     return b;
    // }

    @GetMapping("/businesses/popular")
    public Category getMostPopularBusiness(){
        List<Business> b = businesses.findByCity("Austin");
        Category mostPopularBusiness = linearSearch.findMostPopularBusiness(b, 20);
        return mostPopularBusiness;
    }

    @GetMapping("/businesses/category/{categoryName}")
    public int getNumberOfOccurences (@PathVariable (value = "categoryName") String categoryName){
        List<Business> b = businesses.findByCity("Austin");
        Category c  = new Category(categoryName);
        return linearSearch.findOccurences(c, b);
        
    }


    
    
}
