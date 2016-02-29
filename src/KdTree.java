import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Thomas A. Rieck
 * 02/27/2016
 * Purpose: 2d tree class
 * <p/>
 * Search and insert. The algorithms for search and insert are
 * similar to those for BSTs, but at the root we use the
 * x-coordinate (if the point to be inserted has a smaller
 * x-coordinate than the point at the root, go left; otherwise go
 * right); then at the next level, we use the y-coordinate (if the
 * point to be inserted has a smaller y-coordinate than the point in
 * the node, go left; otherwise go right); then at the next level
 * the x-coordinate, and so forth.
 */
public class KdTree {
    private Node root = null;

    /**
     * Construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * Unit testing of the methods
     *
     * @param args the program arguments
     */
    public static void main(String[] args) {
        KdTree tree = new KdTree();

        assert (tree.isEmpty());

        Point2D p1 = new Point2D(0.5, 0.5);
        tree.insert(p1);

        Point2D p2 = new Point2D(0.25, 0.25);
        tree.insert(p2);

        Point2D p3 = new Point2D(0.125, 0.125);
        tree.insert(p3);

        assert (!tree.isEmpty());
        assert (tree.contains(p1));
        assert (tree.contains(p2));
        assert (tree.contains(p3));

        Point2D p4 = tree.nearest(new Point2D(0.110, 0.110));
        assert (p4.equals(p3));

        Point2D p5 = tree.nearest(new Point2D(0.6, 0.6));
        assert (p5.equals(p1));

        for (Point2D p : tree.range(new RectHV(0, 0, 0.125, 0.125))) {
            assert (p.equals(p3));
        }
    }

    private static int compare(Point2D p1, Point2D p2, boolean orientX) {
        int cmp;

        if (orientX) {
            cmp = compare(p1.x(), p2.x());
        } else {
            cmp = compare(p1.y(), p2.y());
        }

        return cmp;
    }

    private static int compare(double d1, double d2) {
        return Double.compare(d1, d2);
    }

    private static void checkNull(Object o) {
        if (o == null)
            throw new NullPointerException();
    }

