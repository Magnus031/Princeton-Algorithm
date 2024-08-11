/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private class Node {
        // 0 represents horizontal
        // 1 represents vertical
        private int axis;
        private Node left;
        private Node right;
        private Point2D point;
        private int size;

        // Represents the rectangle which the point stays;
        private RectHV rect;


        public Node(int axis, Point2D p, int size, RectHV r) {
            this.axis = axis;
            point = p;
            this.size = size;
            rect = r;
        }


        /**
         * @return if the point's axis is 0->Horizontal that it's left rectangle represents the down rect;
         * @return if the point's axis is 1->Vertical that it's left rectangel represents the left rect;
         */
        public RectHV LeftRect() {
            if (axis == 0) {// Horizontal
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            }
            else {
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            }
        }

        public RectHV RightRect() {
            if (axis == 0) {
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
            else {
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
        }

        public int getSize() {
            return size;
        }

        public int getAxis() {
            return axis;
        }
    }

    private Node root;
    private double distance;
    private Point2D nearestpoint;

    public KdTree() {
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return size() == 0;
    }                      // is the set empty?

    private int size(Node node) {
        if (node == null)
            return 0;
        else
            return node.size;
    }

    public int size() {
        return size(root);
    }                         // number of points in the set

    private Node InsertHelper(Point2D p, Node r, boolean isleft, Node parent) {
        // 初始化！
        if (r == null) {
            if (parent == null) {
                return new Node(1, p, 1, new RectHV(0, 0, 1, 1));
            }
            else {
                if (isleft) {
                    return new Node(1 - parent.axis, p, 1, parent.LeftRect());
                }
                else {
                    return new Node(1 - parent.axis, p, 1, parent.RightRect());
                }
            }
        }
        else if (r.point.equals(p)) {
            return r;
        }
        else if (r.axis == 0) {
            // The case that Horizontal;
            if (r.point.y() < p.y()) {
                r.right = InsertHelper(p, r.right, false, r);
            }
            else {
                r.left = InsertHelper(p, r.left, true, r);
            }
        }
        else {
            if (r.point.x() < p.x()) {
                r.right = InsertHelper(p, r.right, false, r);
            }
            else {
                r.left = InsertHelper(p, r.left, true, r);
            }
        }
        // Update the size of the node;
        r.size = size(r.left) + size(r.right) + 1;
        return r;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = InsertHelper(p, root, true, null);
    }              // add the point to the set (if it is not already in the set)

    private boolean FindPoint(Node r, Point2D p) {
        if (r == null)
            return false;
        if (r.point.equals(p))
            return true;
        if (r.axis == 0) {
            if (r.point.y() < p.y()) {
                return FindPoint(r.right, p);
            }
            else {
                return FindPoint(r.left, p);
            }
        }
        else {
            if (r.point.x() < p.x()) {
                return FindPoint(r.right, p);
            }
            else {
                return FindPoint(r.left, p);
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return FindPoint(root, p);
    }            // does the set contain point p?

    private void drawHelper(Node r, Node parent) {
        if (r == null)
            return;
        if (parent == null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(r.point.x(), 0, r.point.x(), 1);
        }
        else if (r.axis == 0) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(r.rect.xmin(), r.point.y(), r.rect.xmax(), r.point.y());
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(r.point.x(), r.rect.ymin(), r.point.x(), r.rect.ymax());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(r.point.x(), r.point.y());
        drawHelper(r.left, r);
        drawHelper(r.right, r);
    }

    public void draw() {
        drawHelper(root, null);
    }                         // draw all points to standard draw


    private void rangeHelper(Node r, RectHV rect, List<Point2D> L) {
        if (r == null)
            return;
        if (rect.contains(r.point)) {
            L.add(r.point);
        }
        if (r.axis == 0) {
            if (r.point.y() > rect.ymax()) {
                rangeHelper(r.left, rect, L);
            }
            else if (r.point.y() < rect.ymin()) {
                rangeHelper(r.right, rect, L);
            }
            else {
                rangeHelper(r.left, rect, L);
                rangeHelper(r.right, rect, L);
            }
        }
        else {
            if (r.point.x() > rect.xmax()) {
                rangeHelper(r.left, rect, L);
            }
            else if (r.point.x() < rect.xmin()) {
                rangeHelper(r.right, rect, L);
            }
            else {
                rangeHelper(r.left, rect, L);
                rangeHelper(r.right, rect, L);
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> rangepoints = new ArrayList<>();
        rangeHelper(root, rect, rangepoints);
        return rangepoints;
    }

    private void nearHelper(Node r, Point2D p) {
        if (r == null)
            return;
        if (r.point.distanceSquaredTo(p) < distance) {
            distance = r.point.distanceSquaredTo(p);
            nearestpoint = r.point;
        }
        // 接下来我们要看 Node r的两边是否有比r距离p更近的点，因为考虑到圆的切线/割线
        boolean leftSearch = false, rightSearch = false;
        if (r.left != null && r.left.rect.distanceSquaredTo(p) < distance)
            leftSearch = true;
        if (r.right != null && r.right.rect.distanceSquaredTo(p) < distance)
            rightSearch = true;
        if (leftSearch && rightSearch) {
            if (r.axis == 0) {
                if (r.point.y() < p.y()) {
                    nearHelper(r.right, p);
                    if (r.left.rect.distanceSquaredTo(p) < distance) {
                        nearHelper(r.left, p);
                    }
                }
                else {
                    // 先就近搜 然后再就远搜；
                    nearHelper(r.left, p);
                    if (r.right.rect.distanceSquaredTo(p) < distance)
                        nearHelper(r.right, p);
                }
            }
            else {
                if (r.point.x() < p.x()) {
                    nearHelper(r.right, p);
                    if (r.left.rect.distanceSquaredTo(p) < distance)
                        nearHelper(r.left, p);
                }
                else {
                    nearHelper(r.left, p);
                    if (r.right.rect.distanceSquaredTo(p) < distance)
                        nearHelper(r.right, p);
                }
            }
        }
        else {
            if (leftSearch)
                nearHelper(r.left, p);
            if (rightSearch)
                nearHelper(r.right, p);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        distance = Double.MAX_VALUE;
        nearestpoint = null;
        nearHelper(root, p);
        return nearestpoint;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.7, 0.2));
        System.out.println(kdtree.size());
        System.out.println(kdtree.contains(new Point2D(0.7, 0.2)));

        kdtree.insert(new Point2D(0.5, 0.4));
        System.out.println(kdtree.size());
        System.out.println(kdtree.contains(new Point2D(0.5, 0.4)));

        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        kdtree.insert(new Point2D(0.9, 0.6));
        kdtree.draw();
    }
}
