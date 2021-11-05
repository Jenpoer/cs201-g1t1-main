/*
 * REFERENCES: 
 * https://github.com/stanislav-antonov/kdtree/blob/master/src/pse/KdTree.java
 * http://www.cs.utah.edu/~lifeifei/cis5930/kdtree.pdf
 */

package com.cs201.g1t1.spatial.kdtree;

import java.util.*;

import com.cs201.g1t1.spatial.Dimensional;

import org.slf4j.*;

/**
 * Abstract class for KD-tree, generalizable to many dimensions
 */
public abstract class KDTree {

    private KDTreeNode<? extends Dimensional> root;

    private int dimensions;

    Logger logger = LoggerFactory.getLogger(KDTree.class);

    public KDTree(int dimensions) {
        this.dimensions = dimensions;
    }

    public abstract void build(List<? extends Dimensional> points);

    public KDTreeNode getRoot() {
        return this.root;
    }

    public void setRoot(KDTreeNode<? extends Dimensional> root) {
        this.root = root;
    }

    protected abstract void updateRegions();

}
