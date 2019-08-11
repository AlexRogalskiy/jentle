package com.wildbeeslabs.jentle.collections;

import com.wildbeeslabs.jentle.collections.tree.iface.BSTInterface;
import com.wildbeeslabs.jentle.collections.tree.impl.BSTree;
import com.wildbeeslabs.jentle.collections.tree.node.TreeNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * BSTree Validation Tests
 */
public class BSTTest {
    public BSTInterface<Integer> bst;
    public TreeNode<Integer> NIL;

    @Before
    public void setUp() throws Exception {
        this.bst = new BSTree<>();
        this.NIL = new TreeNode<>();
    }

    @Test
    public void testSearch() {
        assertEquals(this.NIL, this.bst.search(9));
        this.bst.insert(9);

        assertEquals(Integer.valueOf(9), this.bst.search(9).getData());
        assertEquals(this.NIL, this.bst.search(15));

        this.bst.insert(10);
        this.bst.insert(15);
        this.bst.insert(30);
        this.bst.insert(7);

        assertEquals(Integer.valueOf(10), this.bst.search(10).getData());
        assertEquals(Integer.valueOf(15), this.bst.search(15).getData());
        assertEquals(Integer.valueOf(7), this.bst.search(7).getData());
        assertEquals(this.NIL, this.bst.search(45));
    }

    @Test
    public void testInsert() {
        assertTrue(this.bst.isEmpty());
        this.bst.insert(Integer.valueOf(10));

        assertFalse(bst.isEmpty());
        this.bst.insert(Integer.valueOf(20));
        this.bst.insert(Integer.valueOf(30));
        this.bst.insert(Integer.valueOf(5));
        this.bst.insert(Integer.valueOf(7));
        this.bst.insert(Integer.valueOf(3));
        this.bst.insert(Integer.valueOf(15));
        this.bst.insert(Integer.valueOf(25));

        //tests
        assertEquals(Integer.valueOf(10), this.bst.getRoot().getData());
        assertEquals(Integer.valueOf(20), this.bst.getRoot().getRight().getData());
        assertEquals(Integer.valueOf(5), this.bst.getRoot().getLeft().getData());
        assertEquals(Integer.valueOf(7), this.bst.getRoot().getLeft().getRight().getData());
        assertEquals(Integer.valueOf(3), this.bst.getRoot().getLeft().getLeft().getData());
    }

    @Test
    public void testMaximum() {
        assertTrue(this.bst.isEmpty());
        this.bst.insert(Integer.valueOf(10));

        assertFalse(bst.isEmpty());
        this.bst.insert(Integer.valueOf(20));
        this.bst.insert(Integer.valueOf(30));

        assertEquals(Integer.valueOf(30), this.bst.maximum().getData());
        this.bst.insert(Integer.valueOf(5));
        this.bst.insert(Integer.valueOf(7));
        this.bst.insert(Integer.valueOf(100));
        this.bst.insert(Integer.valueOf(3));
        this.bst.insert(Integer.valueOf(15));
        this.bst.insert(Integer.valueOf(25));

        //tests
        assertEquals(Integer.valueOf(100), this.bst.maximum().getData());
    }

    @Test
    public void testMinimum() {
        assertTrue(this.bst.isEmpty());
        this.bst.insert(Integer.valueOf(10));

        assertFalse(this.bst.isEmpty());
        this.bst.insert(Integer.valueOf(20));
        this.bst.insert(Integer.valueOf(30));

        assertEquals(Integer.valueOf(10), this.bst.minimum().getData());
        this.bst.insert(Integer.valueOf(5));
        this.bst.insert(Integer.valueOf(7));

        assertEquals(Integer.valueOf(5), this.bst.minimum().getData());
        this.bst.insert(Integer.valueOf(100));
        this.bst.insert(Integer.valueOf(3));
        this.bst.insert(Integer.valueOf(15));
        this.bst.insert(Integer.valueOf(25));
        assertEquals(Integer.valueOf(3), this.bst.minimum().getData());
    }

    @Test
    public void testSucessor() {
        this.bst.insert(Integer.valueOf(10));
        Assert.assertNull(this.bst.sucessor(Integer.valueOf(10)));
        this.bst.insert(Integer.valueOf(20));
        assertEquals(Integer.valueOf(20), this.bst.sucessor(Integer.valueOf(10)).getData());
        this.bst.insert(Integer.valueOf(30));

        assertEquals(Integer.valueOf(30), this.bst.sucessor(Integer.valueOf(20)).getData());
        assertNotEquals(Integer.valueOf(30), this.bst.sucessor(new Integer(10)).getData());
        this.bst.insert(Integer.valueOf(5));

        assertEquals(Integer.valueOf(10), this.bst.sucessor(Integer.valueOf(5)).getData());
        this.bst.insert(Integer.valueOf(7));

        assertEquals(Integer.valueOf(7), this.bst.sucessor(Integer.valueOf(5)).getData());
        this.bst.insert(Integer.valueOf(100));

        assertEquals(Integer.valueOf(100), this.bst.sucessor(Integer.valueOf(30)).getData());
        assertNotEquals(Integer.valueOf(100), this.bst.sucessor(Integer.valueOf(20)).getData());
        this.bst.insert(Integer.valueOf(3));

        assertEquals(Integer.valueOf(5), this.bst.sucessor(Integer.valueOf(3)).getData());
        this.bst.insert(Integer.valueOf(15));

        assertEquals(Integer.valueOf(20), this.bst.sucessor(Integer.valueOf(15)).getData());
        this.bst.insert(Integer.valueOf(25));
        assertEquals(Integer.valueOf(30), this.bst.sucessor(Integer.valueOf(25)).getData());
    }


