/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wn;

    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException();
        wn = wordnet;
    }         // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        if (nouns == null)
            throw new IllegalArgumentException();
        int[] distance = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                if (i == j)
                    continue;
                distance[i] += wn.distance(nouns[i], nouns[j]);
            }
        }
        int index = 0;
        int maxminum = distance[0];
        for (int i = 1; i < nouns.length; i++) {
            if (distance[i] > maxminum) {
                maxminum = distance[i];
                index = i;
            }
        }
        return nouns[index];
    }   // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
