import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayDeque;

public class Solver {
    private final Board initial;
    private final int moves;
    private final ArrayDeque<Board> solution;
    private final boolean isSolvable;
    
    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.IllegalArgumentException();
        this.initial = initial;
        if (initial.isGoal()) {
            solution = new ArrayDeque<Board>();
            solution.add(initial);
            moves = 0;
            isSolvable = true;
        } else {
            solution = runPuzzle();
            if (solution == null) {
                moves = -1;
                isSolvable = false;
            } else {
                moves = solution.size() - 1;
                isSolvable = true;
            }
        }
    }
    
    public int moves() { return moves; }
    
    public boolean isSolvable() { return isSolvable; }
    
    public Iterable<Board> solution() {
        return solution == null ? null : new ArrayDeque<Board>(solution);
    }
    
    private ArrayDeque<Board> runPuzzle() {
        ArrayDeque<Board> res = new ArrayDeque<>();
        MinPQ<Node> orig = new MinPQ<>();
        MinPQ<Node> twin = new MinPQ<>();
        orig.insert(new Node(initial));
        twin.insert(new Node(initial.twin()));
        
        while (!orig.isEmpty() && !twin.isEmpty()) {
            Node curOrig = orig.delMin();
            Node curTwin = twin.delMin();
            
            if (curOrig.board.isGoal()) {
                while (curOrig != null) {
                    res.addFirst(curOrig.board);
                    curOrig = curOrig.prev;
                }
                return res;
            } else if (curTwin.board.isGoal()) {
                return null;
            } else {
                Iterable<Board> neighborsOrig = curOrig.board.neighbors();
                for (Board neighbor : neighborsOrig) {
                    if (curOrig.prev == null || !neighbor.equals(curOrig.prev.board))
                        orig.insert(new Node(neighbor, curOrig, curOrig.moves+1));
                }
                Iterable<Board> neighborsTwin = curTwin.board.neighbors();
                for (Board neighbor : neighborsTwin) {
                    if (curTwin.prev == null || !neighbor.equals(curTwin.prev.board))
                        twin.insert(new Node(neighbor, curTwin, curTwin.moves+1));
                }
            }
        }
        
        return res;
    }

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int priority;
        private final int moves;
        private final Node prev;
        
        public Node(Board board) {
            this(board, null, 0);
        }
        private Node(Board board, Node prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            this.priority = moves + board.manhattan();
        }
        public int compareTo(Node that) {
            if (this.priority > that.priority) return +1;
            if (this.priority < that.priority) return -1;
            return 0;
        }
    }
    
    public static void main(String[] args) {
        String file = "./8puzzle/puzzle00.txt";
//        String file = "./mypuzzle0.txt";
        // create initial board from file
        In in = new In(file);
//        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            StdOut.println();
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }

}
