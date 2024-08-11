/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class SAP {

    private Digraph DG;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        DG = new Digraph(G);

    }

    private boolean checkinbound(int a) {
        return a >= 0 && a < DG.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!checkinbound(v) || !checkinbound(w))
            throw new IllegalArgumentException();
        int V = DG.V();
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(DG, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(DG, w);
        int minpath = Integer.MAX_VALUE;
        for (int i = 0; i < V; i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                if (bfsv.distTo(i) + bfsw.distTo(i) < minpath) {
                    minpath = bfsv.distTo(i) + bfsw.distTo(i);
                }
            }
        }
        return minpath == Integer.MAX_VALUE ? -1 : minpath;
    }


    /**
     * @param v
     * @param w
     * @return return the clostest ancestor in the path during BFS
     */
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!checkinbound(v) || !checkinbound(w))
            throw new IllegalArgumentException();
        // Used to record all the paths from v/w in the DirectedGraph;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(DG, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(DG, w);
        int minPath = DG.V();
        int n = DG.V();
        int commonAncestor = -1;
        for (int i = 0; i < n; i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int temp = bfsv.distTo(i) + bfsw.distTo(i);
                if (temp < minPath) {
                    minPath = temp;
                    commonAncestor = i;
                }
            }
        }
        return commonAncestor;
    }

    private boolean hasValue(Iterable<Integer> a) {
        Iterator<Integer> temp = a.iterator();
        if (temp.hasNext())
            return true;
        return false;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        if (!hasValue(v) || !hasValue(w))
            return -1;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(DG, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(DG, w);
        int minPath = Integer.MAX_VALUE;
        int number = DG.V();
        for (int i = 0; i < number; i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                minPath = Math.min(bfsv.distTo(i) + bfsw.distTo(i), minPath);
            }
        }
        return minPath == Integer.MAX_VALUE ? -1 : minPath;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        if (!hasValue(v) || !hasValue(w))
            return -1;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(DG, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(DG, w);
        int minPath = Integer.MAX_VALUE;
        int common = -1;
        int number = DG.V();
        for (int i = 0; i < number; i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                minPath = Math.min(bfsv.distTo(i) + bfsw.distTo(i), minPath);
                common = i;
            }
        }
        return common;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
