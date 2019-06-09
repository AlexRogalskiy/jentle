package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.wildbeeslabs.jentle.algorithms.utils.Strings.quote;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Locale.ENGLISH;

/**
 * Utility methods related to <a
 * href="http://java.sun.com/docs/books/tutorial/javabeans/introspection/index.html">JavaBeans Introspection</a>.
 *
 * @author Alex Ruiz
 */
@UtilityClass
public class Introspection {

    /**
     * Returns the getter {@link Method} for a property matching the given name in the given object.
     *
     * @param propertyName the given property name.
     * @param target       the given object.
     * @return the getter {@code Method} for a property matching the given name in the given object.
     * @throws NullPointerException     if the given property name is {@code null}.
     * @throws IllegalArgumentException if the given property name is empty.
     * @throws NullPointerException     if the given object is {@code null}.
     * @throws IntrospectionError       if the getter for the matching property cannot be found or accessed.
     */
    public static Method getPropertyGetter(String propertyName, Object target) {
        checkNotNull(target);
        Method getter;
        try {
            getter = findGetter(propertyName, target);
            if (Modifier.isPublic(getter.getModifiers())) {
                // force access for static class with public getter
                getter.setAccessible(true);
            }
            getter.invoke(target);
        } catch (Exception t) {
            throw new IllegalArgumentException(propertyNotFoundErrorMessage(propertyName, target), t);
        }
        return getter;
    }

    private static String propertyNotFoundErrorMessage(String propertyName, Object target) {
        String targetTypeName = target.getClass().getName();
        String property = quote(propertyName);
        Method getter = findGetter(propertyName, target);
        if (getter == null) {
            return format("No getter for property %s in %s", property, targetTypeName);
        }
        if (!isPublic(getter.getModifiers())) {
            return format("No public getter for property %s in %s", property, targetTypeName);
        }
        return format("Unable to find property %s in %s", property, targetTypeName);
    }

    private static Method findGetter(String propertyName, Object target) {
        String capitalized = propertyName.substring(0, 1).toUpperCase(ENGLISH) + propertyName.substring(1);
        // try to find getProperty
        Method getter = findMethod("get" + capitalized, target);
        if (getter != null) {
            return getter;
        }
        // try to find isProperty for boolean properties
        return findMethod("is" + capitalized, target);
    }

    private static Method findMethod(String name, Object target) {
        Class<?> clazz = target.getClass();
        // try public methods only
        try {
            return clazz.getMethod(name);
        } catch (NoSuchMethodException | SecurityException ignored) {
        }
        // search all methods
        while (clazz != null) {
            try {
                return clazz.getDeclaredMethod(name);
            } catch (NoSuchMethodException | SecurityException ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
}
