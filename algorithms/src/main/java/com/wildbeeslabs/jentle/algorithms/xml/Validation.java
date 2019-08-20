package com.wildbeeslabs.jentle.algorithms.xml;

import java.io.File;

/*package*/ class Validation {

    public boolean isValidUsbDeviceCandidate(final File file) {
        final boolean retVal;

        if (!file.exists()) {
            retVal = false;
        } else if (!file.isDirectory()) {
            retVal = false;
        } else if (".".equals(file.getName()) || "..".equals(file.getName())) {
            retVal = false;
        } else {
            retVal = true;
        }

        return retVal;
    }

    public File[] getListOfChildren(final File path) {
        final File[] retVal;

        if (path.exists()
            && path.isDirectory()
            && path.listFiles() != null) {
            retVal = path.listFiles();
        } else {
            retVal = new File[0];
        }

        return retVal;
    }
}
