package com.cs201.g1t1.spatial.kdtree;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Rectangle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.*;

@Component
public class KDTree2D extends KDTree {
    private KDTree2DKNN nearestNeighborUtil;

    Logger logger = LoggerFactory.getLogger(KDTree2D.class);

    @Autowired
    public KDTree2D(KDTree2DKNN nearestNeighborUtil) {
        super(2);
        this.nearestNeighborUtil = nearestNeighborUtil;
    }

    @Override
    public KDTreeNode nearestNeighbour(Double[] point) {
        if (super.getRoot() == null) {
            throw new IllegalStateException();
        }

        return nearestNeighborUtil.nearest(super.getRoot(), point);
    }

    @Override
    public List<KDTreeNode> kNearestNeighbour(Double[] point, int k) {
        if (super.getRoot() == null) {
            throw new IllegalStateException();
        }

        return nearestNeighborUtil.kNearest(super.getRoot(), point, k);
    }

    private KDTree2DNode<? extends Dimensional> validateNodeAllowNull(KDTreeNode node) {
        if (node == null) {
            return null;
        }
        return validateNode(node);
    }

    private KDTree2DNode<? extends Dimensional> validateNode(KDTreeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null");
        }
        if (!(node instanceof KDTree2DNode))
            throw new IllegalArgumentException("Node must be an instance of KDTree2DNode");
        return (KDTree2DNode<?>) node;
    }

    @Override
    protected void updateRegions() {
        KDTree2DNode root = validateNode(super.getRoot());
        root.specialUpdateRectangle();
    }

    public Set<KDTree2DNode<? extends Dimensional>> rangeQuery(Rectangle range) {
        Set<KDTree2DNode<? extends Dimensional>> found = new HashSet<>();
        rangeQuery(range, validateNode(super.getRoot()), found);
        return found;

    }

    public void rangeQuery(Rectangle range, KDTree2DNode<? extends Dimensional> node,
            Set<KDTree2DNode<? extends Dimensional>> found) {

        if (node.isLeaf()) {
            if (range.contains(node.getCoords())) {
                found.add(node);
            }
            return;
        }

        if (node.getLeftNode() != null) {
            if (range.contains(validateNode(node.getLeftNode()).getRegion())) {
                // report subtree at node.getLeftNode()
                // logger.info("region of left child fully contained in query range");
                found.addAll(reportSubtree(validateNode(node.getLeftNode())));
            } else if (range.intersects(validateNode(node.getLeftNode()).getRegion())) {
                // logger.info("region of left child intersects query range");
                if (range.contains(node.getCoords())) {
                    found.add(node);
                }
                rangeQuery(range, validateNode(node.getLeftNode()), found);
            }
        }

        if (node.getRightNode() != null) {
            if (range.contains(validateNode(node.getRightNode()).getRegion())) {
                // report subtree at node.getRightNode()
                // logger.info("region of right child fully contained in query range");
                found.addAll(reportSubtree(validateNode(node.getRightNode())));
            } else if (range.intersects(validateNode(node.getRightNode()).getRegion())) {
                // logger.info("region of right child intersects query range");
                if (range.contains(node.getCoords())) {
                    found.add(node);
                }
                rangeQuery(range, validateNode(node.getRightNode()), found);
            }
        }
    }

    public Set<KDTree2DNode<? extends Dimensional>> reportSubtree(KDTree2DNode<? extends Dimensional> startNode) {
        Set<KDTree2DNode<? extends Dimensional>> snapshot = new HashSet<>();
        inorderTraversal(startNode, snapshot);
        return snapshot;
    }

    private void inorderTraversal(KDTree2DNode<? extends Dimensional> node,
            Set<KDTree2DNode<? extends Dimensional>> snapshot) {
        if (node == null) {
            return;
        }

        inorderTraversal(validateNodeAllowNull(node.getLeftNode()), snapshot);
        snapshot.add(node);
        inorderTraversal(validateNodeAllowNull(node.getRightNode()), snapshot);
    }

    // ----------------- NESTED COMPARATOR CLASS -----------------
    private static class LocationComparator implements Comparator<Dimensional> {
        private final int axis;

        LocationComparator(int axis) {
            this.axis = axis;
        }

        @Override
        public int compare(Dimensional o1, Dimensional o2) {
            return o1.getCoords()[axis].compareTo(o2.getCoords()[axis]);
        }

    }

    // ------------- END OF NESTED COMPARATOR CLASS -------------
    @Override
    public void build(List<? extends Dimensional> points) {
        super.setRoot(buildRecursive(points, 0));
        updateRegions();
    }

    private KDTree2DNode<? extends Dimensional> buildRecursive(List<? extends Dimensional> items, int depth) {
        if (items.isEmpty()) {
            return null;
        }

        // Axis changes with every level of the tree
        // if you have 2 dimensions, alternate between them
        final int axis = depth % 2;

        // Sort coordinates based on the specified axis
        Collections.sort(items, new LocationComparator(axis));

        final int length = items.size();

        // Get the median
        final int medianIndex = length / 2;
        final Dimensional medianPoint = items.get(medianIndex);

        // Recursively build the left subtree
        final List<? extends Dimensional> beforeMedianPoints = items.subList(0, medianIndex);
        final KDTree2DNode leftChild = validateNodeAllowNull(buildRecursive(beforeMedianPoints, depth + 1));

        // Recursively build the right subtree
        final List<? extends Dimensional> afterMedianPoints = items.subList(medianIndex + 1, length);
        final KDTree2DNode rightChild = length > 1 ? validateNodeAllowNull(buildRecursive(afterMedianPoints, depth + 1))
                : null;

        // Set the root of the subtree
        return new KDTree2DNode<>(null, leftChild, rightChild, medianPoint, depth, axis);
    }

    public void preorderTraversal(KDTree2DNode<? extends Dimensional> node) {
        if (node != null) {
            logger.info("*" + node.getElement().getCoords()[0] + "," + node.getElement().getCoords()[1] + "*");
            logger.info("Region: " + node.getRegion().toString());
            preorderTraversal((KDTree2DNode) node.getLeftNode());
            preorderTraversal((KDTree2DNode) node.getRightNode());
        }
    }

}
