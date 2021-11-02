package com.cs201.g1t1.map;

import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.repository.CategoryRepository;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cs201.g1t1.model.Business;
import com.cs201.g1t1.model.Category;
import java.util.ArrayList;
import java.util.HashSet;

@RestController
public class Hashmap {
    @Autowired
    private BusinessRepository businesses;

    @Autowired
    private CategoryRepository categories;

    Logger logger = LoggerFactory.getLogger(Hashmap.class);

    @GetMapping("/unique/{city}")
    public String getUniqueCategory(@PathVariable (value = "city") String city){
        return getMostOccurances(city);
    }
    
    // Hardcode the starting city for ease of testing
    @GetMapping("/unique")
    public String getUniqueCategory(){
        String city = "East Point";
        
        return getMostOccurances(city);
    }

    /**
     * Find the category that occurs the most frequently in a particular city
     * 
     * @param city all the businesses to be searched belong to 
     * (done by city to prevent computer hanging)
     * 
     * @return the name of the category that occurs the most in given city
     */
    public String getMostOccurances(String city) {
        // Select all businesses from a particular city
        List<Business> bs = businesses.findByCity(city);

        // Find the total number of categories (will be the number of buckets)
        List<Category> allCategories = categories.findAll();
        int numOfCategories = allCategories.size();
        
        ChainHashMap<String,Business> map = new ChainHashMap<String,Business>(numOfCategories); 

        ArrayList<String> c = new ArrayList<>();

        // Start Time
        final long startTime = System.currentTimeMillis();

        // Save staring Runtime
        Runtime runtime1 = Runtime.getRuntime();
        runtime1.gc();
        long memory1 = runtime1.totalMemory() - runtime1.freeMemory();
        
        // Build HashMap
        for(Business b : bs){
            for (Category category : b.getCategories()) {
                
                // Generate HashCode
                String cno = category.getCategoryName();
                int hash = (int) ((Math.abs(cno.hashCode())) % numOfCategories);
                map.bucketPut(hash, b.getBusinessId(), b);
                
                if (!c.contains(cno)) {
                    c.add(cno);
                }
            }

        }

        // Save ending Runtime
        Runtime runtime2 = Runtime.getRuntime();
        runtime2.gc();
        long memory2 = runtime2.totalMemory() - runtime2.freeMemory();

        UnsortedTableMap<String,Business>[] buckets = map.getTable();

        // Check for largest bucket (the category with the most occurances)
        int max = 0;
        String result = null;
        for (String cno : c) {
            int hash = (int) ((Math.abs(cno.hashCode())) % numOfCategories);
            if (buckets[hash] != null) {
                UnsortedTableMap<String, Business> table = buckets[hash];
                int size = table.size();

                if(size > max){
                    max = size;
                    result = cno;
                }
            }
        }
        
        // End Time
        final long endTime = System.currentTimeMillis();

        // Log Total Elapsed Time (in milliseconds)
        logger.info("Total execution time: " + (endTime - startTime));

        // Log Total Memory Used (converted to kilobytes)
        logger.info("Memory used: {}KB", (memory2 - memory1)/(1024L));

        // Handle null result
        if(result == null){
            String ans = "no reuslt";
            return ans;
        }

        return result;
    }

    @GetMapping("/testHashMap")
    public int checkForCollision() {
        List<Category> allCategories = categories.findAll();
        
        Set<Integer> hashCodeSet = new HashSet<Integer>();

        int counter = 0;

        for (Category category : allCategories) {
            String catName = category.getCategoryName();
            
            // using .hashCode() method
            // int hash = (int) ((Math.abs(cno.hashCode())) % 1330);

            // XOR
            // int hash = 0;
            // for (int i = 0 ; i < catName.length() ; i++) {
            //     hash ^= ((int)catName.charAt(i));
            // }

            // Cyclic Shift
            int hash = 0;
            for (int i = 0 ; i < catName.length() ; i++) {
                hash = (hash << 5) | (hash >>> 27);
                hash += ((int)catName.charAt(i));
            }

            hash = Math.abs(hash) % allCategories.size();
            
            if (hashCodeSet.contains(hash)) {
                logger.info("-------REPEAT-------");
                counter++;

            } else {
                hashCodeSet.add(hash);
            }
            logger.info("Category: {}, Hashcode: {}", category.getCategoryName(), hash);

        }

        logger.info("Number of collisions: {}/1330 = {}%", counter, ((double)counter/(double)1330)*100);

        return counter;
    }

    @GetMapping("/city")
    public List<String> getCities() {
        List<String> allCity = businesses.getAllCity();
        return allCity;

    }

    @GetMapping("/postalCode")
    public List<String> getPostalCode() {
        List<String> postalCode = businesses.getAllPostalCode("Austin");
        return postalCode;
    }

    @GetMapping("/businesses")
    public List<Business> getBusiness() {
        List<Business> b = businesses.findByCity("Concord");
        return b;
    }
}
