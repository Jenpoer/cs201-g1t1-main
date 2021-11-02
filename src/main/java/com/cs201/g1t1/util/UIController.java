package com.cs201.g1t1.util;

import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.spatial.*;
import com.cs201.g1t1.spatial.kdtree.*;
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
    private KDTree2D kdTree2D;

    @GetMapping("/api/kyle")
    public List<Business> getBusinessesInKyle() {
        return businessRepository.findByCity("Kyle");
    }

    @GetMapping("/api/kd-tree/hashmap")
    public String findMostPopularCategory(@RequestParam Double minX, @RequestParam Double maxX,
            @RequestParam Double minY, @RequestParam Double maxY) {
        Double[] pointMin = { minX, minY };
        Double[] pointMax = { maxX, maxY };
        Rectangle range = new Rectangle(pointMin, pointMax);
        List<Business> business = searchBusinessesWithKDTree("Kyle", range);

        return "";
    }

    private List<Business> searchBusinessesWithKDTree(String cityName, Rectangle range) {
        List<Business> businessesInCity = businessRepository.findByCity(cityName);

        List<BusinessGeo> geoLocations = new ArrayList<>();
        businessesInCity.forEach(i -> geoLocations.add(new BusinessGeo(i)));

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
        ChainHashMap<String,Business> map = new ChainHashMap<String,Business>(1330); // should get the category size from the repositry
        ArrayList<String> c = new ArrayList<>();
        final long startTime = System.currentTimeMillis();

        for(Business b : bs){
            
            // Iterator categories = business.getCategories().iterator();
            for(Category category: b.getCategories()){
                String cno = category.getCategoryName(); // use as the key
                int hash = (int) ((Math.abs(cno.hashCode())) % 1330); // use as hash
                map.bucketPut(hash, b.getBusinessId(), b);
                if(!c.contains(cno)){
                    c.add(cno);
                }
                //logger.info(cno);
                //logger.info(Integer.toString(hash));
                //UnsortedTableMap<Integer,Business> businessEntry = new unsortedTableMap<Integer,Business>;

            }
            
        }
        //Iterator<Entry<Integer,Business>> iter = map.entrySet().iterator();
        UnsortedTableMap<String,Business>[] buckets = map.getTable();
        //logger.info(Integer.toString(buckets.length));
        int max =0;
        String result = null;
        for(String cno: c){
            int hash = (int) ((Math.abs(cno.hashCode())) % 1330);
            if(buckets[hash] != null){
                //logger.info(cno);
                 UnsortedTableMap<String,Business> table = buckets[hash];
                int size = table.size();
                //logger.info(Integer.toString(size));

            if(size > max){
                max = size;
                result = cno;
                //logger.info(cno);
                //logger.info(Integer.toString(max));
            }
            }
           
        }
        final long endTime = System.currentTimeMillis();
        logger.info("Total execution time: " + (endTime - startTime));
        if(result == null){
            String ans = "no reuslt";
            return ans;
        }
        return result;
    }

}