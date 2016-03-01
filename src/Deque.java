import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Aaron on 29/02/2016.
 */
public class Deque<Item> implements Iterable<Item> {

    private Node<Item> head = new Node<>(null);
    private Node<Item> tail = new Node<>(null);
    private int size;

    public Deque() {
        head.next = tail;
        tail.prev = head;
    }

    public boolean isEmpty() {
        return head.next == tail;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException("Item can't be null!");

        Node<Item> node = new Node<>(item);

        node.prev = head;
        node.next = head.next;
        node.next.prev = node;
        head.next = node;

        size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new NullPointerException("Item can't be null!");

        Node<Item> node = new Node<>(item);

        node.next = tail;
        node.prev = tail.prev;
        node.prev.next = node;
        tail.prev = node;

        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty!");

        Node<Item> node = head.next;

        head.next = node.next;
        node.next.prev = head;
        node.prev = null;
        node.next = null;

        size--;

        return node.data;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty!");

        Node<Item> node = tail.prev;

        tail.prev = node.prev;
        node.prev.next = tail;
        node.prev = null;
        node.next = null;

        size--;

        return node.data;
    }

    public Iterator<Item> iterator() {

        return new Iterator<Item>() {
            private Node<Item> currNode = head;

            @Override
            public boolean hasNext() {
                return currNode.next != tail;
            }

            @Override
            public Item next() {
                if (currNode.next == tail) throw new NoSuchElementException("End of Deque!");

                currNode = currNode.next;
                return currNode.data;
            }
        };
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        System.out.println("Is Empty? " + deque.isEmpty());

        deque.addFirst(5);
        deque.addFirst(4);
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);

        System.out.println("Size: " + deque.size());

        for (Integer i : deque) {
            System.out.print(i + " ");
        }
        System.out.println();

        deque.addLast(6);
        deque.addLast(7);
        deque.addLast(8);
        deque.addLast(9);
        deque.addLast(10);

        System.out.println("Size: " + deque.size());

        for (Integer i : deque) {
            System.out.print(i + " ");
        }
        System.out.println();

        deque.removeFirst();

        System.out.println("Size: " + deque.size());

        for (Integer i : deque) {
            System.out.print(i + " ");
        }
        System.out.println();

        deque.removeLast();

        System.out.println("Size: " + deque.size());

        Iterator iterator = deque.iterator();
        while (iterator.hasNext())
            System.out.print(iterator.next() + " ");
        System.out.println();

        System.out.println("Is Empty? " + deque.isEmpty());
    }

    private static class Node<Item> {
        private Node<Item> prev;
        private Node<Item> next;
        private Item data;

        private Node(Item item) {
            data = item;
        }
    }
}
