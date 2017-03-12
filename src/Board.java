import edu.princeton.cs.algs4.Queue;

public class Board {

	private int[][] nAABlocks;

	public Board(int[][] blocks) {
		this.nAABlocks = new int[blocks.length][blocks[0].length];
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks.length; j++) {
				nAABlocks[i][j] = blocks[i][j];
			}
		}
	}

	// construct a board from an n-by-n array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public int dimension() {
		return nAABlocks.length;
	}

	// board dimension n
	public int hamming() {
		int nHam = 0;
		for (int i = 0; i < nAABlocks.length; i++) {
			for (int j = 0; j < nAABlocks.length; j++) {
				if (nAABlocks[i][j] != 0 && nAABlocks[i][j] != getLocationToNum(i, j)) {
					nHam++;
				}
			}
		}
		return nHam;
	}

	// number of blocks out of place
	private int getLocationToNum(int i, int j) {
		return i * dimension() + j + 1;
	}
	// calculate the correct number at this place.

	public int manhattan() {
		int nMan = 0;
		for (int i = 0; i < nAABlocks.length; i++) {
			for (int j = 0; j < nAABlocks.length; j++) {
				if (nAABlocks[i][j] != 0) {
					int[] curCo = { i, j };
					int[] expCo = getNumToLocation(nAABlocks[i][j]);
					int nDis = Math.abs(expCo[0] - curCo[0]) + Math.abs(expCo[1] - curCo[1]);
					nMan += nDis;
				}
			}
		}
		return nMan;
	}

	// sum of Manhattan distances between blocks and goal
	private int[] getNumToLocation(int n) {
		int[] coor = new int[2];
		coor[0] = (n - 1) / dimension();
		coor[1] = (n - 1) % dimension();
		return coor;
	}

	// calculate the correct number at this place.
	public boolean isGoal() {
		int nLast = nAABlocks[0][0];
		for (int i = 0; i < nAABlocks.length; i++) {
			for (int j = 0; j < nAABlocks.length; j++) {
				if (i != dimension() - 1 || j != dimension() - 1) {
					if (nLast > nAABlocks[i][j]) {
						return false;
					} else {
						nLast = nAABlocks[i][j];
					}
				}
			}
		}
		return nAABlocks[dimension() - 1][dimension() - 1] == 0 ? true : false;
	}

	// is this board the goal board?

	public Board twin() {
		if (nAABlocks[0][0] == 0) {
			swap(2, 3);
		} else {
			if (nAABlocks[0][1] == 0) {
				swap(1, 3);
			} else {
				swap(1, 2);
			}
		}
		return this;
	}
	// a board that is obtained by exchanging any pair of blocks

	private void swap(int index1, int index2) {
		int[] co1 = getNumToLocation(index1);
		int[] co2 = getNumToLocation(index2);
		swap(co1[0], co1[1], co2[0], co2[1]);
	}

	private void swap(int x1, int y1, int x2, int y2) {
		int temp = nAABlocks[x1][y1];
		nAABlocks[x1][y1] = nAABlocks[x2][y2];
		nAABlocks[x2][y2] = temp;
	}

	public boolean equals(Object y) {
		if (y == this) {
			return true;
		}
		if (y == null) {
			return false;
		}
		if (y.getClass() != this.getClass()) {
			return false;
		}

		int[][] nAABlocksY = ((Board) y).nAABlocks;
		for (int i = 0; i < nAABlocks.length; i++) {
			for (int j = 0; j < nAABlocks.length; j++) {
				if (nAABlocks[i][j] != nAABlocksY[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	// does this board equal y?

	public Iterable<Board> neighbors() {
		Queue<Board> neighbor = new Queue<>();
		int x = -1, y = -1;
		for (int i = 0; i < nAABlocks.length; i++) {
			for (int j = 0; j < nAABlocks.length; j++) {
				if (nAABlocks[i][j] == 0) {
					x = i;
					y = j;
					break;
				}
			}
		}
		if (!isBeyond(x - 1, y)) {
			Board b = new Board(nAABlocks);
			b.swap(x, y, x - 1, y);
			neighbor.enqueue(b);
		}
		if (!isBeyond(x + 1, y)) {
			Board b = new Board(nAABlocks);
			b.swap(x, y, x + 1, y);
			neighbor.enqueue(b);
		}
		if (!isBeyond(x, y - 1)) {
			Board b = new Board(nAABlocks);
			b.swap(x, y, x, y - 1);
			neighbor.enqueue(b);
		}
		if (!isBeyond(x, y + 1)) {
			Board b = new Board(nAABlocks);
			b.swap(x, y, x, y + 1);
			neighbor.enqueue(b);
		}
		return neighbor;
	}
	// all neighboring boards

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(dimension() + "\n");
		for (int i = 0; i < dimension(); i++) {
			for (int j = 0; j < dimension(); j++) {
				s.append(String.format("%2d ", nAABlocks[i][j]));
			}
			s.append("\n");
		}
		return s.toString();
	}
	// string representation of this board (in the output format specified
	// below)

	private boolean isBeyond(int i, int j) {
		return i < 0 || i >= dimension() || j < 0 || j >= dimension() ? true : false;
	}

	public static void main(String[] args) {
		int[][] blocks = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
		Board b = new Board(blocks);
		System.out.println(b.toString());
		for (Board bb : b.neighbors()) {
			System.out.println(bb);
		}

	}
	// unit tests (not graded)
}