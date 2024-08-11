import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private int pointnumber;

    private Point[] ps;

    // Used to store the answers of the LineSegments;
    private List<LineSegment> Res = new ArrayList<>();

    private LineSegment[] answer;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        pointnumber = points.length;
        if (points == null)
            throw new IllegalArgumentException();
        for (Point temp : points) {
            if (temp == null)
                throw new IllegalArgumentException();
        }
        for (int i = 0; i < pointnumber; i++) {
            for (int j = i + 1; j < pointnumber; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        if (pointnumber < 4)
            return;
        ps = Arrays.copyOf(points, pointnumber);
        Arrays.sort(ps);
        Point[] test = Arrays.copyOf(ps, pointnumber);
        for (Point point : ps) {
            Comparator<Point> compare = point.slopeOrder();
            // We do the traversal;
            Arrays.sort(test, compare);
            for (int i = 1; i < pointnumber; ) {
                int j = i + 1;
                while (j < pointnumber && point.slopeTo(test[i]) == point.slopeTo(test[j])) {
                    j++;
                }
                // Here j's location represents the final
                if (j - i >= 3 && test[0].compareTo(test[min_point(test, i, j - 1)]) < 0) {
                    Res.add(new LineSegment(test[0], test[max_point(test, i, j - 1)]));
                }
                if (j == pointnumber)
                    break;
                i = j;
            }
        }

    }

    /**
     * @param array The array that we tried to get in the points set;
     * @param low   The leftest position we have got;
     * @param high  The rightest position we have got;
     * @return The min_points index;
     */
    private int min_point(Point[] array, int low, int high) {
        Point min_p = array[low];
        int min_position = low;
        for (int i = low + 1; i <= high; i++) {
            if (min_p.compareTo(array[i]) > 0) {
                min_p = array[i];
                min_position = i;
            }
        }
        return min_position;

    }

    private int max_point(Point[] array, int low, int high) {
        Point max_p = array[low];
        int max_position = low;
        for (int i = low + 1; i <= high; i++) {
            if (max_p.compareTo(array[i]) < 0) {
                max_p = array[i];
                max_position = i;
            }
        }
        return max_position;
    }

    // the number of line segments
    public int numberOfSegments() {
        return Res.size();
    }

    // the line segments
    public LineSegment[] segments() {
        answer = new LineSegment[Res.size()];
        int number = 0;
        for (LineSegment line : Res) {
            answer[number++] = line;
        }
        return answer;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
