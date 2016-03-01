import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] thresholds;

    /**
     * perform t independent experiments on an n-by-n grid
     *
     * @param n
     * @param t
     */
    public PercolationStats(int n, int t) {
        if (n < 1 || t < 1)
            throw new IllegalArgumentException("N <= 0 or T <= 0!");


        thresholds = new double[t];
        for (int i = 0; i < t; i++) {
            Percolation percolation = new Percolation(n);
            int numOpened = 0;
            double nSquared = n * n;

            while (numOpened < nSquared) {
                int p = StdRandom.uniform(1, n + 1);
                int q = StdRandom.uniform(1, n + 1);

                if (!percolation.isOpen(p, q)) {
                    numOpened++;
                    percolation.open(p, q);

                    if (percolation.percolates()) {
                        thresholds[i] = numOpened / nSquared;
                        break;
                    }
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(thresholds.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(thresholds.length);
    }

    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = " + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi());
    }
}
