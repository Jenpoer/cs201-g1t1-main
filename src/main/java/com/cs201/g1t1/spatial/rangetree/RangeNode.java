package com.cs201.g1t1.spatial.rangetree;

import java.util.List;

import com.cs201.g1t1.spatial.Dimensional;

public class RangeNode<T extends Dimensional> implements Dimensional {

    private RangeNode left;
    private RangeNode right;

    private RangeTree auxTree = null;

    private T element;

    private List<T> elements;

    public RangeNode(T element, List<T> elements) {
        this.element = element;
        this.elements = elements;
    }

    @Override
    public Double[] getCoords() {
        return element.getCoords();
    }

    public RangeNode getLeft() {
        return this.left;
    }

    public void setLeft(RangeNode left) {
        this.left = left;
    }

    public RangeNode getRight() {
        return this.right;
    }

    public void setRight(RangeNode right) {
        this.right = right;
    }

    public RangeTree getAuxTree() {
        return this.auxTree;
    }

    public void setAuxTree(RangeTree auxTree) {
        this.auxTree = auxTree;
    }

    public T getElement() {
        return this.element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public List<T> getElements() {
        return this.elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public Double getX() {
        return getCoords()[0];
    }

    public Double getY() {
        return getCoords()[1];
    }

}
