import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Thomas A. Rieck
 * 02/06/2016
 * Purpose: A client program that takes a command-line integer k;
 * reads in a sequence of N strings from standard input and prints
 * exactly k of them uniformly at random.
 * Each item from the sequence can be printed out at most once.
 * 0<= k <= N is assumed, where N is the number of strings on standard input.
 */

public class Subset {

    /**
     * Entry point of the program
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            queue.enqueue(s);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
