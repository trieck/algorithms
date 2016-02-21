import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

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
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private void solve(Board initial) {
        // Initialize the priority queue with the initial board, 0 moves
        // and a null previous search node
        SearchNode node = new SearchNode();
        node.board = initial;
        node.moves = 0;
        node.previous = null;
        queue.insert(node);

        // Delete from the priority queue the search node with the
        // minimum priority, and insert onto the priority queue all neighboring
        // search nodes (those that can be reached in one move from the
        // dequeued search node). Repeat this procedure until the search node
        // dequeued corresponds to a goal board.
        for (int moves = 0; !queue.isEmpty(); ++moves) {
            node = queue.delMin();
            if (node.isGoal()) {
                goal = node;
                break;
            }

            for (Board board : node.neighbors()) {
                // when considering the neighbors of a search node, don't
                // enqueue a neighbor if its board is the same as the board of
                // the previous search node.
                if (node.previous != null && board.equals(node.previous.board))
                    continue;

                SearchNode neighbor = new SearchNode();
                neighbor.board = board;
                neighbor.moves = moves + 1;
                neighbor.previous = node;
                queue.insert(neighbor);
            }
        }
    }

    /**
     * is the initial board solvable?
     *
     * @return true if solvable, otherwise false
     */
    public boolean isSolvable() {
        return true;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     *
     * @return the number of moves
     */
    public int moves() {
        if (!isSolvable())
            return -1;

        int moves = 0;
        SearchNode node = goal;
        while (node != null) {
            moves++;
            node = node.previous;
        }

        return Math.max(0, moves - 1);
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
        private int moves;
        private SearchNode previous;

        private int priority() {
            int p = moves;
            if (board != null)
                p += board.manhattan();

            return p;
        }

        @Override
        public int compareTo(SearchNode o) {
            int left = this.priority();
            int right = o.priority();

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
