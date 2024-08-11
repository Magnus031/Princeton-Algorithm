/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Iterator;

public class Board {

    private int gridwidth;

    private int[][] Grids;
    // To check whether it is the goal board;
    private boolean isGoal;
    private int hamming;
    private int manhattan;
    // used to record the position of the '0';
    private int pos;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        gridwidth = tiles.length;
        Grids = new int[gridwidth][gridwidth];
        for (int i = 0; i < gridwidth; i++) {
            for (int j = 0; j < gridwidth; j++) {
                Grids[i][j] = tiles[i][j];
                if (tiles[i][j] == 0)
                    pos = i * gridwidth + j + 1;
            }
        }
        isGoal = false;
        hamming = 0;
        manhattan = 0;
    }

    // string representation of this board
    public String toString() {
        StringBuilder ans = new StringBuilder();
        ans.append(Integer.toString(gridwidth));
        ans.append('\n');
        for (int i = 0; i < gridwidth; i++) {
            StringBuilder temp = new StringBuilder();
            for (int j = 0; j < gridwidth; j++) {
                StringBuilder tmp = new StringBuilder();
                tmp.append(' ');
                tmp.append(Integer.toString(Grids[i][j]));
                tmp.append(' ');
                temp.append(tmp.toString());
            }
            temp.append('\n');
            ans.append(temp);
        }
        return ans.toString();
    }

    // board dimension n
    public int dimension() {
        return gridwidth;
    }

    /**
     * @param row
     * @param col
     * @return The index of this position in the goal board;
     * @Example (0, 0) -> index = 1;  if the index is in the (grid-1,grid-1) the return index should
     * be 0;Exactly the blank space.
     */
    private int calIndex(int row, int col) {
        int index;
        index = row * gridwidth + col + 1;
        return index == gridwidth * gridwidth ? 0 : index;
    }

    // number of tiles out of place
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < gridwidth; i++) {
            for (int j = 0; j < gridwidth; j++) {
                int index = calIndex(i, j);
                if (Grids[i][j] != 0 && Grids[i][j] != index)
                    sum++;
            }
        }
        hamming = sum;
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < gridwidth; i++) {
            for (int j = 0; j < gridwidth; j++) {
                int goal = calIndex(i, j);
                if (Grids[i][j] != 0 && Grids[i][j] != goal) {
                    int row = (Grids[i][j] - 1) / gridwidth;
                    int col = (Grids[i][j] - 1) % gridwidth;
                    distance += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        manhattan = distance;
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < gridwidth; i++) {
            for (int j = 0; j < gridwidth; j++) {
                if (Grids[i][j] != calIndex(i, j))
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (this == y)
            return true;
        if (!(y instanceof Board))
            return false;
        Board that = (Board) y;
        if (that.gridwidth != this.gridwidth)
            return false;
        for (int i = 0; i < gridwidth; i++) {
            for (int j = 0; j < gridwidth; j++) {
                if (this.Grids[i][j] != that.Grids[i][j])
                    return false;
            }
        }
        return true;
    }

    private class Neighboor implements Iterator<Board> {
        private int n;
        // used to record the number of the neighboor;
        private int number;
        private Board[] nb;

        private int[] dy = { -1, 0, 1, 0 };
        private int[] dx = { 0, 1, 0, -1 };

        private int ptr;

        public Neighboor() {
            n = gridwidth;
            number = 0;
            int row = (pos - 1) / n;
            int col = (pos - 1) % n;
            nb = new Board[4];
            for (int i = 0; i < 4; i++) {
                if (inBound(row + dx[i], col + dy[i])) {
                    int[][] newGrid = copyofBoard(Grids);
                    int temp = newGrid[row][col];
                    newGrid[row][col] = newGrid[row + dx[i]][col + dy[i]];
                    newGrid[row + dx[i]][col + dy[i]] = temp;
                    nb[number] = new Board(newGrid);
                    number++;
                }
            }
            ptr = 0;
        }

        @Override
        public Board next() {
            return nb[ptr++];
        }

        @Override
        public boolean hasNext() {
            return ptr != number;
        }

        private boolean inBound(int row, int col) {
            return (row >= 0 && row <= gridwidth - 1) && (col >= 0 && col <= gridwidth - 1);
        }

        private int[][] copyofBoard(int[][] initial) {
            int m = initial.length;
            int[][] test = new int[m][m];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    test[i][j] = initial[i][j];
                }
            }
            return test;
        }

    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Neighboor();
            }
        };

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] temp = new int[gridwidth][gridwidth];
        for (int i = 0; i < gridwidth; i++) {
            for (int j = 0; j < gridwidth; j++) {
                temp[i][j] = Grids[i][j];
            }
        }
        for (int i = 0; i < gridwidth; i++) {
            for (int j = 0; j < gridwidth - 1; j++) {
                if (temp[i][j + 1] != 0 && temp[i][j] != 0) {
                    int tmp = temp[i][j + 1];
                    temp[i][j + 1] = temp[i][j];
                    temp[i][j] = tmp;
                    return new Board(temp);

                }

            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] Grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Grid[i][j] = in.readInt();
            }
        }
        // constructor
        Board test = new Board(Grid);
        // System.out.print(test.toString());
        // System.out.println(test.hamming());
        // System.out.println(test.manhattan());
        Iterator<Board> T = test.neighbors().iterator();
        while (T.hasNext()) {
            Board B1 = T.next();
            Iterator<Board> T1 = B1.neighbors().iterator();
            while (T1.hasNext()) {
                System.out.println(T1.next().toString());
            }
            Board B2 = T.next();
            Iterator<Board> T2 = B2.neighbors().iterator();
            while (T2.hasNext()) {
                System.out.println(T2.next().toString());
            }
        }

    }
}
