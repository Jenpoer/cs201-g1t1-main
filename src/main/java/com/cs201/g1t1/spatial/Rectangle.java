/*
 * REFERENCES: 
 * https://www.javatips.net/api/algs4-master/src/main/java/edu/princeton/cs/algs4/RectHV.java
 */

package com.cs201.g1t1.spatial;

import java.util.List;
import org.slf4j.*;

/**
 * Rectangle class to represent regions
 */
public class Rectangle {
    private final double xMin;
    private final double yMin;
    private final double xMax;
    private final double yMax;

    Logger logger = LoggerFactory.getLogger(Rectangle.class);

    public Rectangle(double xMin, double yMin, double xMax, double yMax) {
        if (Double.isNaN(xMin) || Double.isNaN(xMax) || Double.isNaN(yMin) || Double.isNaN(yMax)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        if (xMax < xMin || yMax < yMin) {
            throw new IllegalArgumentException("Invalid rectangle");
        }
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public Rectangle(Rectangle that) {
        this.xMax = that.xMax;
        this.xMin = that.xMin;
        this.yMin = that.yMin;
        this.yMax = that.yMax;
    }

    public Rectangle(Double[] min, Double[] max) {
        this(min[0], min[1], max[0], max[1]);
        if (min.length != 2 || max.length != 2) {
            throw new IllegalArgumentException("Must have x and y coordinates");
        }
        if (min == null || max == null) {
            throw new IllegalArgumentException("Both coordinates must be provided");
        }

    }

    public double getXMin() {
        return this.xMin;
    }

    public double getYMin() {
        return this.yMin;
    }

    public double getXMax() {
        return this.xMax;
    }

    public double getYMax() {
        return this.yMax;
    }

    public double width() {
        return xMax - xMin;
    }

    public double height() {
        return yMax - yMin;
    }

    /**
     * Method to check if a rectangle intersects another rectangle
     * 
     * @param that the other rectangle
     * @return true if intersect, false if not
     */
    public boolean intersects(Rectangle that) {
        return this.xMin <= that.xMax && this.xMax >= that.xMin && this.yMax >= that.yMin && this.yMin <= that.yMax;
    }

    /**
     * Method to check if a rectangle is fully contained in another rectangle
     * 
     * @param that the other rectangle
     * @return true if intersect, false if not
     */
    public boolean contains(Rectangle that) {
        Double[] thatMinPoint = { that.xMin, that.yMin };
        Double[] thatMaxPoint = { that.xMax, that.yMax };
        return this.contains(thatMinPoint) && this.contains(thatMaxPoint);
    }

    public boolean contains(Double[] point) {
        return (point[0] >= xMin) && (point[0] <= xMax) && (point[1] >= yMin) && (point[1] <= yMax);
    }

    public double distanceTo(Double[] point) {
        return Math.sqrt(distanceSquaredTo(point));
    }

    public double distanceSquaredTo(Double[] point) {
        double dx = 0.0, dy = 0.0;
        if (point[0] < xMin)
            dx = point[0] - xMin;
        else if (point[0] > xMax)
            dx = point[0] - xMax;
        if (point[1] < yMin)
            dy = point[1] - yMin;
        else if (point[1] > yMax)
            dy = point[1] - yMax;
        return dx * dx + dy * dy;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (other == null)
            return false;
        if (other.getClass() != this.getClass())
            return false;
        Rectangle that = (Rectangle) other;
        if (this.xMin != that.xMin)
            return false;
        if (this.yMin != that.yMin)
            return false;
        if (this.xMax != that.xMax)
            return false;
        if (this.yMax != that.yMax)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash1 = ((Double) xMin).hashCode();
        int hash2 = ((Double) yMin).hashCode();
        int hash3 = ((Double) xMax).hashCode();
        int hash4 = ((Double) yMax).hashCode();
        return 31 * (31 * (31 * hash1 + hash2) + hash3) + hash4;
    }

    @Override
    public String toString() {
        return String.format("~~Left (xMin): %f, Right (xMax): %f, Bottom (yMin): %f, Top (yMax): %f~~", xMin, xMax,
                yMin, yMax);
    }

}
