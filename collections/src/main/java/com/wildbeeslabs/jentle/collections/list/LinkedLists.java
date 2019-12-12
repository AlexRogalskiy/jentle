package com.wildbeeslabs.jentle.collections.list;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by areed on 3/27/16.
 */
public class LinkedLists {

    public static <T> Node<T> generateIntegerList(T ... values) {

        Node<T> head = new Node<T>(values[0]);

        Node<T> curr = head;

        for (int i = 1; i < values.length; i++) {
            curr.next = new Node<T>(values[i]);
            curr = curr.next;
        }
        return head;
    }

    public static <T> void removeDuplicatesSet(Node<T> head) {
        Set<T> valSet = Sets.newHashSet();
        valSet.add(head.value);
        Node<T> curr = head;

        while(curr.hasNext()) {
            if (valSet.contains(curr.next.value)) {
                curr.next = curr.next.next;
            } else {
                curr = curr.next;
            }

            valSet.add(curr.value);
        }
    }

    public static <T> void removeDuplicatesDoublePointer(Node<T> head) {

        Node<T> curr = head;
        while (curr != null && curr.hasNext()) {
            Node<T> iterPtr = curr;
            while(iterPtr.hasNext()) {
                if (curr.value == iterPtr.next.value) {
                    iterPtr.next = iterPtr.next.next;
                } else {
                    iterPtr = iterPtr.next;
                }
            }
            curr = curr.next;
        }
    }

    public static <T> Node<T> findKthToLast(Node<T> head, int k) {
        Node<T> endPointer = head;
        for (int i = 0; i < k; i++) {
            if (endPointer.next == null) {
                return null; //not enough elements to find kth
            }
            endPointer = endPointer.next;
        }

        Node<T> kthPointer = head;
        while(endPointer.hasNext()) {
            endPointer = endPointer.next;
            kthPointer = kthPointer.next;
        }

        return kthPointer;
    }

    public static <T> void deleteMe(Node<T> toDelete) {
        Node<T> curr = toDelete;
        while(curr.next.hasNext()) {
            curr.value = curr.next.value;
            curr = curr.next;
        }
        curr.value = curr.next.value;
        curr.next = null;
    }

    public static Node<Integer> sum(Node<Integer> num1, Node<Integer> num2) {

        int carry = 0;

        ListAdditionResult onesDigitSum = add(num1.value, num2.value, carry);
        carry = onesDigitSum.carry;
        Node<Integer> result = new Node<Integer>(onesDigitSum.value);
        Node<Integer> resultCurr = result;

        num1 = num1.next;
        num2 = num2.next;

        while(num1 != null || num2 != null) {
            ListAdditionResult sum = add(num1 == null ? 0 : num1.value, num2 == null ? 0 : num2.value, carry);
            resultCurr.next = new Node<Integer>(sum.value);
            resultCurr = resultCurr.next;
            carry = sum.carry;
            if (num1 != null) {
                num1 = num1.next;
            }
            if (num2 != null) {
                num2 = num2.next;
            }
        }

        if (carry != 0) {
            resultCurr.next = new Node<Integer>(carry);
        }

        return result;
    }

    private static ListAdditionResult add(int value1, int value2, int carry) {
        int tmp = value1 + value2 + carry;
        return new ListAdditionResult(tmp % 10, tmp / 10);
    }

    private static class ListAdditionResult {
        private int carry;
        private int value;

        private ListAdditionResult(int value, int carry) {
            this.value = value;
            this.carry = carry;
        }
    }
}
