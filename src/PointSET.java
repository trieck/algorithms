import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Thomas A. Rieck
 * 02/27/2016
 * Purpose: Point set class
 * <p/>
 * Corner cases.
 * Throw a java.lang.NullPointerException if any argument is null.
 * Performance requirements:  Supports insert() and contains() in time
 * proportional to the logarithm of the number of points in the set in the
 * worst case; it should support nearest() and range() in time proportional
 * to the number of points in the set.
 */
public class PointSET {

    private final SET<Point2D> points;

    /**
     * Construct an empty points of points
     */
    public PointSET() {
        points = new SET<>();
    }

    /**
     * Unit testing of the methods
     *
     * @param args the program arguments
     */
    public static void main(String[] args) {
    }

    /**
     * Is the points empty
     *
     * @return true if set is empty, otherwise false
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * Number of points in the points
     *
     * @return the number of points in the set
     */
    public int size() {
        return points.size();
    }

    /**
     * Add the point to the set
     * if it is not already in the set
     *
     * @param p the point
     */
    public void insert(Point2D p) {
        points.add(p);
    }

    private void checkNull(Object o) {
        if (o == null)
            throw new NullPointerException();
    }

    /**
     * Does the points contain the point p?
     *
     * @param p the point
     * @return true if the point is contained otherwise fales
     */
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
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
        for (Point2D point : points) {
            if (rect.contains(point))
                set.add(point);
        }
        return set;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p the point
     * @return the nearest neighbor or null
     */
    public Point2D nearest(Point2D p) {
        checkNull(p);

        Point2D minPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Point2D point : points) {
            double distance = point.distanceSquaredTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                minPoint = point;
            }
        }

        return minPoint;
    }
}
