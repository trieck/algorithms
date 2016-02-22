import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

/**
 * Thomas A. Rieck
 * 02/20/2016
 * Purpose: Solver for 8-puzzle problem
 */
public class Solver {

    private final MinPQ<SearchNode> queue;
    private SearchNode goal;

    /**
     * Find a solution to the initial board (using the A* algorithm)
     *
     * @param initial board
     */
    public Solver(Board initial) {
        if (initial == null)
            throw new NullPointerException();

        goal = null;
        queue = new MinPQ<>();
        solve(initial);
    }

    /**
     * Solve a slider puzzle (given  below)
     *
     * @param args the program arguments
     */
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Stopwatch stopwatch = new Stopwatch();
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

        StdOut.printf("elapsed time: %.2f seconds.%n", stopwatch.elapsedTime());
    }

    private void solve(Board initial) {
        // Initialize the priority queue with the initial board, 0 moves
        // and a null previous search node
        SearchNode node = new SearchNode();
        node.board = initial;
        node.moves = 0;
        node.previous = null;
        queue.insert(node);

        // Twin board to check solvability
        Board twin = initial.twin();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();
        SearchNode twinNode = new SearchNode();
        twinNode.board = twin;
        twinNode.moves = 0;
        twinNode.previous = null;
        twinQueue.insert(twinNode);

        // Delete from the priority queue the search node with the
        // minimum priority, and insert onto the priority queue all neighboring
        // search nodes (those that can be reached in one move from the
        // dequeued search node). Repeat this procedure until the search node
        // dequeued corresponds to a goal board.
        while (!queue.isEmpty() && !twinQueue.isEmpty()) {
            twinNode = twinQueue.delMin();
            if (twinNode.isGoal()) {
                goal = null;    // unsolvable
                break;
            }

            node = queue.delMin();
            if (node.isGoal()) {
                goal = node;
                break;
            }

            enqueNeighbors(node, queue);
            enqueNeighbors(twinNode, twinQueue);
        }
    }

    private void enqueNeighbors(SearchNode node, MinPQ<SearchNode> pq) {

        for (Board board : node.neighbors()) {
            // when considering the neighbors of a search node, don't
            // enqueue a neighbor if its board is the same as the board of
            // the previous search node.
            if (node.previous != null && board.equals(node.previous.board))
                continue;

            SearchNode neighbor = new SearchNode();
            neighbor.board = board;
            neighbor.moves = node.moves + 1;
            neighbor.previous = node;
            pq.insert(neighbor);
        }
    }

    /**
     * is the initial board solvable?
     *
     * @return true if solvable, otherwise false
     */
    public boolean isSolvable() {
        return goal != null;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     *
     * @return the number of moves
     */
    public int moves() {
        if (!isSolvable())
            return -1;

        return goal.moves;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable
     *
     * @return sequence of boards
     */
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        Stack<Board> stack = new Stack<>();
        stack.push(goal.board);

        SearchNode node = goal.previous;
        while (node != null) {
            stack.push(node.board);
            node = node.previous;
        }

        return stack;
    }

    /**
     * We define a search node of the game to be a board, the number of moves
     * made to reach the board, and the previous search node.
     */
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves, manhattan = Integer.MAX_VALUE,
                hamming = Integer.MAX_VALUE;
        private SearchNode previous;

        private int getManhattan() {
            if (board != null) {
                if (manhattan == Integer.MAX_VALUE) {
                    manhattan = board.manhattan();
                }
            }

            return manhattan;
        }

        private int getHamming() {
            if (board != null) {
                if (hamming == Integer.MAX_VALUE) {
                    hamming = board.hamming();
                }
            }

            return hamming;
        }

        @Override
        public int compareTo(SearchNode o) {
            int left = this.getManhattan() + moves;
            int right = o.getManhattan() + o.moves;

            if (left < right)
                return -1;

            if (left > right)
                return 1;

            left = this.getManhattan();
            right = o.getManhattan();

            if (left < right)
                return -1;

            if (left > right)
                return 1;

            return 0;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public Iterable<Board> neighbors() {
            return board.neighbors();
        }
    }
}
