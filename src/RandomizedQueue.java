import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Thomas A. Rieck
 * 02/05/2016
 * Purpose: implement Randomized queue data structure
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;   // array of items
    private int N;      // number of elements on queue

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        N = 0;
        resize(2);
    }

    /**
     * Unit testing
     *
     * @param args the program arguments
     */
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue();

        queue.enqueue("rieck");
        queue.enqueue("andrew");
        queue.enqueue("thomas");

        String s = queue.dequeue();
        s = queue.dequeue();
        s = queue.dequeue();

        for (String t : queue) {
            StdOut.println(t);
        }

        queue.enqueue("lily");
        queue.enqueue("caleb");
        queue.enqueue("aaron");

        for (String t : queue) {
            StdOut.println(t);
            for (String u : queue) {
                StdOut.println("   " + u);
            }
        }
    }

    /**
     * Resize the underlying array holding the elements
     *
     * @param capacity the capacity
     */
    @SuppressWarnings({"unchecked", "ManualArrayCopy"})
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    /**
     * Is the queue empty?
     *
     * @return true if queue is empty otherwise false
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Return the number of items on the queue
     *
     * @return the number of items on the queue
     */
    public int size() {
        return N;
    }

    /**
     * Add the item
     *
     * @param item the item
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();

        // double the size of the array if necessary
        if (N == a.length) resize(2 * a.length);

        a[N++] = item;
    }

    /**
     * Remove and return a random item
     *
     * @return the random item
     */
    public Item dequeue() {
        if (N == 0) throw new java.util.NoSuchElementException();

        int i = StdRandom.uniform(N);

        exch(i, N - 1);

        Item item = a[N - 1];
        a[N - 1] = null;  // don't loiter
        N--;

        if (N > 0 && N == a.length / 4) resize(a.length / 2);

        return item;
    }

    private void exch(int i, int j) {
        validate(i);
        validate(j);

        if (i != j) {
            Item t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }

    private void validate(int i) {
        if (0 > i || N <= i)
            throw new java.util.NoSuchElementException();
    }

    /**
     * Return (but do not remove) a random item
     *
     * @return the item
     */
    public Item sample() {
        if (N == 0) throw new java.util.NoSuchElementException();
        int i = StdRandom.uniform(N);
        return a[i];
    }

    /**
     * Return an independent iterator over items in random order
     *
     * @return the iterator
     */
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {

        private final int[] perms;
        private int i;

        QueueIterator() {
            i = 0;
            perms = new int[N];
            for (int j = 0; j < N; j++) {
                perms[j] = j;
            }
            StdRandom.shuffle(perms);
        }

        @Override
        public boolean hasNext() {
            return i < N;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[perms[i++]];
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }
}