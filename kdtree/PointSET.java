import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    // declare instance variables
    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        // initialize the pointSet in constructor
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input pointset is null.");
        }
        pointSet.add(p);

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input pointset is null");
        }
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        // draw each point to standard draw
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // corner case
        if (rect == null) {
            throw new IllegalArgumentException("The input rectangle is null");
        }

        // declare iterable object to store point
        ArrayList<Point2D> pointInRect = new ArrayList<>();

        // check the rectangle contains the point inside or on boundary
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                pointInRect.add(point);
            }
        }

        // return the iterable object
        return pointInRect;
    }

    // a nearest neighbor in the set to point p, null if the set is empty
    public Point2D nearest(Point2D p) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input point is null.");
        }

        // check the set is empty
        if (pointSet.isEmpty()) {
            return null;
        }
        // initialize the nearest neighbour object
        Point2D nearestPoint = null;
        double minDis = Double.POSITIVE_INFINITY;

        // check the nearest neighbour in set
        for (Point2D candidatePoint : pointSet) {
            // compute current distance between p and point in pointSet
            // use squared distances to improve efficiency to avoid expensive operation of taking square roots
            double curDis = candidatePoint.distanceSquaredTo(p);

            if (curDis < minDis) {
                minDis = curDis;
                nearestPoint = candidatePoint;
            }
        }

        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointset = new PointSET();
        Point2D p1 = new Point2D(0, 0);
        Point2D p2 = new Point2D(1, 1);
        Point2D p3 = new Point2D(2, 2);
        Point2D p4 = new Point2D(4, 3);
        pointset.insert(p1);
        pointset.insert(p2);
        pointset.insert(p3);
        pointset.insert(p4);
        System.out.println("cloest neigbour to (5, 6): " + pointset.nearest(new Point2D(5, 6)));

        // test iterable function
        RectHV rect = new RectHV(0, 0, 3, 3);

        System.out.println("Points are inside the rectangle: ");
        for (Point2D point : pointset.range(rect)) {
            System.out.println(point);
        }

    }
}
