import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/*
 Brute-force searching
 examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments
 */
public class BruteCollinearPoints {
    // declare of class variables
    private final ArrayList<LineSegment> lines; // LineSegment arraylist stores new line dynamically

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

        // initialize new array for copied points
        Point[] newpoints = new Point[points.length];

        // copy all points to new array to prevent changing the original order of input array
        for (int i = 0; i < newpoints.length; i++) {
            newpoints[i] = points[i];
        }

        // sort the copied points in natural order
        Arrays.sort(newpoints);

        // initialize the arraylist lines storing lineSegment type
        lines = new ArrayList<LineSegment>();

        // brute-force search for 3 pairs of points having same slope
        for (int i = 0; i < newpoints.length; i++) {
            for (int j = i + 1; j < newpoints.length; j++) {
                for (int k = j + 1; k < newpoints.length; k++) {
                    for (int m = k + 1; m < newpoints.length; m++) {
                        // compute the slopes of 3 pairs of points
                        double slopePQ = newpoints[i].slopeTo(newpoints[j]);
                        double slopePR = newpoints[i].slopeTo(newpoints[k]);
                        double slopePS = newpoints[i].slopeTo(newpoints[m]);

                        // construct the line segments if the slopes are equal
                        if (slopePQ == slopePR && slopePQ == slopePS) {
                            // instantiate the line segment object if 4 points are collinear
                            LineSegment line = new LineSegment(newpoints[i], newpoints[m]);

                            // insert the line into arraylist of LineSegment type
                            if (!lines.contains(line)) {
                                lines.add(line);
                            }

                        }

                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
