package com.wildbeeslabs.jentle.algorithms;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;

/**
 *
 * Initial Application Class Loader
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-08
 */
public class AppLoader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = CSort.binarySearch(new Integer[]{3, 4, 5, 6, 7}, new Integer(33));
        System.out.println(i);
    }
}
