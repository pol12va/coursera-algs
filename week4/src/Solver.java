import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final Queue<Board> solutions;

    private static class ManhattanComparator implements Comparator<Board> {
        @Override
        public int compare(Board o1, Board o2) {
            return o1.manhattan() - o2.manhattan();
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        solutions = new Queue<>();
        MinPQ<Board> boardsQueue = new MinPQ<>(new ManhattanComparator());

        boardsQueue.insert(initial);
        Board maxBoard;
        maxBoard = boardsQueue.delMin();
        Board previousStep = maxBoard;

        while (!maxBoard.isGoal()) {
            for (Board neighbor : maxBoard.neighbors()) {
                if (!previousStep.equals(neighbor)) {
                    boardsQueue.insert(neighbor);
                }
            }

            previousStep = maxBoard;
            maxBoard = boardsQueue.delMin();
            solutions.enqueue(maxBoard);
        }
    }

    public boolean isSolvable() {
        return true;
    }

    public int moves() {
        return solutions.size();
    }

    public Iterable<Board> solution() {
        return solutions;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
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
}
