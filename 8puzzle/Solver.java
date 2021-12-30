import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // declare instance variable
    private boolean solvable;
    private SearchNode solutionNode;

    /*
    searchNode is a comparable object which is capable of comparing itself with another object
    the class of searchNode is implement the Comparable interface to compare its instances
     */
    // define search node of the game
    private class SearchNode implements Comparable<SearchNode> {
        // declare instance variables of searchNode
        private final Board board;
        private int moves;
        private final int priority;
        private final SearchNode prevNode;

        // constructor to create searchNode
        public SearchNode(Board board, SearchNode prevNode) {
            this.board = board;
            this.prevNode = prevNode;

            // update moves if the current searchNode has previous searchNode
            if (prevNode != null) {
                moves = prevNode.moves + 1;
            }
            // set moves to zero if it is initial search node
            else {
                moves = 0;
            }

            // cache optimization to avoid recomputing priority of search node in pq operation
            // compute priority of search node in constructor
            priority = moves + this.board.manhattan();
        }

        // default procedure of comparable interface to implement CompareTo method to define natural ordering of the search node objects using comparable interface
        @Override
        public int compareTo(SearchNode that) {
            // ordering is sorted by priority of search node
            return Integer.compare(this.priority, that.priority);
        }

        public SearchNode getPrevNode() {
            return prevNode;
        }

        public int getMoves() {
            return moves;
        }

        public Board getBoard() {
            return board;
        }


    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // corner cases
        if (initial == null) {
            throw new IllegalArgumentException("The input board is null.");
        }
        /*
        Twin minimum priority queues are created to reduce the number of .equals() called to optimize memory allocation
         */
        // create the minimum priority queue
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        // create the minimum priority queue for twin board
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();

        // create instance of initial search node
        SearchNode initSearchNode = new SearchNode(initial, null);

        // create instance of search node that initial board modified by swapping a pair of tiles
        SearchNode twinSearchNode = new SearchNode(initial.twin(), null);

        // insert initial search node
        pq.insert(initSearchNode);

        // insert initial twin search node
        pqTwin.insert(twinSearchNode);

        // delete the search node with lowest priority
        SearchNode deleteNode = pq.delMin();

        // delete the search node with lowest priority
        SearchNode deleteNodeTwin = pqTwin.delMin();

        // insert all neighbouring search nodes into priority queue if the dequeued node does not corresponds to the goal board
        while (!deleteNode.getBoard().isGoal() && !deleteNodeTwin.getBoard().isGoal()) {

            // use iterable to insert all neighbouring search nodes of dequeued search nodes
            for (Board neighbourBoard : deleteNode.getBoard().neighbors()) {

                // critical optimization to reduce useless neighbouring search nodes if its board is same as the board of previous nodes
                if (deleteNode.getPrevNode() == null || !neighbourBoard
                        .equals(deleteNode.getPrevNode()
                                          .getBoard())) {
                    pq.insert(new SearchNode(neighbourBoard, deleteNode));
                }
            }

            // dequeue the search node with min. priority in the updated priority queue
            // update the dequeued search node
            deleteNode = pq.delMin();

            // use iterable to insert all neighbouring search nodes of dequeued search nodes
            for (Board neighbourBoard : deleteNodeTwin.getBoard().neighbors()) {

                // critical optimization to reduce useless neighbouring search nodes if its board is same as the board of previous nodes
                if (deleteNodeTwin.getPrevNode() == null || !neighbourBoard
                        .equals(deleteNodeTwin.getPrevNode()
                                              .getBoard())) {
                    pqTwin.insert(new SearchNode(neighbourBoard, deleteNodeTwin));
                }
            }

            deleteNodeTwin = pqTwin.delMin();

        }

        // check the deleted node from PQ is from initial board rather than initial twin board
        if (deleteNode.getBoard().isGoal()) {
            solvable = true;
            solutionNode = deleteNode;
        }
        else {
            solvable = false;
        }

        /*
        // check the root of game tree from the deleted Node
        while (node != null) {
            if (node.getPrevNode() == null) {
                // if the root of delete Node is equal to the initial twin board, the board is insolvable
                if (node.getBoard().equals(twinSearchNode.getBoard())) {
                    solvable = false;
                }
                // if the root of delete Node is equal to the initial board, the board is solvable
                else {
                    solvable = true;
                    solutionNode = deleteNode;
                }
            }
            // update node to traverse game tree from bottom to the root
            node = node.getPrevNode();
        }
         */


    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) {
            return -1;
        }
        return solutionNode.getMoves();
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.isSolvable()) {
            // instantiate iterable object by stack to iterate object by first in last out
            Stack<Board> solution = new Stack<Board>();
            SearchNode node = solutionNode;

            while (node != null) {
                solution.push(node.getBoard());
                // update node to traverse game tree from bottom to the root
                node = node.getPrevNode();
            }
            return solution;
        }
        else {
            return null;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

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

}
