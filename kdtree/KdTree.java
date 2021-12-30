import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    // declare instance variables
    private Node root; // declare the root node
    private int size; // store number of nodes in 2d tree
    private final RectHV rootSq; // declare the unit square
    private Point2D nearestPoint; // declare the nearest point
    private double minDis; // declare closest distance between query point and point in the node

    // inner private static class to represent a node in 2d-tree that does not refer to a generic key or value type
    private static class Node {
        private final Point2D p; // the point
        private final RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left / bottom subtree
        private Node rt; // the right / top subtree
        private final boolean isVertical; // store the orientation of the node

        // constructor to create a node object
        public Node(Point2D p, RectHV rect, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.isVertical = isVertical;
            lb = null;
            rt = null;
        }
    }

    // construct an empty 2d tree
    public KdTree() {
        // initialize root node pointing to null
        root = null;
        // initialize no. of points in 2d tree
        size = 0;
        // set axis-aligned RectHV for root node to enclose all of the points in subtree
        rootSq = new RectHV(0.0, 0.0, 1.0, 1.0);

    }

    // is the set empty?
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // compare the x-coordinate or y-coordinate of the point with the node's point
    private int compareCor(Node n, Point2D p) {
        // check orientation of the node
        if (n.isVertical) {
            // compare the x-coordinate
            return Double.compare(p.x(), n.p.x());
        }
        // compare the y-coordinate
        return Double.compare(p.y(), n.p.y());
    }

    // return the orientation of the next level node
    private boolean nextVertical(Node n) {
        // check the orientation of current node
        if (n.isVertical) {
            return false;
        }
        return true;
    }


    // add the point to the 2d tree
    public void insert(Point2D p) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input point is null.");
        }

        // insert the point to the node pointed by root and setup it RectHV
        root = insert(root, p, rootSq, true);

    }

    // private helper method to insert the point to 2d tree by recursion
    private Node insert(Node n, Point2D p, RectHV rect, boolean vertical) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input point is null.");
        }

        // base case, if the node is null, create a new node
        if (n == null) {
            // increment the no.of node in 2D tree
            size++;

            // create new node and setup the RectHV
            return new Node(p, rect, vertical);
        }

        // recursive case, insert the point to the subtree and create the node
        int cmp = compareCor(n, p);

        // x-cor or y-cor of input point is smaller than the point in the current node
        if (cmp < 0) {
            // check the orientation of current node
            if (n.isVertical) {
                // insert the point to the left sub tree, create node to be horizontal and set its RectHV
                n.lb = insert(n.lb, p,
                              new RectHV(rect.xmin(), rect.ymin(), n.p.x(), rect.ymax()),
                              nextVertical(n));
            }
            else {
                // insert the point to the left sub tree, create node to be vertical and set its RectHV
                n.lb = insert(n.lb, p, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.p.y()),
                              nextVertical(n));
            }
        }

        // x-cor or y-cor of input point is greater than the point in the current node
        else if (cmp > 0) {
            // check the orientation of current node
            if (n.isVertical) {
                // insert the point to the right sub tree, create node to be horizontal and set its RectHV
                n.rt = insert(n.rt, p,
                              new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax()),
                              nextVertical(n));
            }
            else {
                // insert the point to the right sub tree, create node to be vertical and set its RectHV
                n.rt = insert(n.rt, p, new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax()),
                              nextVertical(n));
            }

        }

        // x-cor or y-cor of input point is same as the point in the current node
        else {
            // check the orientation of current node
            if (n.isVertical) {
                // check the input point has the same y-cor as the point in a node
                if (Double.compare(p.y(), n.p.y()) == 0) {
                    // return the node
                    return n;
                }
                else {
                    // insert the point to the right sub tree, create node to be horizontal and set its RectHV
                    n.rt = insert(n.rt, p,
                                  new RectHV(n.p.x(), rect.ymin(), rect.xmax(), rect.ymax()),
                                  nextVertical(n));
                }

            }
            else {
                // check the input point has the same x-cor as the point in a node
                if (Double.compare(p.x(), n.p.x()) == 0) {
                    // return the node
                    return n;
                }

                else {
                    // insert the point to the right sub tree, create node to be vertical and set its RectHV
                    n.rt = insert(n.rt, p,
                                  new RectHV(rect.xmin(), n.p.y(), rect.xmax(), rect.ymax()),
                                  nextVertical(n));
                }

            }
        }

        // return the root node
        return n;
    }


    public boolean contains(Point2D p) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input point is null.");
        }
        // search the 2d tree recursively
        return contains(p, root);

    }

    // private helper method to search the point in 2d tree by recursion
    private boolean contains(Point2D p, Node n) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input point is null.");
        }

        // base case of searching the 2d tree recursively
        if (n == null) {
            // the 2d tree does not contain the point
            return false;
        }

        // check the point equals to the point in the node
        if (p.equals(n.p)) {
            return true;
        }

        // recursive case, compare the point with the point in the node
        int cmp = compareCor(n, p);

        // x-cor or y-cor of input point is smaller than the point in the current node
        if (cmp < 0) {
            return contains(p, n.lb);
        }

        // x-cor or y-cor of input point is greater than the point in the current node
        else if (cmp > 0) {
            return contains(p, n.rt);
        }

        // either x-cor or y-cor of the point is equal to the point in the node
        else {
            // search the right subtree if only x-cor or y-cor of the point is same as the point in the node
            return contains(p, n.rt);
        }


    }

    // draw all points to standard draw
    public void draw() {
        // pass the root to the draw function
        draw(root);
    }

    // private helper method to draw the point recursively
    private void draw(Node n) {
        // base case
        if (n == null) {
            return;
        }

        /* recursive case to draw the point */
        // set the pen to be black
        StdDraw.setPenColor(StdDraw.BLACK);

        // set the size of the pen
        StdDraw.setPenRadius(0.01);

        // draw the point of the node
        n.p.draw();

        // check the orientation of the node
        if (n.isVertical) {
            // set the vertical splitting line to be red
            StdDraw.setPenColor(StdDraw.RED);

            // draw the line
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        }

        else {
            // set the horizontal splitting line to be blue
            StdDraw.setPenColor(StdDraw.BLUE);

            // draw the line
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }


        // draw the point of left subtree recursively
        draw(n.lb);
        // draw the point of right subtree recursively
        draw(n.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // corner case
        if (rect == null) {
            throw new IllegalArgumentException("The input rectangle is null.");
        }

        // declare iterable object to store point inside the rectangle
        ArrayList<Point2D> pointInRect = new ArrayList<>();

        // pass the query rect, root node and arraylist to start range search
        range(rect, root, pointInRect);

        // return the iterable object
        return pointInRect;
    }

    // private helper method to search the point inside query rectangle
    private void range(RectHV rect, Node n, ArrayList<Point2D> pointInRect) {
        // base case
        if (n == null) {
            return;
        }

        // check the point in current node inside the rectangle
        if (rect.contains(n.p)) {
            // add the point to the arraylist
            pointInRect.add(n.p);
        }

        /* recursively search two subtrees */
        // check the RectHV of left/bottom intersects the query rect
        if (n.lb != null && rect.intersects(n.lb.rect)) {
            // search the left subtree recursively
            range(rect, n.lb, pointInRect);
        }

        // check the RectHV of right/top intersects the query rect
        if (n.rt != null && rect.intersects((n.rt.rect))) {
            // search the right subtree recursively
            range(rect, n.rt, pointInRect);
        }


    }

    // search a nearest neighbor to point p
    public Point2D nearest(Point2D p) {
        // corner case
        if (p == null) {
            throw new IllegalArgumentException("The input point is null.");
        }

        // initialize the nearest neighbour object
        nearestPoint = null;
        minDis = Double.POSITIVE_INFINITY;

        // pass the root node and query point to start nearest neighbour search
        nearestPoint = nearest(p, root);

        // return the nearest point
        return nearestPoint;

    }

    // private helper method to search a nearest neighbor to point p
    private Point2D nearest(Point2D p, Node n) {
        // base case, n reaches the null link, all the point has been searched
        if (n == null) {
            return nearestPoint;
        }

        // check the distance between the query point and the node's rect smaller than the current minDis
        // if yes, search the current node
        if (n.rect.distanceSquaredTo(p) < minDis) {
            // check the distance between the query point and the candidate point in the node
            if (n.p.distanceSquaredTo(p) < minDis) {
                // update the current nearest point and closest distance
                nearestPoint = n.p;
                minDis = n.p.distanceSquaredTo(p);

            }

            /* recursive search two subtrees following pruning rule */

            // choose subtree that is on the same side of splitting line as the query point
            if (n.lb != null && n.lb.rect.contains(p)) {
                nearestPoint = nearest(p, n.lb);
                nearestPoint = nearest(p, n.rt);
            }

            else if (n.rt != null && n.rt.rect.contains(p)) {
                nearestPoint = nearest(p, n.rt);
                nearestPoint = nearest(p, n.lb);

            }

            // search the two subtree does not contain query point
            else if (n.lb != null && n.rt != null) {
                // search the left subtree first because the rect of the left subtree's node is closer to query point
                if (n.lb.rect.distanceSquaredTo(p) < n.rt.rect.distanceSquaredTo(p)) {
                    nearestPoint = nearest(p, n.lb);
                    nearestPoint = nearest(p, n.rt);
                }
                // search the right subtree first because the rect of the right subtree's node is closer to query point
                else {
                    nearestPoint = nearest(p, n.rt);
                    nearestPoint = nearest(p, n.lb);
                }
            }

            // search the subtrees does not contain query point (either one subtree is null or both subtrees is null)
            else {
                nearestPoint = nearest(p, n.rt);
                nearestPoint = nearest(p, n.lb);
            }

        }

        return nearestPoint;

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        /* optional test cases */

    }
}
