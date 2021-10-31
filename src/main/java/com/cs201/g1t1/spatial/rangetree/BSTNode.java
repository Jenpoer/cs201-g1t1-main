package com.cs201.g1t1.spatial.rangetree;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Node;

public class BSTNode<T extends Dimensional> implements Dimensional, Node<T> {
    private T element;
    private BSTNode<T> leftNode;
    private BSTNode<T> rightNode;

    public BSTNode(T element) {
        this.element = element;
    }

    public BSTNode getLeftNode() {
        return this.leftNode;
    }

    public void setLeftNode(BSTNode leftNode) {
        this.leftNode = leftNode;
    }

    public BSTNode getRightNode() {
        return this.rightNode;
    }

    public void setRightNode(BSTNode rightNode) {
        this.rightNode = rightNode;
    }

    public boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }

    @Override
    public Double[] getCoords() {
        return element.getCoords();
    }

    @Override
    public T getElement() {
        return element;
    }

    @Override
    public void setElement(T element) {
        this.element = element;

    }

}
