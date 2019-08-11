package com.wildbeeslabs.jentle.collections.array.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class ArrayUtils {

    public static char nextGreatestLetter(final char[] letters, final char target) {
        final int length = letters.length;
        char value = target;
        if (target >= letters[length - 1]) {
            value = letters[0];
        } else {
            value++;
        }

        int left = 0, right = length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (letters[mid] == value)
                return letters[mid];

            if (letters[mid] < value) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return letters[right];
    }

    public static int smallestDistancePair(final int[] nums, final int k) {
        Arrays.sort(nums);
        int n = nums.length;
        int left = 0;
        int right = nums[n - 1] - nums[0];

        for (int cnt = 0; left < right; cnt = 0) {
            int mid = left + (right - left) / 2;

            for (int i = 0, j = 0; i < n; i++) {
                while (j < n && nums[j] <= nums[i] + mid) j++;
                cnt += j - i - 1;
            }

            if (cnt < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public static int search(final int[] nums, final int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;
        // binary search algorithm
        while (left <= right) {
            // unsigned right means divided 2's n square
            int middle = (left + right) >>> 1;
            // compare target element
            if (nums[middle] < target) {
                left = middle + 1;
            } else if (nums[middle] > target) {
                right = middle - 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    /**
     * Binary Search Solution
     *
     * @param x
     * @return
     */
    public static int mySqrt(final int x) {
        if (x == 0) {
            return 0;
        }
        int left = 1, right = Integer.MAX_VALUE;
        while (true) {
            int mid = left + (right - left) / 2;
            if (mid > x / mid) {
                right = mid - 1;
            } else {
                if (mid + 1 > x / (mid + 1)) {
                    return mid;
                } else {
                    left = mid + 1;
                }
            }
        }
    }

    /**
     * Newton Solution
     *
     * @param x
     * @return
     */
    public static int mySqrt2(final int x) {
        if (x == 0) {
            return 0;
        }
        long i = x;
        while (i > x / i) {
            i = (i + x / i) / 2;
        }
        return (int) i;
    }

    /**
     * Brute Force Solution
     *
     * @param x
     * @return
     */
    public static int mySqrt3(final int x) {
        if (x == 0) {
            return 0;
        }
        for (int i = 1; i <= x / i; i++) {
            // Look for the critical point: i*i <= x && (i+1)(i+1) > x
            if (i <= x / i && (i + 1) > x / (i + 1)) {
                return i;
            }
        }
        return -1;
    }

    public static List<Integer> findClosestElements(final int[] arr, final int k, final int x) {
        int left = 0;
        int right = arr.length - 1;
        while (right - left >= k) {
            if (Math.abs(arr[left] - x) > Math.abs(arr[right] - x)) {
                left++;
            } else {
                right--;
            }
        }
        final List<Integer> result = new ArrayList<Integer>(k);
        for (int i = left; i <= right; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    public static double myPow(final double x, final int n) {
        double temp;
        if (n == 0) {
            return 1;
        }
        temp = myPow(x, n / 2);
        if (n % 2 == 0) {
            return temp * temp;
        } else {
            if (n > 0) {
                return x * temp * temp;
            } else {
                return (temp * temp) / x;
            }
        }
    }

    public static int splitArray(final int[] nums, final int m) {
        int max = 0;
        long sum = 0;
        for (int num : nums) {
            max = Math.max(num, max);
            sum += num;
        }
        if (m == 1) return (int) sum;
        long left = max;
        long right = sum;
        while (left <= right) {
            long mid = (left + right) / 2;
            if (valid(mid, nums, m)) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return (int) left;
    }

    private static boolean valid(final long target, final int[] nums, final int m) {
        int count = 1;
        long total = 0;
        for (int num : nums) {
            total += num;
            if (total > target) {
                total = num;
                count++;
                if (count > m) {
                    return false;
                }
            }
        }
        return true;
    }

    public static double findMedianSortedArrays(final int[] nums1, final int[] nums2) {
        final int m = nums1.length;
        final int n = nums2.length;
        if (m > n) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int i = 0, j = 0, imin = 0, imax = m, half = (m + n + 1) / 2;
        double maxLeft = 0, minRight = 0;
        while (imin <= imax) {
            i = (imin + imax) / 2;
            j = half - i;
            if (j > 0 && i < m && nums2[j - 1] > nums1[i]) {
                imin = i + 1;
            } else if (i > 0 && j < n && nums1[i - 1] > nums2[j]) {
                imax = i - 1;
            } else {
                if (i == 0) {
                    maxLeft = (double) nums2[j - 1];
                } else if (j == 0) {
                    maxLeft = (double) nums1[i - 1];
                } else {
                    maxLeft = (double) Math.max(nums1[i - 1], nums2[j - 1]);
                }
                break;
            }
        }

        if ((m + n) % 2 == 1) {
            return maxLeft;
        }

        if (i == m) {
            minRight = (double) nums2[j];
        } else if (j == n) {
            minRight = (double) nums1[i];
        } else {
            minRight = (double) Math.min(nums1[i], nums2[j]);
        }
        return (maxLeft + minRight) / 2;
    }

    public static boolean isPerfectSquare(int num) {
        long x = num;
        while (x * x > num) {
            x = (x + num / x) >> 1;
        }
        return x * x == num;
    }

    public static boolean isPerfectSquare2(final int num) {
        int low = 1, high = num;
        while (low <= high) {
            long mid = (low + high) >>> 1;
            if (mid * mid == num) {
                return true;
            } else if (mid * mid < num) {
                low = (int) mid + 1;
            } else {
                high = (int) mid - 1;
            }
        }
        return false;
    }

    public static boolean isPerfectSquare3(final int num) {
        int i = 1, value = num;
        while (value > 0) {
            value -= i;
            i += 2;
        }
        return value == 0;
    }

    public static int[] intersect(final int[] nums1, final int[] nums2) {
        final List<Integer> res = new ArrayList<>();
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        for (int i = 0, j = 0; i < nums1.length && j < nums2.length; ) {
            if (nums1[i] < nums2[j]) {
                i++;
            } else if (nums1[i] == nums2[j]) {
                res.add(nums1[i]);
                i++;
                j++;
            } else {
                j++;
            }
        }
        final int[] result = new int[res.size()];
        for (int i = 0; i < res.size(); i++) {
            result[i] = res.get(i);
        }
        return result;
    }

    public static int[] searchRange(final int[] nums, final int target) {
        final double left = target - 0.5, right = target + 0.5;
        final int leftIndex = binarySearch(nums, left);
        final int rightIndex = binarySearch(nums, right);

        if (leftIndex == rightIndex) {
            return new int[]{-1, -1};
        }
        return new int[]{leftIndex, rightIndex - 1};

    }

    private static int binarySearch(final int[] nums, final double target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (target > nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    public static int[] intersection(int[] nums1, int[] nums2) {
        final Set<Integer> set = new HashSet<>();
        Arrays.sort(nums2);
        for (Integer num : nums1) {
            if (binarySearch(nums2, num)) {
                set.add(num);
            }
        }
        int i = 0;
        final int[] result = new int[set.size()];
        for (Integer num : set) {
            result[i++] = num;
        }
        return result;
    }

    public static boolean binarySearch(final int[] nums, final int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                return true;
            }
            if (nums[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return false;
    }

    public static int search2(final int[] nums, final int target) {
        int start = 0, end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            // If this condition satisfied, we can return immediately
            if (nums[mid] == target) {
                return mid;
            }

            if (nums[start] <= nums[mid]) {
                if (target < nums[mid] && target >= nums[start]) {
                    end = mid - 1;
                } else {
                    start = mid + 1;
                }
            }

            if (nums[mid] <= nums[end]) {
                if (target > nums[mid] && target <= nums[end]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    /**
     * Binary Search + Pigeonhole Principle
     * <p>
     * https://en.wikipedia.org/wiki/Pigeonhole_principle
     *
     * @param nums pending array
     * @return duplicate number
     */
    public static int findDuplicate(final int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            int count = numBelow(nums, mid);
            if (count > mid) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private static int numBelow(final int[] nums, final int target) {
        int result = 0;
        for (int num : nums) {
            if (num <= target) {
                result++;
            }
        }
        return result;
    }

    public static int[] twoSum(final int[] numbers, final int target) {
        final int[] indice = new int[2];
        if (numbers == null || numbers.length < 2) return indice;
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int twoSumValue = numbers[left] + numbers[right];
            if (twoSumValue == target) {
                indice[0] = left + 1;
                indice[1] = right + 1;
                break;
            } else if (twoSumValue > target) {
                right--;
            } else {
                left++;
            }
        }
        return indice;
    }

    public static int findPeakElement(final int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] < nums[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public static int findPeakElement2(final int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        int left = 0, right = nums.length - 1;
        while (right - left > 1) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < nums[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return (left == nums.length - 1 || nums[left] > nums[left + 1]) ? left : right;
    }

    public static int findMin(final int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] < nums[right]) {
                right = mid;
            } else if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right--;
            }
        }
        return nums[left];
    }
}
