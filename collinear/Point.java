/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        // compute slope of degenerate line segment
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }
        // compute slope of vertical line segment
        else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        // compute slope of horizontal line segment
        else if (this.y == that.y) {
            return +0.0;
        }
        else {
            return (that.y * 1.0 - this.y) / (that.x * 1.0 - this.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        // corner case to check illegal argument
        if (that == null) {
            throw new NullPointerException();
        }
        // this point is equal to the argument point (x0 == x1 and y0 == y1)
        if (this.x == that.x && this.y == that.y) {
            return 0;
        }
        // this point is less than the argument point
        else if (this.y < that.y || (this.y == that.y && this.x < that.x)) {
            return -1;
        }
        // this point is greater than the argument point
        else {
            return 1;
        }

    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     * <p>
     * define customized comparison method which is sorting points by the slopes
     *
     * @return the Comparator that defines this ordering on points
     */

    /*
    use built-in double compare method
        return 0 if equal
        return positive value if d1 > d2
        return negative value if d1 < d2
    */
    public Comparator<Point> slopeOrder() {
        // return the anonymous comparator object by
        return new Comparator<Point>() {
            @Override
            // function of comparator object to compare its two argument points by slope
            public int compare(Point a, Point b) {
                // corner case to check illegal argument
                if (a == null || b == null) {
                    throw new NullPointerException();
                }
                // return value by calling slopeTo on each Point object
                double slopeA = slopeTo(a);
                double slopeB = slopeTo(b);

                return Double.compare(slopeA, slopeB);

            }
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point origin = new Point(0, 0);
        Point[] points = {
                new Point(7, 2), new Point(2, 3), new Point(4, 5), new Point(1, 6), new Point(4, 6)
        };

        origin.draw();

        for (Point point : points) {
            point.draw();
            point.drawTo(origin);
        }

        // sort the array by self-defined comparator by slope order
        Arrays.sort(points, origin.slopeOrder());
        System.out.println("Slope order in ascending order: ");
        for (int i = 0; i < points.length; i++) {
            System.out.println(points[i] + ", m = " + points[i].slopeTo(origin));
        }

    }
}
