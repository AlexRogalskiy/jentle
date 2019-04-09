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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom reflection utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CReflectionUtils {

    /**
     * Returns the method by name of the supplied class
     *
     * @param clazz      The class to introspect.
     * @param methodName The method name.
     * @return The method of the supplied class, null - otherwise
     */
    public static Method getMethod(final Class<?> clazz, final String methodName) {
        try {
            return clazz.getMethod(methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
        } catch (NoSuchMethodException ex) {
            log.error(String.format("ERROR: no such method=%s, message=%s", methodName, ex.getMessage()));
            return null;
        }
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
     * @param clazz   The class to introspect from.
     * @param classes The list of classes to add to all found super classes and
     *                interfaces.
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

    public static List<Field> getValidFields(final Field[] fields, boolean returnFinalFields) {
        return Arrays.stream(fields)
            .filter(field -> CReflectionUtils.isNotStaticOrFinal(field, returnFinalFields))
            .collect(Collectors.toList());
    }

    /**
     * Returns binary flag based on static/final field {@link Field} modifier
     *
     * @param field             - initial field {@link Field} to be checked
     * @param returnFinalFields - flag to filter non-static/final fields
     * @return true - if field is non-static/final, false - otherwise
     */
    public static boolean isNotStaticOrFinal(final Field field, boolean returnFinalFields) {
        return !Modifier.isStatic(field.getModifiers()) && (returnFinalFields || !Modifier.isFinal(field.getModifiers()));
    }

    /**
     * Returns property value of an object
     *
     * @param value        - initial argument {@link Object} to get property value from
     * @param propertyName - initial property name
     * @return property value of object
     */
    public static Object getProperty(final Object value, final String propertyName) {
        return getProperty(value, propertyName, Object.class);
    }

    /**
     * Returns property value of an object argument {@link T}
     *
     * @param <T>
     * @param value        - initial argument {@link T} to get property value from
     * @param propertyName - initial property name
     * @param clazz        - initial class to be casted to {@link Class}
     * @return property value {@link T}
     */
    public static <T> T getProperty(final T value, final String propertyName, final Class<? extends T> clazz) {
        try {
            return safeCast(BeanUtils.getProperty(value, propertyName), clazz);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(String.format("ERROR: cannot access property = {%s}, {%s}, argument: {%s}, message = ${%s}", propertyName, value, ex.getMessage()));
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(String.format("ERROR: cannot get value of property = {%s}, argument: {%s}, message = ${%s}", propertyName, value, ex.getMessage()));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(String.format("ERROR: cannot get value of property = {%s}, argument: {%s}, message = ${%s}", propertyName, value, ex.getMessage()));
        }
    }

    /**
     * Returns null-safe argument cast {@link Class}
     *
     * @param <T>
     * @param value - initial argument to be casted {@link Object}
     * @param clazz - initial class to be casted to {@link Class}
     * @return object casted to class {@link T}
     */
    public static <T> T safeCast(final Object value, final Class<? extends T> clazz) {
        Objects.requireNonNull(clazz);
        return (clazz.isInstance(value)) ? clazz.cast(value) : null;
    }

    /**
     * Returns type of the supplied argument {@link Member}
     * or {@link Method}
     *
     * @param member - initial argument to reflect on {@link Member}
     * @return type of the supplied argument {@link Member}
     */
    public static Type typeOf(final Member member) {
        Objects.requireNonNull(member);
        if (member instanceof Field) {
            return ((Field) member).getGenericType();
        }
        if (member instanceof Method) {
            return ((Method) member).getGenericReturnType();
        }
        throw new IllegalArgumentException(String.format("ERROR: no such class member = {%s}, neither a field nor a method", member));
    }

    /**
     * Returns list of super-classes {@link List} of the supplied class {@link Class}
     *
     * @param clazz - class to reflect on {@link Class}
     * @return list of super-classes of the supplied class {@link Class}
     */
    public static List<Class<?>> getAllSuperclasses(final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        final List<Class<?>> classes = new ArrayList<>();
        Class<?> superclass = clazz.getSuperclass();
        while (Objects.nonNull(superclass)) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }

    /**
     * Returns an array of all fields {@link Field} of the supplied class. Union of
     * {@link Class#getDeclaredFields()} which ignores and super-classes, and
     * {@link Class#getFields()} which ignored non-public fields
     *
     * @param clazz - initial class to reflect on {@link Class}
     * @return array of fields {@link Field} of the supplied class
     */
    public static Field[] getAllFields(final Class<?> clazz) {
        final List<Class<?>> classes = getAllSuperclasses(clazz);
        classes.add(clazz);
        return getAllFields(classes);
    }

    /**
     * Returns array of fields {@link #getAllFields(Class)} but acts on a
     * list of {@link Class}s and uses only {@link Class#getDeclaredFields()}.
     *
     * @param classes - collection of classes to reflect on {@link List}
     * @return array of fields {@link Field} of the supplied list of classes
     */
    private static Field[] getAllFields(final List<Class<?>> classes) {
        Objects.requireNonNull(classes);
        final Set<Field> fields = new HashSet<>();
        for (final Class<?> clazz : classes) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * Returns collection of fields {@link Field} filtered by static/final/accessible field modifiers
     *
     * @param fields                 - initial array of fields {@link Field} to be filtered on
     * @param returnFinalFields      - flag to filter non-static/final fields (true - to return static/final fields, false - otherwise)
     * @param returnAccessibleFields - flag to filter non-accessible fields (true - to return only accessible fields, false - otherwise)
     * @return collection of fields {@link Field}
     */
    public static List<Field> getValidFields(final Field[] fields, boolean returnFinalFields, boolean returnAccessibleFields) {
        return Arrays.stream(fields).filter(field -> isNotStaticOrFinalOrAccessible(field, returnFinalFields, returnAccessibleFields)).collect(Collectors.toList());
    }

    /**
     * Returns binary flag based on static/final field {@link Field} modifier
     *
     * @param field                  - initial field {@link Field} to be checked
     * @param returnFinalFields      - flag to filter non-static/final fields
     * @param returnAccessibleFields - flag to filter non-accessible fields
     * @return true - if field is non-static/final/accessible, false - otherwise
     */
    public static boolean isNotStaticOrFinalOrAccessible(final Field field, boolean returnFinalFields, boolean returnAccessibleFields) {
        return isNotStaticOrFinal(field, returnFinalFields) && (!returnAccessibleFields || field.trySetAccessible());
    }

    public static void makeFinalStatic(final Field field, final Object newValue) throws Exception {
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    public static Annotation getAnnotation(final AnnotatedElement element, final String annotationTypeName) {
        Class<?> annotationType = null;
        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return element.getAnnotation(annotationType.asSubclass(Annotation.class));
    }
}
