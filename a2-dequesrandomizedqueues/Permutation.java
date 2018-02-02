// Class Permutation
// Author: Nathaniel Hayden
// Purpose: Testing class of RandomizedQueue
// Based on assignment 2 of Princeton Algorithms I course

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {
    
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }
        Iterator<String> it = q.iterator();
        for (int i = 0; i < k && it.hasNext(); i++) {
            StdOut.println(it.next());
        }
    }

}
