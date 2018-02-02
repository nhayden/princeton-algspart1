// Class PercolationStats
// Author: Nathaniel Hayden
// Purpose: Run Monte Carlo simulations of percolation
// Based on assignment 1 of Princeton Algorithms I course

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
  private int trials, numTotalSites;
  private double[] ratios;
  
  public double mean() { return StdStats.mean(ratios); }
  public double stddev() { return StdStats.stddev(ratios); }
  public double confidenceLo() {
    return mean() - (1.96 * stddev()) / Math.sqrt(trials);
  }
  public double confidenceHi() {
    return mean() + (1.96 * stddev()) / Math.sqrt(trials);
  }
  
  public PercolationStats(int n, int trials) {
    if(n <= 0 || trials <= 0) {
      throw new java.lang.IllegalArgumentException(); 
    }
    this.trials = trials;
    
    ratios = new double[trials];
    numTotalSites = n * n;
    int row, col;
    
    for(int numTrial = 0; numTrial < trials; numTrial++) {
      // a new grid for each trial
      Percolation p = new Percolation(n);
      int numOpenSites = 0;
      while(!p.percolates()) {
        row = StdRandom.uniform(n) + 1;
        col = StdRandom.uniform(n) + 1;
        // ignore if already opened
        if(!p.isOpen(row, col)) {
          p.open(row, col);
          numOpenSites++;
        }
      }
      ratios[numTrial] = (double)numOpenSites / numTotalSites;
    }
    
  }
  
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    
    PercolationStats ps = new PercolationStats(n, trials);
    StdOut.printf("%-23s = %f\n", "mean", ps.mean());
    StdOut.printf("%-23s = %f\n", "stddev", ps.stddev());
    StdOut.printf("%-23s = [%f, %f]", "95% confidence interval\n", ps.confidenceLo(), ps.confidenceHi());
    
    
  }
}