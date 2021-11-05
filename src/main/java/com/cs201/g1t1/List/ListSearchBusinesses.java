package com.cs201.g1t1.List;

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
public class ListSearchBusinesses {

    @Autowired
    private BusinessRepository businesses;

    @Autowired
    private LinearSearch linearSearch;

    Logger logger = LoggerFactory.getLogger(ListSearchBusinesses.class);

    @GetMapping("businesses/{postalCode}")
    public List<Business> getBusinessesByPostalCode (String postalCode){
        return businesses.findByPostalCode(postalCode);
    }

    @GetMapping("/businesses/popular")
    public Category getMostPopularCategory(){
        List<Business> b = businesses.findByCity("Titusville");

        // Start Time
        long start = System.nanoTime();
        
        // Save staring Runtime
        Runtime runtime1 = Runtime.getRuntime();
        runtime1.gc();
        long memory1 = runtime1.totalMemory() - runtime1.freeMemory();
        
        // Function Call
        Category mostPopularBusiness = linearSearch.findMostPopularCategory(b);
        
        // Save ending Runtime
        Runtime runtime2 = Runtime.getRuntime();
        runtime2.gc();
        long memory2 = runtime2.totalMemory() - runtime2.freeMemory();

        // End Time
        long end = System.nanoTime();

        // Log Total Elapsed Time (converted to milliseconds)
        logger.info("Time Elapsed: {}ms", (end - start)/1000000);

        // Log Total Memory Used (converted to kilobytes)
        logger.info("Memory used: {}KB", (memory2 - memory1)/(1024L));

        return mostPopularBusiness;
    }

    @GetMapping("/businesses/category/{categoryName}")
    public int getNumberOfOccurences (@PathVariable (value = "categoryName") String categoryName){
        
        List<Business> b = businesses.findByCity("Concord");
        return linearSearch.findOccurrences(categoryName, b);
        
    }

}
