/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> pointset;

    public PointSET() {
        pointset = new SET<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return pointset.isEmpty();
    }                      // is the set empty?

    public int size() {
        return pointset.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Sorry the Point is null");
        pointset.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return pointset.contains(p);
    }            // does the set contain point p?

    public void draw() {
        for (Point2D point : pointset) {
            point.draw();
        }
    }                         // draw all points to standard draw

    // We should know that Iterable we should write a iterable data type 容器;
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException(
                    "The rectangle is null.Please input the argument again!");
        List<Point2D> containPoints = new ArrayList<>();
        for (Point2D point : pointset) {
            if (rect.contains(point)) {
                containPoints.add(point);
            }
        }
        return containPoints;
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        double min_distance = 10000;
        Point2D minP = null;
        for (Point2D point : pointset) {
            if (point.distanceTo(p) < min_distance) {
                min_distance = point.distanceTo(p);
                minP = point;
            }
        }
        return minP;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        
    }
}
