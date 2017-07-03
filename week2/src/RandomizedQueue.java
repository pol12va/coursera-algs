import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int size;
    private int first;
    private int last;

    public RandomizedQueue() {
        q = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item i) {
        if (i == null) {
            throw new IllegalArgumentException();
        }

        if (size == q.length || last + 1 == q.length) {
            resize(size  * 2);
        }

        if (size == 0) {
            q[last] = i;
        } else {
            q[++last] = i;
        }

        if (size > 1) {
            StdRandom.shuffle(q, first, last);
        }

        size++;
    }

    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        if (size == q.length / 4) {
            resize(q.length / 2);
        }

        Item i = q[first];
        size--;
        if (size == 0) {
            first = 0;
            last = 0;
        } else {
            q[first++] = null;
        }

        if (size > 1) {
            StdRandom.shuffle(q, first, last);
        }

        return i;
    }

    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return q[StdRandom.uniform(first, last + 1)];
    }

    @Override
    public Iterator<Item> iterator() {
        Item[] iterable = copyQueue(size);
        StdRandom.shuffle(iterable);
        return new RandomizedQueueIterator(iterable);
    }

    private void resize(int newSize) {
        Item[] nq = (Item[]) new Object[newSize];
        int i = 0;

        for (Item item : q) {
            if (item != null) {
                nq[i++] = item;
            }
        }

        first = 0;
        last = i - 1;

        q = nq;
    }

    private Item[] copyQueue(int newSize) {
        Item[] nq = (Item[]) new Object[newSize];
        int i = 0;

        for (Item item : q) {
            if (item != null) {
                nq[i++] = item;
            }
        }

        return nq;
    }

    private class RandomizedQueueIterator implements Iterator {

        private int cur;
        private final Item[] items;

        public RandomizedQueueIterator(Item[] items) {
            this.items = items;
        }

        @Override
        public boolean hasNext() {
            return items.length != 0 && cur != items.length;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return items[cur++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();


        rq.enqueue(498);
        rq.enqueue(676);
        System.out.println(rq.isEmpty());
        System.out.println(rq.isEmpty());
        System.out.println(rq.dequeue());


    }
}
