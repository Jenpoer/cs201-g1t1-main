package com.cs201.g1t1.list;

import com.cs201.g1t1.repository.BusinessRepository;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cs201.g1t1.model.Business;
import com.cs201.g1t1.model.Category;

@RestController
public class ListController {

    @Autowired
    private BusinessRepository businesses;

    @Autowired
    private LinearSearch linearSearch;

    // Logger to log information to console
    Logger logger = LoggerFactory.getLogger(ListController.class);

    /**
     * Endpoint to get the most frequently occuring business category in a particular city
     * 
     * Note: City to be searched is hardcoded in Line 37 for ease of testing
     * 
     * @return name of business category with the highest number of occurances
     */
    @GetMapping("/list/most-popular-category")
    public Category getMostPopularCategory(){
        List<Business> b = businesses.findByCity("Titusville");

        // Start Time
        long start = System.nanoTime();
        
        // Save staring Runtime
        Runtime runtime1 = Runtime.getRuntime();
        runtime1.gc();
        long memory1 = runtime1.totalMemory() - runtime1.freeMemory();
        
        // Function Call
        Category mostPopularCategory = linearSearch.findMostPopularCategory(b);
        
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

        return mostPopularCategory;
    }

    /**
     * Endpoint to find the number of occurances of a particular category in a particular city
     * 
     * Note: City to be searched is hardcoded in Line 84 for ease of testing
     * 
     * @param categoryName to return the number of occurances of
     * @return the number of occurances of categoryName
     */
    @GetMapping("/list/categories/{categoryName}")
    public int getNumberOfOccurences (@PathVariable (value = "categoryName") String categoryName){
        
        List<Business> b = businesses.findByCity("Concord");
        return linearSearch.findOccurrences(categoryName, b);
        
    }

}
