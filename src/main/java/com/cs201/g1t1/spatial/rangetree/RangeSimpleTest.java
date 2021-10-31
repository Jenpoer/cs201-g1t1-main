package com.cs201.g1t1.spatial.rangetree;

import java.util.*;

import com.cs201.g1t1.spatial.Point;
import com.cs201.g1t1.spatial.Rectangle;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RangeSimpleTest {
    @Autowired
    private RangeTree rangeTree;

    Logger logger = LoggerFactory.getLogger(RangeSimpleTest.class);

    @GetMapping("/idiot")
    public List<Point> pointsInARectangle() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(30.0, 40.0));
        points.add(new Point(5.0, 25.0));
        points.add(new Point(10.0, 12.0));
        points.add(new Point(70.0, 70.0));
        points.add(new Point(50.0, 30.0));
        points.add(new Point(35.0, 45.0));

        rangeTree.buildFrom2DPoints(points);

        rangeTree.preorderTraversal((RangeNode)rangeTree.getRoot());

        Double[] pointMin = { 0.0, 0.0 };
        Double[] pointMax = { 29.0, 30.0 };
        Rectangle range = new Rectangle(pointMin, pointMax);
        List<BSTNode> nn = rangeTree.rangeQuery(range);

        List<Point> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            logger.info("Test");
            if (i instanceof BSTNode<?>)
                toReturn.add(((BSTNode<Point>) i).getElement());
        });

        return toReturn;

    }
}
