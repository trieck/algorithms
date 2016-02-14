import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Thomas A. Rieck
 * 02/13/2016
 * Purpose: Brute collinear points
 * <p/>
 * Examines 4 points at a time and checks whether they all lie on the same
 * line segment, returning all such line segments.
 * To check whether the 4 points p, q, r, and s are collinear,
 * check whether the three slopes between p and q, between p and r, and
 * between p and s are all equal.
 * <p/>
 * Performance requirement. The order of growth of the running time of
 * the program should be N^4 in the worst case and it should use space
 * proportional to N plus the number of line segments returned.
 */
public class BruteCollinearPoints {

    private final Point[] points;
    private int nsegments;    // number of segments
    private LineSegment[] segments;

    /**
     * Find all segments containing 4 points
     *
     * @param points the points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();

        this.points = points;
        this.nsegments = 0;
        this.segments = new LineSegment[points.length];

        int N = points.length;
        for (int i = 0; i < N; ++i) {
            for (int j = i + 1; j < N; ++j) {
                validate(i, j);
                for (int k = j + 1; k < N; ++k) {
                    for (int m = k + 1; m < N; ++m) {
                        if (isCollinear(i, j, k, m)) {
                            addSegment(i, j, k, m);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
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
    }

    private void addSegment(int i, int j, int k, int m) {
        int min, max;

        min = points[i].compareTo(points[j]) < 0 ? i : j;
        min = points[min].compareTo(points[k]) < 0 ? min : k;
        min = points[min].compareTo(points[m]) < 0 ? min : m;

        max = points[i].compareTo(points[j]) > 0 ? i : j;
        max = points[max].compareTo(points[k]) > 0 ? max : k;
        max = points[max].compareTo(points[m]) > 0 ? max : m;

        segments[nsegments++] = new LineSegment(points[min], points[max]);
    }

    private boolean isCollinear(int i, int j, int k, int m) {
        validate(i, j, k, m);

        if (!isCollinear(i, j, k))
            return false;

        if (!isCollinear(j, k, m))
            return false;

        return true;
    }

    private boolean isCollinear(int i, int j, int k) {
        return points[i].slopeOrder().compare(points[j], points[k]) == 0;
    }

    private void validate(int i, int j, int k, int m) {
        validate(i, j);
        validate(i, k);
        validate(i, m);
        validate(j, k);
        validate(j, m);
        validate(k, m);
    }

    private void validate(int i, int j) {
        validate(i);
        validate(j);
        if (points[i].compareTo(points[j]) == 0)
            throw new IllegalArgumentException();
    }

    private void validate(int i) {
        if (points[i] == null)
            throw new NullPointerException();
    }

    /**
     * The number of line segments
     *
     * @return the number of segments
     */
    public int numberOfSegments() {
        return nsegments;
    }

    /**
     * The line segments
     *
     * @return the segments
     */
    public LineSegment[] segments() {
        LineSegment[] output = new LineSegment[nsegments];
        for (int i = 0; i < nsegments; ++i) {
            output[i] = segments[i];
        }

        return output;
    }
}
