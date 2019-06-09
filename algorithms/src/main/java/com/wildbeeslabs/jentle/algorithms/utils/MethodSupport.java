package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * Utility class for reflective method invocation.
 *
 * @author Michał Piotrkowski
 */
@UtilityClass
public class MethodSupport {

    private static final String METHOD_HAS_NO_RETURN_VALUE = "Method '%s' in class %s.class has to return a value!";
    private static final String METHOD_NOT_FOUND = "Can't find method '%s' in class %s.class. Make sure public method " +
        "exists and accepts no arguments!";

    /**
     * Returns result of given method invocation on provided object.
     * <p>
     * Following requirements have to be met to extract method results:
     * <ul>
     * <li>method has to be public,</li>
     * <li>method cannot accept any arguments,</li>
     * <li>method cannot return void.</li>
     * </ul>
     *
     * @param instance   object on which
     * @param methodName name of method to be invoked
     * @return result of method invocation
     * @throws IllegalArgumentException if method does not exist or is not public, method returns void or method accepts
     *                                  any argument
     */
    public static Object methodResultFor(Object instance, String methodName) {
        checkNotNull(instance, "Object instance can not be null!");
        Method method = findMethod(methodName, instance.getClass());
        return invokeMethod(instance, method);
    }

    private static Object invokeMethod(Object item, Method method) {
        try {
            return method.invoke(item);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static Method findMethod(String methodName, Class<?> itemClass) {
        try {
            Method method = itemClass.getMethod(methodName);
            assertHasReturnType(itemClass, method);
            return method;
        } catch (SecurityException e) {
            throw prepareMethodNotFoundException(methodName, itemClass, e);
        } catch (NoSuchMethodException e) {
            throw prepareMethodNotFoundException(methodName, itemClass, e);
        }
    }

    private static IllegalArgumentException prepareMethodNotFoundException(String methodName,
                                                                           Class<?> itemClass,
                                                                           Exception cause) {
        String message = format(METHOD_NOT_FOUND, methodName, itemClass.getSimpleName());
        return new IllegalArgumentException(message, cause);
    }

    private static void assertHasReturnType(Class<?> itemClass, Method method) {
        checkArgument(!Void.TYPE.equals(method.getReturnType()), METHOD_HAS_NO_RETURN_VALUE, method.getName(), itemClass.getSimpleName());
    }

}
