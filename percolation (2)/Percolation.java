/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // For convenience we start with 1 ->(0,0)->represents(1,1) (row-1)*gridwidth+col;
    /**
     * uf instance
     * uf -> 0 1~n^2 n^2+1
     * we need to find if uf.find(0,n^2+1) is true to distinguish they percolate
     */
    private WeightedQuickUnionUF uf;

    /**
     * ufForfull instance
     * used to distinguish whether it is a opensite
     * if(ufForfull.find(0,GetIndex(row,col))) -> it is full!
     * otherwise it is not.
     */
    private WeightedQuickUnionUF ufForfull;
    private int gridwidth;
    private boolean isopen[][];
    private boolean ispercolation;
    private int dx[] = { 0, -1, 0, 1 };// WEST NORTH EAST SOUTH
    private int dy[] = { -1, 0, 1, 0 };// WEST NORTH EAST SOUTH

    private int opensites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // Here we define WeightedQuickUF class;
        if (n <= 0)
            throw new IllegalArgumentException("n should be nonegative!");
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufForfull = new WeightedQuickUnionUF(n * n + 1);
        gridwidth = n;
        isopen = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isopen[i][j] = false;
            }
        }
        ispercolation = false;
        opensites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValidBound(row, col))
            throw new IllegalArgumentException("You put an invalid grid!");
        if (!isopen[row - 1][col - 1]) {
            isopen[row - 1][col - 1] = true;
            for (int i = 0; i < 4; i++) {
                if (isValidBound(row + dx[i], col + dy[i]) && isopen[row - 1 + dx[i]][col - 1
                        + dy[i]]) {
                    int a = GetIndex(row, col);
                    int temp = GetIndex(row + dx[i], col + dy[i]);
                    uf.union(a, temp);
                    ufForfull.union(a, temp);
                }
            }
            opensites++;
            int a = GetIndex(row, col);
            if (row == 1) {
                uf.union(0, a);
                ufForfull.union(0, a);
            }
            if (row == gridwidth) {
                uf.union(a, gridwidth * gridwidth + 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValidBound(row, col))
            throw new IllegalArgumentException("You put an invalid grid!");
        return isopen[row - 1][col - 1];
    }

    // get the index of the grid;
    private int GetIndex(int row, int col) {
        if (!isValidBound(row, col))
            throw new IllegalArgumentException("You put an invalid grid!");
        // Like (1,1) -> (1-1)*4+1=1;
        return (row - 1) * gridwidth + col;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // Error check part;
        if (!isValidBound(row, col))
            throw new IllegalArgumentException("You put an invalid grid!");
        return ufForfull.find(GetIndex(row, col)) == ufForfull.find(0);
    }

    // return if the site in the bound
    private boolean isValidBound(int row, int col) {
        return (row >= 1 && row <= gridwidth) && (col >= 1 && col <= gridwidth);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        ispercolation = (uf.find(0) == uf.find(gridwidth * gridwidth + 1));
        return ispercolation;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation test = new Percolation(3);
        test.open(1, 1);
        test.open(1, 2);
        boolean b = test.isFull(2, 1);
        System.out.println(b);
        System.out.println(test.numberOfOpenSites());
        test.open(2, 2);
        boolean a = test.isFull(2, 2);
        System.out.println(a);
        System.out.println(test.numberOfOpenSites());
        test.open(3, 2);
        boolean c = test.percolates();
        System.out.println(c);
        System.out.println(test.numberOfOpenSites());
    }
}
