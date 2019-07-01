package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FileFilterUtils {

    public static void sortFileArrayByName(File[] fileArray) {
        Arrays.sort(fileArray, new Comparator<File>() {
            public int compare(File o1, File o2) {
                String o1Name = o1.getName();
                String o2Name = o2.getName();
                return (o1Name.compareTo(o2Name));
            }
        });
    }

    public static void reverseSortFileArrayByName(File[] fileArray) {
        Arrays.sort(fileArray, new Comparator<File>() {
            public int compare(File o1, File o2) {
                String o1Name = o1.getName();
                String o2Name = o2.getName();
                return (o2Name.compareTo(o1Name));
            }
        });
    }

    public static String afterLastSlash(final String sregex) {
        int i = sregex.lastIndexOf('/');
        if (i == -1) {
            return sregex;
        }
        return sregex.substring(i + 1);
    }

    public static boolean isEmptyDirectory(final File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("[" + dir + "] must be a directory");
        }
        final String[] filesInDir = dir.list();
        if (filesInDir == null || filesInDir.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * Return the set of files matching the stemRegex as found in 'directory'. A
     * stemRegex does not contain any slash characters or any folder separators.
     *
     * @param file
     * @param stemRegex
     * @return
     */
    public static File[] filesInFolderMatchingStemRegex(final File file, final String stemRegex) {
        if (Objects.isNull(file)) {
            return new File[0];
        }
        if (!file.exists() || !file.isDirectory()) {
            return new File[0];
        }
        return file.listFiles((dir, name) -> name.matches(stemRegex));
    }

    public static int findHighestCounter(final File[] matchingFileArray, final String stemRegex) {
        int max = Integer.MIN_VALUE;
        for (final File aFile : matchingFileArray) {
            int aCounter = FileFilterUtils.extractCounter(aFile, stemRegex);
            if (max < aCounter) {
                max = aCounter;
            }
        }
        return max;
    }

    public static int extractCounter(final File file, final String stemRegex) {
        Pattern p = Pattern.compile(stemRegex);
        String lastFileName = file.getName();

        Matcher m = p.matcher(lastFileName);
        if (!m.matches()) {
            throw new IllegalStateException("The regex [" + stemRegex + "] should match [" + lastFileName + "]");
        }
        final String counterAsStr = m.group(1);
        return Integer.valueOf(counterAsStr).intValue();
    }

    public static String slashify(final String in) {
        return in.replace('\\', '/');
    }

    public static void removeEmptyParentDirectories(final File file, int recursivityCount) {
        if (recursivityCount >= 3) {
            return;
        }
        final File parent = file.getParentFile();
        if (parent.isDirectory() && FileFilterUtils.isEmptyDirectory(parent)) {
            parent.delete();
            removeEmptyParentDirectories(parent, recursivityCount + 1);
        }
    }
}
