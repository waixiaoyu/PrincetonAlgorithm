package edu.princeton.percolation;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] bSites;
    private WeightedQuickUnionUF uf;
    private int nUpPivotIndex = 0;
    private int nDownPivotIndex = 0;
    private int nNumOpenSites = 0;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        // init sites, if blocked, it is false,
        if (n > 0) {
            bSites = new boolean[n][n];
        } else {
            throw new IllegalArgumentException();
        }
        uf = new WeightedQuickUnionUF(n * n + 2); // because we need 2 more
                                                  // nodes
        nUpPivotIndex = n * n;
        nDownPivotIndex = n * n + 1;
    }

    // open site (row, col) if it is not open already
    // row and col are between [1,n]
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            nNumOpenSites++;
            bSites[row - 1][col - 1] = true;
            int nCurIndex = coToIndex(row, col);
            if (row == 1) {
                uf.union(nCurIndex, nUpPivotIndex);
            }
            if (row == bSites.length) {
                uf.union(nCurIndex, nDownPivotIndex);
            }
            if (isInRange(row + 1, col) && isOpen(row + 1, col)) {
                uf.union(nCurIndex, coToIndex(row + 1, col));
            }
            if (isInRange(row - 1, col) && isOpen(row - 1, col)) {
                uf.union(nCurIndex, coToIndex(row - 1, col));
            }
            if (isInRange(row, col + 1) && isOpen(row, col + 1)) {
                uf.union(nCurIndex, coToIndex(row, col + 1));
            }
            if (isInRange(row, col - 1) && isOpen(row, col - 1)) {
                uf.union(nCurIndex, coToIndex(row, col - 1));
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (isInRange(row, col)) {
            return bSites[row - 1][col - 1];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    // is site (row, col) full? it is very important, because the water can not
    // flow from bottom to top
    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            return row == 1 || (isInRange(row - 1, col) && isFull(row - 1, col))
                    || (isInRange(row, col + 1) && isFull(row, col + 1))
                    || (isInRange(row, col - 1) && isFull(row, col - 1))
                    || (isInRange(row - 1, col) && isFull(row - 1, col)) ? true : false;
        } else {
            return false;
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return nNumOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(nDownPivotIndex, nUpPivotIndex);
    }

    // judge given row and col is in range
    private boolean isInRange(int row, int col) {
        return row > 0 && row <= bSites.length && col > 0 && col <= bSites[0].length ? true : false;
    }

    // transfor 2D to 1D
    private int coToIndex(int row, int col) {
        return (row - 1) * bSites[0].length + col - 1;
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
