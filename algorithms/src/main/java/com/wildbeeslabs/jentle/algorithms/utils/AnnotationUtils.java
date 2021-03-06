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

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toUnmodifiableList;

@UtilityClass
public class AnnotationUtils {

    /**
     * Determine if an annotation of {@code annotationType} is either
     * <em>present</em> or <em>meta-present</em> on the supplied optional
     * {@code element}.
     *
     * @see #findAnnotation(Optional, Class)
     * @see org.junit.platform.commons.support.AnnotationSupport#isAnnotated(Optional, Class)
     */
    public static boolean isAnnotated(final Optional<? extends AnnotatedElement> element, final Class<? extends Annotation> annotationType) {

        return findAnnotation(element, annotationType).isPresent();
    }

    /**
     * Determine if an annotation of {@code annotationType} is either
     * <em>present</em> or <em>meta-present</em> on the supplied
     * {@code element}.
     *
     * @see #findAnnotation(AnnotatedElement, Class)
     * @see org.junit.platform.commons.support.AnnotationSupport#isAnnotated(AnnotatedElement, Class)
     */
    public static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        return findAnnotation(element, annotationType).isPresent();
    }

    /**
     * @see org.junit.platform.commons.support.AnnotationSupport#findAnnotation(Optional, Class)
     */
    public static <A extends Annotation> Optional<A> findAnnotation(Optional<? extends AnnotatedElement> element,
                                                                    Class<A> annotationType) {

        if (element == null || !element.isPresent()) {
            return Optional.empty();
        }

        boolean inherited = annotationType.isAnnotationPresent(Inherited.class);

        return findAnnotation(element.get(), annotationType, inherited, new HashSet<>());
    }

    /**
     * @see org.junit.platform.commons.support.AnnotationSupport#findAnnotation(AnnotatedElement, Class)
     */
    public static <A extends Annotation> Optional<A> findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        boolean inherited = annotationType.isAnnotationPresent(Inherited.class);
        return findAnnotation(element, annotationType, inherited, new HashSet<>());
    }

    private static <A extends Annotation> Optional<A> findAnnotation(AnnotatedElement element, Class<A> annotationType,
                                                                     boolean inherited, Set<Annotation> visited) {

        Objects.requireNonNull(annotationType, "annotationType must not be null");

        if (element == null) {
            return Optional.empty();
        }

        // Directly present?
        A annotation = element.getDeclaredAnnotation(annotationType);
        if (annotation != null) {
            return Optional.of(annotation);
        }

        // Meta-present on directly present annotations?
        Optional<A> directMetaAnnotation = findMetaAnnotation(annotationType, element.getDeclaredAnnotations(),
            inherited, visited);
        if (directMetaAnnotation.isPresent()) {
            return directMetaAnnotation;
        }

        if (element instanceof Class) {
            Class<?> clazz = (Class<?>) element;

            // Search on interfaces
            for (Class<?> ifc : clazz.getInterfaces()) {
                if (ifc != Annotation.class) {
                    Optional<A> annotationOnInterface = findAnnotation(ifc, annotationType, inherited, visited);
                    if (annotationOnInterface.isPresent()) {
                        return annotationOnInterface;
                    }
                }
            }

            // Indirectly present?
            // Search in class hierarchy
            if (inherited) {
                Class<?> superclass = clazz.getSuperclass();
                if (superclass != null && superclass != Object.class) {
                    Optional<A> annotationOnSuperclass = findAnnotation(superclass, annotationType, inherited, visited);
                    if (annotationOnSuperclass.isPresent()) {
                        return annotationOnSuperclass;
                    }
                }
            }
        }

        // Meta-present on indirectly present annotations?
        return findMetaAnnotation(annotationType, element.getAnnotations(), inherited, visited);
    }

    private static <A extends Annotation> Optional<A> findMetaAnnotation(Class<A> annotationType,
                                                                         Annotation[] candidates, boolean inherited, Set<Annotation> visited) {

        for (Annotation candidateAnnotation : candidates) {
            Class<? extends Annotation> candidateAnnotationType = candidateAnnotation.annotationType();
            if (!isInJavaLangAnnotationPackage(candidateAnnotationType) && visited.add(candidateAnnotation)) {
                Optional<A> metaAnnotation = findAnnotation(candidateAnnotationType, annotationType, inherited,
                    visited);
                if (metaAnnotation.isPresent()) {
                    return metaAnnotation;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * @see org.junit.platform.commons.support.AnnotationSupport#findRepeatableAnnotations(AnnotatedElement, Class)
     */
    public static <A extends Annotation> List<A> findRepeatableAnnotations(AnnotatedElement element,
                                                                           Class<A> annotationType) {

        Objects.requireNonNull(annotationType, "annotationType must not be null");
        final Repeatable repeatable = annotationType.getAnnotation(Repeatable.class);
        Objects.requireNonNull(repeatable, () -> annotationType.getName() + " must be @Repeatable");
        Class<? extends Annotation> containerType = repeatable.value();
        boolean inherited = containerType.isAnnotationPresent(Inherited.class);

        // Short circuit the search algorithm.
        if (element == null) {
            return Collections.emptyList();
        }

        // We use a LinkedHashSet because the search algorithm may discover
        // duplicates, but we need to maintain the original order.
        Set<A> found = new LinkedHashSet<>(16);
        findRepeatableAnnotations(element, annotationType, containerType, inherited, found, new HashSet<>(16));
        // unmodifiable since returned from public, non-internal method(s)
        return Collections.unmodifiableList(new ArrayList<>(found));
    }

    private static <A extends Annotation> void findRepeatableAnnotations(AnnotatedElement element,
                                                                         Class<A> annotationType, Class<? extends Annotation> containerType, boolean inherited, Set<A> found,
                                                                         Set<Annotation> visited) {

        if (element instanceof Class) {
            Class<?> clazz = (Class<?>) element;

            // Recurse first in order to support top-down semantics for inherited, repeatable annotations.
            if (inherited) {
                Class<?> superclass = clazz.getSuperclass();
                if (superclass != null && superclass != Object.class) {
                    findRepeatableAnnotations(superclass, annotationType, containerType, inherited, found, visited);
                }
            }

            // Search on interfaces
            for (Class<?> ifc : clazz.getInterfaces()) {
                if (ifc != Annotation.class) {
                    findRepeatableAnnotations(ifc, annotationType, containerType, inherited, found, visited);
                }
            }
        }

        // Find annotations that are directly present or meta-present on directly present annotations.
        findRepeatableAnnotations(element.getDeclaredAnnotations(), annotationType, containerType, inherited, found,
            visited);

        // Find annotations that are indirectly present or meta-present on indirectly present annotations.
        findRepeatableAnnotations(element.getAnnotations(), annotationType, containerType, inherited, found, visited);
    }

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> void findRepeatableAnnotations(Annotation[] candidates,
                                                                         Class<A> annotationType, Class<? extends Annotation> containerType, boolean inherited, Set<A> found,
                                                                         Set<Annotation> visited) {

        for (Annotation candidate : candidates) {
            Class<? extends Annotation> candidateAnnotationType = candidate.annotationType();
            if (!isInJavaLangAnnotationPackage(candidateAnnotationType) && visited.add(candidate)) {
                // Exact match?
                if (candidateAnnotationType.equals(annotationType)) {
                    found.add(annotationType.cast(candidate));
                }
                // Container?
                else if (candidateAnnotationType.equals(containerType)) {
                    // Note: it's not a legitimate containing annotation type if it doesn't declare
                    // a 'value' attribute that returns an ArrayUtils of the contained annotation type.
                    // See https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.6.3
                    final Method method = ReflectionUtils2.getMethod(containerType, "value").orElseThrow(
                        () -> new IllegalArgumentException(String.format(
                            "Container annotation type '%s' must declare a 'value' attribute of type %s[].",
                            containerType, annotationType)));

                    Annotation[] containedAnnotations = (Annotation[]) ReflectionUtils.invokeMethod(method, candidate);
                    found.addAll((Collection<? extends A>) asList(containedAnnotations));
                }
                // Otherwise search recursively through the meta-annotation hierarchy...
                else {
                    findRepeatableAnnotations(candidateAnnotationType, annotationType, containerType, inherited, found,
                        visited);
                }
            }
        }
    }

    /**
     * @see org.junit.platform.commons.support.AnnotationSupport#findPublicAnnotatedFields(Class, Class, Class)
     */
    public static List<Field> findPublicAnnotatedFields(Class<?> clazz, Class<?> fieldType,
                                                        Class<? extends Annotation> annotationType) {

        Objects.requireNonNull(clazz, "Class must not be null");
        Objects.requireNonNull(fieldType, "fieldType must not be null");
        Objects.requireNonNull(annotationType, "annotationType must not be null");

        // @formatter:off
        return java.util.Arrays.stream(clazz.getFields())
            .filter(field -> fieldType.isAssignableFrom(field.getType()) && isAnnotated(field, annotationType))
            .collect(toUnmodifiableList());
        // @formatter:on
    }

    /**
     * Find all {@linkplain Field fields} of the supplied class or interface
     * that are annotated or <em>meta-annotated</em> with the specified
     * {@code annotationType} and match the specified {@code predicate}, using
     * top-down search semantics within the type hierarchy.
     *
     * @see #findAnnotatedFields(Class, Class, Predicate, HierarchyTraversalMode)
     */
    public static List<Field> findAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationType, Predicate<Field> predicate) {

        return findAnnotatedFields(clazz, annotationType, predicate, ReflectionUtils2.HierarchyTraversalMode.TOP_DOWN);
    }

    /**
     * Find all {@linkplain Field fields} of the supplied class or interface
     * that are annotated or <em>meta-annotated</em> with the specified
     * {@code annotationType} and match the specified {@code predicate}.
     *
     * @param clazz          the class or interface in which to find the fields; never {@code null}
     * @param annotationType the annotation type to search for; never {@code null}
     * @param predicate      the field filter; never {@code null}
     * @param traversalMode  the hierarchy traversal mode; never {@code null}
     * @return the list of all such fields found; neither {@code null} nor mutable
     */
    public static List<Field> findAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationType,
                                                  Predicate<Field> predicate, ReflectionUtils2.HierarchyTraversalMode traversalMode) {

        CPreconditionsUtils.notNull(clazz, "Class must not be null");
        CPreconditionsUtils.notNull(annotationType, "annotationType must not be null");
        CPreconditionsUtils.notNull(predicate, "Predicate must not be null");

        Predicate<Field> annotated = field -> isAnnotated(field, annotationType);

        return ReflectionUtils2.findFields(clazz, annotated.and(predicate), traversalMode);
    }

    /**
     * @see org.junit.platform.commons.support.AnnotationSupport#findAnnotatedMethods(Class, Class, org.junit.platform.commons.support.HierarchyTraversalMode)
     */
    public static List<Method> findAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationType,
                                                    ReflectionUtils2.HierarchyTraversalMode traversalMode) {

        Objects.requireNonNull(clazz, "Class must not be null");
        Objects.requireNonNull(annotationType, "annotationType must not be null");

        return ReflectionUtils2.findMethods(clazz, method -> isAnnotated(method, annotationType), traversalMode);
    }

    private static boolean isInJavaLangAnnotationPackage(Class<? extends Annotation> annotationType) {
        return (annotationType != null && annotationType.getName().startsWith("java.lang.annotation"));
    }

}
