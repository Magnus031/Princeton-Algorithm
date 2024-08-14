/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.LinkedList;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output

    public static void transform() {
        // String s = BinaryStdIn.readString();
        String s = BinaryStdIn.readString();
        CircularSuffixArray CS = new CircularSuffixArray(s);
        int first = -1;
        for (int i = 0; i < s.length(); i++) {
            if (CS.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < s.length(); i++) {
            char x = s.charAt((CS.index(i) + 11) % 12);
            BinaryStdOut.write(x, 8);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        LinkedList<Character> list = new LinkedList<>();
        while (!BinaryStdIn.isEmpty()) {
            list.add(BinaryStdIn.readChar());
        }
        char[] input = new char[list.size()];
        int[] next = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            input[i] = list.get(i);
        }
        Arrays.sort(input);
        // Now we decide to construct the next-array;
        for (int i = 0; i < list.size(); ) {
            int ptr = i + 1;
            while (ptr < list.size() && input[i] == input[ptr])
                ptr++;
            for (int k = 0; k < list.size(); k++) {
                if (list.get(k) == input[i]) {
                    next[i++] = k;
                    if (i == ptr)
                        break;
                }
            }
        }
        int point = first;
        for (int i = 0; i < list.size(); i++) {
            BinaryStdOut.write(input[next[point]]);
            point = next[point];
        }
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
