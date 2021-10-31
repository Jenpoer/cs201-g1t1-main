package com.cs201.g1t1.spatial.rangetree;

import java.util.Comparator;

public class NodeComparator implements Comparator<BSTNode> {
    private int axis;

    public NodeComparator(int axis) {
        this.axis = axis;
    }

    @Override
    public int compare(BSTNode o1, BSTNode o2) {
        return o1.getCoords()[axis].compareTo(o2.getCoords()[axis]);
    }

}
