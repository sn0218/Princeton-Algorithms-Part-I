import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {

        if (args.length != 1) {
            throw new IllegalArgumentException("Incorrect number of cmd arguments.");
        }

        // parse the command-line arguments into an integer value
        int k = Integer.parseInt(args[0]);
        // track the no.of element from input
        int n = 0;
        // declaration of Randomized queue and create instance of randomized queue object
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        // press ctrl+D and then enter to exit the loop
        while (!StdIn.isEmpty()) {
            String data = StdIn.readString();
            if (n > k && StdRandom.uniform(n) < k) {
                randomizedQueue.dequeue();
            } else if (n > k) {
                continue;
            }
            randomizedQueue.enqueue(data);
            n++;
        }

        // print k of them uniformly at random by dequeuing the Randomized Queue
        for (int i = 0; i < k; i++) {
            System.out.println(randomizedQueue.dequeue());
        }

    }
}
