// REFERENCES:
// https://algs4.cs.princeton.edu/92search/RangeSearch.java.html

package com.cs201.g1t1.spatial.rangetree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Node;

import org.slf4j.*;

public class BSTCoords {
    private BSTNode root;

    private int axis;

    Logger logger = org.slf4j.LoggerFactory.getLogger(BSTCoords.class);

    // -----------------------------------------
    // Constructors
    // -----------------------------------------
    public BSTCoords(int axis) {
        this.axis = axis;
        this.root = null;
    }

    public BSTCoords(List<? extends BSTNode> nodes, int axis) {
        this.axis = axis;
        buildBST(nodes);
    }

    public void buildBST(List<? extends BSTNode> nodes) {
        this.root = build(nodes);
    }

    public BSTNode build(List<? extends BSTNode> nodes) {
        if (nodes.isEmpty()) {
            return null;
        }

        Collections.sort(nodes, new NodeComparator(axis));

        final int length = nodes.size();

        // Get the median
        final int medianIndex = length / 2;
        final BSTNode medianPoint = nodes.get(medianIndex);

        // Recursively build the left subtree
        final List<? extends BSTNode> beforeMedianPoints = nodes.subList(0, medianIndex);
        final BSTNode leftChild = build(beforeMedianPoints);

        // Recursively build the right subtree
        final List<? extends BSTNode> afterMedianPoints = nodes.subList(medianIndex + 1, length);
        final BSTNode rightChild = length > 1 ? build(afterMedianPoints) : null;

        // Set the root of the subtree
        medianPoint.setLeftNode(leftChild);
        medianPoint.setRightNode(rightChild);
        return medianPoint;
    }

    public void preorder(BSTNode node) {
        if (node != null) {
            logger.info("--y: " + (node.getCoords()[1]).toString() + " x: " + (node.getCoords()[0]).toString() + "--");
            preorder(node.getLeftNode());
            preorder(node.getRightNode());
        }
    }

    // -----------------------------------------
    // BST search
    // -----------------------------------------
    // public boolean contains(Double key) {
    // return (get(key) != null);
    // }

    // public Double get(Double key) {
    // return get(root, key);
    // }

    // private Double get(BSTNode node, Double key) {
    // if (node == null) {
    // return null;
    // }

    // int cmp = key.compareTo(node.getCoords()[axis]);
    // if (cmp == 0)
    // return node.value;
    // else if (cmp < 0)
    // return get(node.leftNode, key);
    // else
    // return get(node.rightNode, key);
    // }

    private Double getKeyOf(BSTNode node) {
        return node.getCoords()[axis];
    }

    public BSTNode getRoot() {
        return this.root;
    }

    public void setRoot(BSTNode root) {
        this.root = root;
    }

    // -----------------------------------------
    // Range search
    // -----------------------------------------

    public List<BSTNode> range(Double from, Double to) {
        List<BSTNode> buffer = new ArrayList<>();
        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException("Starting key must be smaller than ending key");
        } else {
            range(from, to, root, buffer);
        }
        return buffer;
    }

    private void range(Double from, Double to, BSTNode node, List<BSTNode> buffer) {
        if (node == null)
            return;
        if (!node.isLeaf()) {
            if (getKeyOf(node).compareTo(from) < 0)
                // node's key is less than fromKey, so any relevant entries are to the right
                range(from, to, node.getRightNode(), buffer);
            else {
                range(from, to, node.getLeftNode(), buffer); // first consider left subtree
                if (getKeyOf(node).compareTo(to) < 0) { // p is within range
                    buffer.add(node); // so add it to buffer, and consider
                    range(from, to, node.getRightNode(), buffer); // right subtree as well
                }
            }
        }
    }
}
