/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private String[] Dictionary;
    // Used to record the trie;
    private Node root;
    private int m;
    private int n;

    private boolean[][] visited;

    private Set<String> validSet;

    private int[] dx = { -1, -1, -1, 0, 1, 1, 1, 0 };
    private int[] dy = { -1, 0, 1, 1, 1, 0, -1, -1 };

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary) {
            put(s);
        }
    }

    // Used to construct the trie;
    private static class Node {
        private boolean exist;

        private Node[] next = new Node[26];
    }

    private void put(String s) {
        root = put(root, s, 0);
    }

    private Node put(Node node, String s, int d) {
        if (node == null) {
            node = new Node();
        }
        if (d == s.length()) {
            node.exist = true;
            return node;
        }
        if (d > s.length())
            return node;
        char c = s.charAt(d);
        // iterative for the new node;
        node.next[c - 'A'] = put(node.next[c - 'A'], s, d + 1);
        return node;
    }

    private boolean get(String s) {
        return get(root, s, 0);
    }

    private boolean get(Node node, String s, int d) {
        if (node == null)
            return false;
        if (d == s.length())
            return node.exist;
        char c = s.charAt(d);
        return get(node.next[c - 'A'], s, d + 1);
    }


    // Check for whether the point is in the board;
    private boolean isInBound(int row, int col) {
        if (row < 0 || row >= m)
            return false;
        if (col < 0 || col >= n)
            return false;
        return true;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new IllegalArgumentException();
        validSet = new HashSet<>();
        n = board.cols();
        m = board.rows();
        // Here we use (m,n) represents the point;
        if (root == null)
            return validSet;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (root.next[board.getLetter(i, j) - 'A'] != null) {
                    visited = new boolean[m][n];
                    dfs(board, root.next[board.getLetter(i, j) - 'A'], i, j, "");
                }
            }
        }
        return validSet;
    }

    private void dfs(BoggleBoard board, Node node, int row, int col, String word) {
        visited[row][col] = true;
        char c = board.getLetter(row, col);
        String curword = word + c;
        if (c == 'Q') {
            curword += 'U';
            node = node.next['U' - 'A'];
        }
        if (node == null) {
            visited[row][col] = false;
            return;
        }
        if (curword.length() >= 3 && node.exist)
            validSet.add(curword);
        for (int i = 0; i < 8; i++) {
            int newr = row + dx[i];
            int newc = col + dy[i];
            if (!isInBound(newr, newc)) continue;
            char newchar = board.getLetter(newr, newc);
            if (node.next[newchar - 'A'] != null && !visited[newr][newc])
                dfs(board, node.next[newchar - 'A'], newr, newc, curword);
        }
        visited[row][col] = false;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!get(word)) return 0;
        int n = word.length();
        if (n < 3)
            return 0;
        else if (n <= 6)
            return n == 3 ? 1 : n - 3;
        else if (n == 7)
            return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
