package com.cs201.g1t1.spatial.kdtree;

import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.spatial.*;
import com.cs201.g1t1.model.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KDTreeController {

    @Autowired
    private BusinessRepository businessRepository;

    /**
     * Ranged query test for KD-Tree
     * Hard-coded to the city of Austin
     * Hard-coded with a specific rectangle
     * For an implementation that responds to user input, see: UIController
     * 
     * @return list of businesses within the specified rectangle
     */
    @GetMapping("/kd-tree/range-query")
    public List<Business> getRangeOfBusinesses() {
        List<Business> businessesInCity = businessRepository.findByCity("Austin");

        List<BusinessGeo> geoLocations = new ArrayList<>();
        businessesInCity.forEach(i -> geoLocations.add(new BusinessGeo(i)));

        KDTree2D kdTree2D = new KDTree2D();

        kdTree2D.build(geoLocations);

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

    /**
     * Simple test of the range query method of KD-Tree using traditional 2D cartesian coordinates
     * Hard-coded with a certain number of points and a certain rectangle
     * 
     * @return list of points within the given rectangle
     */
    @GetMapping("/kd-tree/simple-test")
    public List<Point> pointsInARectangle() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(30.0, 40.0));
        points.add(new Point(5.0, 25.0));
        points.add(new Point(10.0, 12.0));
        points.add(new Point(70.0, 70.0));
        points.add(new Point(50.0, 30.0));
        points.add(new Point(35.0, 45.0));

        KDTree2D kdTree = new KDTree2D();

        kdTree.build(points);

        kdTree.preorderTraversal((KDTree2DNode) kdTree.getRoot());

        Double[] pointMin = { 0.0, 0.0 };
        Double[] pointMax = { 29.0, 30.0 };
        Rectangle range = new Rectangle(pointMin, pointMax);
        Set<KDTree2DNode<?>> nn = kdTree.rangeQuery(range);

        List<Point> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            if (i instanceof KDTree2DNode<?>) {
                toReturn.add(((KDTreeNode<Point>) i).getElement());
            }
        });

        return toReturn;

    }

}
