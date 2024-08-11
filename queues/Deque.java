/* *****************************************************************************
 *  Name: Magnus
 *  Date: 2024.07.21
 *  Description: deque implementated based on linked-list;
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item Value;
        Node next;
        Node pre;

        public Node() {
            Value = null;
            next = null;
            pre = null;
        }

        // construct function for 3 arguments
        public Node(Item T, Node ppre, Node nnext) {
            Value = T;
            next = nnext;
            pre = ppre;
        }

        // construct function for 2 arguments
        public Node(Node ppre, Node nnext) {
            next = nnext;
            pre = ppre;
        }
    }

    private Node guard;
    private int size;

    // construct an empty deque
    public Deque() {
        size = 0;
        guard = new Node();
        guard.pre = guard;
        guard.next = guard;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node temp = new Node(item, guard, guard.next);
        size++;
        guard.next.pre = temp;
        guard.next = temp;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node temp = new Node(item, guard.pre, guard);
        size++;
        guard.pre.next = temp;
        guard.pre = temp;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0)
            throw new java.util.NoSuchElementException();
        Node temp = guard.next;
        temp.next.pre = guard;
        guard.next = temp.next;
        size--;
        return temp.Value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size() == 0)
            throw new java.util.NoSuchElementException();
        Node temp = guard.pre;
        temp.pre.next = guard;
        guard.pre = temp.pre;
        size--;
        return temp.Value;
    }

    private class dequeIterator implements Iterator<Item> {
        private Node ptr;
        private int remains;

        public dequeIterator() {
            ptr = guard.next;
            remains = size;
        }

        @Override
        public Item next() {
            if (remains == 0)
                throw new NoSuchElementException();
            Item value = ptr.Value;
            ptr = ptr.next;
            remains--;
            return value;
        }

        @Override
        public boolean hasNext() {
            return remains > 0;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new dequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<>();
        for (int i = 0; i < 5; i++) {
            dq.addFirst("A" + i);
        }
        for (int i = 0; i < 5; i++) {
            dq.addLast("B" + i);
        }
        Iterator<String> test = dq.iterator();
        // for (String s : dq) {
        //     System.out.println(s);
        // }
        // System.out.println("dq has " + dq.size() + " elements in total");
        // for (int i = 0; i < 5; i++) {
        //     System.out.println(dq.removeFirst());
        //     System.out.println(dq.removeLast());
        //     System.out.println(dq.size());
        // }
        while (test.hasNext()) {
            System.out.println(test.next());
        }
    }

}
