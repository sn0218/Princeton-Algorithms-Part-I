import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Deque class hold Deque implemented by doubly-linked list
public class Deque<Item> implements Iterable<Item> {
    // Item is a generic type and placeholder for some concrete type
    // declare two references of head and tail pointing to first node and last node
    private Node head;
    private Node tail;
    private int capacity;

    // private inner class of Node with two reference
    private class Node {
        // declare instance variables
        Item item; // store the data in type Item
        Node next; // next node reference for storing next reference to itself
        Node prev; // prev node reference for storing previous reference to itself

        // constructor to create a new node by default initialization of instance variables
        public Node(Item item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }
    }

    // construct an empty deque
    public Deque() {
        /*
        this is a reference variable that refers to the current object of a method or a constructor
         */

        // head and tail nodes are both null
        this.head = null;
        this.tail = null;
        this.capacity = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return capacity == 0;
    }

    // return the number of items on the deque
    public int size() {
        return capacity;
    }

    // add the item to the front
    public void addFirst(Item item) {
        // corner case to check validity of input of data
        if (item == null) {
            throw new IllegalArgumentException("The item cannot be null");
        }
        // create a new node with given data for the Deque object
        Node newNode = new Node(item);

        // check the deque if it is empty
        if (isEmpty()) {
            // set head and tail pointing to newNode
            head = newNode;
            head.prev = null;
            tail = head;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        // increment the capacity of current deque object after addFirst
        capacity++;

    }

    // add the item to the back
    public void addLast(Item item) {
        // corner case to check validity of input of data
        if (item == null) {
            throw new IllegalArgumentException("The item cannot be null");
        }

        // create a new node with given data for the Deque object
        Node newNode = new Node(item);

        // check the deque if it is empty
        if (isEmpty()) {
            tail = newNode;
            head = tail;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        // increment the capacity of current deque object after addFirst
        capacity++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        // corner case to check the deque is empty
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty");
        }

        // Store the data of first node
        Item data = head.item;
        head = head.next;

        // check if deque has one element
        if (capacity == 1) {
            tail = head;
        }
        // more than one element in the deque
        else {
            head.prev = null;
        }

        // decrement the capacity of current deque object
        capacity--;

        return data;
    }

    // remove and return the item from the back
    public Item removeLast() {
        // corner case to check the deque is empty
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty");
        }

        // Store the data of last node
        Item data = tail.item;
        // move the tail reference pointing to the new last node
        tail = tail.prev;

        if (capacity == 1) {
            head = tail;
        } else {
            tail.next = null;
        }

        // decrement the capacity of current deque object
        capacity--;

        return data;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            // corner case to check no more items to return
            if (!hasNext()) {
                throw new NoSuchElementException("No items to return.");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        // declaration of deque and create the instance of deque object from Deque<Item> class
        Deque<Integer> deque = new Deque<>();

        System.out.println("Insert 5 at the head");
        deque.addFirst(5);
        System.out.println("Insert 20 at the head");
        deque.addFirst(20);
        System.out.println("Insert 10 at the tail");
        deque.addLast(10);
        System.out.println("Insert 7 at the tail");
        deque.addLast(7);
        System.out.println("Insert 44 at the head");
        deque.addFirst(44);

        System.out.println("Number of elements in Deque: " + deque.size());
        for (Integer s : deque) {
            StdOut.println(s);
        }

        System.out.println("Remove at the head: " + deque.removeFirst());
        System.out.println("Remove at the tail: " + deque.removeLast());
        System.out.println("Number of elements in Deque: " + deque.size());

        // print the element of deque by Iterators
        for (Integer s : deque) {
            StdOut.println(s);
        }
    }

}
