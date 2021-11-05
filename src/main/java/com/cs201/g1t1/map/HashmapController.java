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
public class HashmapController {
    @Autowired
    private BusinessRepository businesses;

    @Autowired
    private CategoryRepository categories;

    // Logger to log information to console
    Logger logger = LoggerFactory.getLogger(HashmapController.class);

    /**
     * Endpoint to get the most frequently occuring business category in a
     * particular city
     * 
     * Note: City to be searched is hardcoded in Line 41 for ease of testing
     * 
     * @return name of business category with the highest number of occurances
     */
    @GetMapping("/hashmap/most-popular-category")
    public String getMostOccurancesCategory() {
        String city = "East Point";
        return getMostOccurances(city);
    }

    /**
     * Endpoint to get the most frequently occuring business category in a
     * particular city
     * 
     * @param city in which to find the most frequently occuring business category
     * @return name of business category with the highest number of occurances
     */
    @GetMapping("/hashmap/categories/{city}")
    public String getUniqueCategory(@PathVariable(value = "city") String city) {
        return getMostOccurances(city);
    }

    /**
     * Find the category that occurs the most frequently in a particular city
     * 
     * @param city all the businesses to be searched belong to (done by city to
     *             prevent cases of computer hanging)
     * 
     * @return the name of the category that occurs the most in given city
     */
    public String getMostOccurances(String city) {
        // Select all businesses from a particular city
        List<Business> bs = businesses.findByCity(city);

        // Find the total number of categories (will be the number of buckets)
        List<Category> allCategories = categories.findAll();
        int numOfCategories = allCategories.size();

        ChainHashMap<String, Business> map = new ChainHashMap<String, Business>(numOfCategories);

        ArrayList<String> c = new ArrayList<>();

        // Start Time
        final long startTime = System.currentTimeMillis();

        // Save staring Runtime
        Runtime runtime1 = Runtime.getRuntime();
        runtime1.gc();
        long memory1 = runtime1.totalMemory() - runtime1.freeMemory();

        // Build HashMap
        for (Business b : bs) {
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

        UnsortedTableMap<String, Business>[] buckets = map.getTable();

        // Check for largest bucket (the category with the most occurances)
        int max = 0;
        String result = null;
        for (String cno : c) {
            int hash = (int) ((Math.abs(cno.hashCode())) % numOfCategories);
            if (buckets[hash] != null) {
                UnsortedTableMap<String, Business> table = buckets[hash];
                int size = table.size();

                if (size > max) {
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
        logger.info("Memory used: {}KB", (memory2 - memory1) / (1024L));

        // Handle null result
        if (result == null) {
            String ans = "no result";
            return ans;
        }

        return result;
    }

    /**
     * Check number of collisions that occurred Hash function is hard-coded for ease
     * of testing
     * 
     * @return number of collisions caused by that hash function
     */
    @GetMapping("/hashmap/collisions")
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
            // hash ^= ((int)catName.charAt(i));
            // }

            // Cyclic Shift
            int hash = 0;
            for (int i = 0; i < catName.length(); i++) {
                hash = (hash << 5) | (hash >>> 27);
                hash += ((int) catName.charAt(i));
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

        logger.info("Number of collisions: {}/1330 = {}%", counter, ((double) counter / (double) 1330) * 100);

        return counter;
    }

}
