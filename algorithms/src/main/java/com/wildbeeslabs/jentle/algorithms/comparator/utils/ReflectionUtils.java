package com.wildbeeslabs.jentle.algorithms.comparator.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom reflection utilities implementation
 */
public final class ReflectionUtils {

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(ReflectionUtils.class);

    /**
     * Default private constructor
     */
    private ReflectionUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
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
     * @param field             - initial field {@link Field} to be checked
     * @param returnFinalFields - flag to filter non-static/final fields
     * @return true - if field is non-static/final, false - otherwise
     */
    public static boolean isNotStaticOrFinal(final Field field, boolean returnFinalFields) {
        return !Modifier.isStatic(field.getModifiers()) && (returnFinalFields || !Modifier.isFinal(field.getModifiers()));
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
        return isNotStaticOrFinal(field, returnFinalFields) && (!returnAccessibleFields || field.isAccessible());
    }
}
