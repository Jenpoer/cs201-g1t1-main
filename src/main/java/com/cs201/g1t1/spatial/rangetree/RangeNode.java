package com.cs201.g1t1.spatial.rangetree;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Node;

public class RangeNode<T extends Dimensional> extends BSTNode<T> {

    private BSTCoords bst;

    public RangeNode(T element) {
        super(element);
    }

    public RangeNode(BSTNode<T> bstNode) {
        super(bstNode.getElement());
        super.setLeftNode(bstNode.getLeftNode());
        super.setRightNode(bstNode.getRightNode());
        this.bst = null;
    }

    public BSTCoords getBst() {
        return this.bst;
    }

    public void setBst(BSTCoords bst) {
        this.bst = bst;
    }

    public Double getX() {
        return super.getCoords()[0];
    }

    public Double getY() {
        return super.getCoords()[1];
    }

}
