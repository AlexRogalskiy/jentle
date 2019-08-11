package com.wildbeeslabs.jentle.collections.list.impl;

import lombok.NoArgsConstructor;

/**
 * This class Implements Singly Linked List
 */
public class SinglyLinkedList<T> {

    /* Linked List TreeNode */
    @NoArgsConstructor
    private static class Node<K> {

        /*data in TreeNode*/
        private K data;
        /*Next node reference*/
        private Node next;

        /**
         * Constructor
         *
         * @param d to initialize data
         */
        Node(final K d) {
            this.data = d;
        }
    }

    /*Head Reference to Front of List */
    private Node<T> headNode;
    /*Size of List*/
    private int listSize;

    /**
     * Constructor for class SinglyLinkedList Initialize head Reference to Null
     * Initialize Size of list to 0
     */
    public SinglyLinkedList() {
        this.headNode = null;
        this.listSize = 0;
    }

    /*
     * @return head Reference of TreeNode
     */
    public Node headNode() {
        return this.headNode;
    }

    /*
     * @return size of list
     */
    public int listSize() {
        return this.listSize;
    }

    /**
     * create new new TreeNode increment in list size
     *
     * @param data data to store in new node
     * @return Reference of new TreeNode
     */
    private Node addNode(final T data) {
        final Node<T> newNode = new Node(data);
        this.listSize++;
        return newNode;
    }

    /**
     * Add TreeNode at the end of list
     *
     * @param data for data of new TreeNode
     */
    public void addAtEnd(final T data) {
        final Node<T> newNode = this.addNode(data);
        if (this.headNode == null) {
            this.headNode = newNode;
        } else {
            Node<T> temp = this.headNode;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    /**
     * Add TreeNode at the front of List
     *
     * @param data for date of new node
     */
    public void addAtFront(final T data) {
        final Node<T> newNode = this.addNode(data);
        newNode.next = this.headNode;
        this.headNode = newNode;
    }

    /**
     * add TreeNode at specific index
     *
     * @param data  to add data in new TreeNode
     * @param index index where new node has to be created
     */
    public void addAtPosition(final T data, final int index) {
        if (index <= 0) {
            this.addAtFront(data);
        } else if (index >= this.listSize) {
            this.addAtEnd(data);
        } else {
            Node<T> newNode = this.addNode(data);
            Node<T> temp = headNode;
            Node<T> tempTail = null;
            int tempIndex = 0;
            while (tempIndex < index && temp.next != null) {
                tempTail = temp;
                temp = temp.next;
                tempIndex++;
            }
            newNode.next = temp;
            tempTail.next = newNode;
        }
    }

    /**
     * delete Given key
     *
     * @param key data to be deleted
     * @return true if deleted successfully
     * false if deletion was unsuccessful
     */
    public boolean delete(final T key) {
        if (this.headNode != null) {
            if (this.headNode.data == key) {
                this.headNode = this.headNode.next;
            } else {
                Node<T> temp = this.headNode;
                Node<T> tempTail = null;
                while (temp != null && temp.data != key) {
                    tempTail = temp;
                    temp = temp.next;
                }
                if (temp == null) {
                    return false;
                } else {
                    tempTail.next = temp.next;
                }
            }
            this.listSize--;
            return true;
        }
        return false;
    }

    /**
     * delete first node
     *
     * @return true if deleted successfully
     * false if deletion was unsuccessful
     */
    public boolean deleteHead() {
        if (this.headNode != null) {
            this.headNode = this.headNode.next;
            this.listSize--;
            return true;
        }
        return false;
    }

    /**
     * delete last node
     *
     * @return true if deleted successfully
     * false if deletion was unsuccessful
     */
    public boolean deleteTail() {
        if (this.headNode != null) {
            Node<T> temp = this.headNode;
            Node<T> tempTail = temp;
            while (temp.next != null) {
                tempTail = temp;
                temp = temp.next;

            }
            tempTail.next = null;
            listSize--;
            return true;
        }
        return false;
    }

    /**
     * delete node at specific index
     *
     * @param index the node to be deleted
     * @return true if deleted successfully
     * false if deletion was unsuccessful
     */
    public boolean deleteIndex(final int index) {
        if (this.headNode != null) {
            if (index >= 0 && index < listSize) {
                if (index == 0) {
                    this.deleteHead();
                } else if (index == listSize - 1) {
                    this.deleteTail();
                } else {
                    Node<T> temp = this.headNode;
                    Node<T> tempTail = null;
                    int tempIndex = 0;
                    while (tempIndex < index && temp != null) {
                        tempTail = temp;
                        temp = temp.next;
                        tempIndex++;
                    }
                    if (temp != null) {
                        tempTail.next = temp.next;
                    }
                }
                this.listSize--;
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * delete whole list
     *
     * @return true if deleted successfully
     * false if deletion was unsuccessful
     */
    public boolean deleteList() {
        while (this.headNode != null) {
            this.headNode = this.headNode.next;
        }
        return true;
    }

    /**
     * Print List
     */
    public void printList() {
        Node temp = this.headNode;
        while (temp != null) {
            System.out.print(temp.data);
            System.out.print(" ");
            temp = temp.next;
        }
        System.out.println();
    }

    // Driver Program
    public static void main(final String args[]) {
        SinglyLinkedList<Character> obj = new SinglyLinkedList();
        obj.addAtEnd('A');
        obj.addAtEnd('B');
        obj.addAtEnd('C');
        obj.addAtFront('D');
        obj.addAtPosition('E', 3);
        obj.printList();
        obj.deleteIndex(2);
        obj.printList();
        obj.delete('A');
        obj.printList();
        obj.deleteHead();
        obj.printList();
        obj.deleteTail();
        obj.printList();
    }
}
