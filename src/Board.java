import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Thomas A. Rieck
 * 02/20/2016
 * Purpose: Board class for 8-puzzle problem
 * <p/>
 * Performance requirements.
 * <p/>
 * Implementation should support all Board methods in time proportional to N^2
 * (or better) in the worst case.
 */
public class Board {
    private final int N;
    private final int[][] tiles;

    /**
     * Construct a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     *
     * @param blocks the blocks
     */
    public Board(int[][] blocks) {
        N = blocks.length;
        tiles = new int[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                tiles[i][j] = blocks[i][j];
            }
        }
    }

    public Board(Board other) {
        this(other.tiles);
    }

    /**
     * Unit tests (not graded)
     *
     * @param args the program arguments
     */
    public static void main(String[] args) {
        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int N = in.readInt();
            int[][] tiles = new int[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            Board board = new Board(tiles);
            StdOut.printf("filename: %s: hamming: %d, manhattan: %d%n",
                    filename, board.hamming(), board.manhattan());
        }
    }

    private int goal(int i, int j) {
        int g;
        if (i == j && i == N - 1) {
            g = 0;
        } else {
            g = i * N + (j + 1);
        }

        return g;
    }

    /**
     * Board dimension N
     *
     * @return the dimension
     */
    public int dimension() {
        return N;
    }

    /**
     * Number of blocks out of place
     *
     * @return the hamming
     */
    public int hamming() {
        int h = 0;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] == 0)
                    continue;   // blank

                if (goal(i, j) != tiles[i][j])
                    h++;
            }
        }

        return h;
    }

    /**
     * Sum of Manhattan distances between blocks and goal
     *
     * @return the sum
     */
    public int manhattan() {
        int m = 0;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] == 0)
                    continue;   // blank

                m += manhattanDistance(i, j);
            }
        }

        return m;
    }

    /**
     * Sum of the vertical and horizontal distance
     * from a block to goal position
     *
     * @param i the row
     * @param j the column
     * @return the sum
     */
    private int manhattanDistance(int i, int j) {
        int g = goal(i, j) - 1;
        int gr = g / N;
        int gc = g % N;

        int b = tiles[i][j] - 1;
        int br = b / N;
        int bc = b % N;

        return Math.abs(br - gr) + Math.abs(bc - gc);
    }

    /**
     * Is this board the goal board?
     *
     * @return true if the goal board otherwise false
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * A board that is obtained by exchanging any pair of blocks
     * (the blank square is not a block).
     *
     * @return the twin board
     */
    public Board twin() {
        Board other = new Board(this);

        int i = StdRandom.uniform(N * N);
        while (other.isBlank(i)) {
            i = StdRandom.uniform(N * N);
        }

        int j = StdRandom.uniform(N * N);
        while (other.isBlank(j) || i == j) {
            j = StdRandom.uniform(N * N);
        }

        int ir = i / N;
        int ic = i % N;

        int jr = j / N;
        int jc = j % N;

        other.exchange(ir, ic, jr, jc);

        return other;
    }

    private boolean isBlank(int index) {
        int r = index / N;
        int c = index % N;
        return tiles[r][c] == 0;
    }

    /**
     * Does this board equal y?
     *
     * @param y the other board
     * @return true if equal otherwise false
     */
    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.N != this.N) return false;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        }

        return true;
    }

    /**
     * All neighboring boards
     * those that can be constructed by one move
     *
     * @return neighboring boards
     */
    public Iterable<Board> neighbors() {
        Queue<Board> boards = new Queue<>();
        Board b;

        outer:
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (tiles[i][j] == 0) {
                    if (i > 0) {    // move from above
                        b = new Board(this);
                        b.exchange(i, j, i - 1, j);
                        boards.enqueue(b);
                    }

                    if (i < N - 1) { // move from below
                        b = new Board(this);
                        b.exchange(i, j, i + 1, j);
                        boards.enqueue(b);
                    }

                    if (j > 0) {    // move from left
                        b = new Board(this);
                        b.exchange(i, j, i, j - 1);
                        boards.enqueue(b);
                    }

                    if (j < N - 1) {    // move from right
                        b = new Board(this);
                        b.exchange(i, j, i, j + 1);
                        boards.enqueue(b);
                    }
                    break outer;
                }
            }
        }

        return boards;
    }

    private void exchange(int i1, int j1, int i2, int j2) {
        int t = tiles[i1][j1];
        tiles[i1][j1] = tiles[i2][j2];
        tiles[i2][j2] = t;
    }

    /**
     * String representation of this board
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
