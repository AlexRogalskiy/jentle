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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for setting JavaBeans-style properties on instances.
 */
@Slf4j
@UtilityClass
public class CPropertyUtils {

    /**
     * Empty {@link Object} array
     */
    public static final Object[] NO_ARGUMENTS = new Object[0];

    public static void setProperty(Object instance, String name, String value) {
        if (instance == null) {
            log.warn("Cannot set property " + name + " with value " + value + ". The target instance is null");
            return;
        }

        Class propClass = getPropertyType(instance.getClass(), name);
        if (propClass == null) {
            log.warn(
                "Cannot set property "
                    + name
                    + " with value "
                    + value
                    + ". Property class could not be found");
            return;
        }

        Object realValue = convertType(propClass, value, name);
        // TODO: Here the property desc is serched again
        setPropertyRealValue(instance, name, realValue);
    }

    public static Class getPropertyType(Class instanceClass, String propertyName) {
        if (instanceClass == null) {
            log.warn("Cannot retrieve property class for " + propertyName + ". Target instance class is null");
        }
        PropertyDescriptor propDesc = getPropertyDescriptor(instanceClass, propertyName);
        if (propDesc == null) {
            return null;
        }
        return propDesc.getPropertyType();
    }

    private static PropertyDescriptor getPropertyDescriptor(Class targetClass, String propertyName) {
        PropertyDescriptor result = null;
        if (targetClass == null) {
            log.warn("Cannot retrieve property " + propertyName + ". Class is null");
        } else {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);
                PropertyDescriptor[] propDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propDesc : propDescriptors) {
                    if (propDesc.getName().equals(propertyName)) {
                        result = propDesc;
                        break;
                    }
                }
            } catch (IntrospectionException ie) {
                log.warn("Cannot retrieve property " + propertyName + ". Cause is: " + ie);
            }
        }
        return result;
    }

    public static void setPropertyRealValue(Object instance, String name, Object value) {
        if (instance == null) {
            log.warn(
                "Cannot set property " + name + " with value " + value + ". Targe instance is null");
            return;
        }

        PropertyDescriptor propDesc = getPropertyDescriptor(instance.getClass(), name);
        if (propDesc == null) {
            log.warn(
                "Cannot set property " + name + " with value " + value + ". Property does not exist");
            return;
        }

        Method method = propDesc.getWriteMethod();
        try {
            method.invoke(instance, value);
        } catch (IllegalAccessException | InvocationTargetException iae) {
            log.warn("Cannot set property " + name + " with value " + value + ". Cause " + iae);
        }
    }

    public static <T> T convertType(Class<T> type, String value, String paramName) {
        try {
            if (value == null || "null".equals(value.toLowerCase())) {
                if (type.isPrimitive()) {
                    log.warn(
                        "Parameters",
                        2,
                        "Attempt to pass null value to primitive type parameter '" + paramName + "'");
                }

                return null; // null value must be used
            }

            if (type == String.class) {
                return (T) value;
            }
            if (type == int.class || type == Integer.class) {
                return (T) Integer.valueOf(value);
            }
            if (type == boolean.class || type == Boolean.class) {
                return (T) Boolean.valueOf(value);
            }
            if (type == byte.class || type == Byte.class) {
                return (T) Byte.valueOf(value);
            }
            if (type == char.class || type == Character.class) {
                return (T) Character.valueOf(value.charAt(0));
            }
            if (type == double.class || type == Double.class) {
                return (T) Double.valueOf(value);
            }
            if (type == float.class || type == Float.class) {
                return (T) Float.valueOf(value);
            }
            if (type == long.class || type == Long.class) {
                return (T) Long.valueOf(value);
            }
            if (type == short.class || type == Short.class) {
                return (T) Short.valueOf(value);
            }
            if (type.isEnum()) {
                return (T) Enum.valueOf((Class<Enum>) type, value);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Conversion issue on parameter: " + paramName, e);
        }
        throw new IllegalArgumentException("Unsupported type parameter : " + type);
    }

    /**
     * Returns the description of the property with the provided
     * name on the provided object's interface.
     *
     * @return the descriptor of the property, or null if the property does not exist.
     * @throws IllegalArgumentException if there's a introspection failure
     */
    public static PropertyDescriptor getPropertyDescriptor(final String propertyName, final Object fromObj) throws IllegalArgumentException {
        for (PropertyDescriptor property : propertyDescriptorsFor(fromObj, null)) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    /**
     * Returns all the property descriptors for the class associated with the given object
     *
     * @param fromObj   Use the class of this object
     * @param stopClass Don't include any properties from this ancestor class upwards.
     * @return Property descriptors
     * @throws IllegalArgumentException if there's a introspection failure
     */
    public static PropertyDescriptor[] propertyDescriptorsFor(final Object fromObj, final Class<Object> stopClass) throws IllegalArgumentException {
        try {
            return Introspector.getBeanInfo(fromObj.getClass(), stopClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException("Could not get property descriptors for " + fromObj.getClass(), e);
        }
    }
}
