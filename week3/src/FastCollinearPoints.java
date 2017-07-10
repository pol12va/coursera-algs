import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        validate(points);

        Point[] copiedPoints = new Point[points.length];
        System.arraycopy(points, 0, copiedPoints, 0, points.length);

        findSegments(copiedPoints);
    }

    public int numberOfSegments() {
        return lineSegments.length;
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

        Queue<LineSegmentWithEnd> segments = new Queue<>();
        for (int i = 0; i < points.length - 1; i++) {
            Point[] sortedPoints = new Point[points.length - i - 1];
            System.arraycopy(points, i + 1, sortedPoints, 0, points.length - i - 1);
            Arrays.sort(sortedPoints, points[i].slopeOrder());

            for (int j = 0; j < sortedPoints.length - 2;) {
                double curSlope = sortedPoints[j].slopeTo(points[i]);
                if (curSlope == sortedPoints[j + 1].slopeTo(points[i])
                        && curSlope == sortedPoints[j + 2].slopeTo(points[i])) {

                    while (j < sortedPoints.length && curSlope == sortedPoints[j].slopeTo(points[i])) {
                        j++;
                    }

                    addSegment(segments, curSlope, points[i], sortedPoints[j - 1]);
                } else {
                    j++;
                }
            }
        }

        saveSegments(segments);
    }

    private void saveSegments(Queue<LineSegmentWithEnd> segments) {
        lineSegments = new LineSegment[segments.size()];
        int pos = 0;
        for (LineSegmentWithEnd ls : segments) {
            lineSegments[pos++] = ls.ls;
        }
    }

    private void addSegment(Queue<LineSegmentWithEnd> segments, double curSlope,
                            Point startPoint, Point endPoint) {
        LineSegment ls = new LineSegment(startPoint, endPoint);
        if (segments.isEmpty()) {
            segments.enqueue(new LineSegmentWithEnd(ls, endPoint, curSlope));
        } else {
            boolean has = false;
            for (LineSegmentWithEnd lswe : segments) {
                if (lswe.end == endPoint && lswe.slope == curSlope) {
                    has = true;
                    break;
                }
            }

            if (!has) {
                segments.enqueue(new LineSegmentWithEnd(ls, endPoint, curSlope));
            }
        }
    }

    private class LineSegmentWithEnd {
        private final LineSegment ls;
        private final Point end;
        private final double slope;

        public LineSegmentWithEnd(LineSegment ls, Point end, double slope) {
            this.ls = ls;
            this.end = end;
            this.slope = slope;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
