import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Aaron on 29/02/2016.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int DEFAULT_BACKING_SIZE = 1;
    private Item[] backingArray;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public RandomizedQueue() {
        backingArray = (Item[]) new Object[DEFAULT_BACKING_SIZE];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException("Item can't be null!");

        if (size == backingArray.length) resize(size << 1);
        backingArray[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("RandomizedQueue is empty!");

        if (size > 0 && size == backingArray.length >>> 2)
            resize(backingArray.length >>> 1);

        int randIdx = StdRandom.uniform(size);
        Item item = backingArray[randIdx];
        backingArray[randIdx] = backingArray[--size];
        backingArray[size] = null;

        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("RandomizedQueue is empty!");

        return backingArray[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            @SuppressWarnings("unchecked")
            private final Item[] cachedArray = (Item[]) new Object[backingArray.length];
            private int i = 0;

            {
                System.arraycopy(backingArray, 0, cachedArray, 0, backingArray.length);
            }

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public Item next() {
                if (i == size) throw new NoSuchElementException("End of RandomizedQueue!");

                int randIdx = i + StdRandom.uniform(size - i);
                Item item = cachedArray[randIdx];
                cachedArray[randIdx] = cachedArray[i];
                cachedArray[i] = item;
                i++;

                return item;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        Item[] resizedArray = (Item[]) new Object[newSize];

        System.arraycopy(backingArray, 0, resizedArray, 0, size);

        backingArray = resizedArray;
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

        System.out.println("Is Empty? " + randomizedQueue.isEmpty());

        randomizedQueue.enqueue(5);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(1);

        System.out.println("Size: " + randomizedQueue.size());

        for (Integer i : randomizedQueue) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("Dequeue: " + randomizedQueue.dequeue());
        System.out.println("Dequeue: " + randomizedQueue.dequeue());

        System.out.println("Size: " + randomizedQueue.size());

        randomizedQueue.enqueue(6);
        randomizedQueue.enqueue(7);
        randomizedQueue.enqueue(8);
        randomizedQueue.enqueue(9);
        randomizedQueue.enqueue(10);

        System.out.println("Size: " + randomizedQueue.size());

        System.out.println("Dequeue: " + randomizedQueue.dequeue());

        System.out.println("Size: " + randomizedQueue.size());

        new Thread(() -> {
            for (Integer i : randomizedQueue) {
                System.out.print(i + " ");
            }
            System.out.println();
        }).start();

        new Thread(() -> {
            Iterator iterator = randomizedQueue.iterator();
            while (iterator.hasNext())
                System.out.print(iterator.next() + " ");
            System.out.println();
        }).start();

        new Thread(() -> {
            for (Integer i : randomizedQueue) {
                System.out.print(i + " ");
            }
            System.out.println();
        }).start();

        System.out.println("Size: " + randomizedQueue.size());


        System.out.println("Is Empty? " + randomizedQueue.isEmpty());
    }
}
