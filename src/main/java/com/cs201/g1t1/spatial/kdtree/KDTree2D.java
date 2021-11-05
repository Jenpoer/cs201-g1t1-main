/*
 * REFERENCES: 
 * https://github.com/stanislav-antonov/kdtree/blob/master/src/pse/KdTree.java
 * http://www.cs.utah.edu/~lifeifei/cis5930/kdtree.pdf
 */

package com.cs201.g1t1.spatial.kdtree;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Rectangle;

import org.slf4j.*;

/**
 * Class for 2D KD-tree (or kd-tree with k = 2), extends abstract class KDTree
 */
public class KDTree2D extends KDTree {

    Logger logger = LoggerFactory.getLogger(KDTree2D.class);

    public KDTree2D() {
        super(2);
    }

    private KDTree2DNode<? extends Dimensional> validateNodeAllowNull(KDTreeNode node) {
        if (node == null) {
            return null;
        }
        return validateNode(node);
    }

    /**
     * Utility method: converts KDTreeNode to KDTree2DNode if valid
     * 
     * @param node KDTreeNode
     * @return node casted to KDTree2DNode
     */
    private KDTree2DNode<? extends Dimensional> validateNode(KDTreeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null");
        }
        if (!(node instanceof KDTree2DNode))
            throw new IllegalArgumentException("Node must be an instance of KDTree2DNode");
        return (KDTree2DNode<?>) node;
    }

    /**
     * Method to update the regions contained by every node.
     * 
     * Calls specialUpdateRectangle because it is a 2D tree, therefore region is a
     * rectangle
     */
    @Override
    protected void updateRegions() {
        KDTree2DNode root = validateNode(super.getRoot());
        root.specialUpdateRectangle();
    }

    /**
     * Wrapper method to do rangeQuery, called by most functions
     * 
     * @param range rectangle object denoting range to search for
     * @return set of KDTree2DNode's that lie within the range
     */
    public Set<KDTree2DNode<? extends Dimensional>> rangeQuery(Rectangle range) {
        Set<KDTree2DNode<? extends Dimensional>> found = new HashSet<>();
        rangeQuery(range, validateNode(super.getRoot()), found);
        return found;

    }

    /**
     * Recursive method to do rangeQuery
     * 
     * @param range rectangle object denoting range to search for
     * @param node  current node being searched
     * @param found set to add nodes that are found to
     */
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
                found.addAll(reportSubtree(validateNode(node.getLeftNode())));
            } else if (range.intersects(validateNode(node.getLeftNode()).getRegion())) {
                if (range.contains(node.getCoords())) {
                    found.add(node);
                }
                rangeQuery(range, validateNode(node.getLeftNode()), found);
            }
        }

        if (node.getRightNode() != null) {
            if (range.contains(validateNode(node.getRightNode()).getRegion())) {
                // report subtree at node.getRightNode()
                found.addAll(reportSubtree(validateNode(node.getRightNode())));
            } else if (range.intersects(validateNode(node.getRightNode()).getRegion())) {
                if (range.contains(node.getCoords())) {
                    found.add(node);
                }
                rangeQuery(range, validateNode(node.getRightNode()), found);
            }
        }
    }

    /**
     * Wrapper method to report a subtree rooted at a certain node
     * 
     * @param startNode root of subtree
     * @return set of KDTree2DNode's of the subtree
     */
    public Set<KDTree2DNode<? extends Dimensional>> reportSubtree(KDTree2DNode<? extends Dimensional> startNode) {
        Set<KDTree2DNode<? extends Dimensional>> snapshot = new HashSet<>();
        inorderTraversal(startNode, snapshot);
        return snapshot;
    }

    /**
     * Utility method to do inorder traversal and adding nodes into a snapshot
     * 
     * @param node     current node being visited
     * @param snapshot set of found nodes
     */
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
    /**
     * Comparator class for location
     */
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

    /**
     * Wrapper method to construct a 2D-tree, updates regions after building the
     * whole tree
     */
    @Override
    public void build(List<? extends Dimensional> points) {
        super.setRoot(buildRecursive(points, 0));
        updateRegions();
    }

    /**
     * Recursive method to build the tree
     * 
     * @param items points to add into the tree
     * @param depth current depth
     * @return node added into the tree
     */
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

    /**
     * Utility method to do pre-order traversal and print out the element of nodes +
     * their regions
     * 
     * @param node node being visited
     */
    public void preorderTraversal(KDTree2DNode<? extends Dimensional> node) {
        if (node != null) {
            logger.info("*" + node.getElement().getCoords()[0] + "," + node.getElement().getCoords()[1] + "*");
            logger.info("Region: " + node.getRegion().toString());
            preorderTraversal((KDTree2DNode) node.getLeftNode());
            preorderTraversal((KDTree2DNode) node.getRightNode());
        }
    }

}
