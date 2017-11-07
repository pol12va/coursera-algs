import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private final Set<Point2D> bst;

    public PointSET() {
        bst = new TreeSet<>();
    }

    public boolean isEmpty() {
        return bst.isEmpty();
    }

    public int size() {
        return bst.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        bst.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        return bst.contains(p);
    }

    public void draw() {
        for (Point2D p : bst) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        Queue<Point2D> points = new Queue<>();
        for (Point2D p : bst) {
            if (rect.contains(p)) {
                points.enqueue(p);
            }
        }

        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        Point2D target = null;

        for (Point2D cur : bst) {
            if (target == null) {
                target = cur;
                continue;
            }

            if (cur.distanceSquaredTo(p) < target.distanceSquaredTo(p)) {
                target = cur;
            }
        }

        return target;
    }



}
