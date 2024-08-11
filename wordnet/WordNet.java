/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private Digraph G;
    // Used to count the number of the words
    private int number;
    // constructor takes the name of the two input files

    // The first thing : 我们需要的上位词的映射关系;
    private Map<Integer, Set<Integer>> wordmap;
    // The second thing : 我们需要存放的是一个Map,其实就是每个单词：所属于的类
    private Map<String, Set<Integer>> nounset;

    private SAP sapCounter;
    // The third thing : 我们需要的是一个Linked-list 用于存放每个组的单词;
    private List<Set<String>> ll;

    private List<String> orisysnet;

    public WordNet(String synsets, String hypernyms) {
        // Check for arguments
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        int number1 = countLines(synsets);
        int number2 = countLines(hypernyms);
        if (number1 != number2)
            throw new IllegalArgumentException();
        else
            number = number1;
        G = new Digraph(number);
        wordmap = new HashMap<>();
        orisysnet = new ArrayList<>();
        wordmap = readTxt(hypernyms);
        nounset = new HashMap<>();
        ll = new ArrayList<>();
        readTxt_wordset(synsets, nounset);
        // initialize;
        for (int i = 0; i < number; i++) {
            Set<Integer> temp = wordmap.get(i);
            for (Integer a : temp) {
                G.addEdge(i, a);
            }
        }
        hasCircle();
        hasRoot();
        sapCounter = new SAP(G);

    }

    private static int countLines(String filePath) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                count++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    private static Map<Integer, Set<Integer>> readTxt(String filePath) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String temp;
            while ((temp = reader.readLine()) != null) {
                String[] parts = temp.split(",");
                Integer index = Integer.parseInt(parts[0]);
                Set<Integer> values = map.getOrDefault(index, new HashSet<>());
                for (int i = 1; i < parts.length; i++) {
                    values.add(Integer.parseInt(parts[i]));
                }
                map.put(index, values);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void readTxt_wordset(String filePath, Map<String, Set<Integer>> wordset) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String temp;
            while ((temp = reader.readLine()) != null) {
                String[] parts = temp.split(",");
                Set<String> tset = new HashSet<>();
                orisysnet.add(parts[1]);
                for (String s : parts[1].split(" ")) {
                    tset.add(s);
                    if (!wordset.containsKey(s)) {
                        Set<Integer> tmp = new HashSet<>();
                        tmp.add(Integer.parseInt(parts[0]));
                        wordset.put(s, tmp);
                    }
                    else {
                        wordset.get(s).add(Integer.parseInt(parts[0]));
                    }
                }
                ll.add(tset);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hasCircle() {
        DirectedCycle G_cicle = new DirectedCycle(G);
        if (G_cicle.hasCycle())
            throw new IllegalArgumentException("There has a circle in the Graph");
    }

    private void hasRoot() {
        int roots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                roots++;
                if (roots >= 2)
                    throw new IllegalArgumentException("There has more than 1 root");
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounset.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return nounset.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sapCounter.length(nounset.get(nounA), nounset.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return orisysnet.get(sapCounter.ancestor(nounset.get(nounA), nounset.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
