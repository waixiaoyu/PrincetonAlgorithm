package edu.princeton.puzzle8;
import java.util.Comparator;

import edu.princeton.cs.algs4.*;

public class Solver {

	private static final int CONTINUE = 0;
	private static final int SOLVED = 1;
	private static final int UNSOLVED = 2;

	private int nMinStep = 0;

	private BoardNode bn;
	private BoardNode bnt;
	private boolean bSolve = false;

	private MinPQ<BoardNode> pq = new MinPQ<>();
	private MinPQ<BoardNode> pqt = new MinPQ<>();

	private Queue<Board> qSolution = new Queue<Board>();
	private Queue<Board> qSolutiont = new Queue<Board>();

	public Solver(Board initial) {
		if (initial == null) {
			throw new NullPointerException();
		}
		this.bn = new BoardNode(initial, 0, null);
		this.bnt = new BoardNode(initial.twin(), 0, null);

		pq.insert(bn);
		pqt.insert(bnt);

		while (true) {
			// check origin
			int nPq = solve(pq, qSolution);
			switch (nPq) {
			case SOLVED:
				bSolve = true;
				return;
			case UNSOLVED:
				bSolve = false;
				return;
			default:
				break;
			}
			// check twin
			int nPqt = solve(pqt, qSolutiont);
			switch (nPqt) {
			case SOLVED:
				bSolve = false;
				return;
			case UNSOLVED:
				bSolve = true;
				return;
			default:
				break;
			}
			nMinStep++;
		}
	}

	// 0=continue 1=solved 2=unsolved
	private int solve(MinPQ<BoardNode> mpq, Queue<Board> queue) {
		if (mpq.isEmpty()) {
			return UNSOLVED;
		}
		BoardNode curBn = mpq.delMin();
		queue.enqueue(curBn.board);
		int nCurStep = curBn.nStep;
		if (curBn.board.isGoal()) {
			nMinStep = curBn.nStep;
			return SOLVED;
		} else {
			for (Board b : curBn.board.neighbors()) {
				BoardNode bnLast = curBn;
				boolean bFindSame = false;
				while (bnLast != null) {
					if (b.equals(bnLast.board)) {
						bFindSame = true;
						break;
					} else {
						bnLast = bnLast.last;
					}
				}
				if (!bFindSame) {
					BoardNode bno = new BoardNode(b, nCurStep + 1, curBn);
					mpq.insert(bno);
				}
			}
			return CONTINUE;
		}
	}

	// find a solution to the initial board (using the A* algorithm)
	public boolean isSolvable() {
		return bSolve;
	}

	// is the initial board solvable?
	public int moves() {
		return isSolvable() ? nMinStep : -1;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public Iterable<Board> solution() {
		return isSolvable() ? qSolution : null;
	}

	// sequence of boards in a shortest solution; null if unsolvable

	private class BoardNode implements Comparable<BoardNode> {

		private Board board;
		private int nStep;
		private int nPriority;
		private BoardNode last;

		public BoardNode(Board board, int nStep, BoardNode last) {
			this.board = board;
			this.nStep = nStep;
			this.last = last;
			this.nPriority = nStep + board.manhattan();
		}

		@Override
		public int compareTo(BoardNode o) {
			// TODO Auto-generated method stub
			return this.nPriority - o.nPriority;
		}

	}

	public static void main(String[] args) {

	}
	// solve a slider puzzle (given below)
}
