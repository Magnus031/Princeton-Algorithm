/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.Arrays;

public class ShortPath {
    private double E[][];
    private int width;
    private int height;

    private int path[];

    private double totalEnergy;

    static class Node implements Comparable<Node> {
        private int x;
        private int y;
        private double energy;

        public Node(int c, int r, double e) {
            this.x = c;
            this.y = r;
            energy = e;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.energy, other.energy);
        }
    }

    public ShortPath(double[][] Energy, int s) {
        if (Energy == null)
            throw new IllegalArgumentException();
        width = Energy.length;
        height = Energy[0].length;
        if (s < 0 || s >= width)
            throw new IllegalArgumentException();
        E = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                E[i][j] = Energy[i][j];
        double[][] dist = new double[width][height];
        // Initial
        for (double[] row : dist) {
            Arrays.fill(row, Double.MAX_VALUE);
        }
        dist[s][0] = E[s][0];
        Node[][] prev = new Node[width][height];

        MinPQ<Node> pq = new MinPQ<>();
        pq.insert(new Node(s, 0, E[s][0]));
        int[][] directions = { { -1, 1 }, { 0, 1 }, { 1, 1 } };
        while (!pq.isEmpty()) {
            Node temp = pq.delMin();
            for (int[] dir : directions) {
                int newx = temp.x + dir[0];
                int newy = temp.y + dir[1];
                if (newx >= 0 && newx <= width - 1 && newy >= 0 && newy <= height - 1) {
                    double newE = dist[temp.x][temp.y] + E[newx][newy];
                    if (newE < dist[newx][newy]) {
                        dist[newx][newy] = newE;
                        pq.insert(new Node(newx, newy, newE));
                        prev[newx][newy] = temp;
                    }
                }
            }
        }
        double minE = Double.MAX_VALUE;
        int minx = -1;
        for (int i = 0; i < width; i++) {
            if (dist[i][height - 1] < minE) {
                minE = dist[i][height - 1];
                minx = i;
            }
        }
        totalEnergy = minE;
        path = new int[height];
        Node current = prev[minx][height - 1];
        path[height - 1] = minx;
        for (int i = height - 2; i >= 0; i--) {
            path[i] = current.x;
            current = prev[current.x][current.y];
        }
    }

    public int[] getPath() {
        return path;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public static void main(String[] args) {
        double temp[][] = {
                { 1000, 1000, 1000, 1000, 1000, 1000 },
                { 1000, 237.35, 151.02, 234.09, 107.89, 1000 },
                { 1000, 138.69, 228.10, 133.07, 211.51, 1000 },
                { 1000, 153.88, 174.01, 284.01, 194.50, 1000 },
                { 1000, 1000, 1000, 1000, 1000, 1000 }
        };
        double[][] test = new double[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                test[i][j] = temp[j][i];
            }
        }
        ShortPath sp = new ShortPath(test, 3);
        int[] P = new int[5];
        P = sp.getPath();
        for (int i = 0; i < 5; i++) {
            System.out.println(P[i]);
        }
    }
}
