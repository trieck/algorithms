import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Thomas A. Rieck
 * 1/30/2016
 * Purpose: perform T independent experiments on an N-by-N grid
 */
public class PercolationStats {

    private static final double CONFIDENCE_COEFFICIENT = 1.96;
    private final int T;
    private final double[] samples;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();

        this.T = T;
        samples = new double[T];

        for (int i = 0, count; i < T; i++) {
            Percolation percolation = new Percolation(N);
            count = 0;
            while (!percolation.percolates()) {
                int j = StdRandom.uniform(1, N + 1);
                int k = StdRandom.uniform(1, N + 1);
                if (!percolation.isOpen(j, k)) {
                    percolation.open(j, k);
                    count++;
                }
            }
            samples[i] = count / (double) (N * N);
        }
    }

    /**
     * Main entry point of application
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.printf("%s: N T%n", PercolationStats.class.getSimpleName());
            System.exit(1);
        }

        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(N, T);

        StdOut.printf("mean                    = %.10f%n", stats.mean());
        StdOut.printf("stddev                  = %.10f%n", stats.stddev());
        StdOut.printf("95%% confidence interval = %.10f, %.10f%n",
                stats.confidenceLo(), stats.confidenceHi());
    }

    /**
     * Sample mean of percolation threshold
     *
     * @return the mean
     */
    public double mean() {
        return StdStats.mean(samples);
    }

    /**
     * Sample standard deviation of percolation threshold
     *
     * @return the standard deviation
     */
    public double stddev() {
        return StdStats.stddev(samples);
    }

    /**
     * Low endpoint of 95% confidence interval
     *
     * @return the low endpoint
     */
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_COEFFICIENT * stddev()) / Math.sqrt(T));
    }

    /**
     * High endpoint of 95% confidence interval
     *
     * @return the high endpoint
     */
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_COEFFICIENT * stddev()) / Math.sqrt(T));
    }
}
