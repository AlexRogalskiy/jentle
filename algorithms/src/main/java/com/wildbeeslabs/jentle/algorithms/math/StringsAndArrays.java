package com.wildbeeslabs.jentle.algorithms.math;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class StringsAndArrays {
    public static boolean isUniqueHashSet(String s) {

        Set<Character> seenCharacters = Sets.newHashSet();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (seenCharacters.contains(c)) {
                return false;
            } else {
                seenCharacters.add(c);
            }
        }

        return true;
    }

    public static boolean isUniqueSort(String s) {
        char[] sortedString = s.toCharArray();
        Arrays.sort(sortedString);

        for (int i = 0; i < sortedString.length - 1; i++) {
            if (sortedString[i] == sortedString[i + 1]) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPermutationHashMap(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }

        Map<Character, Integer> charCountMap = Maps.newHashMap();
        for (int i = 0; i < s1.length(); i++) {
            int count = charCountMap.getOrDefault(s1.charAt(i), 0);
            count = count + 1;
            charCountMap.put(s1.charAt(i), count);
        }

        for (int i = 0; i < s2.length(); i++) {
            int count = charCountMap.getOrDefault(s2.charAt(i), 0);
            count = count - 1;
            if (count < 0) {
                return false;
            }
            charCountMap.put(s2.charAt(i), count);
        }
        return true;
    }

    public static boolean isPermutationSort(String s1, String s2) {

        if (s1.length() != s2.length()) {
            return false;
        }

        char[] s1CharArray = s1.toCharArray();
        char[] s2CharArray = s2.toCharArray();
        Arrays.sort(s1CharArray);
        Arrays.sort(s2CharArray);

        for (int i = 0; i < s1CharArray.length; i++) {
            if (s1CharArray[i] != s2CharArray[i]) {
                return false;
            }
        }
        return true;
    }

    public static char[] URLify(char[] paddedString) {
        int lastPlace = paddedString.length - 1;
        int currPos = paddedString.length - 1;

        while (currPos >= 0 && paddedString[currPos] == ' ') {
            currPos = currPos - 1;
        }

        while (currPos >= 0) {
            if (paddedString[currPos] == ' ') {
                paddedString[lastPlace--] = '0';
                paddedString[lastPlace--] = '2';
                paddedString[lastPlace--] = '%';

            } else {
                paddedString[lastPlace--] = paddedString[currPos];
            }
            currPos--;
        }
        return paddedString;
    }

    public static boolean isPalindromePermutation(String s) {
        s = s.toLowerCase();
        Map<Character, Integer> charCount = Maps.newHashMap();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                continue;
            }
            int count = charCount.getOrDefault(s.charAt(i), 0);
            count = count + 1;
            charCount.put(s.charAt(i), count);
        }
        boolean foundOdd = false;
        for (Character c : charCount.keySet()) {
            int count = charCount.get(c);
            if (count % 2 != 0) {
                if (foundOdd) {
                    return false;
                } else {
                    foundOdd = true;
                }
            }
        }
        return true;
    }

    public static boolean isOneEditAway(String s1, String s2) {
        if (Math.abs(s1.length() - s2.length()) > 1) {
            return false;
        }
        int edit = 0;
        int s1Pos = 0;
        int s2Pos = 0;

        while (s1Pos < s1.length() && s2Pos < s2.length()) {
            if (s1.charAt(s1Pos) == s2.charAt(s2Pos)) {
                s1Pos++;
                s2Pos++;
            } else if (s1Pos != s1.length() - 1 && s1.charAt(s1Pos + 1) == s2.charAt(s2Pos)) {
                edit++;
                s1Pos++;
            } else if (s2Pos != s2.length() - 1 && s2.charAt(s2Pos + 1) == s1.charAt(s1Pos)) {
                edit++;
                s2Pos++;
            } else {
                edit++;
                s1Pos++;
                s2Pos++;
            }

            if (edit > 1) {
                return false;
            }

        }
        return true;
    }
}
