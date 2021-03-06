import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Thomas A. Rieck
 * 02/13/2016
 * Purpose: Fast collinear points
 * <p/>
 * Examines 4 points at a time and checks whether they all lie on the same
 * line segment, returning all such line segments.
 * To check whether the 4 points p, q, r, and s are collinear,
 * check whether the three slopes between p and q, between p and r, and
 * between p and s are all equal.
 * <p/>
 * Performance requirement. The order of growth of the running time of the
 * program should be N^2 log N in the worst case and it should use space
 * proportional to N plus the number of line segments returned.
 * FastCollinearPoints should work properly even if the input has 5 or
 * more collinear points.
 */
public class FastCollinearPoints {

    private final Point[] points, aux;
    private final int N;
    private final int[] V;
    private int nsegments;    // number of segments
    private LineSegment[] segments;

    /**
     * Find all segments containing 4 points
     *
     * @param points the points
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null)

            throw new NullPointerException();

        this.points = points;
        this.N = points.length;
        this.aux = new Point[N];

        this.V = new int[N];
        this.nsegments = 0;
        this.segments = new LineSegment[N * N];

        for (int i = 0; i < N; ++i) {
            for (int j = i + 1; j < N; ++j) {
                if (points[i] == null || points[j] == null)
                    throw new NullPointerException();

                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }

            aux[i] = points[i];
        }

        for (int i = 0; i < N; ++i) {
            addSegments(i);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }

    private void addSegments(int i) {

        sort(i);

        for (int j = 0, k, n; j < N; j = k) {
            if (points[i] == aux[j]) {  // self
                k = j + 1;
                continue;
            }

            double slope = points[i].slopeTo(aux[j]);
            for (k = j + 1, n = 1, V[0] = j; k < N; ++k) {
                if (points[i] == aux[j]) {  // self
                    continue;
                }

                double next = points[i].slopeTo(aux[k]);
                if (Double.compare(slope, next) != 0)
                    break;
                V[n++] = k;
            }

            if (n >= 3) {
                addSegment(i, n);
            }
        }
    }

    private void addSegment(int i, int n) {
        Point min = points[i], max = points[i];

        for (int j = 0; j < n; ++j) {
            min = min.compareTo(aux[V[j]]) < 0 ? min : aux[V[j]];
            max = max.compareTo(aux[V[j]]) > 0 ? max : aux[V[j]];
        }

        if (points[i] == min)
            segments[nsegments++] = new LineSegment(min, max);
    }

    private void sort(int i) {
        Arrays.sort(aux, 0, N, points[i].slopeOrder());
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
