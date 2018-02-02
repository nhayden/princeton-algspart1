// Class FastCollinearPoints
// Author: Nathaniel Hayden
// Purpose: Efficient detection of collinear points
// Additional comments:
//   - For simplicity ad hoc unit tests included here
// Based on assignment 3 of Princeton Algorithms I course

import java.util.Arrays;
import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
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
    
    private static Point[] copyfast(Point[] orig) {
        Point[] copy = new Point[orig.length];
        System.arraycopy(orig, 0, copy, 0, orig.length);
        return copy;
    }
    
    private static boolean isMaximal(ArrayList<Point> segment) {
        if(segment.get(0).compareTo(segment.get(1)) < 0)
            return true;
        return false;
    }
    
    private ArrayList<LineSegment> makeSegments(Point[] points) {
//        StdOut.println(Arrays.toString(points));
        ArrayList<LineSegment> segs = new ArrayList<>();
        
        Point[] naturalSorted = points;
        
        for(Point origin : naturalSorted) {
            Point[] slopeSorted = copyfast(naturalSorted);
            Arrays.sort(slopeSorted, origin.slopeOrder());
//            StdOut.println(Arrays.toString(slopeSorted));
            
            ArrayList<Point> potential = new ArrayList<>();
            potential.add(origin);
            potential.add(slopeSorted[1]);
            double prevSlope = origin.slopeTo(slopeSorted[1]);
            
            for(int i = 2; i < slopeSorted.length; i++) {
                double newSlope = origin.slopeTo(slopeSorted[i]);
                if(dEqual(prevSlope, newSlope))
                    potential.add(slopeSorted[i]);
                
                // end of points or new slope run
                if(!dEqual(prevSlope,newSlope) || i == slopeSorted.length - 1) {
                    
                    if(potential.size() > 3 && isMaximal(potential)) {
                        segs.add(new LineSegment(potential.get(0),
                                potential.get(potential.size() - 1)));
                    }
                    potential.clear();
                    potential.add(origin);
                    potential.add(slopeSorted[i]);
                    prevSlope = origin.slopeTo(slopeSorted[i]);
                }
            }
        }

        return segs;
    }
    
    public FastCollinearPoints(Point[] points) {
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
         
         ArrayList<LineSegment> segs = makeSegments(uniq);         
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
        
        FastCollinearPoints fcp = new FastCollinearPoints(points);
//        StdOut.println("Final Segments!");
        for(LineSegment seg : fcp.segments) {
            StdOut.println("  " + seg);
        }
    }
}
