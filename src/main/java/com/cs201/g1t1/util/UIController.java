package com.cs201.g1t1.util;

import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.spatial.*;
import com.cs201.g1t1.spatial.kdtree.*;
import com.cs201.g1t1.spatial.rangetree.RangeTree;
import com.cs201.g1t1.list.LinearSearch;
import com.cs201.g1t1.map.ChainHashMap;
import com.cs201.g1t1.map.UnsortedTableMap;
import com.cs201.g1t1.model.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UIController {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private LinearSearch linearSearch;

    /**
     * Endpoint to get all businesses in the city Kyle
     * @return all businesses in the city Kyle
     */
    @GetMapping("/api/kyle")
    public List<Business> getBusinessesInKyle() {
        return businessRepository.findByCity("Kyle");
    }

    /**
     * Endpoint to find the most frequently occuring business category in Kyle using kd-tree and list
     * @param xMin x coordinate of bottom left corner of area to be selected
     * @param xMax x coordinate of top right corner of area to be selected
     * @param yMin y coordinate of bottom left corner of area to be selected
     * @param yMax y coordinate of top right corner of area to be selected
     * @return name of most frequently occuring business category in selected area, kd-tree search time, list search time
     */
    @GetMapping("/api/kd-tree/list")
    public AlgoResult findMostPopularCategoryKDList(@RequestParam Double xMin, @RequestParam Double xMax,
            @RequestParam Double yMin, @RequestParam Double yMax) {
        Double[] pointMin = { xMin, yMin };
        Double[] pointMax = { xMax, yMax };
        Rectangle range = new Rectangle(pointMin, pointMax);

        final long startRangeSearchTime = System.currentTimeMillis();
        List<Business> business = searchBusinessesWithKDTree("Kyle", range);
        final long endRangeSearchTime = System.currentTimeMillis();
        final long rangeSearchTime = endRangeSearchTime - startRangeSearchTime;

        final long startCategorySearchTime = System.currentTimeMillis();
        String categoryName = getUniqueCategoryWithList(business);
        final long endCategorySearchTime = System.currentTimeMillis();
        final long categorySearchTime = endCategorySearchTime - startCategorySearchTime;

        return new AlgoResult(categoryName, rangeSearchTime, categorySearchTime);
    }

    /**
     * Endpoint to find the most frequently occuring business category in Kyle using kd-tree and hashmap
     * @param xMin x coordinate of bottom left corner of area to be selected
     * @param xMax x coordinate of top right corner of area to be selected
     * @param yMin y coordinate of bottom left corner of area to be selected
     * @param yMax y coordinate of top right corner of area to be selected
     * @return name of most frequently occuring business category in selected area, kd-tree search time, hashmap search time
     */
    @GetMapping("/api/kd-tree/hashmap")
    public AlgoResult findMostPopularCategoryKDHash(@RequestParam Double xMin, @RequestParam Double xMax,
            @RequestParam Double yMin, @RequestParam Double yMax) {
        Double[] pointMin = { xMin, yMin };
        Double[] pointMax = { xMax, yMax };
        Rectangle range = new Rectangle(pointMin, pointMax);

        final long startRangeSearchTime = System.currentTimeMillis();
        List<Business> business = searchBusinessesWithKDTree("Kyle", range);
        final long endRangeSearchTime = System.currentTimeMillis();
        final long rangeSearchTime = endRangeSearchTime - startRangeSearchTime;

        final long startCategorySearchTime = System.currentTimeMillis();
        String categoryName = getUniqueCategoryWithHashMap(business);
        final long endCategorySearchTime = System.currentTimeMillis();
        final long categorySearchTime = endCategorySearchTime - startCategorySearchTime;
        return new AlgoResult(categoryName, rangeSearchTime, categorySearchTime);
    }

    /**
     * Endpoint to find the most frequently occuring business category in Kyle using range tree and list
     * @param xMin x coordinate of bottom left corner of area to be selected
     * @param xMax x coordinate of top right corner of area to be selected
     * @param yMin y coordinate of bottom left corner of area to be selected
     * @param yMax y coordinate of top right corner of area to be selected
     * @return name of most frequently occuring business category in selected area, range tree search time, list search time
     */
    @GetMapping("/api/range-tree/list")
    public AlgoResult findMostPopularCategoryRangeList(@RequestParam Double xMin, @RequestParam Double xMax,
            @RequestParam Double yMin, @RequestParam Double yMax) {
        Double[] pointMin = { xMin, yMin };
        Double[] pointMax = { xMax, yMax };
        Rectangle range = new Rectangle(pointMin, pointMax);

        final long startRangeSearchTime = System.currentTimeMillis();
        List<Business> business = searchBusinessesWithRangeTree("Kyle", range);
        final long endRangeSearchTime = System.currentTimeMillis();
        final long rangeSearchTime = endRangeSearchTime - startRangeSearchTime;

        final long startCategorySearchTime = System.currentTimeMillis();
        String categoryName = getUniqueCategoryWithList(business);
        final long endCategorySearchTime = System.currentTimeMillis();
        final long categorySearchTime = endCategorySearchTime - startCategorySearchTime;
        return new AlgoResult(categoryName, rangeSearchTime, categorySearchTime);
    }

    /**
     * Endpoint to find the most frequently occuring business category in Kyle using range tree and hashmap
     * @param xMin x coordinate of bottom left corner of area to be selected
     * @param xMax x coordinate of top right corner of area to be selected
     * @param yMin y coordinate of bottom left corner of area to be selected
     * @param yMax y coordinate of top right corner of area to be selected
     * @return name of most frequently occuring business category in selected area, range tree search time, hashmap search time
     */
    @GetMapping("/api/range-tree/hashmap")
    public AlgoResult findMostPopularCategoryRangeHash(@RequestParam Double xMin, @RequestParam Double xMax,
            @RequestParam Double yMin, @RequestParam Double yMax) {
        Double[] pointMin = { xMin, yMin };
        Double[] pointMax = { xMax, yMax };
        Rectangle range = new Rectangle(pointMin, pointMax);

        final long startRangeSearchTime = System.currentTimeMillis();
        List<Business> business = searchBusinessesWithRangeTree("Kyle", range);
        final long endRangeSearchTime = System.currentTimeMillis();
        final long rangeSearchTime = endRangeSearchTime - startRangeSearchTime;

        final long startCategorySearchTime = System.currentTimeMillis();
        String categoryName = getUniqueCategoryWithHashMap(business);
        final long endCategorySearchTime = System.currentTimeMillis();
        final long categorySearchTime = endCategorySearchTime - startCategorySearchTime;

        return new AlgoResult(categoryName, rangeSearchTime, categorySearchTime);
    }

    /**
     * Get the list of businesses within a specified range with KD-Tree
     * @param cityName of city area to be searched is in
     * @param range return businesses if inside range (based on coordinates/longitude&latitude)
     * @return list of businesses within range
     */
    private List<Business> searchBusinessesWithKDTree(String cityName, Rectangle range) {
        List<Business> businessesInCity = businessRepository.findByCity(cityName);

        List<BusinessGeo> geoLocations = new ArrayList<>();
        businessesInCity.forEach(i -> geoLocations.add(new BusinessGeo(i)));

        KDTree2D kdTree2D = new KDTree2D();
        kdTree2D.build(geoLocations);

        Set<KDTree2DNode<?>> nn = kdTree2D.rangeQuery(range);

        List<Business> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            if (i instanceof KDTree2DNode<?>) {
                toReturn.add(((KDTreeNode<BusinessGeo>) i).getElement().getBusiness());
            }
        });
        return toReturn;
    }

    /**
     * Get the list of businesses within a specified range with Range Tree
     * @param cityName of city area to be searched is in
     * @param range return businesses if inside range (based on coordinates/longitude&latitude)
     * @return list of businesses within range
     */
    private List<Business> searchBusinessesWithRangeTree(String cityName, Rectangle range) {
        List<Business> businessesInCity = businessRepository.findByCity(cityName);

        List<BusinessGeo> geoLocations = new ArrayList<>();
        businessesInCity.forEach(i -> geoLocations.add(new BusinessGeo(i)));

        RangeTree<BusinessGeo> rangeTree = new RangeTree<>(0);
        rangeTree.construct(geoLocations);

        Set<BusinessGeo> nn = rangeTree.rangeQuery(range);

        List<Business> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            toReturn.add(i.getBusiness());
        });

        return toReturn;
    }

    /**
     * Get the most frequently occuring business category with hashmap
     * @param bs list of businesses to be searched
     * @return name of most frequently occuring business category
     */
    private String getUniqueCategoryWithHashMap(List<Business> bs) {
        ChainHashMap<String, Business> map = new ChainHashMap<String, Business>(1330);
        ArrayList<String> c = new ArrayList<>();

        for (Business b : bs) {

            // Iterator categories = business.getCategories().iterator();
            for (Category category : b.getCategories()) {
                String cno = category.getCategoryName(); // use as the key
                int hash = (int) ((Math.abs(cno.hashCode())) % 1330); // use as hash
                map.bucketPut(hash, b.getBusinessId(), b);
                if (!c.contains(cno)) {
                    c.add(cno);
                }

            }

        }

        UnsortedTableMap<String, Business>[] buckets = map.getTable();
        int max = 0;
        String result = null;
        for (String cno : c) {
            int hash = (int) ((Math.abs(cno.hashCode())) % 1330);
            if (buckets[hash] != null) {
                UnsortedTableMap<String, Business> table = buckets[hash];
                int size = table.size();

                if (size > max) {
                    max = size;
                    result = cno;
                }
            }
        }

        return result;
    }

    /**
     * Get most frequently occuring business category with list
     * @param b list of businesses to be searched
     * @return name of most frequently occuring business category
     */
    private String getUniqueCategoryWithList(List<Business> b) {
        Category mostPopularBusiness = linearSearch.findMostPopularCategory(b);
        return mostPopularBusiness.getCategoryName();
    }
}
