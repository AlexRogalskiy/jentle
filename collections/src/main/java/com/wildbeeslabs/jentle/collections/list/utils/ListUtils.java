package com.wildbeeslabs.jentle.collections.list.utils;

import com.wildbeeslabs.jentle.collections.list.node.LinkedNode;
import lombok.experimental.UtilityClass;

/**
 * 141. Linked List Cycle
 * <p>
 * Given a linked list, determine if it has a cycle in it.
 * <p>
 * Follow up:
 * <p>
 * Can you solve it without using extra space?
 */
@UtilityClass
public class ListUtils {

    public static <T> boolean hasCycle(final LinkedNode<T> head) {
        if (head == null) {
            return false;
        }
        /**
         * Use two pointers, walker and runner
         */
        LinkedNode<T> walker = head;
        LinkedNode<T> runner = head;
        while (runner.getNext() != null && runner.getNext().getNext() != null) {
            /**
             * walker moves step by step,
             * runner moves two steps at time
             */
            walker = walker.getNext();
            runner = runner.getNext().getNext();
            /**
             * If the Linked List has a cycle walker and runner will meet at some point
             */
            if (walker == runner) {
                return true;
            }
        }
        return false;
    }

    public static <T> LinkedNode<T> detectCycle(final LinkedNode<T> head) {
        LinkedNode<T> slow = head;
        LinkedNode<T> fast = head;
        /**
         * If fast != null or fast.next != null is false,
         * it's mean this linkedlist is no circle
         */
        while (fast != null && fast.getNext() != null) {
            fast = fast.getNext().getNext();
            slow = slow.getNext();
            /**
             * If fast == slow is true,
             * it's mean this linkedlist have circle
             */
            if (fast == slow) {
                LinkedNode<T> slow2 = head;
                while (slow2 != slow) {
                    slow = slow.getNext();
                    slow2 = slow2.getNext();
                }
                return slow;
            }
        }
        return null;
    }

    public static <T> LinkedNode<T> getIntersectionNode(final LinkedNode<T> headA, final LinkedNode<T> headB) {
        /**
         * Boundary check
         */
        if (headA == null || headB == null) {
            return null;
        }
        LinkedNode<T> nodeA = headA;
        LinkedNode<T> nodeB = headB;
        /**
         * If a & b have different len,
         * then we will stop the loop after second iteration
         */
        while (nodeA != nodeB) {
            /**
             * For the end of first iteration,
             * we just reset the pointer to the head of another linkedlist
             */
            nodeA = (nodeA == null ? headB : nodeA.getNext());
            nodeB = (nodeB == null ? headA : nodeB.getNext());
        }
        return nodeA;
    }

    public static <T> LinkedNode<T> removeNthFromEnd(final LinkedNode<T> head, final int n) {
        LinkedNode<T> start = new LinkedNode<>();
        LinkedNode<T> slow = start, fast = start;
        slow.setNext(head);
        /**
         * Move fast in front so that the gap between slow and fast becomes n
         */
        for (int i = 1; i <= n + 1; i++) {
            fast = fast.getNext();
        }
        /**
         * Move fast to the end, maintaining the gap
         */
        while (fast != null) {
            slow = slow.getNext();
            fast = fast.getNext();
        }
        /**
         * Skip the desired node
         */
        slow.setNext(slow.getNext().getNext());
        return start.getNext();
    }

    public static <T> LinkedNode<T> removeElements(final LinkedNode<T> head, final T value) {
        if (head == null) {
            return null;
        }
        head.setNext(removeElements(head.getNext(), value));
        return head.getValue() == value ? head.getNext() : head;
    }

    /**
     * iterative solution
     */
    public static <T> LinkedNode<T> reverseList(LinkedNode<T> head) {
        LinkedNode<T> newHead = null;
        while (head != null) {
            LinkedNode<T> nextNode = head.getNext();
            head.setNext(newHead);
            newHead = head;
            head = nextNode;
        }
        return newHead;
    }

    /**
     * recursive solution
     */
    public static <T> LinkedNode<T> reverseList2(final LinkedNode<T> head) {
        if (head == null || head.getNext() == null) {
            return head;
        }
        LinkedNode<T> nextNode = head.getNext();
        LinkedNode<T> newHead = reverseList2(nextNode);
        nextNode.setNext(head);
        head.setNext(null);
        return newHead;
    }

    public static <T> boolean isPalindrome(final LinkedNode<T> head) {
        LinkedNode<T> fast = head, slow = head;
        /**
         * when while loop execute over, fast come to last node
         */
        while (fast != null && fast.getNext() != null) {
            fast = fast.getNext().getNext();
            slow = slow.getNext();
        }
        /**
         * odd nodes, let right half smaller
         */
        if (fast != null) {
            slow = slow.getNext();
        }
        slow = reverseList(slow);
        fast = head;
        /**
         * compare link list
         */
        while (slow != null) {
            if (fast.getValue() != slow.getValue()) {
                return false;
            }
            fast = fast.getNext();
            slow = slow.getNext();
        }
        return true;
    }

    public static <T> LinkedNode<T> oddEvenList(LinkedNode<T> head) {
        if (head != null) {
            /**
             * initial odd node and even node
             */
            LinkedNode<T> odd = head;
            LinkedNode<T> even = head.getNext();
            /**
             * keep first even node and when while loop over,
             * linked last odd node and first even node
             */
            LinkedNode<T> evenHead = even;
            while (even != null && even.getNext() != null) {
                odd.setNext(odd.getNext().getNext());
                even.setNext(even.getNext().getNext());
                odd = odd.getNext();
                even = even.getNext();
            }
            /**
             * linked last odd node and first even node, keep link list completed
             */
            odd.setNext(evenHead);
        }
        return head;
    }
}
