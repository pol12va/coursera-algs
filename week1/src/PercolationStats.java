import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] results;
    private int traits;

    public PercolationStats(int n, int traits) {
        if (n <= 0 || traits <= 0) {
            throw new IllegalArgumentException("incorrect arguments");
        }
        this.traits = traits;
        results = new double[traits];

        for (int i = 0; i < traits; i++) {
            Percolation p = new Percolation(n);

            do {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            } while (!p.percolates());

            addResult(i, (double) p.numberOfOpenSites() / (n * n));
        }

    }

    public double mean() {
        return StdStats.mean(results);
    }

    public double stddev() {
        return StdStats.stddev(results);
    }

    public double confidenceLo() {
        return mean() - 1.96d * stddev() / Math.sqrt(traits);
    }

    public double confidenceHi() {
        return mean() + 1.96d * stddev() / Math.sqrt(traits);
    }

    private void addResult(int i, double res) {
        results[i] = res;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int traits = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, traits);

    }

}
