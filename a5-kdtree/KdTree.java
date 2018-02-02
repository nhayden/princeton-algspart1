import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;


public class KdTree {
    private Node root;
    private int size;
    private int nodeVisits;
    
    public KdTree() {
        root = null;
        size = 0;
        nodeVisits = 1;
    }
    
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    private RectHV makeRect(Corners corners) {
        return new RectHV(corners.xmin, corners.ymin, corners.xmax, corners.ymax);
    }
    private static class Corners {
        public double xmin;
        public double ymin;
        public double xmax;
        public double ymax;
        public Corners(double xmin, double ymin, double xmax, double ymax) {
            this.xmin = xmin;
            this.ymin = ymin;
            this.xmax = xmax;
            this.ymax = ymax;
        }
        public double xmin(double xmin) {
            double old = this.xmin;
            this.xmin = xmin;
            return old;
        }
        public double ymin(double ymin) {
            double old = this.ymin;
            this.ymin = ymin;
            return old;
        }
        public double xmax(double xmax) {
            double old = this.xmax;
            this.xmax = xmax;
            return old;
        }
        public double ymax(double ymax) {
            double old = this.ymax;
            this.ymax = ymax;
            return old;
        }
    }
    
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        int[] sizea = {size};
        Corners corners = new Corners(0, 0, 1, 1); // xmin, ymin, xmax, ymax
        root = insert(root, p, sizea, true, corners);
        size = sizea[0];
    }
    private Node insert(Node x, Point2D p, int[] sizea, boolean vertical,
            Corners corners) {
        if (x == null) {
            sizea[0]++;
//            StdOut.println(p + ": " + makeRect(corners));
            return new Node(p, makeRect(corners));
        }
        int cmp = compareKdPoint(p, x.p, vertical);
//        int cmp = vertical ?
//                Double.compare(p.x(), x.p.x()) : Double.compare(p.y(), x.p.y());
        if (cmp < 0) {
            if (vertical) { // next is horizontal left
                double oldval = corners.xmax(x.p.x());
                x.lb = insert(x.lb, p, sizea, !vertical, corners);
                corners.xmax(oldval);
            } else { // next is vertical below
                double oldval = corners.ymax(x.p.y());
                x.lb = insert(x.lb, p, sizea, !vertical, corners);
                corners.ymax(oldval);
            }
        }
        else if (cmp > 0 || p.compareTo(x.p) != 0) {
            if (vertical) { // next is horizontal right
                double oldval = corners.xmin(x.p.x());
                x.rt = insert(x.rt, p, sizea, !vertical, corners);
                corners.xmin(oldval);
            } else { // next is vertical top
                double oldval = corners.ymin(x.p.y());
                x.rt = insert(x.rt, p, sizea, !vertical, corners);
                corners.ymin(oldval);
            }
        }
        return x;
    }
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p, true);
    }
    private boolean contains(Node x, Point2D p, boolean xnext) {
        if (x == null) return false;
        int cmp = xnext ?
                Double.compare(p.x(), x.p.x()) : Double.compare(p.y(), x.p.y());
        if (cmp < 0) return contains(x.lb, p, !xnext);
        if (cmp > 0 || p.compareTo(x.p) != 0) return contains(x.rt, p, !xnext);
        else return true;
    }
    
    public void draw() { return; }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> res = new ArrayList<>();
        range(root, rect, res);
        return res;
    }
    private void range(Node x, RectHV rect, ArrayList<Point2D> res) {
        if (x == null) return;
        if (rect.contains(x.p)) res.add(x.p);
        if (rect.intersects(x.rect)) {
            range(x.lb, rect, res);
            range(x.rt, rect, res);
        }
    }
    
    private static int compareKdPoint(Point2D p, Point2D xp, boolean vertical) {
        return vertical ?
                Double.compare(p.x(), xp.x()) : Double.compare(p.y(), xp.y());
    }
    
    
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        
        Point2D[] best = new Point2D[]{root.p};
        nearest(root, p, best, new double[]{p.distanceSquaredTo(best[0])}, true);
        return best[0];
    }
    private void nearest(Node x, Point2D p, Point2D[] best, double[] bestDistSq,
            boolean vertical) {
        if (x == null) return;
        
//        StdOut.println(nodeVisits++);
        
        if (p.compareTo(x.p) == 0) {
            best[0] = x.p;
            bestDistSq[0] = 0.0;
            return;
        }
        double xDistSq = p.distanceSquaredTo(x.p);
        if (xDistSq < bestDistSq[0]) {
            bestDistSq[0] = xDistSq;
            best[0] = x.p;
        }
        
        double xRectDistSq = x.rect.distanceSquaredTo(p);
        if (bestDistSq[0] < xRectDistSq) return;
        
        if ((vertical && p.x() < x.p.x()) || (!vertical && p.y() < x.p.y())) {
            nearest(x.lb, p, best, bestDistSq, !vertical);
            nearest(x.rt, p, best, bestDistSq, !vertical);
        } else { // n.b. different from insert methodology
            nearest(x.rt, p, best, bestDistSq, !vertical);
            nearest(x.lb, p, best, bestDistSq, !vertical);
        }
    }
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        public Node(Point2D p, RectHV rect) {
            this(p, rect, null, null);
        }
        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            if (p == null) throw new IllegalArgumentException();
            this.p = p; this.rect = rect; this.lb = lb; this.rt = rt;
        }
    }
    
    // before implementing recthv stuff
    private static void firstTest() {
        KdTree tree = new KdTree();
        
        Point2D p1 = new Point2D(0.5, 0.3);
        tree.insert(p1);
        System.out.printf("1 %s %d\n", (tree.root.p == p1), tree.size());
        Point2D p2 = new Point2D(0.6, 0.7);
        tree.insert(p2);
        System.out.printf("2 %s %d\n", (tree.root.rt.p == p2), tree.size());
        Point2D p3 = new Point2D(0.3, 0.7);
        tree.insert(p3);
        System.out.printf("3 %s %d\n", (tree.root.lb.p == p3), tree.size());
        Point2D dup = new Point2D(0.3, 0.7);
        tree.insert(dup);
        System.out.println("tree size == 3 " + (tree.size() == 3));
        Point2D p4 = new Point2D(0.5, 0.2);
        tree.insert(p4);
        System.out.printf("4 %s %d\n", (tree.root.rt.lb.p == p4), tree.size());
        Point2D p5 = new Point2D(0.5, 0.8);
        tree.insert(p5);
        System.out.printf("5 %s %d\n", (tree.root.rt.rt.p == p5), tree.size());

        
        System.out.println();
        System.out.println(tree.contains(new Point2D(0.3, 0.9)));
    }

    private static boolean testInsertMyPoints() {
        
        KdTree tree = new KdTree();
        ArrayList<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.1, 0.9));
        points.add(new Point2D(0.8, 0.2));
        points.add(new Point2D(0.7, 0.6));
        points.add(new Point2D(0.3, 0.5));
        points.add(new Point2D(0.4, 0.35));
        points.add(new Point2D(0.15, 0.44));
        points.add(new Point2D(0.45, 0.25));
        points.add(new Point2D(0.65, 0.37));
        
        // 0.1, 0.9
        tree.insert(points.get(0));
        if (tree.root.p != points.get(0)) {
            StdOut.println("1st node wrong"); return false;
        }
        if (!tree.root.rect.equals(new RectHV(0,0,1,1))) {
            StdOut.println("1st rect wrong"); return false;
        }
        
        // 0.8, 0.2
        tree.insert(points.get(1));
        if (tree.root.rt.p != points.get(1)) {
            StdOut.println("2nd node wrong"); return false;
        }
        if (!tree.root.rt.rect.equals(new RectHV(0.1, 0 , 1, 1))) {
            StdOut.println("2nd rect wrong"); return false;
        }
        
        // 0.7, 0.6
        tree.insert(points.get(2));
        if (tree.root.rt.rt.p != points.get(2)) {
            StdOut.println("3rd node wrong"); return false;
        }
        if (!tree.root.rt.rt.rect.equals(new RectHV(0.1,0.2,1,1))) {
            StdOut.println("3rd rect wrong"); return false;
        }
        
        // 0.3, 0.5
        Point2D p = points.get(3);
        tree.insert(p);
        if (tree.root.rt.rt.lb.p != points.get(3)) {
            StdOut.println("4th node wrong"); return false;
        }
        if (!tree.root.rt.rt.lb.rect.equals(new RectHV(0.1,0.2,0.7,1))) {
            StdOut.println("4th rect wrong"); return false;
        }
        
        // 0.4, 0.35
        tree.insert(points.get(4));
        if (tree.root.rt.rt.lb.lb.p != points.get(4)) {
            StdOut.println("5th node wrong"); return false;
        }
        if (!tree.root.rt.rt.lb.lb.rect.equals(new RectHV(0.1,0.2,0.7,0.5))) {
            StdOut.println("5th rect wrong"); return false;
        }
        
        // 0.15, 0.44
        tree.insert(points.get(5));
        if (tree.root.rt.rt.lb.lb.lb.p != points.get(5)) {
            StdOut.println("6th node wrong"); return false;
        }
        if (!tree.root.rt.rt.lb.lb.lb.rect.equals(new RectHV(0.1,0.2,0.4,0.5))) {
            StdOut.println("6th rect wrong"); return false;
        }
        
        // 0.45, 0.25
        tree.insert(points.get(6));
        if (tree.root.rt.rt.lb.lb.rt.p != points.get(6)) {
            StdOut.println("7th node wrong"); return false;
        }
        if (!tree.root.rt.rt.lb.lb.rt.rect.equals(new RectHV(0.4,0.2,0.7,0.5))) {
            StdOut.println("7th rect wrong"); return false;
        }
        
        // 0.65, 0.37
        tree.insert(points.get(7));
        if (tree.root.rt.rt.lb.lb.rt.rt.p != points.get(7)) {
            StdOut.println("8th node wrong"); return false;
        }
        if (!tree.root.rt.rt.lb.lb.rt.rt.rect.equals(new RectHV(0.4,0.25,0.7,0.5))) {
            StdOut.println("8th rect wrong"); return false;
        }
        
        return true;
    }
    
    private static boolean testNearest10() {
        PointSET pset = new PointSET();
        KdTree tree = new KdTree();
        ArrayList<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.206107, 0.095492));
        points.add(new Point2D(0.975528, 0.654508));
        points.add(new Point2D(0.024472, 0.345492));
        points.add(new Point2D(0.793893, 0.095492));
        points.add(new Point2D(0.793893, 0.904508));
        points.add(new Point2D(0.975528, 0.345492));
        points.add(new Point2D(0.206107, 0.904508));
        points.add(new Point2D(0.500000, 0.000000));
        points.add(new Point2D(0.024472, 0.654508));
        points.add(new Point2D(0.500000, 1.000000));
        
        for (Point2D p : points) tree.insert(p);
        for (Point2D p : points) pset.insert(p);
        
        Point2D testp = new Point2D(0.81, 0.3);
        Point2D treenear = tree.nearest(testp);
        Point2D setnear = pset.nearest(testp);
        StdOut.println(setnear);
        StdOut.println(treenear);
        if (!treenear.equals(setnear)) return false;
        
        
        return true;
    }
    
    public static void main(String[] args) {
        if (testInsertMyPoints()) StdOut.println("testInsertMyPoints passed");
        else StdOut.println("testInsertMyPoints FAILED");
        StdOut.println();
        
        if (testNearest10()) StdOut.println("testNearest10 passed");
        else StdOut.println("testNearest10 FAILED");
        
    }

}
