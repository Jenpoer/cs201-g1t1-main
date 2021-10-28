package com.cs201.g1t1.kdtree;

public class Point implements Dimensional {
    private Double[] coords;

    public Point(Double[] coords) {
        this.coords = coords;
    }

    public Point(Double x, Double y) {
        this.coords = new Double[2];
        this.coords[0] = x;
        this.coords[1] = y;
    }

    @Override
    public Double[] getCoords() {
        return this.coords;
    }

    public Double getX() {
        return this.coords[0];
    }

    public Double getY() {
        return this.coords[1];
    }

    public void setCoords(Double[] coords) {
        this.coords = coords;
    }

    public void setCoords(Double x, Double y) {
        this.coords[0] = x;
        this.coords[1] = y;
    }

    public void setX(Double x) {
        this.coords[0] = x;
    }

    public void setY(Double y) {
        this.coords[1] = y;
    }
}
