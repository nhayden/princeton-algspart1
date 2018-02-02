import java.util.Arrays;
import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
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
        
        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();
//        for(LineSegment seg : fcp.segments)
//            seg.draw();
//        StdDraw.show();  
        

        
        
        
//      int[][] pairs = { { 1, 1 }, { -1, -1 }, // slope 1
//      { 2, 2 }, { 3, 3 },
//      { 1, 2 }, { 2, 4 }, { 3, 6 }, // slope 2
//      { 5, 8 }, // no collinears
//      { 0, 0 }}; // degenerate
//ArrayList<Point> pointsList = new ArrayList<Point>();
//for(int[] pair : pairs) pointsList.add(new Point(pair[0], pair[1]));
//Point[] points = pointsList.toArray(new Point[0]);
//FastCollinearPoints fcp = new FastCollinearPoints(points);
//for(LineSegment seg : fcp.segments) {
//StdOut.println(seg);
//}

//StdRandom.shuffle(points);
//for(Point p : points) StdOut.println(p);
//StdOut.println();
//Point cur = new Point(0, 0);
//StdOut.println("cur: " + cur);
//Arrays.sort(points, 0, points.length, cur.slopeOrder());
//for(int i = 0; i < points.length; i++)
//StdOut.println(points[i] + " : " + cur.slopeTo(points[i]));

        
    }

}


//private ArrayList<LineSegment> makeSegments(ArrayList<PseudoSeg> pseudos) {
//    ArrayList<LineSegment> segs = new ArrayList<>();
//    if(pseudos.size() == 1) {
//        LineSegment seg =
//                new LineSegment(pseudos.get(0).p1, pseudos.get(0).p2);
//        segs.add(seg);
//        return segs;
//    }
//        
//    
////    StdOut.println(Arrays.toString(pseudos.toArray(new PseudoSeg[0])));
//    Collections.sort(pseudos);
////    StdOut.println(Arrays.toString(pseudos.toArray(new PseudoSeg[0])));
//    
//    List<PseudoSeg> segsList = new LinkedList<>(pseudos);
//    while(!segsList.isEmpty()) {
//        Iterator<PseudoSeg> li = segsList.iterator();
//        PseudoSeg ref = li.next();
//        li.remove();
//        Point min = min(ref.p1, ref.p2);
//        Point max = max(ref.p1, ref.p2);
//        while(li.hasNext()) {
//            PseudoSeg cur = li.next();
//            if(isCollinearPseudo(ref, cur)) {
//                min = min(min(min, cur.p1), cur.p2);
//                max = max(max(max, cur.p1), cur.p2);
//                li.remove();
//            }
//        }
//        segs.add(new LineSegment(min, max));
//    }
//    
//    
//    
////    PseudoSeg ref = pseudos.get(0);
////    Point min = min(ref.p1, ref.p2);
////    Point max = max(ref.p1, ref.p2);
////    for(int i = 1; i < pseudos.size(); i++) {
////        PseudoSeg cur = pseudos.get(i);
////        // same slope
////        if(ref.compareTo(cur) == 0) {
////            min = min(min(min, cur.p1), cur.p2);
////            max = max(max(max, cur.p1), cur.p2);
////        } else {
////            segs.add(new LineSegment(min, max));
////            ref = cur;
////            min = min(ref.p1, ref.p2);
////            max = max(ref.p1, ref.p2);
////        }
////    }
////    segs.add(new LineSegment(min, max));
//    
//    return segs;
//}


//private class PseudoSegment implements Comparable<PseudoSegment> {
//private Point p1, p2;
//private final double slope;
//public PseudoSegment(double slope, Point p1, Point p2) {
//  this.slope = slope;
//  this.p1 = p1;
//  this.p2 = p2;
//}
//
//public int compareTo(PseudoSegment that) {
//  if(isEqual(this.slope, that.slope)) return 0;
//  if(this.slope > that.slope) return +1;
//  // if(this.slope < that.slope) 
//  return -1;
//}
//public boolean isCollinear(PseudoSegment that) {
//  boolean slopesMatch = isEqual(this.slope, that.slope);
//  if(!slopesMatch) return false;
//  boolean sharedPoints = this.p1.compareTo(that.p1) == 0 ||
//          this.p1.compareTo(that.p2) == 0 ||
//          this.p2.compareTo(that.p1) == 0 ||
//          this.p2.compareTo(that.p2) == 0;
//  if(sharedPoints) return true;
//  return isEqual(this.p1.slopeTo(that.p1), this.slope);
//  
//}
//public String toString() {
//  return this.p1 + " -> " + this.p2 + " : " + this.slope;
//}
//}

//private ArrayList<PseudoSegment> makePseudoSegments(Point[] points) {
//ArrayList<PseudoSegment> pseudos = new ArrayList<PseudoSegment>();
//for(int curIdx = 0; points.length - curIdx > 3; curIdx++) {
//  Point cur = points[curIdx];
//  Arrays.sort(points, curIdx+1, points.length, cur.slopeOrder());
//  int beg = curIdx+1;
//  int end = curIdx+3;
//  
//  while(end < points.length) {
//      double curSlope = cur.slopeTo(points[end]);
//      
//      if(curSlope != Double.NEGATIVE_INFINITY &&
//              isEqual(cur.slopeTo(points[beg]), curSlope) &&
//              isEqual(cur.slopeTo(points[beg+1]), curSlope)) {
//          while(end < points.length &&
//                  isEqual(cur.slopeTo(points[end]), curSlope)) {
//              end++;
//          }
//          Point minPoint = cur;
//          Point maxPoint = cur;
//          while(beg != end) {
//              minPoint = min(minPoint, points[beg]);
//              maxPoint = max(maxPoint, points[beg]);
//              beg++;
//          }
//          pseudos.add(new PseudoSegment(curSlope, minPoint, maxPoint));
//          beg = end;
//          end += 2;
//      } else {
//          beg++;
//          end++;
//      }
//  }
//}
//
//return pseudos;
//}

//private static ArrayList<Integer> findRunsMinX(double[] slopes,
//int startIdx, int min) {
//ArrayList<Integer> runs = new ArrayList<>();
//if(slopes.length - startIdx < min)
//return runs;
//
//int beg = startIdx, end = startIdx+1;
//for(int i = startIdx+1; i < slopes.length; i++) {
//if(dEqual(slopes[i], slopes[i-1])) {
//  end++;
//} else {
//  if(end - beg > min-1) {
//      runs.add(beg);
//      runs.add(end-1);
//  }
//  beg = i;
//  end = i+1;
//}
//}
//if(end - beg > min-1) {
//runs.add(beg);
//runs.add(end-1);
//}
//return runs;
//}
