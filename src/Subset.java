import edu.princeton.cs.algs4.StdIn;

/**
 * Created by Aaron on 01/03/2016.
 */
public class Subset {
    public static void main(String[] args) {
        final int k = Integer.valueOf(args[0]);

        RandomizedQueue<String> randQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            randQueue.enqueue(StdIn.readString());
        }

        int i = 0;
        for (String string : randQueue) {
            if (i++ == k) break;

            System.out.println(string);
        }
    }
}
