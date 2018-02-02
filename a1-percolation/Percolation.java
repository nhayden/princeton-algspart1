// Class Percolation
// Author: Nathaniel Hayden
// Purpose: Maintain grid using weighted quick union-find algorithm in
//   Monte Carlo simulations of percolation
// Additional comments:
//   - For simplicity ad hoc unit tests included here
// Based on assignment 1 of Princeton Algorithms I course

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
  private boolean[][] grid;
  private int width;
  private int virtualTop, virtualBottom;
  private WeightedQuickUnionUF wPerc, wFull;
  private int numOpenSites;
  
  private boolean isValidIdx(int row, int col) {
    if(row > 0 && row <= width && col > 0 && col <= width)
      return true;
    return false;
  }
  
  // 1-BASED
  // converts 2D (x, y) coordinates to 1-D system
  private int xy1D(int row, int col) {
    if(isValidIdx(row, col))
      return (row - 1) * width + (col - 1);
    return -1;
  }
  
  // TESTING-ONLY METHODS
  private void printGrid() {
    for(int r = 0; r < this.width; r++) {
      for(int c = 0; c < this.width; c++)
        StdOut.print(this.getSite0B(r,c) ? "T" : "F" + " ");
      StdOut.println();
    }
  }
  private WeightedQuickUnionUF getW() {
//    StdOut.println(w.toString());
    return wPerc;
  }
  // 0-based counting
  private boolean getSite0B(int row, int col) {
    return this.grid[row][col];
  }
  
  // PRIVATE
  // 1-based counting
  private boolean getSite1B(int row, int col) {
    return this.grid[row-1][col-1];
  }

  private int getWidth() {
    return this.width;
  }
  
  private void openSite1B(int row, int col) {
    this.grid[row-1][col-1] = true;
  }
  
  // 1-BASED
  private void connectToTop(int row, int col) {
    wPerc.union(xy1D(row, col), this.virtualTop);
    wFull.union(xy1D(row, col), this.virtualTop);
  }
  private void connectToBottom(int row, int col) {
    wPerc.union(xy1D(row, col), this.virtualBottom);
  }
  
  // API
  public Percolation(int n) { // create n-by-n grid, with all sites blocked
    if(n <= 0)
      throw new java.lang.IllegalArgumentException();
    width = n;
    grid = new boolean[n][n];
    // include virtual top and bottom
    wPerc = new WeightedQuickUnionUF(n*n + 2);
    wFull = new WeightedQuickUnionUF(n*n + 1);
    // ex: n = 4; grid described by 0..15; vTop = 16, vBot = 17
    virtualTop = n*n;
    virtualBottom = n*n + 1;
    numOpenSites = 0;
  }

  // open site (row, col) if it is not open already
  public void open(int row, int col) {
    if(!isValidIdx(row, col))
      throw new java.lang.IndexOutOfBoundsException();
    // check if already open
    if(getSite1B(row,col)) {
      return;
    }
    numOpenSites++;
    this.openSite1B(row, col);
    // in top row (connect to virtual top) OR open neighbor above
    if(row == 1) {
      connectToTop(row, col);
    } else if(this.isValidIdx(row-1, col) && this.getSite1B(row-1, col)) {
      wPerc.union(xy1D(row-1, col), xy1D(row, col));
      wFull.union(xy1D(row-1, col), xy1D(row, col));
      
    }
    
    // in bottom row (connect to virtual bottom) OR open neighbor below
    if(row == width) {
      connectToBottom(row, col);
    } if(this.isValidIdx(row+1, col) && this.getSite1B(row+1, col)) {
      wPerc.union(xy1D(row+1, col), xy1D(row, col));
      wFull.union(xy1D(row+1, col), xy1D(row, col));
    }
    
    // open neighbor to left
    if(this.isValidIdx(row, col-1) && this.getSite1B(row, col-1)) {
      wPerc.union(xy1D(row, col-1), xy1D(row, col));
      wFull.union(xy1D(row, col-1), xy1D(row, col));
    }
    // open neighbor to right
    if(this.isValidIdx(row, col+1) && this.getSite1B(row, col+1)) {
      wPerc.union(xy1D(row, col+1), xy1D(row, col));
      wFull.union(xy1D(row, col+1), xy1D(row, col));
    }
    
  }
  
  // is site (row, col) open?
  public boolean isOpen(int row, int col) {
    return getSite1B(row, col);
  }
  // is site (row, col) full?
  public boolean isFull(int row, int col) {
    return wFull.connected(xy1D(row, col), virtualTop);
  }
  // number of open sites
  public int numberOfOpenSites() { return numOpenSites; }
  // does the system percolate?
  public boolean percolates() {
    return wPerc.connected(virtualTop, virtualBottom);
  }
  
  private static boolean testIsValidIdx() {
    boolean res = false;
    Percolation p = new Percolation(4);
    int row = 4, col = 4;
    StdOut.println("------Testing isValidIdx------");
    String isValidIdxs = "FAILED isValidIdx(%d, %d): %s\n";
    // boolean test value
    res = p.isValidIdx(row, col);
    if(!res) {
      StdOut.printf(isValidIdxs, row, col, res);
      return false;
    }
    row = 5; col = 4;
    res = p.isValidIdx(row, col);
    if(res) {
      StdOut.printf(isValidIdxs, row, col, res);
      return false;
    }
    StdOut.println("PASSED");
    return true;
  }
  private static boolean testXy1D() {
    int res = -1, expect = -1;
    Percolation p = new Percolation(4);
    int row = 4, col = 4;
    StdOut.println("------Testing xy1D------");
    String xy1Ds = "FAILED (%d, %d) -> %d . Expected %d\n";
    // last
    row = 4; col = 4;
    expect = 15;
    res = p.xy1D(row, col);
    if(res != expect) {
      StdOut.printf(xy1Ds, row, col, res, expect);
      return false;
    }
    // first
    row = 1; col = 1;
    expect = 0;
    res = p.xy1D(row, col);
    if(res != expect) {
      StdOut.printf(xy1Ds, row, col, res, expect);
      return false;
    }
    // end of first row
    row = 1; col = 4;
    expect = 3;
    res = p.xy1D(row, col);
    if(res != expect) {
      StdOut.printf(xy1Ds, row, col, res, expect);
      return false;
    }    
    // beginning of last row
    row = 4; col = 1;
    expect = 12;
    res = p.xy1D(row, col);
    if(res != expect) {
      StdOut.printf(xy1Ds, row, col, res, expect);
      return false;
    }
    // arbitrary middle-of-grid
    row = 2; col = 3;
    expect = 6;
    res = p.xy1D(row, col);
    if(res != expect) {
      StdOut.printf(xy1Ds, row, col, res, expect);
      return false;
    }
    // test for bad input
    row = 0; col = 1;
    res = p.xy1D(row, col);
    if(res != -1) {
      StdOut.printf(xy1Ds, row, col, res, -1);
      return false;
    }
    
    StdOut.println("PASSED");
    return true;
  }
  
  private static boolean testOpen() {
    boolean res = false, expect = false;
    Percolation p = new Percolation(4);
    int row = 4, col = 4;
    StdOut.println("------Testing open------");
    String opens = "FAILED grid[%d][%d] is %s . Expected %s\n";

    // no calls to open()
    for(int r = 1; r <= p.getWidth(); r++) {
      for(int c = 1; c <= p.getWidth(); c++) {
        if(p.getSite1B(r, c))
          StdOut.printf(opens, row, col, true, false);
      }
    }
    // throw exception for bad args
    // p.open(0, 1);
    
    // open arbitrary middle site
    row = 2; col = 3;
    expect = true;
    p.open(row, col);
    res = p.getSite1B(row, col);
    if(res != expect) {
      StdOut.printf(opens, row, col, res, expect);
      return false;
    }
    // not connected to previous site
    row = 3; col = 2;
    expect = true;
    p.open(row,col);
    res = p.getSite1B(row,col);
    if(res != expect) {
      StdOut.printf(opens, row, col, res, expect);
      return false;      
    }
    if(p.getW().connected(p.xy1D(2,3), p.xy1D(3,2))) {
      StdOut.println("FAILED (2,3) and (3,2) should not be connected");
    }
    // will connect 2 previous opened sites
    p.open(2,2);
    if(!p.getW().connected(p.xy1D(2,2), p.xy1D(2,3))) {
      StdOut.println("FAILED (2,2) and (2,3) should be connected");
    }
    if(!p.getW().connected(p.xy1D(2,2), p.xy1D(3,2))) {
      StdOut.println("FAILED (2,2) and (3,2) should be connected");
    }
    if(!p.getW().connected(p.xy1D(2,3), p.xy1D(3,2))) {
      StdOut.println("FAILED (2,3) and (3,2) should be connected");
    }
    
    // will connect previous 3 sites
    p.open(3,3);
    boolean conn = true;
    conn = conn && p.getW().connected(p.xy1D(2,2), p.xy1D(3,3));
    
    Percolation p2 = new Percolation(4);
    p2.open(1,2);
    if(!p2.getW().connected(p2.xy1D(1,2), 16)) {
      StdOut.println("FAILED (1,2) should be connected to virtual top");
      return false;
    }
    p2.open(4,2);
    if(!p2.getW().connected(p2.xy1D(4,2), 17)) {
      StdOut.println("FAILED (4,2) should be connected to virtual bottom");
      return false;
    }
    p2.open(3,2);
    if(!p2.getW().connected(p2.xy1D(3,2), 17)) {
      StdOut.println("FAILED (3,2) should be connected to virtual bottom");
      return false;
    }
    p2.open(2,2);
    // grid should now look like
    //  X  O  X  X
    //  X  O  X  X
    //  X  O  X  X
    //  X  O  X  X
    // component identifier of virtual top
    int compid = p2.getW().find(16);
    for(int r = 1; r <= 4; r++) {
      if(p2.getW().find(p2.xy1D(r,2)) != compid) {
        String s = "FAILED (%d,2) should have comp id %d\n";
        StdOut.printf(s, r, compid);
        return false;
      }
    }
    if(p2.getW().find(17) != compid) {
      StdOut.println("FAILED virtual top and bottom should have same comp id");
    }    
    
    StdOut.println("PASSED");    
    return true;
  }
  
  private static boolean testIsFull() {
    StdOut.println("------Testing isFull------");
    Percolation p = new Percolation(3);
    p.open(1,2);
    if(!p.isFull(1,2)) {
      StdOut.println("FAILED (1,2) should be full");
      return false;
    }
    p.open(3,2);
    if(p.isFull(3,2)) {
      StdOut.println("FAILED (3,2) should NOT be full");
      return false;
    }
    p.open(2,2);
    if(!p.isFull(3,2)) {
      StdOut.println("FAILED (3,2) should be full");
      return false;
    }
    if(!p.isFull(2,2)) {
      StdOut.println("FAILED (2,2) should be full");
      return false;
    }
    // to the side
    p.open(2,1);
    if(!p.isFull(2,1)) {
       StdOut.println("FAILED (2,1) (to the side) should be full");
       return false;
    }
    
    StdOut.println("PASSED");
    return true;
  }
  
  private static boolean testNumberOfOpenSites() {
    StdOut.println("------Testing numberOfOpenSites------");
    Percolation p = new Percolation(2);
    String s = "FAILED num open sites: %d; expected: %d\n";
    if(p.numberOfOpenSites() != 0) {
      StdOut.printf(s, p.numberOfOpenSites(), 0);
      return false;
    }
    p.open(1,1);
    if(p.numberOfOpenSites() != 1) {
      StdOut.printf(s, p.numberOfOpenSites(), 1);
      return false;
    }
    // make sure calling open() on open site doesn't affect count
    p.open(1,1);
    if(p.numberOfOpenSites() != 1) {
      StdOut.println("FAILED called open() on open site and count changed");
      return false;
    }
    // open remaining sites
    p.open(1,2);
    p.open(2,1);
    p.open(2,2);
    if(p.numberOfOpenSites() != 4) {
      StdOut.printf(s, p.numberOfOpenSites(), 4);
      return false;
    }
    // all sites are open;
    // confirm calling open() never causes count to exceed possible
    p.open(2,2);
    String s2 = "FAILED calling open() with open grid changed count";
    if(p.numberOfOpenSites() != 4) {
      StdOut.println(s2);
      return false;
    }
    
    StdOut.println("PASSED");
    return true;
  }
  
  private static boolean testPercolates() {
    StdOut.println("------Testing percolates------");
    Percolation p = new Percolation(2);
    p.open(1,1);
    if(p.percolates()) {
      StdOut.println("FAILED grid should NOT percolate");
      return false;
    }
    p.open(2,1);
    if(!p.percolates()) {
      StdOut.println("FAILED grid should percolate");
      return false;
    }
    
    StdOut.println("PASSED");
    return true;
  }
  
  private static boolean testForBackwash() {
    StdOut.println("------Testing for backwash------");
    
    Percolation p = new Percolation(3);
    p.open(1,3);
    p.open(2,3);
    p.open(3,3);
    if(!p.percolates()) {
      StdOut.println("FAILED percolation problem when testing for backwash");
      return false;
    }
    
    p.open(3,1);
    if(p.isFull(3,1)) {
      StdOut.println("FAILED (3,1) should not be full");
      return false;
    }
    
    StdOut.println("PASSED");
    return true;
  }
  
  private static void runTests() {
    // test bad arg
    // Percolation p = new Percolation(0);
    
    if(!testIsValidIdx())
      StdIn.readChar();
    if(!testXy1D())
      StdIn.readChar();
    if(!testOpen())
      StdIn.readChar();   
    if(!testIsFull())
      StdIn.readChar();
    if(!testNumberOfOpenSites())
      StdIn.readChar();
    if(!testPercolates())
      StdIn.readChar();
    if(!testForBackwash())
      StdIn.readChar();
  }
  
  public static void main(String[] args) { // test client (optional) 
    runTests();
    
//    p.printGrid();
  }
}