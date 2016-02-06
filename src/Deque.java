import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Thomas A. Rieck
 * 02/05/2016
 * Purpose: implement Deque data structure
 */
public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int N;

    /**
     * Construct an empty deque
     */
    public Deque() {
        first = null;
        last = null;
        N = 0;
    }

    /**
     * Unit testing
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();

        deque.addLast("rieck");
        deque.addFirst("andrew");
        deque.addFirst("thomas");

        int sz = deque.size();
        assert (sz == 3);

        for (String s : deque) {
            StdOut.println(s);
            for (String t : deque) {
                StdOut.println(t);
            }
        }

        String s = deque.removeFirst();
        assert ("thomas".equals(s));
        s = deque.removeLast();
        assert ("rieck".equals(s));
        s = deque.removeFirst();
        assert ("andrew".equals(s));

        assert (deque.isEmpty());
    }

    /**
     * Is the deque empty
     *
     * @return true if empty otherwise false
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Return the number of items in the deque
     *
     * @return the number of items in the deque
     */
    public int size() {
        return N;
    }

    /**
     * Add the item to the front
     *
     * @param item the item to add
     */
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();

        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        if (first.next != null)
            first.next.prev = first;
        if (N++ == 0)
            last = first;
    }

    /**
     * Add the item to the end
     *
     * @param item the item to add
     */
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();

        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        if (last.prev != null)
            last.prev.next = last;
        if (N++ == 0)
            first = last;
    }

    /**
     * Remove and return the item from the front
     *
     * @return the item from the front
     */
    public Item removeFirst() {
        if (N == 0) throw new NoSuchElementException();

        Node save = first;
        first = save.next;
        if (last == save)
            last = null;
        else if (last.prev == save)
            last.prev = null;
        else if (last.next == save)
            last.next = null;
        if (first != null)
            first.prev = null;
        N--;
        return save.item;
    }

    /**
     * Remove and return the item from the end
     *
     * @return the item from the end
     */
    public Item removeLast() {
        if (N == 0) throw new NoSuchElementException();

        Node save = last;
        last = save.prev;
        if (first == save)
            first = null;
        else if (first.prev == save)
            first.prev = null;
        else if (first.next == save)
            first.next = null;
        if (last != null)
            last.next = null;
        N--;
        return save.item;
    }

    /**
     * Return an iterator over items in order from the front to the end
     *
     * @return the iterator
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
}