    /**
     * Is the set empty
     *
     * @return true if set is empty, otherwise false
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Number of points in the set
     *
     * @return the number of points in the set
     */
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        else return 1 + size(node.lb) + size(node.rt);
    }

    /**
     * Add the point to the set
     * if it is not already in the set
     *
     * @param p the point
     */
    public void insert(Point2D p) {
        checkNull(p);
        root = put(root, root, p, true);
    }

    private Node put(Node parent, Node node, Point2D p, boolean orientX) {
        if (node == null) return new Node(parent, p, orientX);

        int cmp = compare(p, node.p, orientX);
        if (cmp < 0) node.lb = put(node, node.lb, p, !orientX);
        else if (cmp > 0) node.rt = put(node, node.rt, p, !orientX);
        return node;
    }

    /**
     * Does the set contain the point p?
     *
     * @param p the point
     * @return true if the point is contained otherwise fales
     */
    public boolean contains(Point2D p) {
        checkNull(p);
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean orientX) {
        if (node == null) return false;
        int cmp = compare(p, node.p, orientX);
        if (cmp < 0) return contains(node.lb, p, !orientX);
        else if (cmp > 0) return contains(node.rt, p, !orientX);
        else return node.p.equals(p);
    }

    /**
     * Draw all points to standard draw
     * in black and the subdivisions in red (for vertical splits) and blue
     * (for horizontal splits).
     */
    public void draw() {
        for (Node node : nodes()) {
            drawNode(node);
        }
    }

    private void drawNode(Node node) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.p.x(), node.p.y());

        StdDraw.setPenRadius();
        if (node.orientX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(),
                    node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(),
                    node.p.y());
        }
    }

    private Iterable<Node> nodes() {
        SET<Node> set = new SET<>();
        nodes(root, set);
        return set;
    }

    private void nodes(Node node, SET<Node> set) {
        if (node == null) return;
        set.add(node);
        nodes(node.lb, set);
        nodes(node.rt, set);
    }

    /**
     * All points that are inside the rectangle
     *
     * @param rect the rectangle
     * @return the set of points contained
     */
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        SET<Point2D> set = new SET<>();
        range(root, rect, set);
        return set;
    }

    private void range(Node node, RectHV rect, SET<Point2D> set) {
        // To find all points contained in a given query rectangle,
        // start at the root and recursively search for points in both subtrees
        // using the following pruning rule: if the query rectangle does not
        // intersect the rectangle corresponding to a node, there is no need to
        // explore that node (or its subtrees). A subtree is searched only if it
        // might contain a point contained in the query rectangle.

        // OPTIMIZATION:
        // Instead of checking whether the query rectangle
        // intersects the  rectangle corresponding to a node, it suffices to
        // check only whether the query rectangle intersects the splitting line
        // segment: if it does, then recursively search both subtrees;
        // otherwise, recursively search the one subtree where points
        // intersecting the query rectangle could be.
        if (node == null) return;
        if (rect.contains(node.p)) set.add(node.p);
        if (node.lb != null && rect.intersects(node.lb.rect)) {
            range(node.lb, rect, set);
        }
        if (node.rt != null && rect.intersects(node.rt.rect)) {
            range(node.rt, rect, set);
        }
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p the point
     * @return the nearest neighbor or null
     */
    public Point2D nearest(Point2D p) {
        checkNull(p);
        PointDistance pd = nearest(root, p, null);
        if (pd != null)
            return pd.p;

        return null;
    }

    private PointDistance nearest(Node node, Point2D p, PointDistance min) {
        // To find a closest point to a given query point, start at the root
        // and recursively search in both subtrees using the following
        // pruning rule: if the closest point discovered so far is closer
        // than the distance between the query point and the rectangle
        // corresponding to a node, there is no need to explore that node
        // (or its subtrees). That is, a node is searched only if it might
        // contain a point that is closer than the best one found so far.
        // The effectiveness of the pruning rule depends on quickly finding
        // a nearby point. To do this, organize your recursive method so
        // that when there are two possible subtrees to go down, you always
        // choose the subtree that is on the same side of the splitting line
        // as the query point as the first subtree to explore—the closest
        // point found while exploring the first subtree may enable pruning
        // of the second subtree.
        if (node == null) return min;

        double rectDistance = node.rect.distanceSquaredTo(p);
        if (min == null || rectDistance < min.distance) {
            PointDistance dp = new PointDistance(node.p,
                    node.p.distanceSquaredTo(p));

            if (min == null || dp.distance < min.distance)
                min = dp;

            Node first, second;
            if (node.lb != null && node.lb.rect.contains(p)) {
                first = node.lb;
                second = node.rt;
            } else {
                first = node.rt;
                second = node.lb;
            }

            PointDistance df = nearest(first, p, min);
            if (df.distance < min.distance)
                min = df;

            PointDistance ds = nearest(second, p, min);
            if (ds.distance < min.distance)
                min = ds;
        }

        return min;
    }

    private static class Node implements Comparable<Node> {
        private Point2D p;          // the point
        private RectHV rect;        // the axis-aligned rectangle for this node
        private Node lb;            // the left/bottom subtree
        private Node rt;            // the right/top subtree
        private boolean orientX;    // orientation

        private Node(Node parent, Point2D p, boolean orientX) {
            this.p = p;
            this.rect = makeRect(parent, p, orientX);
            this.orientX = orientX;
            this.lb = null;
            this.rt = null;
        }

        private static RectHV makeRect(Node node, Point2D p, boolean orientX) {
            if (node == null)
                return new RectHV(0, 0, 1, 1);

            int cmp = compare(p, node.p, orientX);

            RectHV rect;
            if (orientX) { // split horizontally
                if (cmp < 0) {
                    rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node
                            .rect.xmax(), node.p.y());
                } else {
                    rect = new RectHV(node.rect.xmin(), node.p.y(),
                            node.rect.xmax(), node.rect.ymax());
                }
            } else {    // split vertically
                if (cmp < 0) {
                    rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node
                            .p.x(), node.rect.ymax());
                } else {
                    rect = new RectHV(node.p.x(), node.rect.ymin(),
                            node.rect.xmax(), node.rect.ymax());
                }
            }

            return rect;
        }

        @Override
        public int compareTo(Node o) {
            return this.p.compareTo(o.p);
        }
    }

    private static class PointDistance {
        private Point2D p;
        private double distance;

        private PointDistance(Point2D p, double v) {
            this.p = p;
            this.distance = v;
        }
    }
}
