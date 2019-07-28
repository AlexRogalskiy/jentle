package com.wildbeeslabs.jentle.algorithms;

import java.io.*;

/**
 * @author kevin
 */
public final class TestResourceUtils {

    private static final String TESTPREFIX = "itexttest_";

    private TestResourceUtils() {
    }

    public static String getFullyQualifiedResourceName(Class context, String resourceName) {
        return context.getName().replace('.', '/') + "/" + resourceName;
    }

    public static InputStream getResourceAsStream(Object context, String resourceName) {
        Class contextClass;
        if (context instanceof Class) {
            contextClass = (Class) context;
        } else {
            contextClass = context.getClass();
        }
        return contextClass.getClassLoader().getResourceAsStream(getFullyQualifiedResourceName(contextClass, resourceName));
    }

    public static void purgeTempFiles() {
        File tempFolder = new File(System.getProperty("java.io.tmpdir"));
        File[] itextTempFiles = tempFolder.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getName().startsWith(TESTPREFIX);
            }

        });

        for (File file : itextTempFiles) {
            if (!file.delete()) {
                System.err.println("Unable to delete iText temporary test file " + file);
            }
        }
    }

    public static File getResourceAsTempFile(Object context, String resourceName) throws IOException {
        File f = File.createTempFile(TESTPREFIX, ".pdf");
        f.deleteOnExit();

        InputStream is = getResourceAsStream(context, resourceName);

        final OutputStream os = new BufferedOutputStream(new FileOutputStream(f));

        try {
            writeInputToOutput(is, os);
        } finally {
            is.close();
            os.close();
        }

        return f;
    }

    public static byte[] getResourceAsByteArray(Object context, String resourceName) throws IOException {
        InputStream inputStream = getResourceAsStream(context, resourceName);

        final ByteArrayOutputStream fileBytes = new ByteArrayOutputStream();

        try {
            writeInputToOutput(inputStream, fileBytes);
        } finally {
            inputStream.close();
        }

        return fileBytes.toByteArray();

    }

    private static void writeInputToOutput(InputStream is, OutputStream os) throws IOException {
        final byte[] buffer = new byte[8192];
        while (true) {
            final int bytesRead = is.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            os.write(buffer, 0, bytesRead);
        }

    }
}
