// Class PointSET
// Author: Nathaniel Hayden
// Purpose: Maintain ordered set of 2D points for us in KD tree
// Based on assignment 5 of Princeton Algorithms I course

import java.util.TreeSet;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private final TreeSet<Point2D> tree;
    
    public PointSET() {
        tree = new TreeSet<>();
    }
    
    public boolean isEmpty() { return tree.isEmpty(); }
    public int size() { return tree.size(); }
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        tree.add(p);
    }
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return tree.contains(p);
    }
    
    public void draw() { for (Point2D p : tree) p.draw(); }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> res = new ArrayList<>();
        for (Point2D p : tree) { if (rect.contains(p)) res.add(p); }
        return res;
    }
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (tree.isEmpty()) return null;
        
        Point2D near = tree.first();
        double dist = p.distanceSquaredTo(near);
        for (Point2D that : tree) {
            double thatdist = p.distanceSquaredTo(that);
            if (thatdist < dist) {
                near = that;
                dist = thatdist;
            }
        }
        return near;
    }

    public static void main(String[] args) {

    }

}
