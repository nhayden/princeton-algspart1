import java.util.Arrays;
import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private final LineSegment[] segments;
    
    public int numberOfSegments() { return segments.length; }
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }
    private static boolean dEqual(double a, double b) {
        return Double.compare(a, b) == 0;
    }
    private Point[] cleanInput(Point[] points) {
        Arrays.sort(points);
        ArrayList<Point> unique = new ArrayList<Point>();
        unique.add(points[0]);
        for(int i = 1; i < points.length; i++) {
            if(points[i].compareTo(points[i-1]) != 0)
                unique.add(points[i]);
        }
        return unique.toArray(new Point[unique.size()]);
    }
    
//    private static int binomial(int n, int k) {
//        if (k>n-k)
//            k=n-k;
//
//        int b=1;
//        for (int i=1, m=n; i<=k; i++, m--)
//            b=b*m/i;
//        return b;
//    }
    
    private static boolean isCollinear(Point[] points, int[] idx) {
//        assert(idx.length == 4);
        
        Point ref = points[idx[0]];
        double slope = ref.slopeTo(points[idx[1]]);
        for(int i = 2; i < idx.length; i++) {
            if(!dEqual(ref.slopeTo(points[idx[i]]), slope))
                return false;
        }
        return true;
    }
    
    private static void comboDoUtil(Point[] points, int[] in, int start,
            int choose, ArrayList<LineSegment> out, int[] temp, int tempIdx) {
        if(tempIdx == choose) {
            if(isCollinear(points, temp)) {
                out.add(new LineSegment(points[temp[0]], points[temp[3]]));
            }
            return;
        }
        for(int i = start; i < in.length; i++) {
            temp[tempIdx] = in[i];
            comboDoUtil(points, in, i+1, choose, out, temp, tempIdx+1);
        }
    }
    
    private static ArrayList<LineSegment> comboDo(Point[] points, int choose) {
        int[] idx = new int[points.length];
        for(int i = 0; i < points.length; i++) idx[i] = i;
        int[] temp = new int[choose];
        ArrayList<LineSegment> out = new ArrayList<>();
        comboDoUtil(points, idx, 0, choose, out, temp, 0);
        return out;
    }
    
    public BruteCollinearPoints(Point[] points) {
        // -- begin error checking
        if(points == null)
            throw new java.lang.IllegalArgumentException();
        for(Point p : points) if(p == null)
            throw new java.lang.IllegalArgumentException();
        Point[] uniq = cleanInput(Arrays.copyOf(points, points.length));
        if(points.length != uniq.length)
            throw new java.lang.IllegalArgumentException();
        // -- end error checking
        
        if(uniq.length < 4) {
            segments = new LineSegment[0];
            return;
        }
         
//        int[][] pointCombos = getCombos(uniq, 4);
//        ArrayList<LineSegment> lineSegments = new ArrayList<>();
//        for(int[] combo : pointCombos) {
//            if(isCollinear(uniq, combo)) {
//                lineSegments.add(new LineSegment(uniq[combo[0]],
//                        uniq[combo[3]]));
//            }
//        }
        
        ArrayList<LineSegment> segs = comboDo(uniq, 4);
        
        segments = segs.toArray(new LineSegment[segs.size()]);
         
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for(int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        for(LineSegment seg : bcp.segments) {
            StdOut.println(seg);
            seg.draw();
        }
        StdDraw.show();
        
//        int[][] pairs = { { 1, 1 }, { -1, -1 }, // slope 1
//                { 2, 2 }, { 3, 3 },
//                { 1, 2 }, { 2, 4 }, { 3, 6 }, // slope 2
//                { 5, 8 }, // no collinears
//                { 0, 0 }}; // degenerate
//        ArrayList<Point> pointsList = new ArrayList<Point>();
//        for(int[] pair : pairs) pointsList.add(new Point(pair[0], pair[1]));
//        Point[] points = pointsList.toArray(new Point[0]);
//        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
//        for(LineSegment seg : bcp.segments) {
//            StdOut.println(seg);
//        }
        
//        Point ap1 = new Point(30,21);
//        Point ap2 = new Point(50,35);
//        int[][] slope1 = { { 30, 21, 50, 35 },
//                { 22, 16, 42, 30 },
//                { 20, 14, 40, 28 }, { 30, 21, 60, 42 },
//                { 22, 16, 62, 44 },
//                { 30, 21, 40, 28 }, { 10, 7, 70, 49 } };
//        ArrayList<PseudoSegment> slope1Seg = new ArrayList<>();
//        for(int[] pair : slope1) {
//            Point p1 = new Point(pair[0], pair[1]);
//            Point p2 = new Point(pair[2], pair[3]);
//            slope1Seg.add(new PseudoSegment(p1, p2));
//        }
//        for(PseudoSegment seg : slope1Seg) StdOut.println(seg.toString());
//        StdOut.println("\n--calling makeLineSegments--\n");
//        LineSegment[] segs = makeLineSegments(slope1Seg);
//        StdOut.println("\n--printing output--");
//        for(LineSegment seg : segs) StdOut.println(seg);

        
//        int[][] horiz = { { 4, 10, 6, 10 }, {4, 10, 8, 10 } };
//        for(int[] pair : horiz) {
        
        
    }

}

