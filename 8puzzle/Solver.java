/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private boolean canSolve;
    private int moves;

    private Stack<Board> solutions = new Stack<>();

    private class State {
        Board board;
        int move;
        // int ham_priority;
        int man_priority;

        State parent;

        public State(State P, Board temp, int move_last) {
            parent = P;
            move = move_last + 1;
            board = temp;
            // ham_priority = temp.hamming() + move;
            man_priority = temp.manhattan() + move;
        }

        public State(Board temp) {
            parent = null;
            move = 0;
            board = temp;
            // ham_priority = temp.hamming();
            man_priority = temp.manhattan();
        }

        public int getMan_priority() {
            return man_priority;
        }

        // public int getHam_priority() {
        //  return ham_priority;
        //}

        // public int getMove() {
        //     return move;
        // }

    }

    private class Statecompare implements Comparator<State> {
        public int compare(State a, State b) {
            int a_1 = a.getMan_priority();
            int b_1 = b.getMan_priority();
            if (a_1 < b_1)
                return -1;
            else if (a_1 == b_1) {
                if (a.board.manhattan() < b.board.manhattan())
                    return -1;
                else if (a.board.manhattan() > b.board.manhattan())
                    return 1;
                return 0;
            }
            else
                return 1;
        }
    }

    private void MakeSolution(State Node) {
        moves = Node.move;
        while (Node != null) {
            solutions.push(Node.board);
            Node = Node.parent;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        // initial construct function
        State state_test = new State(initial);
        Board init_twin = initial.twin();
        State state_test_twin = new State(init_twin);
        canSolve = false;
        MinPQ<State> T = new MinPQ<>(new Statecompare());
        MinPQ<State> T_twin = new MinPQ<>(new Statecompare());
        T.insert(state_test);
        T_twin.insert(state_test_twin);
        while (!T.isEmpty()) {
            state_test_twin = T_twin.delMin();
            if (state_test_twin.board.isGoal()) {
                canSolve = false;
                return;
            }
            state_test = T.delMin();
            if (state_test.board.isGoal()) {
                MakeSolution(state_test);
                canSolve = true;
                return;
            }
            else {
                State child;
                State childtwin;
                // 用来检验是否这一轮的加入进入死循环了;
                boolean guard_child = true;
                boolean guard_child_twin = true;
                for (Board b : state_test.board.neighbors()) {
                    if (guard_child) {
                        // The case that the next step is the same as the previous
                        if (state_test.parent != null && b.equals(state_test.parent.board)) {
                            guard_child = false;
                        }
                        else {
                            child = new State(state_test, b, state_test.move);
                            T.insert(child);
                        }
                    }
                    else {
                        child = new State(state_test, b, state_test.move);
                        T.insert(child);
                    }
                }
                // 同步进行twin的操作;
                for (Board b : state_test_twin.board.neighbors()) {
                    if (guard_child_twin) {
                        // The case that the next step is the same as the previous
                        if (state_test_twin.parent != null && b.equals(
                                state_test_twin.parent.board)) {
                            guard_child_twin = false;
                        }
                        else {
                            childtwin = new State(state_test_twin, b, state_test_twin.move);
                            T_twin.insert(childtwin);
                        }
                    }
                    else {
                        childtwin = new State(state_test_twin, b, state_test_twin.move);
                        T_twin.insert(childtwin);
                    }
                }

            }


        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return canSolve;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (canSolve)
            return moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solutions;
        }
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        System.out.println(solver.moves);
        System.out.println(solver.canSolve);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
