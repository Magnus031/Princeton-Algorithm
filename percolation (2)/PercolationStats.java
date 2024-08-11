/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform independent trials on an n-by-n grid

    private int gridwith;
    private Percolation per_test[];// used to store the test result;
    private int T;
    private double ans[];

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        gridwith = n;
        T = trials;
        ans = new double[T];
        per_test = new Percolation[T];
        for (int i = 0; i < T; i++) {
            // For-loop for the percolations
            per_test[i] = new Percolation(gridwith);
            while (!per_test[i].percolates()) {
                /** [1,n*n] */
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                per_test[i].open(row, col);
            }
            ans[i] = (1.0 * per_test[i].numberOfOpenSites()) / (gridwith * gridwith);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double result = 0;
        result = StdStats.mean(ans);
        return result;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double result = StdStats.stddev(ans);
        return result;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = this.mean();
        double s = stddev();
        double result = mean - (1.96 * s) / Math.sqrt(T);
        return result;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = this.mean();
        double s = stddev();
        double result = mean + (1.96 * s) / Math.sqrt(T);
        return result;
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java-algs4 PercolationStats n T");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, T);
        System.out.println("mean                    = " + percStats.mean());
        System.out.println("stddev                  = " + percStats.stddev());
        System.out.println("95% confidence interval = [" + percStats.confidenceLo()
                                   + ", " + percStats.confidenceHi() + "]");
    }
    
}
