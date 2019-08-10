package com.wildbeeslabs.jentle.collections.stack;

/**
 * This class Implements Stack
 *
 * @author hamza39460
 */
public class Stack<T> {
    /**
     * node for stack
     */
    private static class Node<K> {
        // data to store in node
        private K data;
        // next node reference
        private Node<K> next;

        /**
         * Constructor for Class Node
         *
         * @param data to be saved in node
         */
        public Node(final K data) {
            this.data = data;
            this.next = null;
        }
    }

    // reference to head node of stack
    private Node headNode;
    // stack size
    private int stackSize;

    /**
     * constructor for class stack
     * initialize head node reference to null
     * initialize stack size to zero
     */
    public Stack() {
        this.headNode = null;
        this.stackSize = 0;
    }

    /**
     * to create new node
     *
     * @param data to be saved in node
     * @return new created node
     */
    private Node addNode(final T data) {
        return new Node<>(data);
    }

    /**
     * @return size of stack
     */
    public int stackSize() {
        return stackSize;
    }

    /**
     * to push in stack
     *
     * @param data to be pushed in stack
     */
    public void push(final T data) {
        final Node newNode = addNode(data);
        newNode.next = this.headNode;
        this.headNode = newNode;
        this.stackSize++;
    }

    /**
     * removes top element
     *
     * @return top element if not empty
     * else return false
     */
    public T pop() {
        if (this.headNode != null) {
            final T data = (T) this.headNode.data;
            this.headNode = this.headNode.next;
            this.stackSize--;
            return data;
        }
        return null;
    }

    /**
     * to get top element of stack
     *
     * @return top of stack if stack is not empty
     * else return null
     */
    public T top() {
        if (this.headNode != null) {
            return (T) this.headNode.data;
        }
        return null;
    }

    /**
     * to check if stack is empty
     *
     * @return true if stack is empty
     * false if stack is not empty
     */
    boolean is_empty() {
        if (this.headNode == null) {
            return true;
        }
        return false;
    }

    /**
     * check if an element exist in stack
     *
     * @param key element to check
     * @return true if element exists
     * false if element does not exist
     */
    boolean exist(final T key) {
        if (this.headNode == null) {
            return false;
        } else if (this.headNode.data == key) {
            return true;
        } else {
            Node temp = this.headNode;
            while (temp != null && temp.data != key) {
                temp = temp.next;
            }
            if (temp != null) {
                return true;
            }
        }
        return false;
    }

    /*
    pop all elements from stack
    */
    public void popAll() {
        while (this.headNode != null) {
            this.headNode = this.headNode.next;
        }
    }

    // driver program
    public static void main(final String args[]) {
        final Stack<Double> obj = new Stack<>();
        obj.push(2.2);
        obj.push(3.3);
        obj.push(4.4);
        obj.push(5.5);
        obj.push(6.6);
        while (!obj.is_empty()) {
            System.out.println(obj.pop());
        }
    }
}
