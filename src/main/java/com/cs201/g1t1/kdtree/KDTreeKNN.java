package com.cs201.g1t1.kdtree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public abstract class KDTreeKNN {
    private static int dimensions;

    public KDTreeKNN(int dimensions) {
        this.dimensions = dimensions;
    }

    private static class Distance {
        private KDTreeNode<? extends Dimensional> node;

        private Double value;

        Distance(double distance, KDTreeNode<? extends Dimensional> node) {
            this.node = node;
            this.value = distance;
        }

        Double getValue() {
            return this.value;
        }

        void setValue(Double value) {
            this.value = value;
        }

        public KDTreeNode getNode() {
            return this.node;
        };

        public void setNode(KDTreeNode<? extends Dimensional> node) {
            this.node = node;
        }
    }

    private static class MinPriorityQueue extends PriorityQueue<Distance> {
        private static Comparator<Distance> comparator = new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };

        MinPriorityQueue() {
            super(11, comparator);
        }
    }

    private static class MaxPriorityQueue extends PriorityQueue<Distance> {
        private static Comparator<Distance> mComparator = new Comparator<Distance>() {
            @Override
            public int compare(Distance o1, Distance o2) {
                return -1 * o1.getValue().compareTo(o2.getValue());
            }
        };

        MaxPriorityQueue() {
            super(11, mComparator);
        }
    }

    public KDTreeNode<? extends Dimensional> nearest(KDTreeNode rootNode, Double[] point) {
        if (rootNode == null) {
            return null;
        }

        final MinPriorityQueue minPriorityQueue = new MinPriorityQueue();
        minPriorityQueue.add(new Distance(0, rootNode));

        Distance bestDistance = new Distance(Double.MAX_VALUE, rootNode);

        while (!minPriorityQueue.isEmpty()) {
            final Distance currentDistance = minPriorityQueue.poll();

            if (currentDistance.getValue() >= bestDistance.getValue()) {
                return bestDistance.getNode();
            }

            final KDTreeNode currentNode = currentDistance.getNode();
            final double distanceFromCurrentNode = distance(point, currentNode.getCoords());

            if (distanceFromCurrentNode < bestDistance.getValue()) {
                bestDistance.setNode(currentNode);
                bestDistance.setValue(distanceFromCurrentNode);
                ;
            }

            final int axis = currentNode.getDepth() % dimensions;
            double delta = point[axis] - currentNode.getCoords()[axis];

            KDTreeNode away = currentNode.getLeftNode();
            KDTreeNode near = currentNode.getRightNode();

            if (delta <= 0) {
                away = currentNode.getRightNode();
                near = currentNode.getLeftNode();
            }

            if (away != null) {
                minPriorityQueue.add(new Distance(delta, away));
            }

            if (near != null) {
                minPriorityQueue.add(new Distance(0, near));
            }
        }

        return bestDistance.getNode();
    }

    public List<KDTreeNode<? extends Dimensional>> kNearest(KDTreeNode<? extends Dimensional> rootNode, Double[] point, int k) {
        if (rootNode == null) {
            return null;
        }

        final MaxPriorityQueue maxPriorityQueue = new MaxPriorityQueue();
        maxPriorityQueue.add(new Distance(Double.MAX_VALUE, rootNode));

        final MinPriorityQueue minPriorityQueue = new MinPriorityQueue();
        minPriorityQueue.add(new Distance(0, rootNode));

        while (!minPriorityQueue.isEmpty()) {
            final Distance currentDistance = minPriorityQueue.poll();

            if (currentDistance.getValue() >= maxPriorityQueue.peek().getValue()) {
                break;
            }

            final KDTreeNode currentNode = currentDistance.getNode();
            final double distanceFromCurrentNode = distance(point, currentNode.getCoords());
            final double maxDistance = maxPriorityQueue.peek().getValue();

            if (maxPriorityQueue.size() < k || distanceFromCurrentNode <= maxDistance) {
                maxPriorityQueue.add(new Distance(distanceFromCurrentNode, currentNode));

                if (maxPriorityQueue.size() > k) {
                    maxPriorityQueue.poll();
                }
            }

            final int axis = currentNode.getDepth() % dimensions;
            double delta = point[axis] - currentNode.getCoords()[axis];

            KDTreeNode<? extends Dimensional> away = currentNode.getLeftNode();
            KDTreeNode<? extends Dimensional> near = currentNode.getRightNode();

            if (delta <= 0) {
                away = currentNode.getRightNode();
                near = currentNode.getLeftNode();
            }

            if (away != null) {
                minPriorityQueue.add(new Distance(delta, away));
            }

            if (near != null) {
                minPriorityQueue.add(new Distance(0, near));
            }
        }

        List<KDTreeNode<? extends Dimensional>> result = new ArrayList<>();
        for (Distance distance : maxPriorityQueue) {
            result.add(distance.getNode());
        }

        return result;
    }

    private static double distance(Double[] point1, Double[] point2) {
        return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
    }

}
