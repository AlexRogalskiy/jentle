package com.wildbeeslabs.jentle.algorithms.utils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A Utils.
 *
 * @version $Revision: $
 */
public class UrlUtils {
    /**
     * Reads the contents of a file into a string variable.
     *
     * @param input url
     * @return string of return
     * @throws IOException ioException
     */
    public static String readFileIntoString(URL input) throws IOException {

        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = input.openStream();
            reader = new InputStreamReader(stream);
            return readStreamIntoString(reader);
        } finally {
            if (reader != null)
                reader.close();
            if (stream != null)
                stream.close();
        }
    }

    /**
     * Reads the contents of a stream into a string variable.
     *
     * @param reader url
     * @return string of return
     * @throws IOException ioException
     */
    private static String readStreamIntoString(Reader reader) throws IOException {
        StringBuilder s = new StringBuilder();
        char a[] = new char[0x10000];
        while (true) {
            int l = reader.read(a);
            if (l == -1)
                break;
            if (l <= 0)
                throw new IOException();
            s.append(a, 0, l);
        }
        return s.toString();
    }

    /**
     * Create source file
     *
     * @param name        The name of the class
     * @param packageName The package name
     * @param outDir      output directory
     * @return The file
     * @throws IOException Thrown if an error occurs
     */
    public static FileWriter createSrcFile(String name, String packageName, String outDir) throws IOException {
        String directory = "src" + File.separatorChar + "main" + File.separatorChar + "java";

        return createPackageFile(name, packageName, directory, outDir);
    }

    /**
     * Create test file
     *
     * @param name        The name of the class
     * @param packageName The package name
     * @param outDir      output directory
     * @return The file
     * @throws IOException Thrown if an error occurs
     */
    public static FileWriter createTestFile(String name, String packageName, String outDir) throws IOException {
        String directory = "src" + File.separatorChar + "test" + File.separatorChar + "java";

        return createPackageFile(name, packageName, directory, outDir);
    }

    /**
     * Test if provided location is an absolute URI or not.
     *
     * @param location location to check, null = relative, having scheme = absolute
     * @return true if location is considered absolute
     */
    public static boolean isAbsoluteUrl(String location) {
        if (location != null && location.length() > 0 && location.contains(":")) {
            try {
                final URI uri = new URI(location);
                return uri.getScheme() != null;
            } catch (URISyntaxException e) {
            }
        }
        return false;
    }

    /**
     * Create file in the package
     *
     * @param name        The name of the class
     * @param packageName The package name
     * @param directory   layout directory
     * @param outDir      output directory
     * @return The file
     * @throws IOException Thrown if an error occurs
     */
    private static FileWriter createPackageFile(String name, String packageName, String directory, String outDir)
        throws IOException {
        if (packageName != null && !packageName.trim().equals("")) {
            directory = directory + File.separatorChar +
                packageName.replace('.', File.separatorChar);
        }

        File path = new File(outDir, directory);
        if (!path.exists()) {
            if (!path.mkdirs())
                throw new IOException("outdir can't be created");
        }

        File file = new File(path.getAbsolutePath() + File.separatorChar + name);

        if (file.exists()) {
            if (!file.delete())
                throw new IOException("there is exist file, please check");
        }

        return new FileWriter(file);
    }

    /**
     * Create file
     *
     * @param name   The name of the class
     * @param outDir output directory
     * @return The file
     * @throws IOException Thrown if an error occurs
     */
    public static FileWriter createFile(String name, String outDir) throws IOException {
        File path = new File(outDir);
        if (!path.exists()) {
            if (!path.mkdirs())
                throw new IOException("outdir can't be created");
        }

        File file = new File(path.getAbsolutePath() + File.separatorChar + name);

        if (file.exists()) {
            if (!file.delete())
                throw new IOException("there is exist file, please check");
        }

        return new FileWriter(file);
    }

    /**
     * Recursive delete
     *
     * @param f The file handler
     * @throws IOException Thrown if a file could not be deleted
     */
    public static void recursiveDelete(File f) throws IOException {
        if (f != null && f.exists()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        recursiveDelete(files[i]);
                    } else {
                        if (!files[i].delete())
                            throw new IOException("Could not delete " + files[i]);
                    }
                }
            }
            if (!f.delete())
                throw new IOException("Could not delete " + f);
        }
    }

    /**
     * copy folders
     *
     * @param sourcePath source folder
     * @param targetPath target folder
     * @param filterName filter name
     * @param recursive  recursive boolean
     * @throws IOException Thrown if an error occurs
     */
    public static void copyFolder(String sourcePath, String targetPath, final String filterName,
                                  final boolean recursive) throws IOException {
        File path = new File(targetPath);
        if (!path.exists()) {
            if (!path.mkdirs())
                throw new IOException("outdir can't be created");
        }
        File a = new File(sourcePath);
        String[] fileNames = a.list(new FilenameFilter() {
            public boolean accept(File dir, String fname) {
                if (new File(dir, fname).isDirectory()) {
                    return true;
                } else {
                    return fname.endsWith(filterName);
                }
            }

        });
        File jarFile = null;
        if (fileNames != null) {
            for (String fileName : fileNames) {
                jarFile = new File(sourcePath + File.separator + fileName);

                if (jarFile.isFile()) {
                    copyFile(jarFile, targetPath);
                }
                if (recursive && jarFile.isDirectory()) {
                    copyFolder(sourcePath + File.separator + fileName, targetPath + File.separator + fileName, filterName,
                        recursive);
                }
            }
        }
    }

    private static void copyFile(File sourceFile, String targetPath) throws FileNotFoundException, IOException {
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(sourceFile);
            to = new FileOutputStream(targetPath + File.separator + sourceFile.getName());
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1)
                to.write(buffer, 0, bytesRead);
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e) {
                    ;
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
    }
}
