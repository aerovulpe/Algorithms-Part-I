import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private double[] thresholds;

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int n, int t) {
		if (n < 1 || t < 1)
			throw new IllegalArgumentException("N <= 0 or T <= 0!");

		thresholds = new double[t];
		for (int c = 0; c < t; c++) {
			final Percolation percolation = new Percolation(n);
			int numOpened = 0;
			while (!percolation.percolates()) {
				int i = StdRandom.uniform(n) + 1;
				int j = StdRandom.uniform(n) + 1;

				if (!percolation.isOpen(i, j)) {
					percolation.open(i, j);
					numOpened++;
				}
			}

			thresholds[c] = numOpened / n * n;
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

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mean() - 1.96 * stddev() / Math.sqrt(thresholds.length);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mean() + 1.96 * stddev() / Math.sqrt(thresholds.length);
	}

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int t = Integer.parseInt(args[1]);

		PercolationStats percolationStats = new PercolationStats(n, t);
		System.out.println("mean                    = " + percolationStats.mean());
		System.out.println("stddev                  = " + percolationStats.stddev());
		System.out.println("95% confidence interval = " + percolationStats.confidenceLo() + ", "
				+ percolationStats.confidenceHi());
	}
}
