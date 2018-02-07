/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom reflection utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CReflectionUtils {

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(CReflectionUtils.class);

    private CReflectionUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Returns the method by name of the supplied class
     *
     * @param clazz The class to introspect.
     * @param methodName The method name.
     * @return The method of the supplied class, null - otherwise
     */
    public static Method getMethod(final Class<?> clazz, final String methodName) {
        try {
            return clazz.getMethod(methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
        } catch (NoSuchMethodException ex) {
            LOGGER.error(String.format("ERROR: no such method=%s, message=%s", methodName, ex.getMessage()));
            return null;
        }
    }

    /**
     * Returns the type of the supplied member {@link java.lang.reflect.Field}
     * or {@link java.lang.reflect.Method}
     *
     * @param member The member to reflect on.
     * @return The type of the supplied member.
     */
    public static Type typeOf(final Member member) {
        if (member instanceof Field) {
            return ((Field) member).getGenericType();
        }
        if (member instanceof Method) {
            return ((Method) member).getGenericReturnType();
        }
        throw new IllegalArgumentException(String.format("ERROR: no such member=%s, neither a field nor a method", member));
    }

    /**
     * Checks if the supplied type is of {@link java.util.Map}
     *
     * @param type The type to reflect on.
     * @return <code>true</code> if <code>type</code> is implementing
     * <code>Map</code> interface, <code>false</code> otherwise.
     */
    public static boolean isMap(final Type type) {
        if (type instanceof Class && isMapClass((Class) type)) {
            return true;
        }
        if (type instanceof ParameterizedType) {
            return isMap(((ParameterizedType) type).getRawType());
        }
        if (type instanceof WildcardType) {
            final Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            return upperBounds.length != 0 && isMap(upperBounds[0]);
        }
        return false;
    }

    /**
     * Checks whether the specified class parameter is an instance of
     * {@link java.util.Map} interface.
     *
     * @param clazz The class to reflect on.
     * @return <code>true</code> if clazz is instance of {@link java.util.Map},
     * <code>false</code> - otherwise.
     */
    private static boolean isMapClass(final Class<?> clazz) {
        final List<Class<?>> classes = new ArrayList<>();
        CReflectionUtils.computeClassHierarchy(clazz, classes);
        return classes.contains(Map.class);
    }

    /**
     * Returns all super-classes and interfaces recursively of the supplied
     * class
     *
     * @param clazz The class to introspect from.
     * @param classes The list of classes to add to all found super classes and
     * interfaces.
     */
    private static void computeClassHierarchy(final Class<?> clazz, final List<Class<?>> classes) {
        for (Class current = clazz; Objects.nonNull(current); current = current.getSuperclass()) {
            if (classes.contains(current)) {
                return;
            }
            classes.add(current);
            for (final Class currentInterface : current.getInterfaces()) {
                CReflectionUtils.computeClassHierarchy(currentInterface, classes);
            }
        }
    }

    /**
     * Returns a list of all fields on the supplied class. Union of
     * {@link Class#getDeclaredFields()} which ignores and super-classes, and
     * {@link Class#getFields()} which ignored non-public fields
     *
     * @param clazz The class to reflect on.
     * @return The complete list of fields of the supplied class
     */
    public static Field[] getAllFields(final Class<?> clazz) {
        final List<Class<?>> classes = getAllSuperclasses(clazz);
        classes.add(clazz);
        return CReflectionUtils.getAllFields(classes);
    }

    /**
     * Returns an array of fields {@link #getAllFields(Class)} but acts on a
     * list of {@link Class}s and uses only {@link Class#getDeclaredFields()}.
     *
     * @param classes The list of classes to reflect on.
     * @return The complete array of fields of the supplied list of classes.
     */
    private static Field[] getAllFields(final List<Class<?>> classes) {
        final Set<Field> fields = new HashSet<>();
        for (final Class<?> clazz : classes) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * Returns a List of super-classes for the supplied class.
     *
     * @param clazz The class to reflect on.
     * @return The list of super-classes of the supplied class.
     */
    public static List<Class<?>> getAllSuperclasses(final Class<?> clazz) {
        final List<Class<?>> classes = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        while (Objects.nonNull(superclass)) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    /**
     * Determines whether the given field is a "public static final" constant.
     *
     * @param field the field to check
     * @return <code>true</code> if the field has
     * <code>public static final</code> modifiers, <code>false</code> otherwise
     */
    public static boolean isPublicStaticFinal(final Field field) {
        int modifiers = field.getModifiers();
        return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
    }
}
