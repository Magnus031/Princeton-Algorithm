/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static int R = 256;
    private static LinkedList<Character> alphabet;

    private static void initialize() {
        alphabet = new LinkedList<>();
        for (char i = 0; i < R; i++) {
            alphabet.add(i);
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initialize();
        String s = BinaryStdIn.readString();
        char[] put = s.toCharArray();
        for (int i = 0; i < put.length; i++) {
            int index = alphabet.indexOf(put[i]);
            BinaryStdOut.write(index, 8);
            char temp = alphabet.remove(index);
            alphabet.add(0, temp);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initialize();
        String s = BinaryStdIn.readString();
        char[] put = s.toCharArray();
        for (int i = 0; i < put.length; i++) {
            BinaryStdOut.write(put[i], 8);
            char x = alphabet.remove(put[i]);
            alphabet.add(0, x);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException();
    }
}
