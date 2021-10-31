package com.cs201.g1t1.spatial.rangetree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Rectangle;

import org.slf4j.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava.Range;
import org.springframework.stereotype.Component;

@Component
public class RangeTree extends BSTCoords {

    private RangeNode<? extends Dimensional> root;

    Logger logger = LoggerFactory.getLogger(RangeTree.class);

    // -----------------------------------------
    // Constructors
    // -----------------------------------------
    public RangeTree() {
        super(0);
    }

    public void buildFrom2DPoints(List<? extends Dimensional> points) {
        List<RangeNode> nodes = points.stream().map(p -> new RangeNode(p))
                .collect(Collectors.toCollection(ArrayList::new));
        super.setRoot(buildXTree(nodes));
    }

    private RangeNode buildXTree(List<RangeNode> nodes) {
        BSTCoords yBST = new BSTCoords(nodes, 1);

        if (nodes.isEmpty()) {
            return null;
        }

        Collections.sort(nodes, new NodeComparator(0));

        final int length = nodes.size();

        // Get the median
        final int medianIndex = length / 2;
        final RangeNode medianPoint = nodes.get(medianIndex);

        // Recursively build the left subtree
        final List<RangeNode> beforeMedianPoints = nodes.subList(0, medianIndex);
        final RangeNode leftChild = buildXTree(beforeMedianPoints);

        // Recursively build the right subtree
        final List<RangeNode> afterMedianPoints = nodes.subList(medianIndex + 1, length);
        final RangeNode rightChild = length > 1 ? buildXTree(afterMedianPoints) : null;

        // Set the root of the subtree
        medianPoint.setLeftNode(leftChild);
        medianPoint.setRightNode(rightChild);
        medianPoint.setBst(yBST);
        logger.info("x: " + (medianPoint.getCoords()[0]).toString());
        return medianPoint;
    }

    public void preorderTraversal(RangeNode node) {
        if (node != null) {
            logger.info("~~x: " + (node.getCoords()[0]).toString() + "y: " + (node.getCoords()[1]).toString() + "~~");
            BSTCoords auxTree = node.getBst();
            auxTree.preorder(auxTree.getRoot());
            preorderTraversal(validate(node.getLeftNode()));
            preorderTraversal(validate(node.getRightNode()));
        }
    }

    // -----------------------------------------
    // Range Search
    // -----------------------------------------

    // Get all points in range
    public List<BSTNode> rangeQuery(Rectangle range) {
        List<BSTNode> nodes = new ArrayList<>();
        rangeQuery(range, nodes);
        return nodes;

    }

    public void rangeQuery(Rectangle rect, List<BSTNode> list) {
        Double xMax = rect.getXMax();
        Double xMin = rect.getXMin();
        // find splitting node h where h.x is in the x-interval
        RangeNode h = root;
        while (h != null && !(h.getX() >= xMin && h.getX() <= xMax)) {
            if (xMax.compareTo(h.getX()) < 0) {
                h = validate(h.getLeftNode());
            } else if ((h.getX()).compareTo(xMin) < 0) {
                h = validate(h.getRightNode());
            }

        }
        if (h == null)
            return;

        if (rect.contains(h.getCoords())) {
            list.add(h);
        }

        queryL(validate(h.getLeftNode()), rect, list);
        queryR(validate(h.getRightNode()), rect, list);
    }

    // find all keys >= xmin in subtree rooted at h
    private void queryL(RangeNode h, Rectangle rect, List<BSTNode> list) {
        if (h == null)
            return;
        if (rect.contains(h.getCoords())) {
            list.add(h);
        }
        if (h.getX().compareTo(rect.getXMin()) < 0) {
            enumerate(validate(h.getRightNode()), rect, list);
            queryL(validate(h.getLeftNode()), rect, list);
        } else {
            queryL(validate(h.getRightNode()), rect, list);
        }
    }

    // find all keys <= xmax in subtree rooted at h
    private void queryR(RangeNode h, Rectangle rect, List<BSTNode> list) {
        Double xMax = rect.getXMax();

        if (h == null)
            return;
        if (rect.contains(h.getCoords()))
            list.add(h);
        if (xMax.compareTo(h.getX()) > 0) {
            enumerate(validate(h.getLeftNode()), rect, list);
            queryR(validate(h.getRightNode()), rect, list);
        } else {
            queryR(validate(h.getLeftNode()), rect, list);
        }
    }

    // precondition: subtree rooted at h has keys between xmin and xmax
    private void enumerate(RangeNode h, Rectangle rect, List<BSTNode> list) {
        if (h == null)
            return;
        Iterable<BSTNode> bIterable = h.getBst().range(rect.getYMin(), rect.getYMax());
        for (BSTNode y : bIterable) {
            list.add(y);
        }
    }

    private RangeNode validate(BSTNode node) {
        if (node instanceof RangeNode) {
            return (RangeNode) node;
        } else {
            throw new IllegalArgumentException("Node be of type RangeNode");
        }
    }

}
