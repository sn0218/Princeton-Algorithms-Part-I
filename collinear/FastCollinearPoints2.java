import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/*
 MergeSort Algorithm to search collinear point
 examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments
 */

// 3 pointer i, j and k solution for traversing pointsSorted array
public class FastCollinearPoints2 {
    private final ArrayList<LineSegment> lines; // LineSegment arraylist stores new line dynamically

    // finds all line segments containing 4 or more points
    public FastCollinearPoints2(Point[] points) {

        // corner case to check if points[] is null
        if (points == null) {
            throw new IllegalArgumentException("The argument array is null.");
        }
        // corner case to check if point is null
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("There is null point in input array.");
            }
        }
        // corner case to check if there is repeated point
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                // use compareTo() method to check duplicated point
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("There is duplicated point.");
                }
            }
        }

        // copy all points to new array to prevent changing the original order of input array
        Point[] newpoints = Arrays.copyOf(points, points.length);

        // sort the copied points in natural order
        Arrays.sort(newpoints);

        // initialize the arraylist lines storing lineSegment type
        lines = new ArrayList<LineSegment>();

        // fast and sorting based algorithm taking ~n^2log(n) array access
        for (int i = 0; i < newpoints.length; i++) {
            // set point p as reference point that p is varying the loop
            Point p = newpoints[i];

            // copy the input array so that the order of original array does not change
            Point[] pointsSorted = Arrays.copyOf(newpoints, newpoints.length);

            /*
            compute the slope making with p
            sort the points array in slope order that adjacent points make with p
             */
            Arrays.sort(pointsSorted, p.slopeOrder()); // self-defined comparator for the array

            /*
            System.out.println(Arrays.toString(pointsSorted));
            System.out.println(i + " trial for sortedPoints");
            */

            int j = 1;
            // compare slope of adjacent points w.r.t. p with reference point
            while (j < pointsSorted.length) {

                // calculate slope of first adjacent point with respect to p as reference slope
                final double refSlope = p.slopeTo(pointsSorted[j]);

                // initialize pointer k equals to the position of j
                int k = j;

                // this while loop takes constant array access to gives O(1) time complexity
                // advance k if the slope of adjacent point making with p equal to reference slope
                // set the boundary of k within the length of pointsSorted
                while (k < pointsSorted.length && p.slopeTo(pointsSorted[k]) == refSlope) {
                    k++;
                }

                // check at least 3 element between pointer k and pointer j
                // check the the reference point is the min to prevent duplicate segment
                if (k - j >= 3 && p.compareTo(pointsSorted[j]) < 0) {
                    // insert the valid maximal line segment into arraylist of LineSegment type
                    LineSegment line = new LineSegment(p, pointsSorted[k - 1]);

                    // insert the line into arraylist of LineSegment type
                    lines.add(line);
                }

                // set the position of j to be equal to k for comparing slope with new reference slope in next iteration
                j = k;
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // return all line segments
    public LineSegment[] segments() {
        // convert arraylist to LineSegment type array and return it
        return lines.toArray(new LineSegment[0]);
    }

    // test client
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints2 collinear = new FastCollinearPoints2(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
