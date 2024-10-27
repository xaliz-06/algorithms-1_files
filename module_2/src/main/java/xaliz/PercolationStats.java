package xaliz;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] thresholds; // stores the threshold for each trial
    private final int trials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }
        this.trials = trials;
        thresholds = new double[trials];

        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            int openSites = 0;

            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);

                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    openSites++;
                }
            }

            thresholds[t] = (double) openSites / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        // threshold for 95% confidence interval low
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi() {
        // threshold for 95% confidence interval high
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: PercolationStats n trials");
        }

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        // long startTime = System.nanoTime();

        PercolationStats stats = new PercolationStats(n, trials);

        // long endTime = System.nanoTime();

        // long duration = (endTime - startTime) / 1000000; // in milliseconds

        System.out.printf("mean                    = %.16f\n", stats.mean());
        System.out.printf("stddev                  = %.16f\n", stats.stddev());
        System.out.printf("95%% confidence interval = [%.16f, %.16f]\n", stats.confidenceLo(), stats.confidenceHi());
        // System.out.printf("Total time: %d ms\n", duration);
    }
}
