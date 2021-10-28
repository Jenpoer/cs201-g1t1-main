package com.cs201.g1t1.kdtree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SimpleTest {

    @Autowired
    private KDTree2D kdTree;

    @GetMapping("/stupid")
    public List<Point> pointsInARectangle() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(30.0, 40.0));
        points.add(new Point(5.0, 25.0));
        points.add(new Point(10.0, 12.0));
        points.add(new Point(70.0, 70.0));
        points.add(new Point(50.0, 30.0));
        points.add(new Point(35.0, 45.0));

        kdTree.build(points);

        Double[] pointMin = { 0.0, 0.0 };
        Double[] pointMax = { 30.0, 30.0 };
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
