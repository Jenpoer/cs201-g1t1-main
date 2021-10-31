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

    @Autowired 
    private CategoryRepository categories;


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

    Logger logger = LoggerFactory.getLogger(ArrayController.class);

    @GetMapping("businesses/{postalCode}")
    public List<Business> getBusinessesByPostalCode (String postalCode){
        return businesses.findByPostalCode(postalCode);
    }

   

    @GetMapping("/businesses/popular")
    public Category getMostPopularCategory(){
        List<Business> b = businesses.findByCity("Concord");
        Category mostPopularBusiness = linearSearch.findMostPopularCategory(b, b.size() * 10);
        return mostPopularBusiness;
    }

    @GetMapping("/businesses/category/{categoryName}")
    public int getNumberOfOccurences (@PathVariable (value = "categoryName") String categoryName){
        Category c = new Category(categoryName);
        List<Business> b = businesses.findByCity("Austin");
        return linearSearch.findOccurences(c, b);
        
    }


    
    
}
