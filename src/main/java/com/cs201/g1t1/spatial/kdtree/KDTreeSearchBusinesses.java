package com.cs201.g1t1.spatial.kdtree;

import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.repository.CategoryRepository;
import com.cs201.g1t1.spatial.BusinessGeo;
import com.cs201.g1t1.spatial.Rectangle;
import com.cs201.g1t1.model.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class KDTreeSearchBusinesses {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KDTree2D kdTree2D;

    @GetMapping("/kd-tree")
    public List<Business> getBusinessesClosestToPoint() {
        List<Business> businessesInCity = businessRepository.findByCity("Austin");

        List<BusinessGeo> geoLocations = new ArrayList<>();
        businessesInCity.forEach(i -> geoLocations.add(new BusinessGeo(i)));

        kdTree2D.build(geoLocations);

        Double[] point = { 30.0, -97.0 };
        List<KDTreeNode> nn = kdTree2D.kNearestNeighbour(point, 10);

        List<Business> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            if (i instanceof KDTreeNode<?>) {
                toReturn.add(((KDTreeNode<BusinessGeo>) i).getElement().getBusiness());
            }
        });
        return toReturn;
    }

    @GetMapping("/kd-tree/range-query")
    public List<Business> getRangeOfBusinesses() {
        List<Business> businessesInCity = businessRepository.findByCity("Austin");

        List<BusinessGeo> geoLocations = new ArrayList<>();
        businessesInCity.forEach(i -> geoLocations.add(new BusinessGeo(i)));

        long KILOBYTE = 1024L;
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory1 = runtime.totalMemory() - runtime.freeMemory();

        kdTree2D.build(geoLocations);

        Runtime runtime2 = Runtime.getRuntime();
        runtime2.gc();
        long memory2 = runtime2.totalMemory() - runtime2.freeMemory();

        Logger logger = LoggerFactory.getLogger(KDTreeSearchBusinesses.class);
        logger.info("Memory used: {}KB", (memory2 - memory1)/KILOBYTE);

        Double[] pointMin = { -97.691, 30.3335 };
        Double[] pointMax = { -97.678, 30.3336 };
        Rectangle range = new Rectangle(pointMin, pointMax);
        Set<KDTree2DNode<?>> nn = kdTree2D.rangeQuery(range);

        List<Business> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            if (i instanceof KDTree2DNode<?>) {
                toReturn.add(((KDTreeNode<BusinessGeo>) i).getElement().getBusiness());
            }
        });
        return toReturn;
    }
}
