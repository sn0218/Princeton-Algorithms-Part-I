import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double FACTOR = 1.96; // declare the constant
    private final double mean;
    private final double stddev;
    private final int trials;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // check valid input
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException(
                    "Invalid Input, n and trials must be greater than 1");
        }
        this.trials = trials;
        double[] threshold = new double[trials]; // set instance variable to be local

        int i = 0;
        // repeat the computation experiement T times and averageing the result
        while (i < trials) {
            // initialize all sites to be blocked
            Percolation grid = new Percolation(n);

            // repeat until the the system percolates
            while (!grid.percolates()) {
                // choose a site uniformly at random among all blocked sites
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                if (!grid.isOpen(row, col)) {
                    grid.open(row, col);
                }
                // compute the percolation threshold i.e. opened sites / total sites
                threshold[i] = (double) grid.numberOfOpenSites() / (n * n);
            }

            i++;
        }
        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - FACTOR * stddev / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + FACTOR * stddev / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = 0;
        int t = 0;

        if (args.length != 2) {
            throw new IllegalArgumentException("Invalid Format");
        }

        try {
            // Parse the string argument into an integer value.
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException nfe) {
            // The first argument isn't a valid integer.  Print
            // an error message, then exit with an error code.
            System.out.println("The first argument must be an integer.");
        }

        PercolationStats test = new PercolationStats(n, t);
        System.out.println("mean                    = " + test.mean());
        System.out.println("stddev                  = " + test.stddev());
        System.out.println(
                "95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceHi()
                        + "]");

    }

}
