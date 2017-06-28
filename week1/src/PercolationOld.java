import edu.princeton.cs.algs4.StdRandom;

public class PercolationOld {

    private final int size;
    private final Site[][] sites;
    private int openCount;
    private Site rootTopSite = new Site(-1);
    private Site rootBottomSite = new Site(-2);

    public PercolationOld(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("size is incorrect");
        }

        this.size = n;
        this.sites = new Site[n][n];

        initTable();
    }

    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IndexOutOfBoundsException("incorrect argument. Row = " + row + ", Col = " + col);
        }

        if (!isOpen(row, col)) {
            int rowIndex = row - 1;
            int colIndex = col - 1;

            Site toOpen = sites[rowIndex][colIndex];
            toOpen.open = true;
            openCount++;
            Site nearBy;

            // TOP
            if (siteExists(row - 1, col)) {
                nearBy = sites[rowIndex - 1][colIndex];
                if (isOpen(row - 1, col)) {
                    union(toOpen, nearBy);
                }
            }

            // RIGHT
            if (siteExists(row, col + 1)) {
                nearBy = sites[rowIndex][colIndex + 1];
                if (isOpen(row, col + 1)) {
                    union(toOpen, nearBy);
                }
            }

            // BOTTOM
            if (siteExists(row + 1, col)) {
                nearBy = sites[rowIndex + 1][colIndex];
                if (isOpen(row + 1, col)) {
                    union(toOpen, nearBy);
                }
            }

            //LEFT
            if (siteExists(row, col - 1)) {
                nearBy = sites[rowIndex][colIndex - 1];
                if (isOpen(row, col - 1)) {
                    union(toOpen, nearBy);
                }
            }
        }

    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IndexOutOfBoundsException("incorrect argument. Row = " + row + ", Col = " + col);
        }

        int rowIndex = row - 1;
        int colIndex = col - 1;

        return sites[rowIndex][colIndex].open;
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        return connected(rootTopSite, rootBottomSite);
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("incorrect argument. Row = " + row + ", Col = " + col);
        }

        int rowIndex = row - 1;
        int colIndex = col - 1;

        return connected(sites[rowIndex][colIndex], rootTopSite);
    }

    private void initTable() {
        for (int i = 0; i < size; i++) {
            sites[i] = new Site[size];
            for (int j = 0; j < size; j++) {
                Site s = new Site(i * size + j);
                sites[i][j] = s;

                if (i == 0) {
                    s.parent = rootTopSite;
                }
                if (i > 0 && i == size - 1) {
                    s.parent = rootBottomSite;
                }
            }
        }
    }

    private boolean siteExists(int row, int col) {
        int rowIndex = row - 1;
        int colIndex = col - 1;

        if (rowIndex < 0 || rowIndex >= size || colIndex < 0 || colIndex >= size) {
            return false;
        }
        return true;
    }

    // ---------------
    // UF
    // ---------------
    private boolean connected(Site s1, Site s2) {
        return root(s1) == root(s2);
    }

    private void union(Site s1, Site s2) {
        if (connected(s1, s2)) {
            return;
        }

        if (s2.sz > s1.sz) {
            s2.sz += s1.sz;
            root(s1).parent = root(s2);
        } else {
            s1.sz += s2.sz;
            root(s2).parent = root(s1);
        }
    }

    private Site root(Site s) {
        if (s.parent == s) {
            return s;
        }

        return root(s.parent);
    }
    // ---------------
    // END OF UF
    // ---------------

    @Override
    public String toString() {
        StringBuilder tableString = new StringBuilder();
        for (Site[] row : sites) {
            for (Site s : row) {
                tableString.append(String.format("%2d", s.open ? 1 : 0)).append(" ");
            }
            tableString.append("\n");
        }

        return tableString.toString();
    }

    private class Site {
        private int sz;
        private boolean open;
        private Site parent;
        private int id;

        public Site() {
            this.sz = 1;
            this.open = false;
            this.parent = this;
        }

        public Site(int id) {
            this();
            this.id = id;
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int size = Integer.parseInt(args[0]);
        int simulationTimes = Integer.parseInt(args[1]);
        int i = 0;
        double[] results = new double[simulationTimes];

        System.out.println("Running simulation for size = " + size + " " + simulationTimes + " times");
        PercolationOld p = new PercolationOld(size);

        while (i < simulationTimes) {
            do {
                int row = StdRandom.uniform(1, size + 1);
                int col = StdRandom.uniform(1, size + 1);
                p.open(row, col);
            } while (!p.percolates() && p.openCount != size * size);

            results[i] = (double) p.numberOfOpenSites() / (size * size);
            i++;
        }

        double sum = 0.0d;
        for (double res : results) {
            sum += res;
        }

        System.out.println("Threshold = " + sum / simulationTimes);
        System.out.println("Simulation took " + (System.currentTimeMillis() - start) + " ms");
    }

}
