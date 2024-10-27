package xaliz;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation 
{
    private final int n;
    private final boolean[][] openSites;
    private final WeightedQuickUnionUF uf;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;
        this.openSites = new boolean[n][n];

        // two extra for the virtual sites
        uf = new WeightedQuickUnionUF(n * n + 2);
        virtualTop = n * n;
        virtualBottom = n * n + 1;

        // connect the virtual top site to the top row sites
        for (int col = 0; col < n; col++) {
            uf.union(virtualTop, getIndex(0, col));
        }

        // connect the virtual bottom site to the bottom row sites
        for (int col = 0; col < n; col++) {
            uf.union(virtualBottom, getIndex(n - 1, col));
        }
    }

    private int getIndex(int row, int col) {
        return row * n + col;
    }
    private void validateIndices(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("row and col must be between 1 and " + n);
        }
    }

    public void open(int row, int col) {
        validateIndices(row, col);
        int r = row - 1, c = col - 1;
        
        if (!openSites[r][c]) {
            openSites[r][c] = true;
            int index = getIndex(r, c);

            // connect the neighboring open sites
            if (r > 0 && isOpen(row - 1, col)) {
                uf.union(index, getIndex(r - 1, c));
            }
            if (r < n - 1 && isOpen(row + 1, col)) {
                uf.union(index, getIndex(r + 1, c));
            }
            if (c > 0 && isOpen(row, col - 1)) {
                uf.union(index, getIndex(r, c - 1));
            }
            if (c < n - 1 && isOpen(row, col + 1)) {
                uf.union(index, getIndex(r, c + 1));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return openSites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        int index = getIndex(row - 1, col - 1);

        // is full if the site is open and connected to the virtual top site
        return isOpen(row, col) && uf.find(virtualTop) == uf.find(index);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                count += openSites[i][j] ? 1 : 0;
            }
        }
        return count;
    }

    public boolean percolates() {
        // if there exists a path from the virtual top site to the virtual bottom site
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }


    public static void main(String[] args)
    {
        // main for testing
        Percolation perc = new Percolation(5);
        System.out.println("Percolation created for 5x5 grid");

        perc.open(1, 1);
        perc.open(1, 3);
        perc.open(3, 1);
        perc.open(2, 1);
        perc.open(2, 2);
        perc.open(3, 3);
        perc.open(4, 3);
        perc.open(5, 3);
        perc.open(3, 2); // starts percolating after opening this site

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                System.out.print(perc.isOpen(i, j) ? "0 " : "X ");
            }
            System.out.println();
        }

        System.out.println("Opened site at (1, 1)");
        System.out.println("Is site (1, 1) open? " + perc.isOpen(1, 1));
        System.out.println("Is site (1, 1) full? " + perc.isFull(1, 1));
        System.out.println("Number of open sites: " + perc.numberOfOpenSites());
        System.out.println("Percolates? " + perc.percolates());
    }
}
