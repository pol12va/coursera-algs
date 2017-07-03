import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    public Deque() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("can't add null");
        }

        Node oldFirst = first;
        Node newFirst = new Node(item);

        newFirst.next = oldFirst;
        first = newFirst;

        if (oldFirst == null) {
            last = first;
        } else {
            oldFirst.prev = first;
        }

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("can't add null");
        }

        Node oldLast = last;
        Node newLast = new Node(item);

        newLast.prev = oldLast;
        last = newLast;

        if (oldLast != null) {
            oldLast.next = newLast;
        } else {
            first = last;
        }

        size++;
    }

    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        Item removed = first.val;
        first = first.next;
        if (first != null) {
            first.prev = null;
        } else {
            last = null;
        }

        size--;

        return removed;
    }

    public Item removeLast() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        Item removed = last.val;
        last = last.prev;
        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }

        size--;

        return removed;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node {
        private final Item val;
        private Node next;
        private Node prev;

        public Node(Item val) {
            this.val = val;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }

            Item i = current.val;
            current = current.next;
            return i;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();

        deque.addFirst("aaa");
        deque.addFirst("bbb");
        deque.addLast("cccc");
        deque.addFirst("ffff");
        deque.removeLast();
        deque.removeLast();
        deque.addLast("eee");
        deque.removeFirst();


        for (String s : deque) {
            System.out.println(s);
        }
    }
}
