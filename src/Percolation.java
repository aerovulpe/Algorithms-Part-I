import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte IS_OPEN = 1 << 0; // 001
    private static final byte IS_CONNECTED_TO_TOP = 1 << 1; // 010
    private static final byte IS_CONNECTED_TO_BOTTOM = 1 << 2; // 100

    private final WeightedQuickUnionUF unionFind;
    private final byte[] state;
    private final int size;
    private final int sizeSquared;
    private boolean percolates;

    // create N-by-N grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException("Invalid size!");

        sizeSquared = n * n;
        unionFind = new WeightedQuickUnionUF(sizeSquared);
        state = new byte[sizeSquared];
        size = n;
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        int idx = computeIdx(i, j);
        if (idx == -1)
            throw new IndexOutOfBoundsException("Index out of bound!");

        if (isOpen(state[idx]))
            return;

        int iPlus1 = i + 1;
        int iMinus1 = i - 1;
        int jPlus1 = j + 1;
        int jMinus1 = j - 1;
        boolean connectedToTop = i == 1;
        boolean connectedToBottom = i == size;

        int currIdx = computeIdx(i, jMinus1);
        ConnectToNeighbour connectToNeighbour = new ConnectToNeighbour(idx, connectedToTop, connectedToBottom, currIdx).invoke();
        currIdx = computeIdx(i, jPlus1);
        connectToNeighbour = new ConnectToNeighbour(idx, connectToNeighbour.connectedToTop, connectToNeighbour.connectedToBottom, currIdx).invoke();
        currIdx = computeIdx(iMinus1, j);
        connectToNeighbour = new ConnectToNeighbour(idx, connectToNeighbour.connectedToTop, connectToNeighbour.connectedToBottom, currIdx).invoke();
        currIdx = computeIdx(iPlus1, j);
        connectToNeighbour = new ConnectToNeighbour(idx, connectToNeighbour.connectedToTop, connectToNeighbour.connectedToBottom, currIdx).invoke();
        connectedToTop = connectToNeighbour.connectedToTop;
        connectedToBottom = connectToNeighbour.connectedToBottom;

        int root = unionFind.find(idx);
        if (connectedToTop)
            state[root] |= IS_CONNECTED_TO_TOP;
        if (connectedToBottom)
            state[root] |= IS_CONNECTED_TO_BOTTOM;

        if (connectedToTop && connectedToBottom)
            percolates = true;

        state[idx] |= IS_OPEN;
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        int idx = computeIdx(i, j);
        if (idx == -1)
            throw new IndexOutOfBoundsException("Index out of bound!");

        return isOpen(state[idx]);
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        int idx = computeIdx(i, j);
        if (idx == -1)
            throw new IndexOutOfBoundsException("Index out of bound!");

        int root = unionFind.find(idx);
        return isOpen(state[idx]) && isConnectedToTop(state[root]);
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    private int computeIdx(int i, int j) {
        return (i > 0 && i <= size && j > 0 && j <= size) ? (i - 1) * size + j - 1 : -1;
    }

    private static boolean isOpen(byte flag) {
        return (flag & IS_OPEN) == IS_OPEN;
    }

    private static boolean isConnectedToTop(byte flag) {
        return (flag & IS_CONNECTED_TO_TOP) == IS_CONNECTED_TO_TOP;
    }

    private static boolean isConnectedToBottom(byte flag) {
        return (flag & IS_CONNECTED_TO_BOTTOM) == IS_CONNECTED_TO_BOTTOM;
    }

    private class ConnectToNeighbour {
        private int idx;
        private boolean connectedToTop;
        private boolean connectedToBottom;
        private int currIdx;

        public ConnectToNeighbour(int idx, boolean connectedToTop, boolean connectedToBottom, int currIdx) {
            this.idx = idx;
            this.connectedToTop = connectedToTop;
            this.connectedToBottom = connectedToBottom;
            this.currIdx = currIdx;
        }

        public ConnectToNeighbour invoke() {
            if (currIdx != -1 && isOpen(state[currIdx])) {
                int root = unionFind.find(currIdx);
                connectedToTop |= Percolation.isConnectedToTop(state[root]);
                connectedToBottom |= Percolation.isConnectedToBottom(state[root]);
                unionFind.union(currIdx, idx);
            }
            return this;
        }
    }
}
