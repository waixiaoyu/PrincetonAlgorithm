package edu.princeton.kdtree;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.Iterator;
import java.util.TreeSet;

public class PointSET {

	private TreeSet<Point2D> set;

	public PointSET() // construct an empty set of points
	{
		set = new TreeSet<Point2D>();
	}

	public boolean isEmpty() // is the set empty?
	{
		return set.isEmpty();
	}

	public int size() // number of points in the set
	{
		return set.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new NullPointerException();
		}
		if (!set.contains(p)) {
			set.add(p);
		}
	}

	public boolean contains(Point2D p) // does the set contain point p?
	{
		if (p == null) {
			throw new NullPointerException();
		}
		return set.contains(p);
	}

	public void draw() // draw all points to standard draw
	{
		Iterator<Point2D> it = set.iterator();
		while (it.hasNext()) {
			Point2D p2d = (Point2D) it.next();
			p2d.draw();
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new NullPointerException();
		}
		Queue<Point2D> queue = new Queue<Point2D>();
		Iterator<Point2D> it = set.iterator();
		while (it.hasNext()) {
			Point2D p2d = (Point2D) it.next();
			if (rect.contains(p2d)) {
				queue.enqueue(p2d);
			}
		}
		return queue;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new NullPointerException();
		}
		Point2D minPoint = null;
		for (Point2D point2d : set) {
			if (minPoint == null || point2d.distanceSquaredTo(p) < minPoint.distanceSquaredTo(p)) {
				minPoint = point2d;
			}
		}
		return minPoint;
	}

	public static void main(String[] args) // unit testing of the methods
											// (optional)
	{
	}
}