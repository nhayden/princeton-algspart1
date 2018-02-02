// Class Deque
// Author: Nathaniel Hayden
// Purpose: Generic deque implementation
// Additional comments:
//   - For simplicity ad hoc unit tests included here
// Based on assignment 2 of Princeton Algorithms I course

import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int n;
    // sentinel nodes
    private Node head, tail;
    
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    // construct an empty deque
    public Deque() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        n = 0;
    }                           
    
    // is the deque empty?
    public boolean isEmpty() { return n == 0; }
    
    // return the number of items on the deque    
    public int size() { return n; }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = head.next;
        newNode.next.prev = newNode;
        newNode.prev = head;
        head.next = newNode;
        n++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Node newNode = new Node();
        newNode.item = item;
        newNode.prev = tail.prev;
        tail.prev = newNode;
        newNode.next = tail;
        newNode.prev.next = newNode;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = head.next.item;
        head.next.next.prev = head;
        head.next = head.next.next;
        n--;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = tail.prev.item;
        tail.prev.prev.next = tail;
        tail.prev = tail.prev.prev;
        n--;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = head.next;
        
        public boolean hasNext() { return current != tail; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    // verify exception thrown when adding null item
//    private static boolean testAddNull() {
//        Deque<Integer> d = new Deque<>();
//        boolean pass = false;
//        try {
//            d.addFirst(null);
//        }
//        catch (Exception e) {
//            pass = true;
//        }
//        if (!pass) {
//            StdOut.println("addFirst(null) did not throw exception");
//            return false;
//        }
//        pass = false;
//        try {
//            d.addLast(null);
//        } catch (Exception e) {
//            pass = true;
//        }
//        if (!pass) {
//            StdOut.println("addLast(null) did not throw exception");
//            return false;
//        }
//        
//        return true;
//    }
//    
//    // isEmpty, addFirst, addLast, removeFirst, removeLast
//    private static boolean testAddRemove1() {
//        Deque<Integer> d = new Deque<>();
//        if (!d.isEmpty() || d.size() != 0) {
//            StdOut.println("deque should be empty");
//            return false;
//        }
//        // addFirst, removeFirst, check size / empty
//        d.addFirst(3);
//        if(d.head.next.item != 3) {
//            StdOut.println("head.next.item should be 3");
//            return false;
//        }
//        if(d.isEmpty() || d.size() != 1) {
//            StdOut.println("deque should be non-empty with size 1");
//            return false;
//        }
//        
//        if(d.removeFirst() != 3) {
//            StdOut.println("returned item should be 3");
//            return false;
//        }
//        if(!d.isEmpty() || d.size() != 0) {
//            StdOut.println("deque should be empty with size 0");
//            return false;
//        }
//        
//        
//        // addLast, removeLast, check size / empty
//        d.addLast(4);
//        if(d.tail.prev.item != 4) {
//            StdOut.println("tail.prev.item should be 4");
//            return false;
//        }
//        if(d.isEmpty() || d.size() != 1) {
//            StdOut.println("deque should be non-empty with size 1");
//            return false;
//        }
//        
//        if(d.removeLast() != 4) {
//            StdOut.println("returned item should be 4");
//            return false;
//        }
//        if(!d.isEmpty() || d.size() != 0) {
//            StdOut.println("deque should be empty with size 0");
//            return false;
//        }
//        
//        // addFirst, removeLast, check size / empty
//        d.addFirst(5);
//        if(d.head.next.item != 5 || d.tail.prev.item != 5) {
//            StdOut.println("head.next and tail.prev should be 5");
//            return false;
//        }
//        if(d.isEmpty() || d.size() != 1) {
//            StdOut.println("deque should be non-empty with size 1");
//            return false;
//        }
//        
//        if(d.removeLast() != 5) {
//            StdOut.println("returned item should be 5");
//            return false;
//        }
//        if(!d.isEmpty() || d.size() != 0) {
//            StdOut.println("deque should be empty with size 0");
//            return false;
//        }
//        
//        // addLast, removeFirst, check size / empty
//        d.addLast(6);
//        if(d.head.next.item != 6 || d.tail.prev.item != 6) {
//            StdOut.println("head.next and tail.prev should be 6");
//            return false;
//        }
//        if(d.isEmpty() || d.size() != 1) {
//            StdOut.println("deque should be non-empty with size 1");
//            return false;
//        }
//        
//        if(d.removeFirst() != 6) {
//            StdOut.println("returned item should be 6");
//            return false;
//        }
//        if(!d.isEmpty() || d.size() != 0) {
//            StdOut.println("deque should be empty with size 0");
//            return false;
//        }
//        return true;
//    }
//
//    private static boolean testRemoveFromEmpty() {
//        boolean pass = false;
//        Deque<Integer> d = new Deque<>();
//        try {
//            d.removeLast();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if(!pass)
//            return false;
//        pass = false;
//        d.addFirst(3);
//        d.removeFirst();
//        try{
//            d.removeFirst();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if(!pass)
//            return false;
//                
//        return true;
//    }
//    
//    private static boolean testAddRemoveMulti() {
//        Deque<Integer> d = new Deque<>();
//        // should be 3 4 5
//        d.addFirst(4);
//        d.addLast(5);
//        d.addFirst(3);
//        if (d.head.next.item != 3) {
//            StdOut.println("first item should be 3");
//            return false;
//        }
//        if(d.head.next.next.item != 4) {
//            StdOut.println("second item should be 4");
//            return false;
//        }
//        if(d.head.next.next.next.item != 5) {
//            StdOut.println("third item should be 5");
//            return false;
//        }
//        if (d.isEmpty() || d.size() != 3) {
//            StdOut.println("deque should be non-empty with size 3");
//            return false;
//        }
//        // remove
//        if(d.removeFirst() != 3) {
//            StdOut.println("removed item should be 3");
//            return false;
//        }
//        if(d.removeLast() != 5) {
//            StdOut.println("removed item should be 5");
//            return false;
//        }
//        if(d.removeFirst() != 4) {
//            StdOut.println("removed item should be 4");
//            return false;
//        }
//        if(!d.isEmpty() || d.size() != 0) {
//            StdOut.println("deque should be empty with size 0");
//            return false;
//        }
//        
//        return true;
//    }
//    
//    private static boolean testIterator() {
//        Deque<Integer> d = new Deque<>();
//        Iterator<Integer> di = d.iterator();
//        boolean pass = false;
//        // empty deque
//        if (di.hasNext()) {
//            StdOut.println("hasNext returned true on empty deque");
//            return false;
//        }
//        try {
//            di.next();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if(!pass) {
//            StdOut.println("next did not throw exception on empty deque");
//            return false;
//        }
//        
//        d.addFirst(3);
//        di = d.iterator();
//        if (!di.hasNext()) {
//            StdOut.println("hasNext returned false with 1 item in deque");
//            return false;
//        }
//        if (di.next() != 3) {
//            StdOut.println("next should have returned 3");
//            return false;
//        }
//        if (di.hasNext()) {
//            StdOut.println("hasNext returned true at end of deque");
//            return false;
//        }
//        pass = false;
//        try {
//            di.next();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if(!pass) {
//            StdOut.println("next did not throw exception at end of deque");
//            return false;
//        }
//        d.addLast(4);
//        di = d.iterator();
//        if (di.next() != 3) {
//            StdOut.println("first call to next should return 3");
//            return false;
//        }
//        if (di.next() != 4) {
//            StdOut.println("second call to next should return 4");
//            return false;
//        }
//        if (di.hasNext()) {
//            StdOut.println("hasNext returned true at end of deque");
//            return false;
//        }
//        pass = false;
//        try {
//            di.next();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if (!pass) {
//            StdOut.println("next at end of deque did not throw exception");
//            return false;
//        }
//        d.removeLast();
//        d.removeLast();
//        di = d.iterator();
//        if (di.hasNext()) {
//            StdOut.println("hasNext returned true on an empty deque");
//            return false;
//        }
//        pass = false;
//        try {
//            di.next();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if (!pass) {
//            StdOut.println("next did not throw an exception at end of deque");
//            return false;
//        }
//        
//        // test remove throws exception try with size 0 and size 1
//        Deque<Integer> d2 = new Deque<>();
//        Iterator<Integer> di2 = d2.iterator();
//        pass = false;
//        try {
//            di2.remove();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if (!pass) {
//            StdOut.println("remove on empty deque did not throw exception");
//            return false;
//        }
//        d2.addFirst(3);
//        pass = false;
//        try {
//            di2.remove();
//        } catch (Exception e) {
//            pass = true;
//        }
//        if (!pass) {
//            StdOut.println("remove on a dque with 1 element did not throw exception");
//            return false;
//        }
//        
//        return true;
//    }
    
//    private static boolean testStrings() {
//        Deque<String> d = new Deque<>();
//        d.addFirst("Merry");
//        d.addLast("Christmas");
//        d.addLast("happy new year");
//        Iterator<String> di = d.iterator();
//        if (!di.next().equals("Merry")) {
//            StdOut.println("first string should be \"Merry\"");
//            return false;
//        }
//        if (!di.next().equals("Christmas")) {
//            StdOut.println("first string should be \"Christmas\"");
//            return false;
//        }
//        if (!di.next().equals("happy new year")) {
//            StdOut.println("first string should be \"happy new year\"");
//            return false;
//        }
//        
//        return true;
//    }
//    
//    private static void runTests() {
//        String pass = "PASSED ---";
//        String fail = "FAILED ---";
//        String teststr = "testAddRemove1";
//        StdOut.printf("%s %s\n", testAddRemove1() ? pass : fail, teststr);
//        teststr = "testRemoveFromEmpty";
//        StdOut.printf("%s %s\n", testRemoveFromEmpty() ? pass : fail, teststr);
//        teststr = "testAddRemoveMulti";
//        StdOut.printf("%s %s\n", testAddRemoveMulti() ? pass : fail, teststr);
//        teststr = "testIterator";
//        StdOut.printf("%s %s\n", testIterator() ? pass : fail, teststr);
//        teststr = "testAddNull";
//        StdOut.printf("%s %s\n", testAddNull() ? pass : fail, teststr);
//        teststr = "testStrings";
//        StdOut.printf("%s %s\n", testStrings() ? pass : fail, teststr);
//    }
    public static void main(String[] args) {
//        runTests();
    }

}
