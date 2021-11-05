package com.cs201.g1t1.spatial.kdtree;

import java.util.Arrays;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Node;

/**
 * Class for nodes of KD-tree in any dimension Specific dimension nodes should
 * extend this class (e.g. see KDTree2DNode)
 */
public class KDTreeNode<T extends Dimensional> implements Dimensional, Node<T> {

    private KDTreeNode<T> parentNode;
    private KDTreeNode<T> leftNode;
    private KDTreeNode<T> rightNode;

    private int depth;

    private int axis;

    private T element;

    public KDTreeNode(T element) {
        this.element = element;
    }

    public KDTreeNode(KDTreeNode<T> parentNode, KDTreeNode<T> leftNode, KDTreeNode<T> rightNode, T element, int depth,
            int axis) {
        this.parentNode = parentNode;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.element = element;
        this.depth = depth;
        this.axis = axis;
    }

    @Override
    public T getElement() {
        return this.element;
    }

    @Override
    public void setElement(T element) {
        this.element = element;

    }

    public KDTreeNode<T> getParentNode() {
        return this.parentNode;
    }

    public void setParentNode(KDTreeNode<T> parentNode) {
        this.parentNode = parentNode;
    }

    public KDTreeNode<T> getLeftNode() {
        return this.leftNode;
    }

    public void setLeftNode(KDTreeNode<T> leftNode) {
        this.leftNode = leftNode;
    }

    public KDTreeNode<T> getRightNode() {
        return this.rightNode;
    }

    public void setRightNode(KDTreeNode<T> rightNode) {
        this.rightNode = rightNode;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public Double[] getCoords() {
        return element.getCoords();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof KDTreeNode) {
            Double[] c1 = ((KDTreeNode) o).getCoords();
            Double[] c2 = this.getCoords();

            return Arrays.equals(c1, c2);
        }

        return false;
    }

    public boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }

    public int getAxis() {
        return this.axis;
    }

    public void setAxis(int axis) {
        this.axis = axis;
    }

}
