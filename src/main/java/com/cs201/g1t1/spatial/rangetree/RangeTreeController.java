package com.cs201.g1t1.spatial.rangetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.cs201.g1t1.model.*;
import com.cs201.g1t1.repository.*;
import com.cs201.g1t1.spatial.*;

@RestController
public class RangeTreeController {
    @Autowired
    private BusinessRepository businessRepository;

    /**
     * Ranged query test for Range Tree 
     * Hard-coded to the city of Austin
     * Hard-coded with a specific rectangle
     * For an implementation that responds to user input, see: UIController
     * 
     * @return list of businesses within the specified rectangle
     */
    @GetMapping("/range-tree/range-query")
    public List<Business> getRangeOfBusinesses() {
        List<Business> businessesInCity = businessRepository.findByCity("Austin");

        List<BusinessGeo> geoLocations = new ArrayList<>();
        businessesInCity.forEach(i -> geoLocations.add(new BusinessGeo(i)));

        RangeTree<BusinessGeo> rangeTree = new RangeTree<>(0);

        rangeTree.construct(geoLocations);

        Double[] pointMin = { -97.691, 30.3335 };
        Double[] pointMax = { -97.678, 30.3336 };
        Rectangle range = new Rectangle(pointMin, pointMax);
        Set<BusinessGeo> nn = rangeTree.rangeQuery(range);

        List<Business> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            toReturn.add((i).getBusiness());
        });

        return toReturn;
    }

    /**
     * Simple test of the range query method of Range Tree using traditional 2D cartesian coordinates
     * Hard-coded with a certain number of points and a certain rectangle
     * 
     * @return list of points within the given rectangle
     */
    @GetMapping("/range-tree/simple")
    public List<Point> pointsInARectangle() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(30.0, 40.0));
        points.add(new Point(5.0, 25.0));
        points.add(new Point(10.0, 12.0));
        points.add(new Point(70.0, 70.0));
        points.add(new Point(50.0, 30.0));
        points.add(new Point(35.0, 45.0));

        RangeTree rangeTree = new RangeTree(0);
        rangeTree.construct(points);

        Double[] pointMin = { 0.0, 0.0 };
        Double[] pointMax = { 29.0, 30.0 };
        Rectangle range = new Rectangle(pointMin, pointMax);
        Set<Point> nn = rangeTree.rangeQuery(range);

        List<Point> toReturn = new ArrayList<>(nn);

        return toReturn;

    }
}
