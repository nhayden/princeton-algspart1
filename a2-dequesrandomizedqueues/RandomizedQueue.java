// Class RandomizedQueue
// Author: Nathaniel Hayden
// Purpose: generic randomized queue. Automatically resizes intelligently
// Additional comments:
//   - For simplicity ad hoc unit tests included here
// Based on assignment 2 of Princeton Algorithms I course

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Item[] a;
    
    
    public RandomizedQueue() {
        n = 0;
        a = (Item[]) new Object[2];
    }
    
    public boolean isEmpty() { return n == 0; }
    public int size() { return n; }
    
    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (n == a.length) resize(2*a.length);
        a[n++] = item;
    }
    
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }
    
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int idx = StdRandom.uniform(n);
        // grab last item in array and place in idx about to vacate
        // item of interest (could be last)
        Item tmp = a[idx];
        Item last = a[n-1];
        a[idx] = last;
        a[--n] = null;
        
        if (n > 0 && n == a.length/4) resize(a.length / 2);

        return tmp;
    }
    
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        return a[StdRandom.uniform(n)];
    }    
    
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] acopy = (Item[]) new Object[n];
        private int count = 0;
        
        public RandomizedQueueIterator() {
            for(int i = 0; i < n; i++) {
                acopy[i] = a[i];
            }
            StdRandom.shuffle(acopy);
        }
        public void remove() { throw new UnsupportedOperationException(); }
        public boolean hasNext() { return count < acopy.length; }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item tmp = acopy[count];
            acopy[count++] = null;
            return tmp;
        }
    }
    
    private static boolean testEnqDeq1() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        if(!q.isEmpty() || q.size() != 0) {
            StdOut.println("should be empty with size 0");
            return false;
        }
        int ref = 3;
        q.enqueue(ref);
        if(q.isEmpty() || q.size() != 1) {
            StdOut.println("should be non-empty with size 1");
            return false;
        }
        if(q.dequeue() != ref) {
            StdOut.println("dequeue should return 3");
            return false;
        }
        if(!q.isEmpty() || q.size() != 0) {
            StdOut.println("should be empty again with size 0");
            return false;
        }
        q.enqueue(4);
        if(q.isEmpty() || q.size() != 1) {
            StdOut.println("size should be back to 1 and non-empty");
            return false;
        }
        
        return true;
    }
    
    private static boolean testEnqDeq3() {
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
        
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.dequeue();
        q.dequeue();
        q.dequeue();
//        StdOut.println(q.size());
        
        return false;
    }
    private static void runTests() {
        String pass = "PASSED ---";
        String fail = "FAILED ---";
        String str = "testEnqDeq1";
        StdOut.printf("%s %s\n", testEnqDeq1() ? pass : fail, str);
        str = "testEnqDeq3";
        StdOut.printf("%s %s\n", testEnqDeq3() ? pass : fail, str);
    }
    
    public static void main(String[] args) {
        runTests();
//        StdOut.println("hello");
//        RandomizedQueue<String> q = new RandomizedQueue<>();
////        StdRandom.setSeed(3);
//        String[] s = { "A", "B", "C", "D", "E" };
//        for (String str : s)
//            q.enqueue(str);
//        Iterator<String> it = q.iterator();
//        StdOut.println(it.hasNext());
//        StdOut.println("end");
        
    }

}
