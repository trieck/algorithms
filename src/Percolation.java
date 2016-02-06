import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Thomas A. Rieck
 * 1/30/2016
 * Purpose: implement percolation algorithm using weighted quick union
 */
public class Percolation {

    private final int N, top, bottom;
    private final boolean[] open;
    private final WeightedQuickUnionUF ufa, ufb;

    /**
     * Create an N-by-N grid with all sites blocked
     *
     * @param N dimension of the grid
     */
    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException();

        this.N = N;
        top = N * N;
        bottom = top + 1;

        ufa = new WeightedQuickUnionUF(N * N + 2);
        ufb = new WeightedQuickUnionUF(N * N + 1);
        open = new boolean[N * N];

        // connect virtual sites
        for (int i = 0; i < N; i++) {
            ufa.union(i, top);
            ufb.union(i, top);
            ufa.union((N - 1) * N + i, bottom);
        }
    }

    /**
     * Open site (row <tt>i</tt>, column <tt>j</tt> if it is not open already
     *
     * @param i the row
     * @param j the column
     */
    public void open(int i, int j) {
        if (isOpen(i, j))
            return;

        int p = xyTo1D(i, j);
        open[p] = true;

        // connect to all adjacent open sites

        // connect to above
        int q;
        if (i > 1 && isOpen(i - 1, j)) {
            q = xyTo1D(i - 1, j);
            ufa.union(p, q);
            ufb.union(p, q);
        }
        // connect to left
        if (j > 1 && isOpen(i, j - 1)) {
            q = xyTo1D(i, j - 1);
            ufa.union(p, q);
            ufb.union(p, q);
        }
        // connect to right
        if (j < N && isOpen(i, j + 1)) {
            q = xyTo1D(i, j + 1);
            ufa.union(p, q);
            ufb.union(p, q);
        }
        // connect to bottom
        if (i < N && isOpen(i + 1, j)) {
            q = xyTo1D(i + 1, j);
            ufa.union(p, q);
            ufb.union(p, q);
        }
    }

    /**
     * Validate index in range
     *
     * @param i the index
     * @throws IndexOutOfBoundsException
     */
    private void validate(int i) {
        if (1 > i || N < i)
            throw new IndexOutOfBoundsException("index i out of bounds");
    }

    /**
     * Is site (row <tt>i</tt>, column <tt>j</tt> open?
     *
     * @param i the row
     * @param j the column
     * @return true if site is open, otherwise false
     */
    public boolean isOpen(int i, int j) {
        int p = xyTo1D(i, j);
        return open[p];
    }

    /**
     * Is site (row <tt>i</tt>, column <tt>j</tt>) full?
     * <p>
     * A full site is an open site that can be connected to an
     * open site in the top row via a chain of neighboring
     * (left, right, up, down) open sites.
     *
     * @param i the row
     * @param j the column
     * @return true if site is full, otherwise false
     */
    public boolean isFull(int i, int j) {
        if (!isOpen(i, j))
            return false;

        int p = xyTo1D(i, j);

        return ufb.connected(p, top);
    }

    /**
     * Does the system percolate?
     * We say the system percolates if there is a full site in the bottom row.
     * In other words, a system percolates if we fill all open sites
     * connected to the top row and that process fills some open
     * site on the bottom row
     *
     * @return true if percolates, otherwise false
     */
    public boolean percolates() {
        // check whether virtual top site is connected to virtual bottom site
        return ufa.connected(top, bottom);
    }

    /**
     * Uniquely map 2D grid coordinates to 1D UF coordinates
     *
     * @param i the row
     * @param j the column
     * @return the 1D coordinate
     */
    private int xyTo1D(int i, int j) {
        validate(i);
        validate(j);
        return (i - 1) * N + (j - 1);
    }
}
