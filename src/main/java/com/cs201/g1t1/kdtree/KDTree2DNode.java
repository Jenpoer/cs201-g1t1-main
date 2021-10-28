package com.cs201.g1t1.kdtree;

import org.slf4j.*;

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

    private void split(KDTree2DNode<T> child, boolean isLeftChild) {
        logger.info("Current: " + this.getCoords()[0] + "," + this.getCoords()[1]);
        if (super.getAxis() == 0) {
            if (isLeftChild) {
                // Bound the right part
                logger.info("Left child of x node");
                utilChecker(this.region.getXMin(), this.region.getYMin(), this.getCoords()[0], this.region.getYMax());
                logger.info("Child: " + child.getCoords()[0] + "," + child.getCoords()[1]);
                child.region = new Rectangle(this.region.getXMin(), this.region.getYMin(), this.getCoords()[0],
                        this.region.getYMax());
            } else {
                // Bound the left part
                logger.info("Right child of x node");
                utilChecker(this.getCoords()[0], this.region.getYMin(), this.region.getXMax(), this.region.getYMax());
                logger.info("Child: " + child.getCoords()[0] + "," + child.getCoords()[1]);
                child.region = new Rectangle(this.getCoords()[0], this.region.getYMin(), this.region.getXMax(),
                        this.region.getYMax());
            }
        } else {
            if (isLeftChild) {
                // Bound the top(?) part
                logger.info("Left child of y node");
                utilChecker(this.region.getXMin(), this.getCoords()[1], this.region.getXMax(), this.region.getYMax());
                logger.info("Child: " + child.getCoords()[0] + "," + child.getCoords()[1]);
                child.region = new Rectangle(this.region.getXMin(), this.getCoords()[1], this.region.getXMax(),
                        this.region.getYMax());

            } else {
                // Bound the bottom(?) part
                logger.info("Right child of y node");
                utilChecker(this.region.getXMin(), this.region.getYMin(), this.region.getXMax(), this.getCoords()[1]);
                logger.info("Child: " + this.getCoords()[0] + "," + this.getCoords()[1]);
                child.region = new Rectangle(this.region.getXMin(), this.region.getYMin(), this.region.getXMax(),
                        this.getCoords()[1]);
            }
        }
    }

    private KDTree2DNode<T> validateAllowNull(KDTreeNode<T> node) {
        if (node == null)
            return null;
        return validate(node);
    }

    private KDTree2DNode<T> validate(KDTreeNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null");
        }
        if (!(node instanceof KDTree2DNode))
            throw new IllegalArgumentException("Node must be an instance of KDTree2DNode");
        return (KDTree2DNode<T>) node;
    }

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

    private void utilChecker(double xMin, double yMin, double xMax, double yMax) {
        logger.info("--------------------------------------------------");
        logger.info("xMin: " + Double.toString(xMin));
        logger.info("yMin: " + Double.toString(yMin));
        logger.info("xMax: " + Double.toString(xMax));
        logger.info("yMax: " + Double.toString(yMax));
        logger.info("--------------------------------------------------");
    }

}
