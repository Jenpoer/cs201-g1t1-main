// REFERENCE: https://github.com/DanialDMQ/RangeTree

package com.cs201.g1t1.spatial.rangetree;

import java.util.*;

import com.cs201.g1t1.spatial.Dimensional;
import com.cs201.g1t1.spatial.Rectangle;

import org.springframework.stereotype.Component;

public class RangeTree<T extends Dimensional> {

    private RangeNode root;

    private int by;

    public RangeTree(int by) {
        this.by = by;
    }

    public RangeNode construct(List<T> points) {

        List<T> sorted = new ArrayList<>(points);

        Collections.sort(sorted, new NodeComparator(this.by));

        this.root = this.utilConstructor(sorted);

        return this.root;
    }

    private RangeNode utilConstructor(List<T> points) {

        if (points.size() == 0) {
            return null;
        }

        else if (points.size() == 1) {
            return new RangeNode(points.get(0), points);
        }

        else {
            final int length = points.size();

            // Get the median
            final int medianIndex = length / 2;
            final Dimensional medianPoint = points.get(medianIndex);

            // Recursively build the left subtree
            final List<T> beforeMedianPoints = points.subList(0, medianIndex);
            final RangeNode leftChild = utilConstructor(beforeMedianPoints);

            // Recursively build the right subtree
            final List<T> afterMedianPoints = points.subList(medianIndex + 1, length);
            final RangeNode rightChild = length > 1 ? utilConstructor(afterMedianPoints) : null;

            // Set the root of the subtree
            RangeNode v = new RangeNode(medianPoint, points);
            v.setLeft(leftChild);
            v.setRight(rightChild);
            return v;
        }
    }

    private boolean isLeaf(RangeNode root) {
        if (root == null || (root.getLeft() == null && root.getRight() == null)) {
            return true;
        }

        return false;
    }

    private RangeNode get(RangeNode root, Rectangle range, int by) {
        if (by == 0) {
            while (!(this.isLeaf(root)) && (root.getX() > range.getXMax() || root.getX() < range.getXMin())) {
                if (root.getX() >= range.getXMax()) {
                    root = root.getLeft();
                } else {
                    root = root.getRight();
                }
            }
            return root;
        } else {
            while (!(this.isLeaf(root)) && (root.getY() > range.getYMax() || root.getY() < range.getYMin())) {
                if (root.getY() >= range.getYMax()) {
                    root = root.getLeft();
                } else {
                    root = root.getRight();
                }
            }
            return root;
        }
    }

    public Set<T> rangeQuery(Rectangle range) {
        Set<T> output = new HashSet<>();

        RangeNode<T> u = this.get(this.root, range, 0);

        if (u != null) {
            if (range.contains(u.getCoords())) {
                output.add(u.getElement());
            }
            RangeNode<T> v = u.getLeft();
            while (v != null) {
                if (range.contains(v.getCoords())) {
                    output.add(v.getElement());
                }

                if (range.getXMin() <= v.getX()) {

                    RangeNode<T> vRight = v.getRight();
                    if (vRight != null) {
                        // construction of the y tree
                        if (vRight.getAuxTree() == null) {
                            vRight.setAuxTree(new RangeTree(1));
                            vRight.getAuxTree().construct(v.getElements());
                            vRight.setElements(null);
                            vRight.getAuxTree().singleQuery(range, output);
                        } else {
                            vRight.getAuxTree().singleQuery(range, output);
                        }
                    }

                    v = v.getLeft();
                } else {
                    v = v.getRight();
                }
            }

            v = u.getRight();
            while (v != null) {
                if (range.contains(v.getCoords())) {
                    output.add(v.getElement());
                }

                if (range.getXMax() >= v.getX()) {

                    RangeNode<T> vLeft = v.getLeft();
                    if (vLeft != null) {
                        // construction of the y tree
                        if (vLeft.getAuxTree() == null) {
                            vLeft.setAuxTree(new RangeTree(1));
                            vLeft.getAuxTree().construct(vLeft.getElements());
                            vLeft.setElements(null);
                            vLeft.getAuxTree().singleQuery(range, output);
                        } else {
                            vLeft.getAuxTree().singleQuery(range, output);
                        }
                    }

                    v = v.getRight();
                } else {
                    v = v.getLeft();
                }
            }
        }

        return output;
    }

    public void singleQuery(Rectangle range, Set<T> output) {

        RangeNode<T> u = this.get(this.root, range, 1);

        if (u != null) {
            if (range.contains(u.getCoords())) {
                output.add(u.getElement());
            }

            RangeNode<T> v = u.getLeft();
            while (v != null) {
                if (range.contains(v.getCoords())) {
                    output.add(v.getElement());
                }

                if (range.getYMin() <= v.getY()) {
                    if (v.getRight() != null) {
                        List<T> ps = v.getRight().getElements();
                        for (T point : ps) {
                            output.add(point);
                        }
                    }

                    v = v.getLeft();
                } else {
                    v = v.getRight();
                }
            }

            v = u.getRight();
            while (v != null) {
                if (range.contains(v.getCoords())) {
                    output.add(v.getElement());
                }

                if (range.getYMax() >= v.getY()) {
                    if (v.getLeft() != null) {
                        List<T> ps = v.getLeft().getElements();
                        for (T point : ps) {
                            output.add(point);
                        }
                    }

                    v = v.getRight();
                } else {
                    v = v.getLeft();
                }
            }
        }
    }

    // ----------------- NESTED COMPARATOR CLASS -----------------
    private static class NodeComparator implements Comparator<Dimensional> {
        private final int axis;

        NodeComparator(int axis) {
            this.axis = axis;
        }

        @Override
        public int compare(Dimensional o1, Dimensional o2) {
            return o1.getCoords()[axis].compareTo(o2.getCoords()[axis]);
        }

    }

    // ------------- END OF NESTED COMPARATOR CLASS -------------

}
