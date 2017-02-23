package edu.princeton.percolation;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] dResults;
    private int nSiteLength;
    private int nTrials;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.nTrials = trials;
        this.dResults = new double[trials];
        this.nSiteLength = n;
        runExper();
    }

    private void runExper() {
        // repeat experiment trials times
        for (int i = 0; i < dResults.length; i++) {
            System.out.println("experiment:" + (i + 1));
            int[] nRandomArray = randomArray(1, nSiteLength * nSiteLength, nSiteLength * nSiteLength);
            Percolation p = new Percolation(nSiteLength);
            for (int j = 0; j < nRandomArray.length; j++) {
                int[] cr = indexToCo(nRandomArray[j]);
                int row = cr[0];
                int col = cr[1];
                p.open(row, col);
                if (p.percolates()) {
                    break;
                }
            }
            dResults[i] = (double) p.numberOfOpenSites() / (double) (nSiteLength * nSiteLength);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(dResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(dResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(nTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(nTrials);
    }

    private static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        // 初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        // Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            // 待选数组0到(len-2)随机一个下标
            index = Math.abs(StdRandom.uniform(Integer.MAX_VALUE) % len--);
            // 将随机到的数放入结果集
            result[i] = source[index];
            // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }

    // transfer index to coordinate
    private int[] indexToCo(int n) {
        if (n <= 0 || n > nSiteLength * nSiteLength) {
            throw new IllegalArgumentException();
        }
        int[] cr = new int[2];
        if (n % nSiteLength == 0) {
            cr[1] = nSiteLength;
            cr[0] = n / nSiteLength;
        } else {
            cr[1] = n % nSiteLength;
            cr[0] = n / nSiteLength + 1;
        }
        return cr;
    }

    // test client (described below)
    public static void main(String[] args) {
        // int[] random = randomArray(0, 200 * 200 - 1, 200 * 200);
        // System.out.println(random);
    }
}
