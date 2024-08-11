/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] que;
    private int capacity;
    private int original = 8;
    private double ratio = 0.25;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        que = (Item[]) new Object[original];
        capacity = original;

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int newsize) {
        Item[] temp = (Item[]) new Object[newsize];
        for (int i = 0; i < size; i++) {
            temp[i] = que[i];
        }
        que = temp;
        capacity = newsize;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size == capacity) {
            resize(size * 2);
        }
        que[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        int idx = StdRandom.uniformInt(0, size);
        Item value = que[idx];
        que[idx] = que[size - 1];
        que[size - 1] = null;
        size--;
        // Reform the RandomizedQueue;
        if (size > 0 && (double) size / capacity <= ratio) {
            resize(capacity / 2);
        }
        return value;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int idx = StdRandom.uniformInt(0, size);
        return que[idx];
    }

    private class Deq implements Iterator<Item> {
        private Item test[];
        private int size_i;

        public Deq() {
            test = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                test[i] = que[i];
            }
            // rerange
            StdRandom.shuffle(test);
            size_i = size;
        }

        @Override
        public Item next() {
            if (size_i == 0)
                throw new NoSuchElementException();
            return test[--size_i];
        }

        @Override
        public boolean hasNext() {
            return size_i != 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Deq();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        for (int i = 0; i < 18; i++) {
            rq.enqueue("A" + i);
        }
        System.out.println("first iterator");
        Iterator<String> test1 = rq.iterator();
        Iterator<String> test2 = rq.iterator();
        // for (String s : rq) {
        //     //System.out.print(s + " ");
        // }
        // System.out.println();
        while (test1.hasNext()) {
            String temp = test1.next();
            System.out.print(temp + " ");
        }
        System.out.println();
        System.out.println("second iterator ");
        while (test2.hasNext()) {
            String temp = test2.next();
            System.out.print(temp + " ");
        }
        // for (String s : rq) {
        //   //  System.out.print(s + " ");
        // }
        // System.out.println();
        // for (int i = 0; i < 18; i++) {
        //     System.out.print("deque ");
        //     System.out.print(rq.dequeue());
        //     System.out.println(". remain " + rq.size() + " elements. now capacity ");
        // }
    }

}
