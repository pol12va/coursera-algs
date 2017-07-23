import java.util.Arrays;

public class Board {

    private final int[][] boardBlocks;
    private int emptyBlockX;
    private int emptyBlockY;
    private int hamming;
    private int manhattan;

    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException();
        }

        boardBlocks = new int[blocks.length][];
        initBoard(blocks);
        calculateHamming();
        calculateManhattan();
    }

    public int dimension() {
        return boardBlocks.length;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        for (int i = 0; i < boardBlocks.length; i++) {
            for (int j = 0; j < boardBlocks.length; j++) {
                if (boardBlocks[i][j] != i * dimension() + j + 1
                        && i * dimension() + j != dimension() * dimension() - 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board twin() {
        int[][] twinBlocks = new int[boardBlocks.length][boardBlocks.length];

        for (int i = 0; i < boardBlocks.length; i++) {
            System.arraycopy(boardBlocks[i], 0, twinBlocks[i], 0, boardBlocks[i].length);
        }

        if (boardBlocks.length > 1) {
            int x1 = 0;
            int y1 = emptyBlockY == 0 ? 1 : 0;
            int x2 = boardBlocks.length - 1;
            int y2 = emptyBlockY == 0 ? 1 : 0;

            exch(twinBlocks, x1, y1, x2, y2);
        }

        return new Board(twinBlocks);
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (getClass() != y.getClass()) {
            return false;
        }

        Board other = (Board) y;

        if (dimension() != other.dimension()) {
            return false;
        }
        for (int i = 0; i < boardBlocks.length; i++) {
            for (int j = 0; j < boardBlocks[i].length; j++) {
                if (boardBlocks[i][j] != other.boardBlocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        Board[] neighbors = new Board[numberOfNeighbors()];
        int idx = 0;

        if (emptyBlockY + 1 < dimension()) {
            neighbors[idx++] = neighbor(emptyBlockX, emptyBlockY + 1);
        }
        if (emptyBlockY - 1 >= 0) {
            neighbors[idx++] = neighbor(emptyBlockX, emptyBlockY - 1);
        }
        if (emptyBlockX + 1 < dimension()) {
            neighbors[idx++] = neighbor(emptyBlockX + 1, emptyBlockY);
        }
        if (emptyBlockX - 1 >= 0) {
            neighbors[idx++] = neighbor(emptyBlockX - 1, emptyBlockY);
        }

        return Arrays.asList(neighbors);
    }

    @Override
    public String toString() {
        StringBuilder boardBuilder = new StringBuilder();

        String format = dimension() < 11 ? "%2d " : "%3d ";
        boardBuilder.append(dimension() + "\n");
        for (int[] boardRow : boardBlocks) {
            for (int block : boardRow) {
                boardBuilder.append(String.format(format, block));
            }
            boardBuilder.append("\n");
        }
        return boardBuilder.toString();
    }

    private void initBoard(int[][] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            boardBlocks[i] = new int[blocks[i].length];

            for (int j = 0; j < blocks[i].length; j++) {
                boardBlocks[i][j] = blocks[i][j];
                if (boardBlocks[i][j] == 0) {
                    emptyBlockX = i;
                    emptyBlockY = j;
                }
            }
        }
    }

    private void calculateHamming() {
        for (int i = 0; i < boardBlocks.length; i++) {
            for (int j = 0; j < boardBlocks[i].length; j++) {
                if (boardBlocks[i][j] != 0 && boardBlocks[i][j] != i * dimension() + j + 1) {
                    hamming++;
                }
            }
        }
    }

    private void calculateManhattan() {
        for (int i = 0; i < boardBlocks.length; i++) {
            for (int j = 0; j < boardBlocks[i].length; j++) {
                int curBlock = boardBlocks[i][j];
                if (curBlock != 0) {
                    int goalBlockX = (curBlock - 1) / dimension();
                    int goalBlockY = (curBlock - 1) % dimension();
                    manhattan += Math.abs(goalBlockX - i) + Math.abs(goalBlockY - j);
                }
            }
        }
    }

    private void exch(int[][] a, int x1, int y1, int x2, int y2) {
        int tmp = a[x1][y1];
        a[x1][y1] = a[x2][y2];
        a[x2][y2] = tmp;
    }

    private int numberOfNeighbors() {
        int num = 0;
        if (emptyBlockY + 1 < dimension()) {
            num++;
        }
        if (emptyBlockY - 1 >= 0) {
            num++;
        }
        if (emptyBlockX + 1 < dimension()) {
            num++;
        }
        if (emptyBlockX - 1 >= 0) {
            num++;
        }

        return num;
    }

    private Board neighbor(int x, int y) {
        int[][] twinBlocks = new int[boardBlocks.length][boardBlocks.length];
        for (int i = 0; i < boardBlocks.length; i++) {
            System.arraycopy(boardBlocks[i], 0, twinBlocks[i], 0, boardBlocks[i].length);
        }
        exch(twinBlocks, emptyBlockX, emptyBlockY, x, y);

        return new Board(twinBlocks);
    }

    public static void main(String[] args) {
        int[][] initialBlocks = new int[][] {
                { 1, 0, 3 },
                { 4, 2, 5 },
                { 7, 8, 6 }
        };

        int[][] initialBlocks2 = new int[][] {
                { 1 }
        };

        Board b = new Board(initialBlocks);
        System.out.println("Initial board:");
        System.out.println(b);
        System.out.println("empty block = (" + b.emptyBlockX + ", " + b.emptyBlockY + ")\n");

        for (Board neighbor : b.neighbors()) {
            System.out.println(neighbor);
        }

        assert !b.isGoal();
        assert b.hamming() == 3;
        assert b.manhattan() == 3;

        System.out.println("Twin:");
        System.out.println(b.twin());

        Board b1 = new Board(initialBlocks2);
        System.out.println("Twin 1:");
        System.out.println(b1.twin());
    }
}
