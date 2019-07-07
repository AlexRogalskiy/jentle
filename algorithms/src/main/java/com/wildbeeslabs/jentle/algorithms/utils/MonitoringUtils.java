package com.wildbeeslabs.jentle.algorithms.utils;

import java.lang.reflect.Constructor;

/**
 * The class, which contains utility methods for monitoring support.
 *
 * @author Alexey Stashok
 */
public class MonitoringUtils {
    /**
     * Load JMX object class and create an instance using constructor with
     * constructorParam.getClass() parameter. The constructorParam will be passed
     * to the constructor as a parameter.
     *
     * @param jmxObjectClassname the JMX object class name.
     * @param constructorParam   the parameter to be passed to the constructor.
     * @return instance of jmxObjectClassname class.
     */
    public static Object loadJmxObject(final String jmxObjectClassname,
                                       final Object constructorParam) {
        return loadJmxObject(jmxObjectClassname, constructorParam,
            constructorParam.getClass());
    }

    /**
     * Load JMX object class and create an instance using constructor with
     * contructorParamType parameter. The constructorParam will be passed to the
     * constructor as a parameter.
     *
     * @param jmxObjectClassname  the JMX object class name.
     * @param constructorParam    the parameter to be passed to the constructor.
     * @param contructorParamType the constructor parameter type, used to find
     *                            appropriate constructor.
     * @return instance of jmxObjectClassname class.
     */
    public static Object loadJmxObject(final String jmxObjectClassname,
                                       final Object constructorParam, final Class contructorParamType) {
        try {
            final Class<?> clazz = loadClass(jmxObjectClassname);
            final Constructor<?> c = clazz.getDeclaredConstructor(contructorParamType);
            return c.newInstance(constructorParam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> loadClass(final String classname) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = MonitoringUtils.class.getClassLoader();
        }

        return classLoader.loadClass(classname);
    }
}
