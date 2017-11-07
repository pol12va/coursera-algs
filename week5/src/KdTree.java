import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private int size;
    private Node root;

    public KdTree() { }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        Node newNode = new Node(p);

        if (contains(p)) {
            return;
        }

        size++;
        if (root == null) {
            root = newNode;
            root.setOrientation(Node.VERTICAL);
            return;
        }

        insertOriented(newNode, root);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        if (isEmpty()) {
            return false;
        }

        return containsPoint(p, root);
    }

    public void draw() {
        if (isEmpty()) {
            return;
        }

        drawNode(root);
    }

    private void drawNode(Node node) {
        if (node.left != null) {
            drawNode(node.left);
        }

        node.point.draw();

        if (node.right != null) {
            drawNode(node.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        if (isEmpty()) {
            return new Queue<>();
        }

        RectHV unitRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        Queue<Point2D> points = new Queue<>();
        findPoints(root, rect, points, unitRect);

        return points;
    }

    private void findPoints(Node parent, RectHV rect, Queue<Point2D> points, RectHV outer) {
        if (parent == null) {
            return;
        }

        if (rect.contains(parent.point)) {
            points.enqueue(parent.point);
        }

        if (parent.orientation == Node.VERTICAL) {

            RectHV leftRect = new RectHV(outer.xmin(), outer.ymin(), parent.point.x(), outer.ymax());
            if (rect.intersects(leftRect)) {
                findPoints(parent.left, rect, points, leftRect);
            }

            RectHV rightRect = new RectHV(parent.point.x(), outer.ymin(), outer.xmax(), outer.ymax());
            if (rect.intersects(rightRect)) {
                findPoints(parent.right, rect, points, rightRect);
            }
        } else {
            RectHV botRec = new RectHV(outer.xmin(), outer.ymin(), outer.xmax(), parent.point.y());
            if (rect.intersects(botRec)) {
                findPoints(parent.left, rect, points, botRec);
            }

            RectHV topRect = new RectHV(outer.xmin(), parent.point.y(), outer.xmax(), outer.ymax());
            if (rect.intersects(topRect)) {
                findPoints(parent.right, rect, points, topRect);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("wrong argument");
        }

        if (isEmpty()) {
            return null;
        }

        return findNearest(p, root.point, root, new RectHV(0, 0, 1.0, 1.0));
    }

    private Point2D findNearest(Point2D target, Point2D nearest, Node n, RectHV r) {
        if (n == null || nearest.distanceSquaredTo(target) < r.distanceSquaredTo(target)) {
            return nearest;
        }

        RectHV r1 = createSubRect(n, r, true);
        RectHV r2 = createSubRect(n, r, false);
        RectHV otherRect = r2;

        if (target.distanceSquaredTo(n.point) < target.distanceSquaredTo(nearest)) {
            nearest = n.point;
        }

        if (r1.contains(target)) {
            nearest = findNearest(target, nearest, n.left, r1);
        } else if (r2.contains(target)) {
            nearest = findNearest(target, nearest, n.right, r2);
            otherRect = r1;
        }

        return findNearest(target, nearest, otherRect == r1 ? n.left : n.right, otherRect);
    }

    /**
     *
     * @param n
     * @param r
     * @param isFirst left/top
     * @return
     */
    private RectHV createSubRect(Node n, RectHV r, boolean isFirst) {
        if (n.orientation == Node.VERTICAL) {
            if (isFirst) {
                return new RectHV(r.xmin(), r.ymin(), n.point.x(), r.ymax());
            } else {
                return new RectHV(n.point.x(), r.ymin(), r.xmax(), r.ymax());
            }
        } else {
            if (isFirst) {
                return new RectHV(r.xmin(), r.ymin(), r.xmax(), n.point.y());
            } else {
                return new RectHV(r.xmin(), n.point.y(), r.xmax(), r.ymax());
            }
        }
    }

    private void insertOriented(Node newNode, Node parent) {
        if (parent.getOrientation() == Node.VERTICAL) {
            insertNode(newNode, newNode.point.x(), parent.point.x(), parent);
        } else if (parent.getOrientation() == Node.HORIZONTAL) {
            insertNode(newNode, newNode.point.y(), parent.point.y(), parent);
        }
    }

    private void insertNode(Node newNode, double a, double b, Node parent) {
        if (a < b) {
            insertLeft(newNode, parent);
        } else {
            insertRight(newNode, parent);
        }
    }

    private void insertLeft(Node newNode, Node parent) {
        if (parent.left == null) {
            newNode.setOrientation(!parent.getOrientation());
            parent.left = newNode;
        } else {
            insertOriented(newNode, parent.left);
        }
    }

    private void insertRight(Node newNode, Node parent) {
        if (parent.right == null) {
            newNode.setOrientation(!parent.getOrientation());
            parent.right = newNode;
        } else {
            insertOriented(newNode, parent.right);
        }
    }

    private boolean containsPoint(Point2D p, Node parent) {
        if (parent == null) {
            return false;
        }

        if (p.equals(parent.point)) {
            return true;
        }

        if (parent.getOrientation() == Node.VERTICAL) {
            if (p.x() < parent.point.x()) {
                return containsPoint(p, parent.left);
            } else {
                return containsPoint(p, parent.right);
            }
        } else {
            if (p.y() < parent.point.y()) {
                return containsPoint(p, parent.left);
            } else {
                return containsPoint(p, parent.right);
            }
        }
    }

    private class Node {
        private static final boolean VERTICAL = true;
        private static final boolean HORIZONTAL = false;

        private final Point2D point;
        private boolean orientation;
        private Node left;
        private Node right;

        public Node(Point2D p) {
            point = p;
        }

        public boolean getOrientation() {
            return orientation;
        }

        public void setOrientation(boolean orientation) {
            this.orientation = orientation;
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();

        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        Iterable<Point2D> points = tree.range(new RectHV(0.3, 0.1, 0.8, 0.8));
        System.out.println("Range search result: ");
        for (Point2D p : points) {
            System.out.println(p);
        }

        Point2D target = new Point2D(0.404, 0.067);
        System.out.println("Nearest to " + target + ": " + tree.nearest(new Point2D(0.404, 0.067)));

    }
}
