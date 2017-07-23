import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final Stack<Board> solutions;
    private boolean solvable = true;

    private static class ManhattanComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode n1, SearchNode n2) {
            return n1.manhattan() - n2.manhattan();
        }
    }

    private static class ManhattanComparatorIncorrect implements Comparator<Board> {
        @Override
        public int compare(Board n1, Board n2) {
            return n1.manhattan() - n2.manhattan();
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        solutions = new Stack<>();
        solve(initial);
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return solvable ? solutions.size() - 1 : -1;
    }

    public Iterable<Board> solution() {
        return solvable ? solutions : null;
    }

    private void solve(Board initial) {
        ManhattanComparator mc = new ManhattanComparator();
        MinPQ<SearchNode> boardsQueue = new MinPQ<>(mc);
        MinPQ<SearchNode> twinBoardsQueue = new MinPQ<>(mc);

        SearchNode initialNode = new SearchNode(initial, 0, null);
        boardsQueue.insert(initialNode);

        SearchNode initialTwinNode = new SearchNode(initial.twin(), 0, null);
        twinBoardsQueue.insert(initialTwinNode);

        SearchNode maxNode = boardsQueue.delMin();
        SearchNode maxTwinNode = twinBoardsQueue.delMin();

        while (!maxNode.board.isGoal() && !maxTwinNode.board.isGoal()) {
            maxNode = putNeighborsAndFindMax(maxNode, boardsQueue);
            maxTwinNode = putNeighborsAndFindMax(maxTwinNode, twinBoardsQueue);
        }

        if (maxTwinNode.board.isGoal()) {
            solvable = false;
        }

        if (solvable) {
            SearchNode node = maxNode;
            while (node != null) {
                solutions.push(node.board);
                node = node.prevBoard;
            }
        }
    }

    private SearchNode putNeighborsAndFindMax(SearchNode maxNode, MinPQ<SearchNode> boardsQueue) {
        for (Board neighbor : maxNode.board.neighbors()) {
            if (maxNode.prevBoard == null || !maxNode.prevBoard.board.equals(neighbor)) {
                SearchNode neighborNode = new SearchNode(neighbor, maxNode.prevStepNum + 1, maxNode);
                boardsQueue.insert(neighborNode);
            }
        }

        return boardsQueue.delMin();
    }

    private class SearchNode {
        private final Board board;
        private final int prevStepNum;
        private final SearchNode prevBoard;

        public SearchNode(Board b, int stepsBefore, SearchNode prev) {
            this.board = b;
            this.prevStepNum = stepsBefore;
            this.prevBoard = prev;
        }

        public int manhattan() {
            return board.manhattan() + prevStepNum;
        }
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
            System.out.println("-----------------");
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
