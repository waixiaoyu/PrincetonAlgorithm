import java.util.Comparator;

import edu.princeton.cs.algs4.*;

public class Solver {

	private Board board;
	private Board boardTwin;
	private boolean isSolve = false;

	private MinPQ<BoardNode> pq = new MinPQ<>();
	private MinPQ<BoardNode> pqTwin = new MinPQ<>();

	public Solver(Board initial) {
		if (initial == null) {
			throw new NullPointerException();
		}
		this.board = initial;
		this.boardTwin = initial.twin();
	}

	// find a solution to the initial board (using the A* algorithm)
	public boolean isSolvable() {
		return isSolve;
	}

	// is the initial board solvable?
	public int moves() {
		return 0;
	}

	Queue<Board> qSolution = new Queue<Board>();

	// min number of moves to solve initial board; -1 if unsolvable
	public Iterable<Board> solution() {
		return (Iterable<Board>) qSolution.iterator();
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public static void main(String[] args) {
	}
	// solve a slider puzzle (given below)

	private class BoardNode implements Comparator<BoardNode> {

		private Board board;
		private int nStep;
		private int nPriority;

		public BoardNode(Board board, int nStep) {
			this.board = board;
			this.nStep = nStep;
			this.nPriority = nStep + board.manhattan();
		}

		@Override
		public int compare(BoardNode o1, BoardNode o2) {
			return o1.nPriority - o2.nPriority;
		}

	}
}
