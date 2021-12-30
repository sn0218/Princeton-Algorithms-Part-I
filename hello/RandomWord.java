import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        // read the first word as champion
        String champion = StdIn.readString();

        // read the remaining words until no more strings available
        int i = 2;
        // StdIn.isEmpty() causes infinity loops, press ctrl + D to terminate after entering words
        while (!StdIn.isEmpty()) {
            
            // read the next word
            String contender = StdIn.readString();
            // return a random boolean from bernoulli distribution
            // probability of new word becomes champion is 1 / i
            if (StdRandom.bernoulli(1.0 / i)) {
                champion = contender;
            }
            i++;
        }
        StdOut.println(champion);
    }
}
