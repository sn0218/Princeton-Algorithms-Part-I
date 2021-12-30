import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] openSites; // declare 2D array for NxN grid of percolation system
    private int numOpenSites; // number of opened sites
    private final int sizeN; // store the value of n of n x n grid
    private final WeightedQuickUnionUF uF;
    // declare WeightedQuickUnionUF object for dynamic connectivity(1D array)
    private final WeightedQuickUnionUF ufNoBottom;
    private final int virtualTopIndex; // declare the virtualTop
    private final int virtualBotIndex; // declare the virtualBottom


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // check for valid input of n
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }

        /*
        if n = 3, total 9 indices of sites stored in UF object (0 - 8), 9 is virtualTopIndex, 10 is virtualBotIndex
        3*3+2 = 11 sites
        */
        // instantiate the WeightedQuickUnionUF object with virtual Top and Bottom Site
        uF = new WeightedQuickUnionUF(n * n + 2);

        // instantiate another instance without virtual bottom site to check particular site if it is full
        ufNoBottom = new WeightedQuickUnionUF(n * n + 1);

        // instantiate 2D array object for NxN grid of percolation system
        openSites = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                openSites[i][j] = false;
            }
        }
        // initialize the class variable
        sizeN = n;
        numOpenSites = 0;
        virtualTopIndex = n * n;
        virtualBotIndex = n * n + 1;

    }

    // convert the 2D array coordinates to WeightedUnionFindUF object indices (1D array)
    private int mapUFindex(int row, int col) {
        return row * sizeN + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // check the valid input of row and col (1 to n)
        if (row < 1 || col < 1 || row > sizeN || col > sizeN) {
            throw new IllegalArgumentException("row and col are between 1 to n");
        }
        // convert current site to 2D array coordinates
        int xCor = row - 1;
        int yCor = col - 1;
        // store the current site to UF object index from 2D array coordinates
        int curUFindex = mapUFindex(xCor, yCor);

        // check if the site is opened, if not, open the specified site
        if (!openSites[xCor][yCor]) {
            openSites[xCor][yCor] = true; // set the current site to open
            numOpenSites++; // update the number of opened sites

            // connect the virtual top site to first row element
            if (row == 1) {
                ufNoBottom.union(virtualTopIndex, curUFindex);
                uF.union(virtualTopIndex, curUFindex);
            }
            // connect the virtual bottom site to last row element
            if (row == sizeN) {
                uF.union(virtualBotIndex, curUFindex);
            }
            // connect the above site opened to current site
            if (row > 1 && isOpen(row - 1, col)) {
                ufNoBottom.union(mapUFindex(xCor - 1, yCor), curUFindex);
                uF.union(mapUFindex(xCor - 1, yCor), curUFindex);
            }
            // connect the below opened site to current site
            if (row < sizeN && isOpen(row + 1, col)) {
                ufNoBottom.union(mapUFindex(xCor + 1, yCor), curUFindex);
                uF.union(mapUFindex(xCor + 1, yCor), curUFindex);
            }
            // connect the left opened site to current site
            if (col > 1 && isOpen(row, col - 1)) {
                ufNoBottom.union(mapUFindex(xCor, yCor - 1), curUFindex);
                uF.union(mapUFindex(xCor, yCor - 1), curUFindex);
            }
            // connect the right opened site to current site
            if (col < sizeN && isOpen(row, col + 1)) {
                ufNoBottom.union(mapUFindex(xCor, yCor + 1), curUFindex);
                uF.union(mapUFindex(xCor, yCor + 1), curUFindex);
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // check the valid input of row and col (1 to n)
        if (row < 1 || col < 1 || row > sizeN || col > sizeN) {
            throw new IllegalArgumentException("row and col are between 1 to n");

        }
        // convert current site to 2D array coordinates
        int xCor = row - 1;
        int yCor = col - 1;
        // return the boolean value of openSites by converting the row and col to 2D array coordinates
        return openSites[xCor][yCor];
    }

    // is the site (row, col) full?
    // full site is that an open site can be connected to an open site in the top row
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > sizeN || col > sizeN) {
            throw new IllegalArgumentException("row and col are between 1 to n");
        }
        // convert current site to 2D array coordinates
        int xCor = row - 1;
        int yCor = col - 1;
        // check connection between the current site and virtual top site
        return ufNoBottom.find(mapUFindex(xCor, yCor)) == ufNoBottom.find(virtualTopIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uF.find(virtualBotIndex) == uF.find(virtualTopIndex);
    }


    // test client (optional)
    public static void main(String[] args) {

        int x = 5;
        Percolation myPercolation = new Percolation(x);
        myPercolation.open(1, 1);
        myPercolation.open(1, 3);
        myPercolation.open(3, 4);
        myPercolation.open(2, 2);
        myPercolation.open(2, 4);
        System.out.println("System is percolated? " + myPercolation.percolates());
        myPercolation.open(3, 5);
        myPercolation.open(5, 5);
        myPercolation.open(4, 5);
        myPercolation.open(4, 3);
        myPercolation.open(4, 1);
        myPercolation.open(2, 3);
        System.out.println("System is percolated? " + myPercolation.percolates());
        myPercolation.open(5, 3);
        myPercolation.open(5, 2);
        myPercolation.open(5, 1);
        System.out.println("(5, 1) is full? " + myPercolation.isFull(5, 1));
        if (myPercolation.isFull(5, 1)) System.out.println("Backwash occurs");
        System.out.println("No Backwash occurs");
        myPercolation.open(3, 1);
        System.out.println("System is percolated? " + myPercolation.percolates());
        System.out.println("No.of opened sites: " + myPercolation.numberOfOpenSites());

    }
}
