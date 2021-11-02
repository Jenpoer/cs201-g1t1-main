package com.cs201.g1t1.spatial.rangetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.cs201.g1t1.model.*;
import com.cs201.g1t1.repository.*;
import com.cs201.g1t1.spatial.*;

@RestController
public class RangeTreeSearchBusinesses {
    @Autowired
    private BusinessRepository businessRepository;

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
}
