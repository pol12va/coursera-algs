import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private static final int SEGMENT_LEN = 4;

    private final Queue<LineSegment> segments;
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        validate(points);

        segments = new Queue<>();

        Point[] copiedPoints = new Point[points.length];
        System.arraycopy(points, 0, copiedPoints, 0, points.length);

        findSegments(copiedPoints);
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] ls = new LineSegment[lineSegments.length];
        System.arraycopy(lineSegments, 0, ls, 0, lineSegments.length);
        return ls;
    }

    private void validate(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Input argument is null");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null is prohibited");
            }

            for (int y = 0; y < i; y++) {
                if (points[y].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException("Equal points were found");
                }
            }
        }
    }

    private void findSegments(Point[] points) {
        Arrays.sort(points);

        for (int i = 0; i < points.length - SEGMENT_LEN + 1; i++) {
            for (int j = i + 1; j < points.length - SEGMENT_LEN + 2; j++) {
                Point[] collinearPoints = new Point[SEGMENT_LEN];
                collinearPoints[0] = points[i];
                collinearPoints[1] = points[j];
                int cPointsNum = 2;

                double slope = points[j].slopeTo(points[i]);

                for (int k = j + 1; k < points.length; k++) {
                    if (slope == points[k].slopeTo(points[j])) {
                        collinearPoints[cPointsNum++] = points[k];
                    }
                }

                if (collinearPoints[SEGMENT_LEN - 1] != null) {
                    segments.enqueue(new LineSegment(collinearPoints[0], collinearPoints[SEGMENT_LEN - 1]));
                }
            }
        }

        lineSegments = new LineSegment[segments.size()];
        int i = 0;

        for (LineSegment ls : segments) {
            lineSegments[i++] = ls;
        }
    }

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
