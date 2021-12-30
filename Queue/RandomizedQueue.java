import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// RandomizedQueue class to hold randomized queue implemented by array
public class RandomizedQueue<Item> implements Iterable<Item> {
    // declare instance variables
    private int capacity; // store the no. of elements in the array queue
    private Item[] items; // declare the resizing array for randomized queue

    // resize array by creating a new array of twice the size and copy items
    private void resize(int newCapacity) {
        Item[] copy = (Item[]) new Object[newCapacity]; // casting of new array as generic array creation is not allowed

        int j = 0; // pointer for copy array
        for (int i = 0; i < items.length; i++) {
            // break the loop when all the elements get copied to new array copy[]
            if (j == capacity) {
                break;
            }
            if (items[i] != null) {
                copy[j++] = items[i];
            }
        }
        // pass the name of the array queue
        items = copy;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        // instantiate the queue object by casting
        items = (Item[]) new Object[2];
        // initialize of instance variables to keep track of no.of elements exists
        this.capacity = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (capacity == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return capacity;
    }

    // add the item
    public void enqueue(Item item) {
        // corner case to check the validity of input of data
        if (item == null) {
            throw new IllegalArgumentException("The item cannot be null");
        }
        // check the size of queue if it is full
        if (capacity == items.length) {
            // if the array is full, create new array of twice the size
            resize(2 * items.length);
        }
        // capacity++ returns the current value of capacity
        items[capacity++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        // check the corner case when the randomized queue is empty
        if (isEmpty()) {
            throw new NoSuchElementException("The randomized queue is empty.");
        }

        // compute the random index
        int randomIndex = StdRandom.uniform(capacity);
        // check the item[randomIndex] if it is null
        while (items[randomIndex] == null) {
            randomIndex = StdRandom.uniform(capacity);
        }
        // dequeue the element by random index
        Item item = items[randomIndex];
        // swap the randomly chosen element with the last element
        items[randomIndex] = items[capacity - 1];
        items[capacity - 1] = null;

        // decrement the no. of elements in array queue
        capacity--;

        // check the array if it is one-quarter full
        if (capacity > 0 && capacity == items.length / 4) {
            resize(items.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        // check the corner case when the randomized queue is empty
        if (isEmpty()) {
            throw new NoSuchElementException("The randomized queue is empty.");
        }

        // compute the random index
        int randomIndex = StdRandom.uniform(capacity);

        return items[randomIndex];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        // return a uniformly random index of the array
        private final int[] randomIndex = StdRandom.permutation(capacity);
        private int i = capacity;

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No items to return.");
            }
            // return the item from random range of indices
            return items[randomIndex[--i]];

        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        // instantiate iterator object
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        // declaration of Randomized queue and create instance of randomized queue object
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();

        System.out.println("Enqueue 1");
        randomizedQueue.enqueue(1);
        System.out.println("Enqueue 2");
        randomizedQueue.enqueue(2);
        System.out.println("Enqueue 3");
        randomizedQueue.enqueue(3);
        System.out.println("Enqueue 4");
        randomizedQueue.enqueue(4);
        System.out.println("Enqueue 5");
        randomizedQueue.enqueue(5);
        System.out.println("Enqueue 6");
        randomizedQueue.enqueue(6);
        System.out.println("Enqueue 7");
        randomizedQueue.enqueue(7);
        System.out.println("Enqueue 8");
        randomizedQueue.enqueue(8);
        System.out.println("No. of elements: " + randomizedQueue.size());

        // print the element of randomized queue by Iterators
        for (Integer s : randomizedQueue) {
            StdOut.println(s);
        }

        System.out.println("Dequeue an element by random: " + randomizedQueue.dequeue());
        System.out.println("No. of elements: " + randomizedQueue.size());

        // print the element of randomized queue by Iterators
        for (Integer s : randomizedQueue) {
            StdOut.println(s);
        }

        System.out.println("Return random element: " + randomizedQueue.sample());


    }

}
