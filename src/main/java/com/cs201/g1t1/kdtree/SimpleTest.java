package com.cs201.g1t1.kdtree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import org.slf4j.*;

@RestController
public class SimpleTest {

    @Autowired
    private KDTree2D kdTree;

    Logger logger = LoggerFactory.getLogger(SimpleTest.class);

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

        kdTree.preorderTraversal((KDTree2DNode) kdTree.getRoot());

        Double[] pointMin = { 0.0, 0.0 };
        Double[] pointMax = { 29.0, 30.0 };
        Rectangle range = new Rectangle(pointMin, pointMax);
        Set<KDTree2DNode<?>> nn = kdTree.rangeQuery(range);

        Rectangle dummy = new Rectangle(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 35.0,
                Double.POSITIVE_INFINITY);

        logger.info(Boolean.toString(range.intersects(dummy)));
        logger.info("this.xMin <= that.xMax: " + (dummy.getXMin() <= range.getXMax()));
        logger.info("this.xMax >= that.xMin: " + (dummy.getXMax() >= range.getXMin()));
        logger.info("" + dummy.getXMax() + ">=" + range.getXMin());
        logger.info("this.yMax >= that.yMin: " + (dummy.getYMax() >= range.getYMin()));
        logger.info("this.yMin <= that.yMax: " + (dummy.getYMin() <= range.getYMax()));

        List<Point> toReturn = new ArrayList<>();

        nn.forEach(i -> {
            if (i instanceof KDTree2DNode<?>) {
                toReturn.add(((KDTreeNode<Point>) i).getElement());
            }
        });

        return toReturn;

    }

}
