package edu.princeton.kdtree;
import java.awt.Color;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	private static final boolean USING_X = true;

	private Node root;
	private int size;

	private class Node {
		private double x;
		private double y;
		/**
		 * if true, split vertical and use the x-coordinate, if false, split
		 * horizontal and use y-coordinate
		 */
		private boolean bDirection;
		private Point2D point;
		private RectHV rect;
		private Node left;
		private Node right;
		private Node parent;

		public Node(boolean bDirection, Node parent, Point2D point, RectHV rect) {
			super();
			this.bDirection = bDirection;
			this.point = point;
			this.parent = parent;
			this.x = point.x();
			this.y = point.y();
			this.rect = rect;
		}
	}

	public KdTree() // construct an empty set of points
	{
		root = null;
		size = 0;
	}

	public boolean isEmpty() // is the set empty?
	{
		return this.size == 0 ? true : false;
	}

	public int size() // number of points in the set
	{
		return this.size;
	}

	private Node findParent(Node nCur, Node nParent, Point2D p) {
		if (nCur == null) {
			return nParent;
		} else if (nCur.point.equals(p)) {
			return null;
		} else {
			if (nCur.bDirection == USING_X) {
				if (nCur.x < p.x()) {
					return findParent(nCur.right, nCur, p);
				} else {
					return findParent(nCur.left, nCur, p);
				}
			} else {
				if (nCur.y < p.y()) {
					return findParent(nCur.right, nCur, p);
				} else {
					return findParent(nCur.left, nCur, p);
				}
			}
		}
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new NullPointerException();
		}
		if (isEmpty()) {
			root = new Node(USING_X, null, p, new RectHV(0, 0, 1, 1));
			size++;
		} else {
			Node nParent = findParent(root, null, p);
			if (nParent != null) {
				insertChild(nParent, p);
				size++;
			}
		}
	}

	private void insertChild(Node nParent, Point2D p) {
		RectHV rect = nParent.rect;
		if (nParent.bDirection == USING_X) {
			if (nParent.x < p.x()) {
				nParent.right = new Node(!nParent.bDirection, nParent, p,
						new RectHV(nParent.x, rect.ymin(), rect.xmax(), rect.ymax()));
			} else {
				nParent.left = new Node(!nParent.bDirection, nParent, p,
						new RectHV(rect.xmin(), rect.ymin(), nParent.x, rect.ymax()));
			}
		} else {
			if (nParent.y < p.y()) {
				nParent.right = new Node(!nParent.bDirection, nParent, p,
						new RectHV(rect.xmin(), nParent.y, rect.xmax(), rect.ymax()));
			} else {
				nParent.left = new Node(!nParent.bDirection, nParent, p,
						new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), nParent.y));
			}
		}
	}

	public boolean contains(Point2D p) // does the set contain point p?
	{
		if (p == null) {
			throw new NullPointerException();
		}
		if (isEmpty()) {
			return false;
		} else {
			Node nParent = findParent(root, null, p);
			if (nParent != null) {
				return false;
			} else {
				return true;
			}
		}
	}

	public void draw() // draw all points to standard draw
	{
		Stack<Node> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node nCur = stack.pop();
			Point2D pCur = nCur.point;
			pCur.draw();
			if (nCur.bDirection == USING_X) {
				StdDraw.setPenColor(Color.RED);
				new Point2D(pCur.x(), nCur.rect.ymin()).drawTo(new Point2D(pCur.x(), nCur.rect.ymax()));
			} else {
				StdDraw.setPenColor(Color.BLUE);
				new Point2D(nCur.rect.xmin(), pCur.y()).drawTo(new Point2D(nCur.rect.xmax(), pCur.y()));
			}
			if (nCur.right != null) {
				stack.push(nCur.right);
			}
			if (nCur.left != null) {
				stack.push(nCur.left);
			}
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new NullPointerException();
		}
		Queue<Point2D> queue = new Queue<Point2D>();
		Stack<Node> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node nCur = stack.pop();
			Point2D pCur = nCur.point;
			if (rect.contains(pCur)) {
				queue.enqueue(pCur);
				if (nCur.right != null) {
					stack.push(nCur.right);
				}
				if (nCur.left != null) {
					stack.push(nCur.left);
				}
			} else {
				if (nCur.bDirection == USING_X) {
					if (nCur.x < rect.xmin() && nCur.right != null) {
						stack.push(nCur.right);
					} else if (nCur.x > rect.xmax() && nCur.left != null) {
						stack.push(nCur.left);
					} else {
						if (nCur.right != null) {
							stack.push(nCur.right);
						}
						if (nCur.left != null) {
							stack.push(nCur.left);
						}
					}
				} else {
					if (nCur.y < rect.ymin() && nCur.right != null) {
						stack.push(nCur.right);
					} else if (nCur.y > rect.ymax() && nCur.left != null) {
						stack.push(nCur.left);
					} else {
						if (nCur.right != null) {
							stack.push(nCur.right);
						}
						if (nCur.left != null) {
							stack.push(nCur.left);
						}
					}
				}
			}
		}
		return queue;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new NullPointerException();
		}
		if (isEmpty()) {
			return null;
		} else {
			Point2D result = null;
			result = nearest(root, p, result);
			return result;
		}
	}

	private Point2D nearest(Node x, Point2D point, Point2D min) {
		if (x != null) {
			if (min == null) {
				min = x.point;
			}
			if (min.distanceSquaredTo(point) >= x.rect.distanceSquaredTo(point)) {
				if (x.point.distanceSquaredTo(point) < min.distanceSquaredTo(point)) {
					min = x.point;
				}
				if (x.right != null && x.right.rect.contains(point)) {
					min = nearest(x.right, point, min);
					min = nearest(x.left, point, min);
				} else {
					min = nearest(x.left, point, min);
					min = nearest(x.right, point, min);
				}
			}
		}

		return min;
	}

	public static void main(String[] args) // unit testing of the methods
											// (optional)
	{
		RectHV rect = new RectHV(0.03, 0.73, 0.14, 0.83);

		String filename = "test/kdtree/circle10.txt";
		In in = new In(filename);

		// initialize the data structures with N points from standard input
		KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
		}

		for (Point2D p : kdtree.range(rect))
			System.out.println(p.toString());
	}
}