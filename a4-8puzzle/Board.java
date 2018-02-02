// Class Board
// Author: Nathaniel Hayden
// Purpose: Maintain board and info for use in A* algorithm to solve
//   8 puzzle sliding game
// Additional comments:
//   - For simplicity some ad hoc testing included here
// Based on assignment 4 of Princeton Algorithms I course

import java.util.ArrayDeque;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int n;
    private final int[][] blocks;
    private final boolean isGoal;
    private final int hamming;
    private final int manhattan;
    
    public Board(int[][] blocks) {
        // --- begin error checking
        if (blocks == null || blocks.length < 2)
            throw new java.lang.IllegalArgumentException();
        this.n = blocks.length;
        if (blocks.length != blocks[0].length)
            throw new java.lang.IllegalArgumentException("non-square array");
        // ---end error checking
        
        this.blocks = copyBlocks(blocks);
        this.hamming = getHamming(blocks);
        this.isGoal = this.hamming == 0;
        this.manhattan = getManhattan(blocks);
    }
    public int dimension() { return n; }
    public int hamming() { return hamming; }
    public int manhattan() { return manhattan; }
    public boolean isGoal() { return isGoal; }
    public Board twin() {
        int[][] twinBlocks = copyBlocks(blocks);
        if (twinBlocks[0][0] != 0 && twinBlocks[0][1] != 0) {
            int val = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[0][1];
            twinBlocks[0][1] = val;
        } else {
            int val = twinBlocks[1][0];
            twinBlocks[1][0] = twinBlocks[1][1];
            twinBlocks[1][1] = val;
        }
        return new Board(twinBlocks);
    }
    public boolean equals(Object x) {
        if (this == x) return true;
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Board that = (Board) x;
        // instance variables
        if (this.n != that.n) return false;
        if (this.isGoal != that.isGoal) return false;
        if (this.hamming != that.hamming) return false;
        if (this.manhattan != that.manhattan) return false;
        if (!isEqual(this.blocks, that.blocks)) return false;
        return true;
    }
    public Iterable<Board> neighbors() { return makeNeighbors(); }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ",  blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    private ArrayDeque<Board> makeNeighbors() {
        ArrayDeque<Board> res = new ArrayDeque<>();
        int[] rowcol = findZero(blocks);
        if (rowcol.length == 0)
            return res;
        int row = rowcol[0], col = rowcol[1];
        
        // if square above
        if (row > 0) {
            int[][] tmp = copyBlocks(blocks);
            tmp[row][col] = tmp[row-1][col];
            tmp[row-1][col] = 0;
            res.add(new Board(tmp));
        }
        // if square below
        if (row < n-1) {
            int[][] tmp = copyBlocks(blocks);
            tmp[row][col] = tmp[row+1][col];
            tmp[row+1][col] = 0;
            res.add(new Board(tmp));
        }
        // if square to left
        if (col > 0) {
            int[][] tmp = copyBlocks(blocks);
            tmp[row][col] = tmp[row][col-1];
            tmp[row][col-1] = 0;
            res.add(new Board(tmp));
        }
        // if square to right
        if (col < n-1) {
            int[][] tmp = copyBlocks(blocks);
            tmp[row][col] = tmp[row][col+1];
            tmp[row][col+1] = 0;
            res.add(new Board(tmp));
        }
        return res;
    }
    
    private static int[] findZero(int[][] a) {
        int n = a.length;
        if (a[n-1][n-1] == 0)
            return new int[]{n-1, n-1};
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0)
                    return new int[]{i, j};
            }
        }
        return new int[0];
    }
    
    private static boolean isEqual(int[][] a, int[][] b) {
        int n = a.length;
        if (n != b.length)
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] != b[i][j])
                    return false;
            }
        }
        return true;
    }
    
    private static int getManhattan(int[][] blocks) {
        int n = blocks.length;
        int count = 0;
        int expect = 1, actual;
        
        // rows 0..n-2
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n; j++) {
                actual = blocks[i][j];
                if (actual != expect && actual != 0) {
                    int rowdiff = Math.abs((actual-1) / n - i);
                    int coldiff = Math.abs((actual-1) % n - j);
                    count += rowdiff + coldiff;
                }
                expect++;
            }
        }
        // last row, except last column
        for (int j = 0; j < n-1; j++) {
            actual = blocks[n-1][j];
            if (actual != expect && actual != 0) {
                int rowdiff = Math.abs((actual-1) / n - (n-1));
                int coldiff = Math.abs((actual-1) % n - j);
                count += rowdiff + coldiff;
            }
            expect++;
        }
        // last row, last column
        if (blocks[n-1][n-1] != 0) {
            actual = blocks[n-1][n-1];
            int rowdiff = Math.abs((actual-1) / n - (n-1));
            int coldiff = Math.abs((actual-1) % n - (n-1));
            count += rowdiff + coldiff;
        }
        return count;
    }
    
    private static int getHamming(int[][] blocks) {
        int n = blocks.length;
        
        int val = 1, count = 0;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != val++ && blocks[i][j] != 0) {
                    count++;
                }
            }
        }
        for (int j = 0; j < n-1; j++) {
            if (blocks[n-1][j] != val++ && blocks[n-1][j] != 0) {
                count++;
            }

        }
        if (blocks[n-1][n-1] != 0)
            count++;
        return count;
    }
    
    private static int[][] copyBlocks(int[][] blocks) {
        int[][] dest = new int[blocks.length][blocks[0].length];
        for (int i = 0; i < blocks.length; i++)
            System.arraycopy(blocks[i], 0, dest[i], 0, blocks[0].length);
        return dest;
    }
    
    private static int[][] readBlocks(String file) {
        // create initial board from file
        In in = new In(file);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        return blocks;
    }
    
    public static void main(String[] args) {
//        int[][] testblocks = readBlocks(args[0]);
//        Board test = new Board(testblocks);
//        StdOut.println(test);
//        StdOut.println(test.hamming());
//        StdOut.println(test.manhattan());
//        StdOut.println(test.isGoal());
//        StdOut.println();

        Board goal = new Board(new int[][]{{1, 2}, {3, 0}});
        StdOut.println(goal);
//        for (Board b : goal.neighbors())
//            StdOut.println(b);
//        StdOut.println(goal.hamming());
//        StdOut.println(goal.manhattan());
        StdOut.println(goal.isGoal());
//        StdOut.println( goal.equals(new Board(new int[][]{{2, 1}, {3, 0}})));
        
//        Board goal2 = new Board(new int[][]{{1, 2, 3}, {4, 7, 6}, {0, 5, 8}});
//        StdOut.println(goal2);
//        StdOut.println("hamming: " + goal2.hamming());
//        StdOut.println("manhattan: " + goal2.manhattan());
//        StdOut.println(goal2.isGoal());
    }

}
