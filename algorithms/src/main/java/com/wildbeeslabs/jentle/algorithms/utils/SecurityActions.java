package com.wildbeeslabs.jentle.algorithms.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Privileged Blocks
 *
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
public class SecurityActions {
    /**
     * Constructor
     */
    private SecurityActions() {
    }

    /**
     * Get the classloader.
     *
     * @param c The class
     * @return The classloader
     */
    static ClassLoader getClassLoader(final Class<?> c) {
        if (System.getSecurityManager() == null)
            return c.getClassLoader();

        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return c.getClassLoader();
            }
        });
    }

    /**
     * Get a system property
     *
     * @param name The property name
     * @return The property value
     */
    static String getSystemProperty(final String name) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(name);
        } else {
            return (String) AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    return System.getProperty(name);
                }
            });
        }
    }

    /**
     * Get the declared methods
     *
     * @param c The class
     * @return The methods
     */
    static Method[] getDeclaredMethods(final Class<?> c) {
        if (System.getSecurityManager() == null)
            return c.getDeclaredMethods();

        return AccessController.doPrivileged(new PrivilegedAction<Method[]>() {
            public Method[] run() {
                return c.getDeclaredMethods();
            }
        });
    }

    /**
     * Get the declared fields
     *
     * @param c The class
     * @return The fields
     */
    static Field[] getDeclaredFields(final Class<?> c) {
        if (System.getSecurityManager() == null)
            return c.getDeclaredFields();

        return AccessController.doPrivileged(new PrivilegedAction<Field[]>() {
            public Field[] run() {
                return c.getDeclaredFields();
            }
        });
    }

    /**
     * Set accessibleo
     *
     * @param ao The object
     */
    static void setAccessible(final AccessibleObject ao) {
        if (System.getSecurityManager() == null)
            ao.setAccessible(true);

        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                ao.setAccessible(true);
                return null;
            }
        });
    }

    /**
     * Get the constructor
     *
     * @param c      The class
     * @param params The parameters
     * @return The constructor
     * @throws NoSuchMethodException If a matching method is not found.
     */
    static Constructor<?> getConstructor(final Class<?> c, final Class<?>... params)
        throws NoSuchMethodException {
        if (System.getSecurityManager() == null)
            return c.getConstructor(params);

        Constructor<?> result = AccessController.doPrivileged(new PrivilegedAction<Constructor<?>>() {
            public Constructor<?> run() {
                try {
                    return c.getConstructor(params);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }
        });

        if (result != null)
            return result;

        throw new NoSuchMethodException();
    }

    /**
     * Get the method
     *
     * @param c      The class
     * @param name   The name
     * @param params The parameters
     * @return The method
     * @throws NoSuchMethodException If a matching method is not found.
     */
    static Method getMethod(final Class<?> c, final String name, final Class<?>... params)
        throws NoSuchMethodException {
        if (System.getSecurityManager() == null)
            return c.getMethod(name, params);

        Method result = AccessController.doPrivileged(new PrivilegedAction<Method>() {
            public Method run() {
                try {
                    return c.getMethod(name, params);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }
        });

        if (result != null)
            return result;

        throw new NoSuchMethodException();
    }
}
