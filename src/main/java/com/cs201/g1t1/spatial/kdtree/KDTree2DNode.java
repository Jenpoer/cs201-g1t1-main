package com.cs201.g1t1.spatial.kdtree;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Rectangle;

import org.slf4j.*;

/**
 * Class for node of 2D-Tree
 */
public class KDTree2DNode<T extends Dimensional> extends KDTreeNode<T> {

    private Rectangle region;

    Logger logger = LoggerFactory.getLogger(KDTree2DNode.class);

    public KDTree2DNode(KDTreeNode<T> parentNode, KDTreeNode<T> leftNode, KDTreeNode<T> rightNode, T element, int depth,
            int axis, Rectangle region) {
        super(parentNode, leftNode, rightNode, element, depth, axis);
        this.region = region;
    }

    public KDTree2DNode(KDTreeNode<T> parentNode, KDTreeNode<T> leftNode, KDTreeNode<T> rightNode, T element,
            int depth) {
        super(parentNode, leftNode, rightNode, element, depth, 0);
        this.region = new Rectangle(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY);
    }

    public KDTree2DNode(KDTreeNode<T> parentNode, KDTreeNode<T> leftNode, KDTreeNode<T> rightNode, T element, int depth,
            int axis) {
        super(parentNode, leftNode, rightNode, element, depth, axis);
        this.region = new Rectangle(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY);
    }

    public Rectangle getRegion() {
        return this.region;
    }

    /**
     * Method that looks at split nodes and updates their region
     * 
     * @param child       child of the node
     * @param isLeftChild if child is a left child
     */
    private void split(KDTree2DNode<T> child, boolean isLeftChild) {
        if (super.getAxis() == 0) {
            if (isLeftChild) {
                // Bound the right part
                child.region = new Rectangle(this.region.getXMin(), this.region.getYMin(), this.getCoords()[0],
                        this.region.getYMax());
            } else {
                // Bound the left part
                child.region = new Rectangle(this.getCoords()[0], this.region.getYMin(), this.region.getXMax(),
                        this.region.getYMax());
            }
        } else {
            if (isLeftChild) {
                // Bound the top part
                child.region = new Rectangle(this.region.getXMin(), this.region.getYMin(), this.region.getXMax(),
                        this.getCoords()[1]);

            } else {
                // Bound the bottom part
                child.region = new Rectangle(this.region.getXMin(), this.getCoords()[1], this.region.getXMax(),
                        this.region.getYMax());
            }
        }
    }

    /**
     * Wrapper method to convert KDTreeNode into KDTree2DNode, but allows null
     * 
     * @param node KDTreeNode
     * @return node casted into KDTree2DNode
     */
    private KDTree2DNode<T> validateAllowNull(KDTreeNode<T> node) {
        if (node == null)
            return null;
        return validate(node);
    }

    /**
     * Wrapper method to convert KDTreeNode into KDTree2DNode, but does NOT allow
     * null
     * 
     * @param node KDTreeNode
     * @return node casted into KDTree2DNode
     */
    private KDTree2DNode<T> validate(KDTreeNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null");
        }
        if (!(node instanceof KDTree2DNode))
            throw new IllegalArgumentException("Node must be an instance of KDTree2DNode");
        return (KDTree2DNode<T>) node;
    }

    /**
     * Method to update region
     */
    protected void specialUpdateRectangle() {
        KDTree2DNode<T> left = validateAllowNull(super.getLeftNode());
        KDTree2DNode<T> right = validateAllowNull(super.getRightNode());
        if (left != null) {
            split(left, true);
            left.specialUpdateRectangle();
        }
        if (right != null) {
            split(right, false);
            right.specialUpdateRectangle();
        }
    }

    public void setRegion(Rectangle region) {
        this.region = region;
    }

}
