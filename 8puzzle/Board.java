import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    // declare instance variables
    private final int[][] blocks; // store the values in n*n 2D array from given n*n array of tiles
    private final int n; // dimension of the board

    // constructor to create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // check the validity of input array
        if (tiles == null) {
            throw new NullPointerException("The input 2D array is null.");
        }

        // initialize dimension of the board by input array
        n = tiles.length;
        // initialize the n-by-n array of tiles in current Board object
        this.blocks = new int[n][n];

        // copy the values from given tiles to the n*n block array in board object
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = tiles[i][j];
            }
        }

    }

    // string representation of this board
    public String toString() {
        // initialize string by StringBuilder Class
        StringBuilder sb = new StringBuilder();

        // return first line containing the board dimension
        sb.append(n).append("\n");

        // return the remaining n lines containing the n*n grid of tiles
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // format the integer numbers to decimal integer
                sb.append(String.format("%d", blocks[i][j]));
                sb.append(" ");
            }
            // newline character for each row
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hDistance = 0;
        int checker = 1;

        // compare every integer with checker to compute Hamming Distance
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != checker && blocks[i][j] != 0) {
                    hDistance++;
                }
                checker++; // increment by 1 after check each integer
            }
        }
        return hDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        /*
        Manhattan distance = |x1-x2| + |y1-y2| (horizontal move and vertical move between 2 points in 2D grid)
         */
        int mDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // check the block value is not zero that computes manhattan distance
                if (blocks[i][j] != 0) {
                    // compute target row for current value
                    int targetRow = (blocks[i][j] - 1) / n;
                    // compute target column for current value
                    int targetCol = (blocks[i][j] - 1) % n;
                    // compute the Manhattan distance for current value by sum of horizontal move and vertical move
                    mDistance += Math.abs(i - targetRow) + Math.abs(j - targetCol);
                }
            }
        }
        return mDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // if no tiles in the wrong position, the board is the goal board
        if (hamming() == 0) {
            return true;
        }
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // optimize for true object equality
        if (y == this) return true;

        // check for null argument
        if (y == null) return false;

        // check object in the same class
        if (y.getClass() != this.getClass()) {
            return false;
        }

        // cast the object argument to board object
        Board that = (Board) y;

        // check for dimension
        if (this.dimension() != that.dimension()) return false;

        // check for each entry of tiles in the same position
        return Arrays.deepEquals(this.blocks, that.blocks);
    }


    // swap function to swap tile position for Iterable<Board> neighbours()
    private void swap(int[][] arr, int row, int col, int row2, int col2) {
        int tmp = arr[row][col];
        arr[row][col] = arr[row2][col2];
        arr[row2][col2] = tmp;
    }

    // all neighboring boards
    // implement iterable<T> interface to allow an object to be the target of the for-each loop
    public Iterable<Board> neighbors() {
        // initialize arraylist to store board objects
        ArrayList<Board> neighbours = new ArrayList<>();


        // initialize variables to store blank square position
        int blankRow = -1, blankCol = -1;

        // find the position of blank square
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }

        // create array to store position of blank square within 1 block distance for movement in further looping
        int[] rowPos = { blankRow - 1, blankRow, blankRow + 1 };
        int[] colPos = { blankCol - 1, blankCol, blankCol + 1 };

        // iterate over the possible position
        for (int i = 0; i < rowPos.length; i++) {
            for (int j = 0; j < colPos.length; j++) {

                int newRow = rowPos[i];
                int newCol = colPos[j];

                // initialize new 2D grid in every iteration
                int[][] newBlocks = new int[n][n];

                //  copy of blocks in current board object
                for (int m = 0; m < blocks.length; m++) {
                    for (int p = 0; p < blocks.length; p++) {
                        newBlocks[m][p] = blocks[m][p];
                    }
                }

                // check for boundary condition
                if (newRow >= 0 && newRow < blocks.length && newCol >= 0
                        && newCol < blocks.length) {
                    // check for top, bottom, left and right neighbours only
                    if ((Math.abs(blankRow - newRow) + Math.abs(blankCol - newCol)) == 1) {
                        // swap the position
                        swap(newBlocks, blankRow, blankCol, newRow, newCol);

                        // add new board to arrayList
                        neighbours.add(new Board(newBlocks));
                    }
                }
            }
        }

        // return iterable object
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // initialize new 2D grid in every iteration
        int[][] twin = new int[n][n];

        //  copy of blocks in current board object (beware for using Arrays.copyOf which mutates blocks 2d grid
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                twin[i][j] = blocks[i][j];
            }
        }

        // swap the board in the same row (the blank square does not count)
        // exchange any pair of tiles such that we try (0, 0) and (0, 1)
        if (twin[0][0] != 0 && twin[0][1] != 0) {
            swap(twin, 0, 0, 0, 1);
        }
        else {
            swap(twin, 1, 0, 1, 1);
        }

        // return instantiated board object after exchanging any pair of tiles
        return new Board(twin);

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // read file
        In in = new In(args[0]);
        // read spec in file
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        StdOut.println(initial.toString());
        StdOut.println("Dimension: " + initial.dimension());
        StdOut.println("Hamming: " + initial.hamming());
        StdOut.println("Manhattan: " + initial.manhattan());
        StdOut.println("Goal?: " + initial.isGoal());
        StdOut.println(initial.twin());
        Board twinBoard = initial.twin();
        StdOut.println("Equal to twin?: " + initial.equals(twinBoard));

        for (Board board : initial.neighbors()) {
            StdOut.println(board.toString());
        }
    }
}
