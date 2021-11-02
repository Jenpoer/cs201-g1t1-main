package com.cs201.g1t1.util;

import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.spatial.*;
import com.cs201.g1t1.spatial.kdtree.*;
import com.cs201.g1t1.spatial.rangetree.RangeTree;
import com.cs201.g1t1.array.LinearSearch;
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

    @GetMapping("/api/kyle")
    public List<Business> getBusinessesInKyle() {
        return businessRepository.findByCity("Kyle");
    }

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

    private String getUniqueCategoryWithHashMap(List<Business> bs) {
        ChainHashMap<String, Business> map = new ChainHashMap<String, Business>(1330); // should get the category size
                                                                                       // from the repositry
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

    private String getUniqueCategoryWithList(List<Business> b) {
        Category mostPopularBusiness = linearSearch.findMostPopularCategory(b);
        return mostPopularBusiness.getCategoryName();
    }

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
}