    @Test
    public void testPredecessor() {
        this.bst.insert(Integer.valueOf(10));
        assertNull(this.bst.predecessor(Integer.valueOf(10)));

        this.bst.insert(Integer.valueOf(20));
        assertEquals(Integer.valueOf(10), this.bst.predecessor(Integer.valueOf(20)).getData());
        this.bst.insert(Integer.valueOf(30));

        assertEquals(Integer.valueOf(20), this.bst.predecessor(Integer.valueOf(30)).getData());
        this.bst.insert(Integer.valueOf(5));

        assertEquals(Integer.valueOf(5), this.bst.predecessor(Integer.valueOf(10)).getData());
        this.bst.insert(Integer.valueOf(7));

        assertEquals(Integer.valueOf(5), this.bst.predecessor(Integer.valueOf(7)).getData());
        Assert.assertNotEquals(Integer.valueOf(5), this.bst.predecessor(Integer.valueOf(10)).getData());
        this.bst.insert(Integer.valueOf(100));

        assertEquals(Integer.valueOf(30), this.bst.predecessor(Integer.valueOf(100)).getData());
        this.bst.insert(Integer.valueOf(3));

        assertEquals(Integer.valueOf(3), this.bst.predecessor(Integer.valueOf(5)).getData());
        this.bst.insert(Integer.valueOf(15));

        assertEquals(Integer.valueOf(10), this.bst.predecessor(Integer.valueOf(15)).getData());
        this.bst.insert(Integer.valueOf(25));
        assertEquals(Integer.valueOf(20), this.bst.predecessor(Integer.valueOf(25)).getData());
    }


    @Test
    public void testRemove() {
        assertTrue(bst.isEmpty());
        this.bst.insert(Integer.valueOf(10));

        assertFalse(bst.isEmpty());
        this.bst.insert(Integer.valueOf(20));
        this.bst.insert(Integer.valueOf(30));
        this.bst.insert(Integer.valueOf(5));
        this.bst.insert(Integer.valueOf(7));
        this.bst.insert(Integer.valueOf(3));
        this.bst.insert(Integer.valueOf(15));

        this.bst.remove(Integer.valueOf(10));
        assertEquals(Integer.valueOf(15), bst.getRoot().getData());
        assertEquals(new TreeNode<Integer>(), bst.search(Integer.valueOf(10)));
        this.bst.remove(Integer.valueOf(30));
        assertEquals(Integer.valueOf(15), bst.getRoot().getData());
        assertEquals(new TreeNode<Integer>(), bst.search(Integer.valueOf(30)));
        this.bst.remove(Integer.valueOf(5));
        assertEquals(new TreeNode<Integer>(), bst.search(Integer.valueOf(5)));
        assertEquals(Integer.valueOf(7), bst.getRoot().getLeft().getData());
        this.bst.remove(Integer.valueOf(20));
        this.bst.remove(Integer.valueOf(15));
        this.bst.remove(Integer.valueOf(7));
        this.bst.remove(Integer.valueOf(3));
        assertTrue(bst.isEmpty());
    }

    @Test
    public void testOrder() {
        assertTrue(this.bst.isEmpty());
        this.bst.insert(Integer.valueOf(10));

        assertFalse(this.bst.isEmpty());
        this.bst.insert(Integer.valueOf(20));
        this.bst.insert(Integer.valueOf(30));
        this.bst.insert(Integer.valueOf(5));
        this.bst.insert(Integer.valueOf(7));
        this.bst.insert(Integer.valueOf(3));
        this.bst.insert(Integer.valueOf(15));

        final Comparable[] postOrder = this.bst.toArrayPostOrder();
        final Comparable[] preOrder = this.bst.toArrayPreOrder();
        final Comparable[] order = this.bst.toArrayOrder();

        //Print Post Order Array
        System.out.println("Array Post Order: ");
        for (int i = 0; i < postOrder.length; i++) {
            System.out.print(postOrder[i] + " ");
        }
        System.out.println("");

        //Print Order Array
        System.out.println("Array Order: ");
        for (int i = 0; i < order.length; i++) {
            System.out.print(order[i] + " ");
        }
        System.out.println("");

        //Print Pre Order Array
        System.out.println("Array Pre Order: ");
        for (int i = 0; i < preOrder.length; i++) {
            System.out.print(preOrder[i] + " ");
        }
        System.out.println("");
    }

    @Test
    public void testHeight() {
        this.bst.insert(Integer.valueOf(30));
        this.bst.insert(Integer.valueOf(10));
        this.bst.insert(Integer.valueOf(15));
        this.bst.insert(Integer.valueOf(14));
        this.bst.insert(Integer.valueOf(13));
        this.bst.insert(Integer.valueOf(12));
        this.bst.insert(Integer.valueOf(11));
        this.bst.insert(Integer.valueOf(42));
        this.bst.insert(Integer.valueOf(5));
        this.bst.insert(Integer.valueOf(3));
        this.bst.insert(Integer.valueOf(2));
        assertEquals(6, this.bst.height());
    }
}
