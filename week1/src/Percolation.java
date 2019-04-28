import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF virtualUf;
    private int openCount;
    private boolean[] openSites;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("size is incorrect");
        }

        size = n;
        uf = new WeightedQuickUnionUF(size * size);
        virtualUf = new WeightedQuickUnionUF(size * size);
        openSites = new boolean[size * size];
        initRoots();
    }

    public void open(int row, int col) {
        int pos;

        try {
            pos = xyTo1D(row, col);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
        if (!openSites[pos]) {
            openSites[pos] = true;
            openCount++;

            // TOP
            openIfExist(row - 1, col, pos);
            // RIGHT
            openIfExist(row, col + 1, pos);
            // BOTTOM
            openIfExist(row + 1, col, pos);
            // LEFT
            openIfExist(row, col - 1, pos);

        }
    }

    public boolean isOpen(int row, int col) {
        try {
            int pos = xyTo1D(row, col);

            return openSites[pos];
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        int bottom = size * (size - 1);
        if (bottom == 0) {
            return isOpen(1, 1);
        }
        return virtualUf.connected(0, size * (size - 1));
    }

    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            int pos = xyTo1D(row, col);

            return uf.connected(pos, 0);
        }
        return false;
    }

    private void openIfExist(int row, int col, int openPos) {
        try {
            if (isOpen(row, col)) {
                int pos = xyTo1D(row, col);

                if (!uf.connected(pos, openPos)) {
                    uf.union(pos, openPos);
                    virtualUf.union(pos, openPos);
                }
            }
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    private int xyTo1D(int x, int y) {
        if (x <= 0 || x > size || y <= 0 || y > size) {
            throw new IndexOutOfBoundsException("row or column index is incorrect");
        }

        int rowIndex = x - 1;
        int colIndex = y - 1;

        return rowIndex * size + colIndex;
    }

    private void initRoots() {
        for (int i = 1; i < size; i++) {
            uf.union(0, i);
            virtualUf.union(0, i);
            virtualUf.union(size * (size - 1), size * (size - 1) + i);
        }
    }

}
